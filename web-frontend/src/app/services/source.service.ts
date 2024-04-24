import { Injectable } from "@angular/core";
import { Source } from "./source";
import { Observable, of } from "rxjs";

export const SOURCES: Source[] = [
  {
    id: "xxx",
    name: "Hello World",
    description: "",
    code: 'let\n    var: hello := "world"\nin\n    print(hello)\nend',
  },
];

@Injectable({
  providedIn: "root",
})
export class SourceService {
  constructor() {}

  getSource(id: String): Observable<Source> {
    const source = of(SOURCES[0]);
    console.log("loading " + id);
    return source;
  }

  getSources(): Observable<Source[]> {
    const sources = of(SOURCES);
    return sources;
  }

  createSource(source: Source): Observable<Source> {
    console.log("create");
    return of(source);
  }

  updateSource(source: Source): Observable<Source> {
    console.log("save");
    return of(source);
  }

  deleteSource(source: Source): Observable<Source> {
    console.log("delete");
    return of(source);
  }
}
