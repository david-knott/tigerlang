import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { AceEditorModule } from "ng2-ace-editor";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { HttpClientModule } from "@angular/common/http";
import { LayoutComponent } from "./layout/layout.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { CompilerInputComponent } from "./compiler-input/compiler-input.component";
import { CompilerOutputComponent } from "./compiler-output/compiler-output.component";
import { CompilerConsoleComponent } from "./compiler-console/compiler-console.component";
import { FooterComponent } from "./footer/footer.component";
import { HeaderComponent } from "./header/header.component";
import { FormsModule } from "@angular/forms";
import { environment } from "@env/environment";
import { SourceEditorComponent } from "./source-editor/source-editor.component";
import { AppRoutingModule } from "./app-routing.module";
import { CoreModule } from './core/core.modules';
import { Version1Component } from './versions/version1/version1.component';
import { ContenteditableValueAccessor } from './shared/contenteditable-value-accessor.directive';
import { SafeHtmlPipe } from './shared/safe-html-pipe';
import { DeleteComponent } from './source-editor/delete/delete.component';
import { DataFlowViewerComponent } from './tests/data-flow-viewer/data-flow-viewer.component';
import { EditorComponent } from './shared/editor/editor.component';

@NgModule({
  declarations: [
    AppComponent,
    LayoutComponent,
    CompilerInputComponent,
    CompilerOutputComponent,
    CompilerConsoleComponent,
    FooterComponent,
    HeaderComponent,
    SourceEditorComponent,
    Version1Component,
    ContenteditableValueAccessor,
    SafeHtmlPipe,
    DeleteComponent,
    DataFlowViewerComponent,
    EditorComponent,
  ],
  imports: [
    BrowserModule,
    AceEditorModule,
    NgbModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    AppRoutingModule,
    CoreModule.forRoot({ environment })
  ],
  entryComponents: [
    DeleteComponent
  ],
  providers: [
    { provide: Window, useValue: window },
    { provide: Document, useValue: document},
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
