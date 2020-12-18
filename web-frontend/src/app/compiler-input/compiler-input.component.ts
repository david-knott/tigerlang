import { Component, OnInit } from "@angular/core";
import { CompilerService } from "../services/compiler.service";

@Component({
  selector: "app-compiler-input",
  templateUrl: "./compiler-input.component.html",
  styleUrls: ["./compiler-input.component.scss"],
})
export class CompilerInputComponent {
  data: string = "print(\"Hello World\")";

  constructor(private compilerService: CompilerService) {}

  inputChanged($event): void {
    this.compilerService.compile({ code: $event, args: null});
    console.log($event);
  }
}