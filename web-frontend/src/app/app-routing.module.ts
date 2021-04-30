import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LayoutComponent } from "./layout/layout.component";
import { SourceEditorComponent } from "./source-editor/source-editor.component";
import { Version1Component } from "./versions/version1/version1.component";

const routes: Routes = [
  { path: "old", component: LayoutComponent },
  { path: "v1", component: Version1Component},
  { path: "source", component: SourceEditorComponent },
  { path: "source/:id", component: SourceEditorComponent },
  { path: "", redirectTo: "/old", pathMatch: "full" },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true, enableTracing: false})],
  exports: [RouterModule],
})
export class AppRoutingModule {}
