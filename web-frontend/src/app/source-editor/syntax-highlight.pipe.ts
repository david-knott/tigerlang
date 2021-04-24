import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'syntaxHighlight'
})
export class SyntaxHighlightPipe implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    console.log(args[0]);
    return value;
  }

}
