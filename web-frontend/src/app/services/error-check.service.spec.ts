import { TestBed } from '@angular/core/testing';

import { ErrorCheckService } from './error-check.service';

describe('ErrorCheckService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ErrorCheckService = TestBed.get(ErrorCheckService);
    expect(service).toBeTruthy();
  });
});
