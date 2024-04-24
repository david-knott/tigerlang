import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompilerConsoleComponent } from './compiler-console.component';

describe('CompilerConsoleComponent', () => {
  let component: CompilerConsoleComponent;
  let fixture: ComponentFixture<CompilerConsoleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompilerConsoleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompilerConsoleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
