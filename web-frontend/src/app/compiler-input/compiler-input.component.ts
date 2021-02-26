import { Component, OnInit } from "@angular/core";
import { CompilerService } from "../services/compiler.service";

@Component({
  selector: "app-compiler-input",
  templateUrl: "./compiler-input.component.html",
  styleUrls: ["./compiler-input.component.scss"],
})
export class CompilerInputComponent implements OnInit {
  data: string = 'let function add():int = (let var a:= 3 var b:= 4 in a + b end) in add() end' ;

  constructor(private compilerService: CompilerService) {}
  ngOnInit(): void {
    this.compilerService.setCode(this.data);
    this.compilerService.compilationRequest({ code: this.data, args: null });
  }

  inputChanged($event): void {
    // notify the output window to call the compiler...
    this.compilerService.setCode($event);
    this.compilerService.compilationRequest({ code: $event, args: null });
  }
}
