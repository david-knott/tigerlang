import { Pipe, PipeTransform, Sanitizer, SecurityContext} from '@angular/core';

@Pipe({
  name: 'syntaxHighlight'
})
export class SyntaxHighlightPipe implements PipeTransform {

   constructor(
    private sanitizer: Sanitizer
  ) {}

  transform(value: any, ...args: any[]): any {
    return this.sanitize(this.replace(value, 'let'));
  }

  replace(str, regex) {
    return str.replace(new RegExp(`(${regex})`, 'gi'), '<b>$1</b>');
  }

  sanitize(str) {
    return this.sanitizer.sanitize(SecurityContext.HTML, str);
  }
}
