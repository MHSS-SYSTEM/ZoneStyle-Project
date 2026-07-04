import { Component, computed, effect, inject, signal, untracked } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { switchMap, tap } from 'rxjs';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { Equipo } from '../../model/equipo';
import { EquipoService } from '../../services/equipo.service';

@Component({
  selector: 'app-equipo',
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
  ],
  templateUrl: './equipo.component.html',
  styleUrl: './equipo.component.css',
})
export class EquipoComponent {
  displayedColumns: string[] = ['idEquipo', 'nombre', 'marca', 'modelo', 'estado', 'acciones'];

  dataSource = signal(new MatTableDataSource<Equipo>());

  // Paginación server-side
  pageRequest = signal({ page: 0, size: 10 });

  private readonly equipoService = inject(EquipoService);
  private readonly snackBar = inject(MatSnackBar);

  private readonly response = toSignal(
    toObservable(this.pageRequest).pipe(
      switchMap(({ page, size }) => this.equipoService.listPageable(page, size)),
      tap(data => this.equipoService.setListChange(data.content))
    )
  );

  totalElements = computed(() => this.response()?.page?.totalElements ?? 0);

  equipo: Equipo = new Equipo();
  isEditing = false;

  equipos$ = this.equipoService.$listChange;

  constructor() {
    this.limpiar();

    effect(() => {
      const list = this.equipos$();
      const ds = this.dataSource();
      ds.data = list;
    });

    effect(() => {
      const message = this.equipoService.$messageChange();
      if (message) {
        this.snackBar.open(message, 'Cerrar', { duration: 3000 });
        untracked(() => this.equipoService.setMessageChange(''));
      }
    });
  }

  changePage(e: any): void {
    this.pageRequest.set({ page: e.pageIndex, size: e.pageSize });
  }

  guardar(): void {
    const request$ = this.isEditing
      ? this.equipoService.update(this.equipo.idEquipo, this.equipo)
      : this.equipoService.save(this.equipo);
    const successMessage = this.isEditing
      ? 'Equipo actualizado correctamente'
      : 'Equipo registrado correctamente';
    const errorMessage = this.isEditing
      ? 'No se pudo actualizar el equipo'
      : 'No se pudo registrar el equipo';

    request$
      .pipe(
        switchMap(() => this.equipoService.listPageable(this.pageRequest().page, this.pageRequest().size)),
        tap(data => this.equipoService.setListChange(data.content)),
        tap(() => this.equipoService.setMessageChange(successMessage))
      )
      .subscribe({
        next: () => this.limpiar(),
        error: () => this.mostrarError(errorMessage),
      });
  }

  editar(equipo: Equipo): void {
    this.equipo = { ...equipo };
    this.isEditing = true;
  }

  eliminar(id: number): void {
    if (confirm('Esta seguro de eliminar este equipo tecnico?')) {
      this.equipoService.delete(id)
        .pipe(
          switchMap(() => this.equipoService.listPageable(this.pageRequest().page, this.pageRequest().size)),
          tap(data => this.equipoService.setListChange(data.content)),
          tap(() => this.equipoService.setMessageChange('Equipo eliminado correctamente'))
        )
        .subscribe({
          error: () => this.mostrarError('No se pudo eliminar el equipo'),
        });
    }
  }

  limpiar(): void {
    this.equipo = new Equipo();
    this.equipo.estado = 'DISPONIBLE';
    this.isEditing = false;
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource().filter = filterValue.trim().toLowerCase();
  }

  private mostrarError(message: string): void {
    this.snackBar.open(message, 'Cerrar', { duration: 4000 });
  }
}
