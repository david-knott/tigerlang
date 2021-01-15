// Set to true for IE 11
const isIE =
  window.navigator.userAgent.indexOf('MSIE ') > -1 ||
  window.navigator.userAgent.indexOf('Trident/') > -1;

export const environment = {
  production: true,
  apiUrl: 'https://tigerlang.chaosopher.com:8080',
};