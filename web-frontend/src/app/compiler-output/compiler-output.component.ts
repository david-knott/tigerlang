import { Component, Inject, OnInit, ViewChild } from "@angular/core";
import { CompilerService } from "../services/compiler.service";
import { OptionsService } from "../services/options.service";
import { graphviz } from "d3-graphviz";
import { wasmFolder } from "@hpcc-js/wasm";
import { ENV_CONFIG, EnvironmentConfig } from "../core/env-config.interface";

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
  cfg: string = "";
  fcg: string = "";
  lastRequest: any;

  constructor(
    private compilerService: CompilerService,
    private optionsService: OptionsService,
    @Inject(ENV_CONFIG) private config: EnvironmentConfig,
  ) {
    this.compilerService.compilerRequest.subscribe((s) => {
      s.rename = this.optionsService.getOption("rename");
      s.inline = this.optionsService.getOption("inline");
      s.prune = this.optionsService.getOption("prune");
      s.bindingsDisplay = this.optionsService.getOption("bindingsDisplay");
      s.desugarForLoop = this.optionsService.getOption("desugarFor");
      s.desugarStringComp = this.optionsService.getOption("desugarStringComp");
      s.escapesCompute = this.optionsService.getOption("escapesCompute");
      s.escapesDisplay = this.optionsService.getOption("escapesDisplay");
      s.staticLinks = this.optionsService.getOption("staticLinks");
      s.staticLinkEscapes = this.optionsService.getOption("staticLinkEscapes");
      s.deatomize = this.optionsService.getOption("deatomize");
      if (this.activeTab == "ast") {
        s.astDisplay = true;
        s.hirDisplay = false;
        s.lirDisplay = false;
        s.regAlloc = false;
        s.cfg = false;
        s.callGraphDisplay = false;
      }
      if (this.activeTab == "hir") {
        s.astDisplay = false;
        s.hirDisplay = true;
        s.lirDisplay = false;
        s.regAlloc = false;
        s.cfg = false;
        s.callGraphDisplay = false;
      }
      if (this.activeTab == "lir") {
        s.astDisplay = false;
        s.hirDisplay = false;
        s.lirDisplay = true;
        s.regAlloc = false;
        s.cfg = false;
        s.callGraphDisplay = false;
      }
      if (this.activeTab == "asm") {
        s.astDisplay = false;
        s.hirDisplay = false;
        s.lirDisplay = false;
        s.regAlloc = true;
        s.cfg = false;
        s.callGraphDisplay = false;
      }
      if (this.activeTab == "cfg") {
        s.astDisplay = false;
        s.hirDisplay = false;
        s.lirDisplay = false;
        s.regAlloc = false;
        s.cfg = true;
        s.callGraphDisplay = false;
      }
      if (this.activeTab == "fcg") {
        s.astDisplay = false;
        s.hirDisplay = false;
        s.lirDisplay = false;
        s.regAlloc = false;
        s.cfg = false;
        s.callGraphDisplay = true;
      }
      this.lastRequest = s;
      this.compilerService.compile(s).subscribe((c) => {
        if (this.activeTab == "ast") {
          this.ast = c.assembly;
        }
        if (this.activeTab == "hir") {
          this.hir = c.assembly;
        }
        if (this.activeTab == "lir") {
          this.lir = c.assembly;
        }
        if (this.activeTab == "asm") {
          this.asm = c.assembly;
        }
        if (this.activeTab == "cfg") {
          graphviz("div.cfg").renderDot(c.assembly);
        }
        if (this.activeTab == "fcg") {
          graphviz("div.fcg").renderDot(c.assembly);
        }
      });
    });
  }

  setActiveTab(activeTab: string): void {
    this.activeTab = activeTab;
    this.compilerService.compilationRequest(this.lastRequest);
  }

  ngOnDestroy() {
  }

  ngOnInit() {
    wasmFolder("./assets/");
  }
}
