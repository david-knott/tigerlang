import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { AceEditorModule } from "ng2-ace-editor";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { HttpClientModule } from "@angular/common/http";
import { LayoutComponent } from "./layout/layout.component";
import { SourceEditorComponent } from "./source-editor/source-editor.component";
import { SettingsDialogComponent } from "./source-editor/settings-dialog.component";
import { TargetEditorComponent } from "./target-editor/target-editor.component";
import { OutputPanelComponent } from "./output-panel/output-panel.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatSliderModule } from "@angular/material/slider";
import { MatIconModule } from "@angular/material/icon";
import { MatDialogModule } from "@angular/material/dialog";
import { MatMenuModule } from "@angular/material/menu";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatDividerModule } from "@angular/material/divider";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatCardModule } from "@angular/material/card";
import { FormsModule } from "@angular/forms";
import { ReactiveFormsModule } from "@angular/forms";
import { MatRadioModule } from "@angular/material/radio";
import {MatGridListModule} from '@angular/material/grid-list';

@NgModule({
  declarations: [
    AppComponent,
    LayoutComponent,
    SourceEditorComponent,
    TargetEditorComponent,
    OutputPanelComponent,
    SettingsDialogComponent,
  ],
  imports: [
    BrowserModule,
    AceEditorModule,
    NgbModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatSliderModule,
    MatIconModule,
    MatDialogModule,
    MatMenuModule,
    MatToolbarModule,
    MatDividerModule,
    MatCheckboxModule,
    MatRadioModule,
    MatCardModule,
    FormsModule,
    ReactiveFormsModule,
    MatGridListModule
  ],
  entryComponents: [SettingsDialogComponent],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
