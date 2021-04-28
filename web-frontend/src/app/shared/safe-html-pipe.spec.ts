import { SafeHtmlPipePipe } from './safe-html-pipe';

describe('SafeHtmlPipePipe', () => {
  it('create an instance', () => {
    const pipe = new SafeHtmlPipePipe();
    expect(pipe).toBeTruthy();
  });
});
