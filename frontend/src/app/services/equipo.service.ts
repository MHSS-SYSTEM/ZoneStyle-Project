import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment.development';
import { Equipo } from '../model/equipo';
import { GenericSignalService } from './generic-signal.service';

@Injectable({
  providedIn: 'root',
})
export class EquipoService extends GenericSignalService<Equipo> {
  protected override url: string = `${environment.HOST}/equipos`;

  listPageable(p: number, s: number) {
    return this.http.get<any>(`${this.url}/pageable?page=${p}&size=${s}`);
  }

  disponibles(fecha: string, horaInicio: string, horaFin: string) {
    const params = new HttpParams()
      .set('fecha', fecha)
      .set('horaInicio', horaInicio)
      .set('horaFin', horaFin);
    return this.http.get<Equipo[]>(`${this.url}/disponibles`, { params });
  }
}
