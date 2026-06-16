import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { ClienteFrecuente, EquipoUsado, IngresoPorFecha, OcupacionSala, PagoPendiente, ReporteResumen, ReservasPorFecha, ServicioSolicitado } from '../../model/reporte';
import { ReporteService } from '../../services/reporte.service';

@Component({
  selector: 'app-reporte',
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatSnackBarModule,
    MatTableModule,
  ],
  templateUrl: './reporte.component.html',
  styleUrl: './reporte.component.css',
})
export class ReporteComponent {
  private readonly reporteService = inject(ReporteService);
  private readonly snackBar = inject(MatSnackBar);

  inicio = '';
  fin = '';
  isLoading = signal(false);
  resumen = signal<ReporteResumen>({ totalReservas: 0, totalClientes: 0, ingresos: 0, saldosPendientes: 0 });
  ingresos = signal<IngresoPorFecha[]>([]);
  reservas = signal<ReservasPorFecha[]>([]);
  pagosPendientes = signal<PagoPendiente[]>([]);
  servicios = signal<ServicioSolicitado[]>([]);
  ocupacionSalas = signal<OcupacionSala[]>([]);
  clientesFrecuentes = signal<ClienteFrecuente[]>([]);
  equiposMasUsados = signal<EquipoUsado[]>([]);

  columnasIngresos = ['fecha', 'total'];
  columnasReservas = ['fecha', 'cantidad'];
  columnasPendientes = ['idReserva', 'cliente', 'sala', 'fecha', 'saldo'];
  columnasServicios = ['servicio', 'cantidadReservas', 'horasVendidas', 'totalGenerado'];
  columnasOcupacion = ['sala', 'reservas', 'horasOcupadas'];
  columnasClientes = ['cliente', 'reservas', 'totalGastado'];
  columnasEquipos = ['equipo', 'reservas'];

  constructor() {
    const hoy = new Date();
    const primerDia = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
    this.inicio = this.toInputDate(primerDia);
    this.fin = this.toInputDate(hoy);
    this.cargar();
  }

  cargar(): void {
    this.isLoading.set(true);
    forkJoin({
      resumen: this.reporteService.resumen(this.inicio, this.fin),
      ingresos: this.reporteService.ingresos(this.inicio, this.fin),
      reservas: this.reporteService.reservas(this.inicio, this.fin),
      pagosPendientes: this.reporteService.pagosPendientes(),
      servicios: this.reporteService.serviciosMasSolicitados(this.inicio, this.fin),
      ocupacionSalas: this.reporteService.ocupacionSalas(this.inicio, this.fin),
      clientesFrecuentes: this.reporteService.clientesFrecuentes(this.inicio, this.fin),
      equiposMasUsados: this.reporteService.equiposMasUsados(this.inicio, this.fin),
    }).subscribe({
      next: data => {
        this.resumen.set(data.resumen);
        this.ingresos.set(data.ingresos);
        this.reservas.set(data.reservas);
        this.pagosPendientes.set(data.pagosPendientes);
        this.servicios.set(data.servicios);
        this.ocupacionSalas.set(data.ocupacionSalas);
        this.clientesFrecuentes.set(data.clientesFrecuentes);
        this.equiposMasUsados.set(data.equiposMasUsados);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        this.snackBar.open('No se pudieron cargar los reportes', 'Cerrar', { duration: 4000 });
      },
    });
  }

  private toInputDate(date: Date): string {
    const offset = date.getTimezoneOffset();
    const localDate = new Date(date.getTime() - offset * 60 * 1000);
    return localDate.toISOString().split('T')[0];
  }
}
