var data = require("self").data;
var pageMod = require("page-mod");

// Встроить свой обработчик в веб-страницу
pageMod.PageMod({
  include: '*',
  contentScriptWhen: 'end',
  contentScriptFile: data.url('inject.js')
});
