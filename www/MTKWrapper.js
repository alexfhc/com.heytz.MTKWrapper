var exec = require('cordova/exec');

exports.setWifi = function (wifiKey,
                            success, error) {
  exec(success, error, "MTKWrapper", "setWifi",
    [
      wifiKey,
    ]);
};
exports.startSocket = function (ip,
                                success, error) {
  exec(success, error, "MTKWrapper", "startSocket",
    [
      ip
    ]);
};
exports.dealloc = function () {
  exec(null, null, "MTKWrapper", "dealloc",
    []);
};
