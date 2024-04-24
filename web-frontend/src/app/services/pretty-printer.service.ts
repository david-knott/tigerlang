import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class PrettyPrinterService {

  constructor() { }

  prettyPrint(source: string) {
    return of( source );
  }
}
