import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
  ViewEncapsulation,
} from "@angular/core";

@Component({
  selector: "app-editor",
  templateUrl: "./editor.component.html",
  styleUrls: ["./editor.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class EditorComponent implements OnInit, OnChanges {
  @Input() content: String;
  @Input() editable!: boolean;
  @Input() highlightedLines!: Array<number>;
  @Output() newItemEvent = new EventEmitter<string>();
  private elementMouseIsOver: Element;
  protected lines: string[];

  @ViewChild("editor", { static: true }) editor: ElementRef;

  buildHtml() {
    const parent = this.editor.nativeElement;
    this.content.split("\n").forEach((value, index) => {
      parent.innerHTML += '<div class="line">' + value + '</div>';
    });
    this.highlightKeywords();
  }

  highlightKeywords() {
    let offset = 0;
    const parent = this.editor.nativeElement;
    for (const node of parent.children) {
      let replacements = node.innerText;
      replacements = replacements
        .replace(
          /\b(let|in|end|function|var|for|while|if|then|do|array|type)\b/g,
          '<span class="keyword">$1</span>'
        )
        .replace(/(:\=)/g, '<span class="special">$1</span>')
        .replace(/(\(|\))/g, '<span class="brace">$1</span>');
      offset += node.innerText.length;
      node.innerHTML = replacements.split("\n").join("<br/>");
    }
  }

  getCaret() {
    const el = this.editor.nativeElement;
    const range = window.getSelection().getRangeAt(0);
    const prefix = range.cloneRange();
    prefix.selectNodeContents(el);
    prefix.setEnd(range.endContainer, range.endOffset);
    return prefix.toString().length;
  }

  setCaret(pos, parent) {
    for (const node of parent.childNodes) {
      // text node, do not recur, return -1
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

  setLines(formatted: string) {
    this.lines = formatted
      .split("\n")
      .map((m) => (m.length > 0 ? m : "&#8204"));
  }

  onKeyUp(event: KeyboardEvent) {
    if (event.keyCode >= 0x30 || event.keyCode == 0x20) {
      const pos = this.getCaret();
      this.highlightKeywords();
      const el = this.editor.nativeElement;
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
    let code = [...this.editor.nativeElement.children]
      .map((m) => m.innerText)
      .join("\n");

    this.newItemEvent.emit("notify listeners text has changed");
  }

  onPaste(event: ClipboardEvent) {
    const el = this.editor.nativeElement;
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
    text.split("\n").forEach((line) => {
      //direct dom manipulation, bad...
      el.innerHTML += '<div class="line">' + line + "</div>";
    });
    this.highlightKeywords();
    this.setCaret(pos, el);
  }

  onMouseOver(event: MouseEvent) {
    var x = event.clientX,
      y = event.clientY;
    this.elementMouseIsOver = document.elementFromPoint(x, y);
    this.elementMouseIsOver = this.elementMouseIsOver.closest(".line");
    if (!!this.elementMouseIsOver) {
      this.elementMouseIsOver.classList.add("over");
      this.newItemEvent.emit("notify listeners mouse has moved over something");
    }
  }

  onMouseOut(event: MouseEvent) {
    if (!!this.elementMouseIsOver) {
      this.elementMouseIsOver.classList.remove("over");
      this.elementMouseIsOver = null;
      this.newItemEvent.emit(
        "notify listeners mouse has moved away from something"
      );
    }
  }

  ngOnInit() {
    this.buildHtml();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const parent = this.editor.nativeElement;
    let lines = parent.getElementsByClassName("line");
    if (changes.highlightedLines.currentValue) {
      changes.highlightedLines.currentValue.forEach((e) => {
        console.log(e, lines.length);
        if (e <= lines.length) {
          lines.item(e - 1).classList.add("over");
        }
      });
    }
  }
}
