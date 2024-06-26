import { Inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Assembly } from "../assembly";
import { BaseService } from "../core/base.service";
import { Observable, Subject } from "rxjs";
import { mergeMap, map } from "rxjs/operators";
import { ENV_CONFIG, EnvironmentConfig } from "../core/env-config.interface";

@Injectable({
  providedIn: "root",
})
export class CompilerService extends BaseService {
  compilerEvent: Subject<Assembly> = new Subject<Assembly>();
  compilerRequest: Subject<any> = new Subject<any>();
  code: string;
  public apiUrl: string;

  constructor(
    @Inject(ENV_CONFIG) private config: EnvironmentConfig,
    protected http: HttpClient
  ) {
    super();
    this.apiUrl = `${config.environment.baseUrl}`;
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
    return this.http.post<Assembly>(url, body).pipe(
      map((data) => {
        this.compilerEvent.next(data);
        return data;
      })
    );
  }
}
