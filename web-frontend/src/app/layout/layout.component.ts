import { Component, OnInit } from "@angular/core";
import { CompilerService } from '../services/compiler.service';
import { OptionsService } from "../services/options.service";

@Component({
  selector: "app-layout",
  templateUrl: "./layout.component.html",
  styleUrls: ["./layout.component.scss"],
})
export class LayoutComponent implements OnInit {
  options: {};

  constructor(private optionsService: OptionsService, private compilerService: CompilerService) {
  }

  ngOnInit() {
    this.options = this.optionsService.getOptions();
  }

  onChange() {
    this.optionsService.update(this.options);
    var code:string = this.compilerService.getCode();
    this.compilerService.compilationRequest({ code: code, args: null });
  }
}