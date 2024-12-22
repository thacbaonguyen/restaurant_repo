import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InspectBillComponent } from './inspect-bill.component';

describe('InspectBillComponent', () => {
  let component: InspectBillComponent;
  let fixture: ComponentFixture<InspectBillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InspectBillComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InspectBillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
