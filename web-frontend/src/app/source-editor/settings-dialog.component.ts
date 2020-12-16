import { Component } from "@angular/core";

@Component({
  selector: "app-settings-dialog",
  templateUrl: "./settings-dialog.html",
})
export class SettingsDialogComponent {
  checked = false;
  indeterminate = false;
  labelPosition: "before" | "after" = "after";
  disabled = false;
}
