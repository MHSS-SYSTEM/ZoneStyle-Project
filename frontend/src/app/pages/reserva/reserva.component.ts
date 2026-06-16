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
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatCardModule } from '@angular/material/card';

import { Reserva } from '../../model/reserva';
import { ReservaDetalle } from '../../model/reserva-detalle';
import { Cliente } from '../../model/cliente';
import { Sala } from '../../model/sala';
import { Servicio } from '../../model/servicio';
import { Equipo } from '../../model/equipo';
import { Pago } from '../../model/pago';

import { ReservaService } from '../../services/reserva.service';
import { ClienteService } from '../../services/cliente.service';
import { SalaService } from '../../services/sala.service';
import { ServicioService } from '../../services/servicio.service';
import { EquipoService } from '../../services/equipo.service';
import { PagoService } from '../../services/pago.service';

@Component({
  selector: 'app-reserva',
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
    MatDividerModule,
    MatTooltipModule,
    MatDatepickerModule,
    MatCardModule,
  ],
  templateUrl: './reserva.component.html',
  styleUrl: './reserva.component.css',
})
export class ReservaComponent {
  displayedColumnsReserva: string[] = ['idReserva', 'cliente', 'sala', 'fecha', 'horario', 'equipos', 'estado', 'total', 'abono', 'saldo', 'metodoPago', 'acciones'];
  displayedColumnsPago: string[] = ['fechaPago', 'monto', 'metodoPago', 'tipoPago'];
  dataSourceReserva = signal(new MatTableDataSource<Reserva>());
  paginatorReserva = viewChild(MatPaginator);

  clientes = signal<Cliente[]>([]);
  salas = signal<Sala[]>([]);
  servicios = signal<Servicio[]>([]);
  equipos = signal<Equipo[]>([]);
  pagosHistorial = signal<Pago[]>([]);

  reserva: Reserva = new Reserva();
  detalles = signal<ReservaDetalle[]>([]);
  pagoParcial: Pago = new Pago();
  reservaPagoId: number | null = null;
  filtroInicio = '';
  filtroFin = '';
  filtroEstado = 'TODOS';

  selectedServicio: Servicio | null = null;
  cantidadHoras = 1;

  selectedDate: Date = new Date();
  hasConflict = signal<boolean>(false);
  abonoInvalido = signal<boolean>(false);

  private readonly reservaService = inject(ReservaService);
  private readonly clienteService = inject(ClienteService);
  private readonly salaService = inject(SalaService);
  private readonly servicioService = inject(ServicioService);
  private readonly equipoService = inject(EquipoService);
  private readonly pagoService = inject(PagoService);
  private readonly snackBar = inject(MatSnackBar);

  reservas$ = this.reservaService.$listChange;

  constructor() {
    this.limpiar();
    this.filtroInicio = this.toInputDate(new Date(new Date().getFullYear(), new Date().getMonth(), 1));
    this.filtroFin = this.toInputDate(new Date());

    this.cargarCatalogos();
    this.cargarEquiposDisponibles();
    this.cargarReservas();

    effect(() => {
      const list = this.reservas$();
      const p = this.paginatorReserva();
      const ds = this.dataSourceReserva();

      ds.data = list;
      ds.paginator = p ?? null;
    });

    effect(() => {
      const message = this.reservaService.$messageChange();
      if (message) {
        this.snackBar.open(message, 'Cerrar', { duration: 3000 });
        untracked(() => this.reservaService.setMessageChange(''));
      }
    });
  }

  compareObjects(o1: any, o2: any): boolean {
    return o1 && o2 ? (
      o1.idCliente === o2.idCliente ||
      o1.idSala === o2.idSala ||
      o1.idServicio === o2.idServicio ||
      o1.idEquipo === o2.idEquipo
    ) : o1 === o2;
  }

