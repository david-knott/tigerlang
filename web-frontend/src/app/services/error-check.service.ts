import { Injectable } from "@angular/core";
import { of } from "rxjs";
import { min } from "rxjs/operators";
import { ErrorCheckResponse } from "./error";
import { ErrorCheckRequest } from "./error-check-request";

export const ERRORS: ErrorCheckResponse[] = [
  { items: [{ name: "Dr Nice", line: 0, col: 0 }] },
];

/**
 * Encapsulates the response from a request to check source
 * code for errors.
 */
@Injectable({
  providedIn: "root",
})
export class ErrorCheckService {
  constructor() {}

  check(request: ErrorCheckRequest) {
    console.log("calling error checker ", request);
    return of( request.tiger.length > 10 ? ERRORS[0] : {items: []});
  }
}
