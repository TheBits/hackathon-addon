// Файл работает в контексте веб-страницы

// функция проходит все дерево
var result = [];
var root = document.body;

var node = root.childNodes[0];
while(node != null) {
    if (node.nodeType == Node.TEXT_NODE) {
        result.push(node.textContent);
    }

    if(node.hasChildNodes()) {
        node = node.firstChild;
    } else {
        while(node.nextSibling == null && node != root) {
            node = node.parentNode;
        }
        node = node.nextSibling;
    }
}

result.forEach(function(r) {
    console.log("!!!");
    console.log(r);
});
