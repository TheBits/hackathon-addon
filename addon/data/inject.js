// Файл работает в контексте веб-страницы

self.port.on('result', function(message) {
    // console.log('message', message);
    // console.log('message', JSON.parse(message)[1][0]['start']);
    var messages;
    try {
        messages = JSON.parse(message);
    } catch(e) {
        // console.log('Error: '+e);
        // console.log('message', message);
        return;
    }

    var index = 0;
    var css =[];
    var root = document.body;
    var node = root.childNodes[0];

    while(node != null) {
        var prev = node.nodeType == Node.TEXT_NODE? node: null;

        if (node.hasChildNodes()  && node.tagName !== 'SCRIPT') {
            node = node.firstChild;
        } else {
            while(node.nextSibling == null && node != root) {
                node = node.parentNode;
            }
            node = node.nextSibling;
        }

        if (prev !== null) {
            var data = messages[index];
            index += 1;

            if (data === void 0) {
                continue;
            };

            var content = prev.textContent;

            for (var i = data.length - 1; i >= 0; i--) {
                item = data[i];
                console.log('item', item);
                console.log('item', item['image']);
                content = [
                    content.substr(0, item['start']),
                    '<span class="yana-highlight">',
                    content.substr(item['start'], item['length']),
                    '<span><img style="float:left" src="'+item['data']['image']+'">'+item['data']['text']+'</span>',
                    '</span>',
                    content.substr(item['length']+item['start'], content.length)
                ].join(' ');
            };

            // Заменить тег
            var el = document.createElement('span');
            el.innerHTML = content;
            el.className = "yana-expand";
            prev.parentNode.replaceChild(el, prev);
        }
    }
    console.log('result merged');
});

// Получает текстовые данные из DOM
var result = [];
var root = document.body;
var node = root.childNodes[0];
while(node != null) {
    if (node.nodeType == Node.TEXT_NODE) {
        // console.log(node.textContent)
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
