import {
  Component,
  ComponentFactoryResolver,
  ElementRef,
  OnInit,
  ViewChild,
  ViewEncapsulation,
} from "@angular/core";
import { Source } from "@app/services/source";
import { SourceService } from "../services/source.service";
import { ErrorCheckService } from "../services/error-check.service";
import { map, mergeMap, catchError } from "rxjs/operators";
import { of } from "rxjs";
import { ErrorCheckResponse } from "@app/services/error";
import { switchMap } from "rxjs/operators";
import { Router, ActivatedRoute, ParamMap } from "@angular/router";
import { PrettyPrinterService } from "@app/services/pretty-printer.service";
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { ThrowStmt } from "@angular/compiler";

@Component({
  selector: "app-source-editor",
  templateUrl: "./source-editor.component.html",
  styleUrls: ["./source-editor.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class SourceEditorComponent implements OnInit {
  private source: Source;
  protected error: ErrorCheckResponse;
  protected lines: string[];
  protected caretPos: number;

  protected codeToHtml = "";

  @ViewChild("sourceEditorCode", { static: true }) sourceEditorCode: ElementRef;

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
    const parent = this.sourceEditorCode.nativeElement;
    source.code.split("\n").forEach((f) => {
      parent.innerHTML += '<div class="line">' + f + "</div>";
    });
    this.highlight();
  }

  highlight() {
    const parent = this.sourceEditorCode.nativeElement;
    for (const node of parent.children) {
      const replacements = node.innerText
        .replace(
          /\b(let|in|end|function|var|for|while|if|then|do|array|type)\b/g,
          '<span class="keyword">$1</span>'
        )
        .replace(/(:\=)/g, '<span class="special">$1</span>')
        .replace(/(\(|\))/g, '<span class="brace">$1</span>');
      node.innerHTML = replacements.split("\n").join("<br/>");
    }
  }

  getCaret() {
    const el = this.sourceEditorCode.nativeElement;
    const range = window.getSelection().getRangeAt(0);
    const prefix = range.cloneRange();
    prefix.selectNodeContents(el);
    prefix.setEnd(range.endContainer, range.endOffset);
    return prefix.toString().length;
  }

  setCaret(pos, parent) {
    for (const node of parent.childNodes) {
      // text node, do not recur, return -1 to
      if (node.nodeType == Node.TEXT_NODE) {
        if (node.length >= pos) {
          const range = document.createRange();
          const sel = window.getSelection();
          range.setStart(node, pos);
          range.collapse(true);
          sel.removeAllRanges();
          sel.addRange(range);
          return -1;
        } else {
          pos = pos - node.length;
        }
      } else {
        //not text node, try recurse
        pos = this.setCaret(pos, node);
        // less that zero means that we found a text node
        if (pos < 0) {
          return pos;
        }
      }
    }
    return pos;
  }

  setErrors(error: ErrorCheckResponse) {
    this.error = error;
  }

  setLines(formatted: string) {
    this.lines = formatted
      .split("\n")
      .map((m) => (m.length > 0 ? m : "&#8204"));
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

  delete() {
    this.sourceService.deleteSource(this.source);
  }

  onKeyUp(event: KeyboardEvent) {
    if (event.keyCode >= 0x30 || event.keyCode == 0x20) {
      const pos = this.getCaret();
      this.highlight();
      const el = this.sourceEditorCode.nativeElement;
      this.setCaret(pos, el);
    }
  }

  onKeyDown(event: KeyboardEvent) {
    if (event.keyCode === 9) {
      event.preventDefault();
      let sel = document.getSelection();
      let range = sel.getRangeAt(0);
      var tabNode = document.createTextNode("\u00a0\u00a0\u00a0\u00a0");
      range.insertNode(tabNode);
      range.setStartAfter(tabNode);
      range.setEndAfter(tabNode);
      sel.removeAllRanges();
      sel.addRange(range);
      event.preventDefault();
    }
  }

  onChange(event: Event) {
    let code = [...this.sourceEditorCode.nativeElement.children].map( m => m.innerText).join('\n');
    let errorCheckRequest = {tiger: code};
    this.errorCheckService
      .check(errorCheckRequest)
      .subscribe((error) => this.setErrors(error));
  }

  //https://stackoverflow.com/questions/34091730/get-and-set-cursor-position-with-contenteditable-div/34214130
  onPaste(event: ClipboardEvent) {
    const el = this.sourceEditorCode.nativeElement;
    event.stopPropagation();
    event.preventDefault();
    const pos = this.getCaret();
    let selected = window.getSelection();
    let range = selected.getRangeAt(0);
    let paste = event.clipboardData.getData("text");
    //remove selected text from document.
    selected.deleteFromDocument();
    range.insertNode(document.createTextNode(paste));
    // get the new text, split it and reset div content.
    const text = el.innerText;
    el.innerHTML = "";
    text.split("\n").forEach((f) => {
      //direct dom manipulation, bad...
      el.innerHTML += '<div class="line">' + f + "</div>";
    });
    this.highlight();
    this.setCaret(pos, el);
  }

  ngOnInit() {
    this.sourceService
      .getSource("someid")
      .pipe(
        map((source) => this.setSource(source)),
        mergeMap(() => this.errorCheckService.check({tiger: this.source.code}))
      )
      .subscribe((error) => this.setErrors(error));
  }
}
