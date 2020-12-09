import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { CompilerService } from './compiler.service';
import { Assembly } from './assembly';
import * as ace from "ace-builds";
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements AfterViewInit {
  title = 'ace-tiger-ui';
  tiger:string = "let\nin\n\tprint(\"Hello World\")\nend";
  assembly:string = "tbc";
  @ViewChild('sourceEditor', { static: false }) sourceEditor;
  @ViewChild('assemblyEditor', { static: false }) assemblyEditor;

  constructor(private compilerService: CompilerService) {}

  ngAfterViewInit() {
    console.log(this.sourceEditor);
    ace.config.set("fontSize", "19px");
    const aceEditor = ace.edit(this.sourceEditor.nativeElement);
  }

  getAssembly(code: string): Observable<Assembly> {
    return this.compilerService.compile({
      code: code,
      args: null
    });
  }

  onChange(event): void {
    this.getAssembly(this.tiger).subscribe(
      result => {
        this.assembly = result.assembly;
      }
    );
  }
}
