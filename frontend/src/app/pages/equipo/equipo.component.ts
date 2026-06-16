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
  paginator = viewChild(MatPaginator);

  equipo: Equipo = new Equipo();
  isEditing = false;

  private readonly equipoService = inject(EquipoService);
  private readonly snackBar = inject(MatSnackBar);

  equipos$ = this.equipoService.$listChange;

  constructor() {
    this.limpiar();

    this.equipoService.findAll().subscribe({
      next: data => this.equipoService.setListChange(data),
      error: () => this.mostrarError('No se pudo cargar la lista de equipos'),
    });

    effect(() => {
      const list = this.equipos$();
      const p = this.paginator();
      const ds = this.dataSource();

      ds.data = list;
      ds.paginator = p ?? null;
    });

    effect(() => {
      const message = this.equipoService.$messageChange();
      if (message) {
        this.snackBar.open(message, 'Cerrar', { duration: 3000 });
        untracked(() => this.equipoService.setMessageChange(''));
      }
    });
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
        switchMap(() => this.equipoService.findAll()),
        tap(data => this.equipoService.setListChange(data)),
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
          switchMap(() => this.equipoService.findAll()),
          tap(data => this.equipoService.setListChange(data)),
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
