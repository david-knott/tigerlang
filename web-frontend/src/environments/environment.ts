// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

// Set to true for IE 11
const isIE =
  window.navigator.userAgent.indexOf('MSIE ') > -1 ||
  window.navigator.userAgent.indexOf('Trident/') > -1;

// For local builds, set to true if running via the Visual Studio environment. Otherwise,
// if just running ng serve, set this value to false.
const vsDevelopment = true;
const port = vsDevelopment ? 44338 : 4201;
const protocol = vsDevelopment ? 'https' : 'http';

export const environment = {
  production: false,
  baseUrl: 'http://localhost:8080',
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
import 'zone.js/dist/zone-error';  // Included with Angular CLI.