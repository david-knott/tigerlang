// Set to true for IE 11
const isIE =
  window.navigator.userAgent.indexOf('MSIE ') > -1 ||
  window.navigator.userAgent.indexOf('Trident/') > -1;
const jsonServer = false;

export const environment = {
  production: true,
  useJsonServer: jsonServer,

  // -------------------------
  // Custom configuration
  // -------------------------

  // Api Url
  apiUrl: jsonServer ? 'https://apiquotetool.jouleiot.com' : 'https://quotetool.jouleiot.com',

  // Development Api Url
  // devApiUrl: 'https://localhost:44373',
  devApiUrl: 'https://quotetool.jouleiot.com',

  // Parent Url
  parentUrl: 'https://quotetool.jouleiot.com',

  // Joule Angular Url
  jouleUrl: 'https://quotetool.jouleiot.com',

  // Default settings for any list of items being displayed in a table.
  tableFilterDefaults: {
    // The default number of records to skip from the database.
    skip: 0,

    // The default number of records to return from the database.
    limit: 15,
  },

  // Quote list filter default settings
  quoteFilterDefaults: {
    // Default number of months to search back.
    months: 6,

    // Index of the default quote status (1 = Quote Created, skipping the 'All' option).
    status: 1,
  },
};