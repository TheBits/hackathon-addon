var data = require("self").data;
var pageMod = require("page-mod");

// Встроить свой обработчик в веб-страницу
pageMod.PageMod({
  include: '*',
  contentScriptWhen: 'end',
  contentScriptFile: data.url('inject.js'),
  onAttach: function(worker) {
    worker.port.on('content', function(content) {
      // Получить контент страницы
      console.log(JSON.stringify(content));
    });
  }
});
