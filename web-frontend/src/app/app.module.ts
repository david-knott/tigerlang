import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { AceEditorModule } from "ng2-ace-editor";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { HttpClientModule } from "@angular/common/http";
import { LayoutComponent } from "./layout/layout.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { CompilerInputComponent } from './compiler-input/compiler-input.component';
import { CompilerOutputComponent } from './compiler-output/compiler-output.component';
import { CompilerConsoleComponent } from './compiler-console/compiler-console.component';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    LayoutComponent,
    CompilerInputComponent,
    CompilerOutputComponent,
    CompilerConsoleComponent,
    FooterComponent,
    HeaderComponent,
  ],
  imports: [
    BrowserModule,
    AceEditorModule,
    NgbModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule
  ],
  entryComponents: [],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
