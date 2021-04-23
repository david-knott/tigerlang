import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { TigerError } from './error';

export const ERRORS: TigerError[] = [
  { name: "Dr Nice", count: 10},
];

@Injectable({
  providedIn: 'root'
})
export class ErrorCheckService {

  constructor() { }

  check() {
    console.log('calling error checker');
    return of(ERRORS[0]);
  }
}
