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

import { Sala } from '../../model/sala';
import { SalaService } from '../../services/sala.service';

@Component({
  selector: 'app-sala',
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
  templateUrl: './sala.component.html',
  styleUrl: './sala.component.css',
})
export class SalaComponent {
  displayedColumns: string[] = ['idSala', 'nombre', 'estado', 'acciones'];

  dataSource = signal(new MatTableDataSource<Sala>());

  // Paginación server-side
  pageRequest = signal({ page: 0, size: 10 });

  private readonly salaService = inject(SalaService);
  private readonly snackBar = inject(MatSnackBar);

  private readonly response = toSignal(
    toObservable(this.pageRequest).pipe(
      switchMap(({ page, size }) => this.salaService.listPageable(page, size)),
      tap(data => this.salaService.setListChange(data.content))
    )
  );

  totalElements = computed(() => this.response()?.page?.totalElements ?? 0);

  sala: Sala = new Sala();
  isEditing = false;

  salas$ = this.salaService.$listChange;

  constructor() {
    effect(() => {
      const list = this.salas$();
      const ds = this.dataSource();
      ds.data = list;
    });

    effect(() => {
      const message = this.salaService.$messageChange();
      if (message) {
        this.snackBar.open(message, 'Cerrar', { duration: 3000 });
        untracked(() => this.salaService.setMessageChange(''));
      }
    });
  }

  changePage(e: any): void {
    this.pageRequest.set({ page: e.pageIndex, size: e.pageSize });
  }

  guardar(): void {
    const request$ = this.isEditing
      ? this.salaService.update(this.sala.idSala, this.sala)
      : this.salaService.save(this.sala);
    const successMessage = this.isEditing
      ? 'Sala actualizada correctamente'
      : 'Sala registrada correctamente';
    const errorMessage = this.isEditing
      ? 'No se pudo actualizar la sala'
      : 'No se pudo registrar la sala';

    request$
      .pipe(
        switchMap(() => this.salaService.listPageable(this.pageRequest().page, this.pageRequest().size)),
        tap(data => this.salaService.setListChange(data.content)),
        tap(() => this.salaService.setMessageChange(successMessage))
      )
      .subscribe({
        next: () => this.limpiar(),
        error: () => this.mostrarError(errorMessage),
      });
  }

  editar(sala: Sala): void {
    this.sala = { ...sala };
    this.isEditing = true;
  }

  eliminar(id: number): void {
    if (confirm('Esta seguro de eliminar esta sala?')) {
      this.salaService.delete(id)
        .pipe(
          switchMap(() => this.salaService.listPageable(this.pageRequest().page, this.pageRequest().size)),
          tap(data => this.salaService.setListChange(data.content)),
          tap(() => this.salaService.setMessageChange('Sala eliminada correctamente'))
        )
        .subscribe({
          error: () => this.mostrarError('No se pudo eliminar la sala'),
        });
    }
  }

  limpiar(): void {
    this.sala = new Sala();
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
