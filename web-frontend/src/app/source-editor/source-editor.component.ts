import { Component, ComponentFactoryResolver, OnInit, ViewChild } from "@angular/core";
import { Source } from "@app/services/source";
import { SourceService } from "../services/source.service";
import { ErrorCheckService } from "../services/error-check.service";
import { map, mergeMap, catchError } from "rxjs/operators";
import { of } from "rxjs";
import { ErrorCheckResponse } from "@app/services/error";
import { switchMap } from "rxjs/operators";
import { Router, ActivatedRoute, ParamMap } from "@angular/router";
import { PrettyPrinterService } from "@app/services/pretty-printer.service";
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: "app-source-editor",
  templateUrl: "./source-editor.component.html",
  styleUrls: ["./source-editor.component.scss"],
})
export class SourceEditorComponent implements OnInit {
  private source: Source;
  protected error: ErrorCheckResponse;
  protected lines: string[];
  @ViewChild('sourceEditorCode', { static : true} ) sourceEditorCode; 

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private sourceService: SourceService,
    private errorCheckService: ErrorCheckService,
    private prettyPrintService: PrettyPrinterService,
    private window: Window,
    private document: Document
  ) {}

  setSource(source: Source) {
    this.source = source;
    this.setLines(source.code);
  }

  setErrors(error: ErrorCheckResponse) {
    this.error = error;
  }

  setLines(formatted: string) {
    console.log(formatted);
    this.lines = formatted.split('\n');
  }

  clean() {
   // this.prettyPrintService
     // .prettyPrint(this.formattedCode)
  //    .subscribe((s) => this.setFormattedSource(s));
  }

  new() {
    this.sourceService.createSource(this.source);
  }

  save() {
    this.sourceService.updateSource(this.source);
  }

  deleteConfirm() {
    this.sourceService.deleteSource(this.source);
  }

  delete() {}

  private getSource() : String {
    let arg = this.sourceEditorCode.nativeElement;
    let code = '';
    for(const node of arg.children) {
      const s = node.innerText;
      code += '\n';
      code += s;
    }
    return code;
  }

  onInput(arg) {

  }


  //https://stackoverflow.com/questions/34091730/get-and-set-cursor-position-with-contenteditable-div/34214130

  onPaste(event: ClipboardEvent) {
    //https://javascript.info/selection-range
    event.preventDefault();
    event.stopPropagation();
    let selected = this.window.getSelection();
    let selectedRange = selected.getRangeAt(0);
    let startPos = 0, endPos = 0;
    if(selectedRange.collapsed) {
      // need to handle new lines in the source.
      for(const node of this.sourceEditorCode.nativeElement.children) {
        if(node === selected.anchorNode.parentNode) {
          startPos += selected.anchorOffset;
          break;
        } else {
          startPos += node.innerText.length;
      //    if(node.className && node.className === 'line') {
            startPos++;
        //  }
        }
      }
      endPos = startPos;
    } else {

    }
    let source = this.getSource();
    let head = source.substring(0, startPos);
    let tail = source.substring(endPos, source.length);
    let clipboardData = event.clipboardData;
    let newText = clipboardData.getData('text');
    let merged = head + newText + tail;
    this.setLines(merged);
  }

  onChange(event: Event) {
    let code = this.getSource();
    let errorCheckRequest = {};
    this.errorCheckService.check(errorCheckRequest).subscribe((error) => this.setErrors(error));
  }

  onClick(event: Event) {
   // this.lines = [];
  }

  ngOnInit() {
    //https://angular.io/guide/router-tutorial-toh
    let errorCheckRequest = {};
    this.sourceService
      .getSource("someid")
      .pipe(
        map((source) => this.setSource(source)),
        mergeMap(() => this.errorCheckService.check(errorCheckRequest))
      )
      .subscribe((error) => (this.setErrors(error)));
  }
}
