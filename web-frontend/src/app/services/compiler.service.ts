import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Assembly } from "../assembly";
import { BaseService } from "../core/base.service";
import { Observable, Subject } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class CompilerService extends BaseService {
  compilerEvent: Subject<Assembly> = new Subject<Assembly>();
  constructor(protected http: HttpClient) {
    super();
  }

  compile(body: any): void {
    const url = "http://localhost:10000/compile";
    this.http.post<Assembly>(url, body)
      .subscribe(res => {
          this.compilerEvent.next(res);
      });
  }
}