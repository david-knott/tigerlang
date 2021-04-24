import { Injectable } from "@angular/core";
import { Source } from "./source";
import { Observable, of } from "rxjs";

export const SOURCES: Source[] = [
  { id: 11, name: "Hello World", description: "", code: "let\n    var: hello := \"world\"\nin\n    print(hello)\nend" },
];

@Injectable({
  providedIn: "root",
})
export class SourceService {
  constructor() {}

  getSource(id: String): Observable<Source> {
    const source = of(SOURCES[0]);
    return source;
  }

  getSources(): Observable<Source[]> {
    const sources = of(SOURCES);
    return sources;
  }

  createSource(source: Source) {
    console.log('create');
  }

  updateSource(source: Source) {
    console.log('save');
  }

  deleteSource(source: Source) {
    console.log('delete');
  }
}
