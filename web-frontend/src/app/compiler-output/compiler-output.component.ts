import { Component, OnInit, ViewChild } from "@angular/core";
import { throwMatDialogContentAlreadyAttachedError } from "@angular/material/dialog";
import { CompilerService } from "../services/compiler.service";
import { OptionsService } from "../services/options.service";

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

  constructor(private compilerService: CompilerService, private optionsService: OptionsService) {
    this.compilerService.compilerRequest.subscribe((s) => {
      if(this.activeTab == 'ast') {
          s.astDisplay = true;
          s.hirDisplay = false;
          s.lirDisplay = false;
          s.regAlloc = false;
        }
        if(this.activeTab == 'hir') {
          s.astDisplay = false;
          s.hirDisplay = true;
          s.lirDisplay = false;
          s.regAlloc = false;

        }
        if(this.activeTab == 'lir') {
          s.astDisplay = false;
          s.hirDisplay = false;
          s.lirDisplay = true;
          s.regAlloc = false;

        }
        if(this.activeTab == 'asm') {
          s.astDisplay = false;
          s.hirDisplay = false;
          s.lirDisplay = false;
          s.regAlloc = true;
        }
      
      this.lastRequest = s;
      console.log("call compiler for assembly", s);
      this.compilerService.compile(s).subscribe((c) => {
        if(this.activeTab == 'ast') {
          this.ast = c.errors;
        }
        if(this.activeTab == 'hir') {
          this.hir = c.errors;
        }
        if(this.activeTab == 'lir') {
          this.lir = c.errors;
        }
        if(this.activeTab == 'asm') {
          this.asm = c.assembly;
        }
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