  onDateChange(date: Date | null): void {
    if (date) {
      this.selectedDate = date;
      const offset = date.getTimezoneOffset();
      const localDate = new Date(date.getTime() - (offset * 60 * 1000));
      const isoString = localDate.toISOString();
      const datePart = isoString.split('T')[0];
      this.reserva.fecha = `${datePart}T${this.reserva.horaInicio || '10:00'}:00`;

      this.checkConflict();
      this.cargarEquiposDisponibles();
    }
  }

  onHoraChange(): void {
    const datePart = (this.reserva.fecha || '').split('T')[0] || this.toInputDate(this.selectedDate);
    this.reserva.fecha = `${datePart}T${this.reserva.horaInicio || '10:00'}:00`;
    this.actualizarHoraFin();
    this.checkConflict();
    this.cargarEquiposDisponibles();
  }

  checkConflict(): void {
    if (!this.reserva.sala || !this.reserva.sala.idSala || !this.reserva.fecha) {
      this.hasConflict.set(false);
      return;
    }

    const selectedDay = this.reserva.fecha.split('T')[0];
    const selectedSalaId = this.reserva.sala.idSala;
    const selectedStart = this.timeToMinutes(this.reserva.horaInicio || '10:00');
    const selectedEnd = this.timeToMinutes(this.reserva.horaFin || '11:00');

    const conflictExist = this.reservas$().some(res => {
      if (!res.fecha || !res.sala || !res.sala.idSala) return false;
      if (res.idReserva === this.reserva.idReserva) return false;

      const resDay = res.fecha.split('T')[0];
      const resStart = this.timeToMinutes(res.horaInicio || res.fecha.split('T')[1]?.substring(0, 5) || '10:00');
      const resEnd = this.timeToMinutes(res.horaFin || this.addHours(res.horaInicio || '10:00', 1));
      const overlap = selectedStart < resEnd && selectedEnd > resStart;
      return resDay === selectedDay && res.sala.idSala === selectedSalaId && overlap;
    });

    this.hasConflict.set(conflictExist);
    this.validarDisponibilidadBackend();
  }

  onSalaChange(): void {
    this.checkConflict();
  }

  agregarDetalle(): void {
    if (!this.selectedServicio) {
      this.mostrarError('Por favor, selecciona un servicio');
      return;
    }
    if (this.cantidadHoras <= 0) {
      this.mostrarError('Las horas deben ser mayor a cero');
      return;
    }

    const subtotal = this.selectedServicio.precioPorHora * this.cantidadHoras;

    const detalle = new ReservaDetalle();
    detalle.servicio = this.selectedServicio;
    detalle.cantidadHoras = this.cantidadHoras;
    detalle.subtotal = subtotal;

    this.detalles.update(current => [...current, detalle]);
    this.recalcularTotal();
    this.actualizarHoraFin();

    this.selectedServicio = null;
    this.cantidadHoras = 1;
  }

  removerDetalle(index: number): void {
    this.detalles.update(current => current.filter((_, i) => i !== index));
    this.recalcularTotal();
    this.actualizarHoraFin();
  }

  recalcularTotal(): void {
    const totalSum = this.detalles().reduce((sum, det) => sum + det.subtotal, 0);
    this.reserva.total = totalSum;
    this.calcularSaldo();
  }

  calcularSaldo(): void {
    const total = this.reserva.total || 0;
    const abono = this.reserva.abono || 0;

    this.reserva.saldo = total - abono;
    this.abonoInvalido.set(total > 0 && abono < (total * 0.5));
  }

  onAbonoChange(): void {
    this.calcularSaldo();
  }

