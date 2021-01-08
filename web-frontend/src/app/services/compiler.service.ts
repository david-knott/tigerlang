import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Assembly } from "../assembly";
import { BaseService } from "../core/base.service";
import { Observable, Subject } from "rxjs";
import { mergeMap, map } from 'rxjs/operators';

@Injectable({
  providedIn: "root",
})
export class CompilerService extends BaseService {
  compilerEvent: Subject<Assembly> = new Subject<Assembly>();
  compilerRequest: Subject<any> = new Subject<any>();
  constructor(protected http: HttpClient) {
    super();
    //there is a compiler request.
  }

  compilationRequest(body: any) {
    this.compilerRequest.next(body);
  }

  compile(body: any): Observable<Assembly> {
    const url = "http://localhost:8080/compile";
    console.log(body);
    return this.http.post<Assembly>(url, body).pipe(map(data => {
      this.compilerEvent.next(data);
      return data;
    }));
  }
}
