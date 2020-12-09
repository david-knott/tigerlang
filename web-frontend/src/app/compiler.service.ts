import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Assembly } from "./assembly";
import { BaseService } from "./base.service";
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CompilerService extends BaseService {
  constructor(protected http: HttpClient) {
    super();
  }

  compile(body: any): Observable<Assembly> {
    const url = "http://localhost:10000/compile";
    return this.http.post<Assembly>(url, body);//.pipe(super.catchError(this.handleError));
  }
}
