import { Inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Assembly } from "../assembly";
import { BaseService } from "../core/base.service";
import { Observable, Subject } from "rxjs";
import { mergeMap, map } from 'rxjs/operators';
import { environment } from '@env/environment';

@Injectable({
  providedIn: "root",
})
export class CompilerService extends BaseService {
  compilerEvent: Subject<Assembly> = new Subject<Assembly>();
  compilerRequest: Subject<any> = new Subject<any>();
  code: string;
  public apiUrl: string;

  constructor(protected http: HttpClient) {
    super();
    this.apiUrl = `${environment.baseUrl}`;
    console.log("XX" + this.apiUrl);
    //there is a compiler request.
  }

  setCode(code: string): void {
    this.code = code;
  }

  getCode(): string {
    return this.code;
  }

  compilationRequest(body: any) {
    this.compilerRequest.next(body);
  }

  compile(body: any): Observable<Assembly> {
    const url = `${this.apiUrl}/compile`;
    return this.http.post<Assembly>(url, body).pipe(map(data => {
      this.compilerEvent.next(data);
      return data;
    }));
  }
}
