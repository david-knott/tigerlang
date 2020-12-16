import {  OnInit } from '@angular/core';
import { Component, ViewChild, AfterViewInit } from "@angular/core";
@Component({
  selector: 'app-target-editor',
  templateUrl: './target-editor.component.html',
  styleUrls: ['./target-editor.component.scss']
})
export class TargetEditorComponent implements OnInit {

  @ViewChild("sourceEditor", { static: false }) sourceEditor;
  constructor() { }

  ngOnInit() {
  }

}
