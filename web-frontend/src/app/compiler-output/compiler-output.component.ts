import { Component, OnInit } from "@angular/core";
import { throwMatDialogContentAlreadyAttachedError } from "@angular/material/dialog";
import { CompilerService } from "../services/compiler.service";

@Component({
  selector: "app-compiler-output",
  templateUrl: "./compiler-output.component.html",
  styleUrls: ["./compiler-output.component.scss"],
})
export class CompilerOutputComponent implements OnInit {
  sub: any;

  constructor(private compilerService: CompilerService) {
    this.sub = compilerService.compilerEvent.subscribe((value) => {
      console.log(value);
    });
  }

  /** depending on the option & flags selected we call the compiler backend. */
  ngOnDestroy() {
    this.sub.unsubsribe();
  }
  ngOnInit() {}
}
