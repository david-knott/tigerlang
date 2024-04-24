import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompilerInputComponent } from './compiler-input.component';

describe('CompilerInputComponent', () => {
  let component: CompilerInputComponent;
  let fixture: ComponentFixture<CompilerInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompilerInputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompilerInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
