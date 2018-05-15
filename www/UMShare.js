// Author: Yu Chen <yu.chen@live.ie>
// License: Apache License 2.0

'use strict';

module.exports = {
  /**
   * @param {object|string} options
   * @param {Function} successCallback ['success']
   * @param {Function} errorCallback ['fail'|'cancel'|'invalid']
   */
  open: function (param, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "UMShare", "open", [param]);
  },
  auth: function (param, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "UMShare", "auth", [param]);
  }
};