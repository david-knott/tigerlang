import {
  Directive,
  ElementRef,
  forwardRef,
  HostListener,
  Renderer2,
} from "@angular/core";
import { DomSanitizer } from '@angular/platform-browser'

import { NG_VALUE_ACCESSOR } from "@angular/forms";

// https://medium.com/its-tinkoff/controlvalueaccessor-and-contenteditable-in-angular-6ebf50b7475e

// https://stackoverflow.com/questions/36265026/angular-2-innerhtml-styling
interface ControlValueAccessor {
  writeValue(value: any): void;
  registerOnChange(fn: (value: any) => void): void;
  registerOnTouched(fn: () => void): void;
  setDisabledState(isDisabled: boolean): void;
}

@Directive({
  selector:
    "[contenteditable][formControlName]," +
    "[contenteditable][formControl]," +
    "[contenteditable][ngModel]",
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => ContenteditableValueAccessor),
      multi: true,
    },
  ],
})
export class ContenteditableValueAccessor implements ControlValueAccessor {
  // ...

  constructor(
    private sanitizer:DomSanitizer,
    private readonly elementRef: ElementRef,
    private readonly renderer: Renderer2
  ) {}

  private onTouched = () => {};

  private onChange: (value: string) => void = () => {};

  registerOnChange(onChange: (value: string) => void) {
    this.onChange = onChange;
  }

  registerOnTouched(onTouched: () => void) {
    this.onTouched = onTouched;
  }

  @HostListener("input")
  onInput() {
    this.onChange(this.elementRef.nativeElement.innerHTML);
  }

  @HostListener("blur")
  onBlur() {
    this.onTouched();
  }

  setDisabledState(disabled: boolean) {
    this.renderer.setAttribute(
      this.elementRef.nativeElement,
      "contenteditable",
      String(!disabled)
    );
  }

  writeValue(value: string) {
    this.renderer.setProperty(
      this.elementRef.nativeElement,
      "innerHTML",
      value
    );
  }
}
