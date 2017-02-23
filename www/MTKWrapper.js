var exec = require('cordova/exec');

exports.setWifi = function (wifiSSID,
                                  wifiKey,
                                  success, error) {
    exec(success, error, "MTKWrapper", "setWifi",
        [
            wifiSSID,
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
    exec( null,null,"ESPWrapper", "dealloc",
        []);
};
