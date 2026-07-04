import { environment } from '../../environments/environment.development';
import { Injectable } from '@angular/core';
import { Cliente } from '../model/cliente';
import { GenericSignalService } from './generic-signal.service';

@Injectable({
  providedIn: 'root',
})
export class ClienteService extends GenericSignalService<Cliente> {
  protected override url: string = `${environment.HOST}/clientes`;

  listPageable(p: number, s: number) {
    return this.http.get<any>(`${this.url}/pageable?page=${p}&size=${s}`);
  }
}
