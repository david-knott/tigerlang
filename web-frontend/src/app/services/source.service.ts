import { Injectable } from "@angular/core";
import { Source } from "./source";
import { Observable, of } from "rxjs";

export const SOURCES: Source[] = [
  { id: 11, name: "Empty", description: "", code: "/* empty file */" },
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

  createSource(source: Source) {}

  updateSource(source: Source) {}
}
