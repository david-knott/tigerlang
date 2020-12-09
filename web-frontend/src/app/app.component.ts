import { Component } from '@angular/core';
import { Observable, of } from 'rxjs';
import { CompilerService } from './compiler.service';
import { Assembly } from './assembly';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'ace-tiger-ui';
  tiger:string = "let\nin\n\tprint(\"Hello World\")\nend";
  assembly:string = "tbc";

  constructor(private compilerService: CompilerService) {}

  getAssembly(code: string): Observable<Assembly> {
    return this.compilerService.compile({
      code: code,
      args: null
    });
  }

  onChange(event): void {
    console.log(event);
    this.getAssembly(this.tiger).subscribe(
      result => {
        this.assembly = result.assembly;
      }
    );
  }
}
