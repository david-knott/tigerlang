import { Injectable } from "@angular/core";

@Injectable({
  providedIn: "root",
})
export class OptionsService {
  options: {};

  constructor() {
    this.options = {
      astSettings: {
        rename:
        {
          elId: 1,
          title: "Rename",
          label: "Rename",
          checked: false,
        },
        inline:
        {
          elId: 2,
          title: "Inline Function",
          label: "Inline Function",
          checked: false,
        },
        prune:
        {
          elId: 3,
          title: "Prune Function",
          label: "Prune Function",
          checked: false,
        },
        bindingsDisplay:
        {
          elId: 4,
          title: "Show Bindings",
          label: "Show Bindings",
          checked: false,
        },
        desugarFor:
        {

          elId: 5,
          title: "Desugar For Loops",
          label: "Desugar For Loops",
          checked: false,
        },
        desugarStringComp:
        {
          elId: 6,
          title: "Desugar String Operations",
          label: "Desugar String Operationss.",
          checked: false,
        },
        escapesCompute:
        {
          elId: 7,
          title: "Compute Non Escaping Variables",
          label: "Compute Non Escaping Variables",
          checked: false,
        },
        escapesDisplay:
        {
          elId: 8,
          title: "Show Escaping Variables",
          label: "Show Escaping Variables",
          checked: false,
        },
        staticLinks:
        {
          elId: 9,
          title: "Compute Static Link",
          label: "Compute Static Link",
          checked: false,
        },
        staticLinkEscapes:
        {
          elId: 10,
          title: "Compute Non Escaping Static Link",
          label: "Compute Non Escaping Static Link",
          checked: false,
        },
        
        deatomize:
        {
          elId: 11,
          title: "Dataflow Analysis",
          label: "Dataflow Analysis",
          checked: false,
        },
      },
    };
  }


  update(options) {
    this.options = options;
  }

  getOptions() {
    return this.options;
  }

  getOption(name: string) {
    return this.options["astSettings"][name].checked;
  }
}
