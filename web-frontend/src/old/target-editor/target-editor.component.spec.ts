import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TargetEditorComponent } from './target-editor.component';

describe('TargetEditorComponent', () => {
  let component: TargetEditorComponent;
  let fixture: ComponentFixture<TargetEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TargetEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TargetEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
