import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LayoutComponent } from "./layout/layout.component";
import { SourceEditorComponent } from "./source-editor/source-editor.component";

const routes: Routes = [
  { path: "old", component: LayoutComponent },
  { path: "source", component: SourceEditorComponent },
  { path: "source/:id", component: SourceEditorComponent },
  { path: "", redirectTo: "/old", pathMatch: "full" },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true, enableTracing: false})],
  exports: [RouterModule],
})
export class AppRoutingModule {}
