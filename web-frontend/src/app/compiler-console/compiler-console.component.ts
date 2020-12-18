import { Component, OnInit } from '@angular/core';
import { CompilerService } from '../services/compiler.service';

@Component({
  selector: 'app-compiler-console',
  templateUrl: './compiler-console.component.html',
  styleUrls: ['./compiler-console.component.scss']
})
export class CompilerConsoleComponent implements OnInit {

  sub: any;
  data: string;

  constructor(private compilerService: CompilerService) {
    this.sub = compilerService.compilerEvent.subscribe((value) => {
      this.data = value.errors;
    });
  }

  ngOnDestroy() {
    this.sub.unsubsribe();
  }
  ngOnInit() {
  }

}
