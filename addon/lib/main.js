var data = require("self").data;
var prefs = require("simple-prefs").prefs;
var Request = require("request").Request;
var pageMod = require("page-mod");

// Встроить свой обработчик в веб-страницу
pageMod.PageMod({
  include: '*',
  contentScriptWhen: 'end',
  contentScriptFile: data.url('inject.js'),
  contentStyle: [
    // "span.yana-expand { padding: 0; margin 0}",
    ".yana-highlight { text-decoration: underline; }",
    ".yana-highlight span {\
      background-color: #FFFF73;\
      border-radius: 5px;\
      display: none;\
      opacity: 0.9;\
      padding: 10px;\
      width: 350px;\
      z-index: 100;\
      text-decoration: none;\
      box-shadow: 0 40px 60px -30px #FFFFFF inset, 0 0 20px rgba(0, 0, 0, 0.2);\
    }",
    ".yana-highlight span img {float: left; margin-right: 10px}",
    ".yana-highlight:hover span { display: block; text-decoration: none; }"
  ],
  onAttach: function(worker) {
    worker.port.on('content', function(content) {
      // Получить контент страницы
      // console.log(JSON.stringify(content));
      console.log('send list: ' + content.length)
      Request({
        url: prefs.service_url,
        content: JSON.stringify(content),
        contentType: "application/json",
        onComplete: function (response) {
          worker.port.emit('result', response.text);
            try {
                messages = JSON.parse(response.text);
            } catch(e) {
                console.log('Error: '+e);
                console.log('message', response.text);
                console.log('message', content);
                return;
            }
        }
      }).post();
    });
  }
});
