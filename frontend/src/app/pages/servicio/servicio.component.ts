import { Component, computed, effect, inject, signal, untracked } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { switchMap, tap } from 'rxjs';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { Servicio } from '../../model/servicio';
import { ServicioService } from '../../services/servicio.service';

@Component({
  selector: 'app-servicio',
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
  ],
  templateUrl: './servicio.component.html',
  styleUrl: './servicio.component.css',
})
export class ServicioComponent {
  displayedColumns: string[] = ['idServicio', 'nombre', 'precioPorHora', 'acciones'];

  dataSource = signal(new MatTableDataSource<Servicio>());

  // Paginación server-side
  pageRequest = signal({ page: 0, size: 10 });

  private readonly servicioService = inject(ServicioService);
  private readonly snackBar = inject(MatSnackBar);

  private readonly response = toSignal(
    toObservable(this.pageRequest).pipe(
      switchMap(({ page, size }) => this.servicioService.listPageable(page, size)),
      tap(data => this.servicioService.setListChange(data.content))
    )
  );

  totalElements = computed(() => this.response()?.page?.totalElements ?? 0);

  servicio: Servicio = new Servicio();
  isEditing = false;

  servicios$ = this.servicioService.$listChange;

  constructor() {
    effect(() => {
      const list = this.servicios$();
      const ds = this.dataSource();
      ds.data = list;
    });

    effect(() => {
      const message = this.servicioService.$messageChange();
      if (message) {
        this.snackBar.open(message, 'Cerrar', { duration: 3000 });
        untracked(() => this.servicioService.setMessageChange(''));
      }
    });
  }

  changePage(e: any): void {
    this.pageRequest.set({ page: e.pageIndex, size: e.pageSize });
  }

  guardar(): void {
    const request$ = this.isEditing
      ? this.servicioService.update(this.servicio.idServicio, this.servicio)
      : this.servicioService.save(this.servicio);
    const successMessage = this.isEditing
      ? 'Servicio actualizado correctamente'
      : 'Servicio registrado correctamente';
    const errorMessage = this.isEditing
      ? 'No se pudo actualizar el servicio'
      : 'No se pudo registrar el servicio';

    request$
      .pipe(
        switchMap(() => this.servicioService.listPageable(this.pageRequest().page, this.pageRequest().size)),
        tap(data => this.servicioService.setListChange(data.content)),
        tap(() => this.servicioService.setMessageChange(successMessage))
      )
      .subscribe({
        next: () => this.limpiar(),
        error: () => this.mostrarError(errorMessage),
      });
  }

  editar(servicio: Servicio): void {
    this.servicio = { ...servicio };
    this.isEditing = true;
  }

  eliminar(id: number): void {
    if (confirm('Esta seguro de eliminar este servicio?')) {
      this.servicioService.delete(id)
        .pipe(
          switchMap(() => this.servicioService.listPageable(this.pageRequest().page, this.pageRequest().size)),
          tap(data => this.servicioService.setListChange(data.content)),
          tap(() => this.servicioService.setMessageChange('Servicio eliminado correctamente'))
        )
        .subscribe({
          error: () => this.mostrarError('No se pudo eliminar el servicio'),
        });
    }
  }

  limpiar(): void {
    this.servicio = new Servicio();
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
