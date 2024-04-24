import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DataFlowViewerComponent } from './data-flow-viewer.component';

describe('DataFlowViewerComponent', () => {
  let component: DataFlowViewerComponent;
  let fixture: ComponentFixture<DataFlowViewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataFlowViewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataFlowViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
