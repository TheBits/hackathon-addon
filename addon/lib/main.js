var data = require("self").data;
var prefs = require("simple-prefs").prefs;
var Request = require("request").Request;
var pageMod = require("page-mod");

// Встроить свой обработчик в веб-страницу
pageMod.PageMod({
  include: '*',
  contentScriptWhen: 'end',
  contentScriptFile: data.url('inject.js'),
  onAttach: function(worker) {
    worker.port.on('content', function(content) {
      // Получить контент страницы
        Request({
          url: prefs.service_url,
          onComplete: function (response) {
            worker.port.emit('result', response.text);
          } 
        }).get();
    });
  }
});
