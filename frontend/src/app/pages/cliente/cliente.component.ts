import { Component, effect, inject, signal, untracked, viewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { switchMap, tap } from 'rxjs';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { Cliente } from '../../model/cliente';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-cliente',
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
  templateUrl: './cliente.component.html',
  styleUrl: './cliente.component.css',
})
export class ClienteComponent {
  displayedColumns: string[] = ['idCliente', 'nombre', 'telefono', 'email', 'acciones'];

  dataSource = signal(new MatTableDataSource<Cliente>());
  paginator = viewChild(MatPaginator);

  cliente: Cliente = new Cliente();
  isEditing = false;

  private readonly clienteService = inject(ClienteService);
  private readonly snackBar = inject(MatSnackBar);

  clientes$ = this.clienteService.$listChange;

  constructor() {
    this.clienteService.findAll().subscribe({
      next: data => this.clienteService.setListChange(data),
      error: () => this.mostrarError('No se pudo cargar la lista de clientes'),
    });

    effect(() => {
      const list = this.clientes$();
      const p = this.paginator();
      const ds = this.dataSource();

      ds.data = list;
      ds.paginator = p ?? null;
    });

    effect(() => {
      const message = this.clienteService.$messageChange();
      if (message) {
        this.snackBar.open(message, 'Cerrar', { duration: 3000 });
        untracked(() => this.clienteService.setMessageChange(''));
      }
    });
  }

  guardar(): void {
    const request$ = this.isEditing
      ? this.clienteService.update(this.cliente.idCliente, this.cliente)
      : this.clienteService.save(this.cliente);
    const successMessage = this.isEditing
      ? 'Cliente actualizado correctamente'
      : 'Cliente registrado correctamente';
    const errorMessage = this.isEditing
      ? 'No se pudo actualizar el cliente'
      : 'No se pudo registrar el cliente';

    request$
      .pipe(
        switchMap(() => this.clienteService.findAll()),
        tap(data => this.clienteService.setListChange(data)),
        tap(() => this.clienteService.setMessageChange(successMessage))
      )
      .subscribe({
        next: () => this.limpiar(),
        error: () => this.mostrarError(errorMessage),
      });
  }

  editar(cliente: Cliente): void {
    this.cliente = { ...cliente };
    this.isEditing = true;
  }

  eliminar(id: number): void {
    if (confirm('Esta seguro de eliminar este cliente?')) {
      this.clienteService.delete(id)
        .pipe(
          switchMap(() => this.clienteService.findAll()),
          tap(data => this.clienteService.setListChange(data)),
          tap(() => this.clienteService.setMessageChange('Cliente eliminado correctamente'))
        )
        .subscribe({
          error: () => this.mostrarError('No se pudo eliminar el cliente'),
        });
    }
  }

  limpiar(): void {
    this.cliente = new Cliente();
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
