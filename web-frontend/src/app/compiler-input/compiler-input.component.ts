import { Component, OnInit } from "@angular/core";
import { CompilerService } from "../services/compiler.service";

@Component({
  selector: "app-compiler-input",
  templateUrl: "./compiler-input.component.html",
  styleUrls: ["./compiler-input.component.scss"],
})
export class CompilerInputComponent implements OnInit {
  data: string = 'print("Hello World")';

  constructor(private compilerService: CompilerService) {}
  ngOnInit(): void {

    this.compilerService.compilationRequest({ code: this.data, args: null });
  }

  inputChanged($event): void {
    // notify the output window to call the compiler...
    this.compilerService.compilationRequest({ code: $event, args: null });
  }
}
