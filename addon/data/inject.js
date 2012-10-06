// Файл работает в контексте веб-страницы

self.port.on('result', function(message) {
    console.log('message', message);
    var messages = JSON.parse(message);
});

// Получает текстовые данные из DOM
var result = [];
var root = document.body;
var node = root.childNodes[0];
while(node != null) {
    if (node.nodeType == Node.TEXT_NODE) {
        result.push(node.textContent);
    }

    if (node.hasChildNodes()  && node.tagName !== 'SCRIPT') {
        node = node.firstChild;
    } else {
        while(node.nextSibling == null && node != root) {
            node = node.parentNode;
        }
        node = node.nextSibling;
    }
}

//Отправляет текстовые данные в аддон
self.port.emit('content', result);