  guardar(): void {
    if (!this.reserva.cliente || !this.reserva.cliente.idCliente) {
      this.mostrarError('Por favor, selecciona un cliente');
      return;
    }
    if (!this.reserva.sala || !this.reserva.sala.idSala) {
      this.mostrarError('Por favor, selecciona una sala');
      return;
    }
    if (!this.reserva.fecha) {
      this.mostrarError('Por favor, selecciona una fecha en el calendario');
      return;
    }
    if (this.detalles().length === 0) {
      this.mostrarError('Debe agregar al menos un servicio a la reserva');
      return;
    }
    if (this.abonoInvalido()) {
      this.mostrarError('El abono inicial debe ser como minimo el 50% del total');
      return;
    }
    if (this.hasConflict()) {
      this.mostrarError('La sala seleccionada ya esta ocupada para esta fecha');
      return;
    }

    this.reserva.detalles = this.detalles();
    this.reserva.fecha = `${this.reserva.fecha.split('T')[0]}T${this.reserva.horaInicio}:00`;

    this.reservaService.save(this.reserva)
      .pipe(
        switchMap(() => this.reservaService.findAll()),
        tap(data => this.reservaService.setListChange(data)),
        tap(() => this.reservaService.setMessageChange('Reserva registrada correctamente'))
      )
      .subscribe({
        next: () => this.limpiar(),
        error: () => this.mostrarError('No se pudo registrar la reserva'),
      });
  }

  cargarPagos(idReserva: number): void {
    this.reservaPagoId = idReserva;
    this.pagoService.findByReserva(idReserva).subscribe({
      next: data => this.pagosHistorial.set(data),
      error: () => this.mostrarError('No se pudo cargar el historial de pagos'),
    });
  }

  registrarPagoParcial(): void {
    if (!this.reservaPagoId) {
      this.mostrarError('Selecciona una reserva con saldo pendiente');
      return;
    }

    if (!this.pagoParcial.monto || this.pagoParcial.monto <= 0) {
      this.mostrarError('Ingresa un monto mayor a cero');
      return;
    }

    this.pagoParcial.tipoPago = 'PAGO_PARCIAL';
    this.pagoService.registrarPagoReserva(this.reservaPagoId, this.pagoParcial)
      .pipe(
        switchMap(() => this.reservaService.findAll()),
        tap(data => this.reservaService.setListChange(data)),
        tap(() => this.cargarPagos(this.reservaPagoId!)),
        tap(() => this.reservaService.setMessageChange('Pago registrado correctamente'))
      )
      .subscribe({
        next: () => {
          this.pagoParcial = new Pago();
          this.pagoParcial.metodoPago = 'EFECTIVO';
        },
        error: () => this.mostrarError('No se pudo registrar el pago'),
      });
  }

  filtrarReservas(): void {
    if (this.filtroEstado !== 'TODOS') {
      this.reservaService.findByEstado(this.filtroEstado).subscribe({
        next: data => this.reservaService.setListChange(data),
        error: () => this.mostrarError('No se pudo filtrar por estado'),
      });
      return;
    }

    this.reservaService.findByFecha(this.filtroInicio, this.filtroFin).subscribe({
      next: data => this.reservaService.setListChange(data),
      error: () => this.mostrarError('No se pudo filtrar por fecha'),
    });
  }

  cancelar(id: number): void {
    if (confirm('Deseas cancelar esta reserva sin borrar su historial?')) {
      this.reservaService.cancelar(id)
        .pipe(
          switchMap(() => this.reservaService.findAll()),
          tap(data => this.reservaService.setListChange(data)),
          tap(() => this.reservaService.setMessageChange('Reserva cancelada correctamente'))
        )
        .subscribe({
          error: () => this.mostrarError('No se pudo cancelar la reserva'),
        });
    }
  }

  nombresEquipos(reserva: Reserva): string {
    return reserva.equipos && reserva.equipos.length
      ? reserva.equipos.map(equipo => equipo.nombre).join(', ')
      : 'Sin equipos';
  }

  eliminar(id: number): void {
    if (confirm('Esta seguro de eliminar esta reserva?')) {
      this.reservaService.delete(id)
        .pipe(
          switchMap(() => this.reservaService.findAll()),
          tap(data => this.reservaService.setListChange(data)),
          tap(() => this.reservaService.setMessageChange('Reserva eliminada correctamente'))
        )
        .subscribe({
          error: () => this.mostrarError('No se pudo eliminar la reserva'),
        });
    }
  }

