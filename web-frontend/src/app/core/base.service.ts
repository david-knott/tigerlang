import { throwError } from 'rxjs';

export abstract class BaseService {
  constructor() { }

  handleError(error: any) {
    // In a real world app, we might send the error to remote logging infrastructure
    // and reformat for user consumption
    return throwError(error);
  }
}