import { Component, OnInit } from "@angular/core";
import { Source } from "@app/services/source";
import { SourceService } from "../services/source.service";
import { ErrorCheckService } from "../services/error-check.service";
import { map, mergeMap, catchError } from "rxjs/operators";
import { of } from "rxjs";
import { TigerError } from "@app/services/error";
import { switchMap } from "rxjs/operators";
import { Router, ActivatedRoute, ParamMap } from "@angular/router";
import { PrettyPrinterService } from "@app/services/pretty-printer.service";

@Component({
  selector: "app-source-editor",
  templateUrl: "./source-editor.component.html",
  styleUrls: ["./source-editor.component.scss"],
})
export class SourceEditorComponent implements OnInit {
  private source: Source;
  private error: TigerError;
  protected formattedCode: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private sourceService: SourceService,
    private errorCheckService: ErrorCheckService,
    private prettyPrintService: PrettyPrinterService
  ) {}

  setSource(source: Source) {
    this.source = source;
    this.formattedCode = source.code;
  }

  setFormattedSource(formatted: string) {
    this.formattedCode = formatted;
  }

  clean() {
    console.log('clean');
    this.prettyPrintService
      .prettyPrint(this.formattedCode)
      .subscribe((s) => this.setFormattedSource(s));
  }

  new() {}

  save() {}

  deleteConfirm() {}

  delete() {}

  ngOnInit() {
    //https://angular.io/guide/router-tutorial-toh
    this.sourceService
      .getSource("someid")
      .pipe(
        map((source) => this.setSource(source)),
        mergeMap(() => this.errorCheckService.check())
      )
      .subscribe((error) => (this.error = error));
  }
}
