import { OnInit } from "@angular/core";
import { Component, ViewChild, AfterViewInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { SettingsDialogComponent } from "./settings-dialog.component";

@Component({
  selector: "app-source-editor",
  templateUrl: "./source-editor.component.html",
  styleUrls: ["./source-editor.component.scss"],
})
export class SourceEditorComponent implements OnInit {
  @ViewChild("sourceEditor", { static: false }) sourceEditor;

  constructor(public dialog: MatDialog) {}

  ngOnInit(): void {}

  ngAfterViewInit() {}

  onChange(event): void {}

  showSettings() {
    const dialogRef = this.dialog.open(SettingsDialogComponent);

    dialogRef.afterClosed().subscribe((result) => {
      console.log(`Dialog result: ${result}`);
    });
  }
}