  limpiar(): void {
    this.reserva = new Reserva();
    this.reserva.total = 0;
    this.reserva.abono = 0;
    this.reserva.saldo = 0;
    this.reserva.metodoPago = 'EFECTIVO';
    this.reserva.estado = 'CONFIRMADA';
    this.reserva.horaInicio = '10:00';
    this.reserva.horaFin = '11:00';
    this.reserva.equipos = [];
    this.detalles.set([]);
    this.selectedServicio = null;
    this.cantidadHoras = 1;
    this.selectedDate = new Date();

    const offset = this.selectedDate.getTimezoneOffset();
    const localDate = new Date(this.selectedDate.getTime() - (offset * 60 * 1000));
    this.reserva.fecha = `${localDate.toISOString().split('T')[0]}T${this.reserva.horaInicio}:00`;
    this.pagoParcial = new Pago();
    this.pagoParcial.metodoPago = 'EFECTIVO';

    this.hasConflict.set(false);
    this.abonoInvalido.set(false);
  }

  private cargarCatalogos(): void {
    this.clienteService.findAll().subscribe({
      next: data => this.clientes.set(data),
      error: () => this.mostrarError('No se pudo cargar clientes'),
    });
    this.salaService.findAll().subscribe({
      next: data => this.salas.set(data.filter(s => s.estado)),
      error: () => this.mostrarError('No se pudo cargar salas'),
    });
    this.servicioService.findAll().subscribe({
      next: data => this.servicios.set(data),
      error: () => this.mostrarError('No se pudo cargar servicios'),
    });
  }

  private cargarReservas(): void {
    this.reservaService.findAll().subscribe({
      next: data => this.reservaService.setListChange(data),
      error: () => this.mostrarError('No se pudo cargar el historial de reservas'),
    });
  }

  private cargarEquiposDisponibles(): void {
    const fecha = (this.reserva.fecha || '').split('T')[0];
    if (!fecha || !this.reserva.horaInicio || !this.reserva.horaFin) {
      return;
    }

    this.equipoService.disponibles(fecha, this.reserva.horaInicio, this.reserva.horaFin)
      .subscribe({
        next: data => this.equipos.set(data),
        error: () => {
          this.equipoService.findAll().subscribe({
            next: data => this.equipos.set(data.filter(e => e.estado === 'DISPONIBLE')),
            error: () => this.mostrarError('No se pudo cargar equipos disponibles'),
          });
        },
      });
  }

  private validarDisponibilidadBackend(): void {
    if (!this.reserva.sala?.idSala || !this.reserva.fecha || !this.reserva.horaInicio || !this.reserva.horaFin) {
      return;
    }

    const fecha = this.reserva.fecha.split('T')[0];
    this.reservaService.disponibilidad(this.reserva.sala.idSala, fecha, this.reserva.horaInicio, this.reserva.horaFin, this.reserva.idReserva)
      .subscribe({
        next: data => this.hasConflict.set(!data.disponible),
        error: () => this.mostrarError('No se pudo validar la disponibilidad'),
      });
  }

  private actualizarHoraFin(): void {
    const horas = this.detalles().reduce((sum, det) => sum + (det.cantidadHoras || 0), 0);
    this.reserva.horaFin = this.addHours(this.reserva.horaInicio || '10:00', Math.max(horas, 1));
  }

  private addHours(time: string, hours: number): string {
    const minutes = this.timeToMinutes(time) + hours * 60;
    const normalized = minutes % (24 * 60);
    const hh = Math.floor(normalized / 60).toString().padStart(2, '0');
    const mm = (normalized % 60).toString().padStart(2, '0');
    return `${hh}:${mm}`;
  }

  private timeToMinutes(time: string): number {
    const [hours, minutes] = time.split(':').map(Number);
    return (hours || 0) * 60 + (minutes || 0);
  }

  private toInputDate(date: Date): string {
    const offset = date.getTimezoneOffset();
    const localDate = new Date(date.getTime() - offset * 60 * 1000);
    return localDate.toISOString().split('T')[0];
  }

  private mostrarError(message: string): void {
    this.snackBar.open(message, 'Cerrar', { duration: 4000 });
  }
}
