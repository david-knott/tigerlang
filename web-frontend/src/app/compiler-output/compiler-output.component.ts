import { Component, OnInit, ViewChild } from "@angular/core";
import { throwMatDialogContentAlreadyAttachedError } from "@angular/material/dialog";
import { CompilerService } from "../services/compiler.service";

@Component({
  selector: "app-compiler-output",
  templateUrl: "./compiler-output.component.html",
  styleUrls: ["./compiler-output.component.scss"],
})
export class CompilerOutputComponent implements OnInit {
  @ViewChild("tabs", { static: false }) tabs;
  compilerSubscription: any;
  activeTab: string = "ast";
  ast: string = "";
  hir: string = "";
  lir: string = "";
  asm: string = "";
  lastRequest: any;

  constructor(private compilerService: CompilerService) {
    this.compilerService.compilerRequest.subscribe((s) => {
      s.args = this.activeTab;
      this.lastRequest = s;
      console.log("call compiler for assembly", s);
      this.compilerService.compile(s).subscribe((c) => {
        this.ast = c.assembly;
      });
    });
  }

  setActiveTab(activeTab: string): void {
    this.activeTab = activeTab;
    this.compilerService.compilationRequest(this.lastRequest);
  }

  /** depending on the option & flags selected we call the compiler backend. */
  ngOnDestroy() {
    this.compilerSubscription.unsubsribe();
  }
  ngOnInit() {}
}
