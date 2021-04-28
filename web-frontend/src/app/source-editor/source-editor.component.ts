import {
  Component,
  ComponentFactoryResolver,
  ElementRef,
  OnInit,
  ViewChild,
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
})
export class SourceEditorComponent implements OnInit {
  private source: Source;
  protected error: ErrorCheckResponse;
  protected lines: string[];
  protected caretPos: number;

   model = 'one\ntwo\three<div>ine</div>';

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
    this.setLines(source.code);
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
    console.log(this.getSource());
  }

  deleteConfirm() {
    this.sourceService.deleteSource(this.source);
  }

  delete() {
    this.sourceService.deleteSource(this.source);
  }

  private getSource(): string {
    let editorDiv = this.sourceEditorCode.nativeElement;
    let code = "";
    for (const node of editorDiv.children) {
        const s = node.innerText + "\n";
        code += s;
    }
    return code;
  }

  getCaret(el) {
    const range = window.getSelection().getRangeAt(0);
    const prefix = range.cloneRange();
    prefix.selectNodeContents(el);
    prefix.setEnd(range.endContainer, range.endOffset);
    return prefix.toString().length;
  }

  setCaret(pos, parent) {
    for (const node of parent.childNodes) {
      if (node.nodeType == Node.TEXT_NODE) {
        // current node length is greater or equal to pos
        // which means pos must be contained in the current node.
        if (node.length >= pos) {
          const range = document.createRange();
          const sel = window.getSelection();
          range.setStart(node, pos);
          range.collapse(true);
          sel.removeAllRanges();
          sel.addRange(range);
          return -1;
        } else {
          // remove length from pos and continue iteration through children
          pos = pos - node.length;
        }
      } else {
        // node is not a text node, recurse into that node
        // and get the position. If pos is -1 then we have
        // found the node. We can terminate the recusion.
        pos = this.setCaret(pos, node);
        if (pos < 0) {
          return pos;
        }
      }
    }
    return pos;
  }

  onKeyUp(event: KeyboardEvent) {
   // if (event.keyCode >= 0x30 || event.keyCode == 0x20) {
      const pos = this.getCaret(this.sourceEditorCode.nativeElement);
      this.caretPos = pos;
   //   this.lines = [];
      for (const node of this.sourceEditorCode.nativeElement.children) {
  //       this.lines.push(node.innerText);
      }
     this.setCaret(10, this.sourceEditorCode.nativeElement);
  //}
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
    return;
    let code = this.getSource();
    let errorCheckRequest = {};
    this.errorCheckService
      .check(errorCheckRequest)
      .subscribe((error) => this.setErrors(error));
  }

  //https://stackoverflow.com/questions/34091730/get-and-set-cursor-position-with-contenteditable-div/34214130

  findLine(node) {
    while (node.parentNode != null) {
      if (node.className && node.className === "line") {
        return node;
      }
      node = node.parentNode;
    }
    throw "no parent line class found";
  }

  onPaste(event: ClipboardEvent) {
    return;
    event.preventDefault();
    event.stopPropagation();
    let selected = this.window.getSelection();
    let selectedRange = selected.getRangeAt(0);
    let startPos = 0,
      endPos = 0;
    // get the line for the selected element.
    const lineStartNode = this.findLine(selected.anchorNode);
    // get all the line divs for the source editor.
    for (const node of this.sourceEditorCode.nativeElement.children) {
      // need to traverse selected to div with line class
      if (node === lineStartNode /*selected.anchorNode.parentNode*/) {
        startPos += selected.anchorOffset;
        break;
      } else {
        startPos += node.innerText.length;
        if (node.className && node.className === "line") {
          startPos++;
        }
      }
    }
    if (selectedRange.collapsed) {
      endPos = startPos;
    } else {
      const lineEndNode = this.findLine(selected.focusNode);
      for (const node of this.sourceEditorCode.nativeElement.children) {
        if (node === lineEndNode) {
          endPos += selected.focusOffset;
          break;
        } else {
          endPos += node.innerText.length;
          if (node.className && node.className === "line") {
            endPos++;
          }
        }
      }
    }
    let source = this.getSource();
    let head = source.substring(0, Math.min(startPos, endPos));
    let tail = source.substring(Math.max(startPos, endPos), source.length - 1);
    let clipboardData = event.clipboardData;
    let newText = clipboardData.getData("text");
    let merged = head + newText + tail;
    this.setLines(merged);
    //  selectedRange.collapse(true);
    //   selectedRange.setStart(selectedRange.startContainer, newText.length );
    // selected.removeAllRanges();
    // selected.addRange(selectedRange);
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
      .subscribe((error) => this.setErrors(error));
  }
}
