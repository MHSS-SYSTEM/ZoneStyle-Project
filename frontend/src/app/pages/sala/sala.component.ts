import { Component, effect, inject, signal, untracked, viewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { switchMap, tap } from 'rxjs';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
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
  paginator = viewChild(MatPaginator);

  sala: Sala = new Sala();
  isEditing = false;

  private readonly salaService = inject(SalaService);
  private readonly snackBar = inject(MatSnackBar);

  salas$ = this.salaService.$listChange;

  constructor() {
    this.salaService.findAll().subscribe({
      next: data => this.salaService.setListChange(data),
      error: () => this.mostrarError('No se pudo cargar la lista de salas'),
    });

    effect(() => {
      const list = this.salas$();
      const p = this.paginator();
      const ds = this.dataSource();

      ds.data = list;
      ds.paginator = p ?? null;
    });

    effect(() => {
      const message = this.salaService.$messageChange();
      if (message) {
        this.snackBar.open(message, 'Cerrar', { duration: 3000 });
        untracked(() => this.salaService.setMessageChange(''));
      }
    });
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
        switchMap(() => this.salaService.findAll()),
        tap(data => this.salaService.setListChange(data)),
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
          switchMap(() => this.salaService.findAll()),
          tap(data => this.salaService.setListChange(data)),
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
