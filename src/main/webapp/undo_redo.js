//undoRedo
var ur = cy.undoRedo({
    isDebug: true
});

// cy.on("afterUndo", function (e, name) {
//     document.getElementById("undos").innerHTML += "<span style='color: darkred; font-weight: bold'>Undo: </span> " + name  +"</br>";
// });
//
// cy.on("afterRedo", function (e, name) {
//     document.getElementById("undos").innerHTML += "<span style='color: darkblue; font-weight: bold'>Redo: </span>" + name  +"</br>";
// });
//
// cy.on("afterDo", function (e, name) {
//     document.getElementById("undos").innerHTML += "<span style='color: darkmagenta; font-weight: bold'>Do: </span>" + name  +"</br>";
// });

document.addEventListener("keydown", function (e) {
    if(e.which === 46) {
        var selecteds = cy.$(":selected");
        if (selecteds.length > 0)
            ur.do("remove", selecteds);
    }
    else if (e.ctrlKey && e.target.nodeName === 'BODY')
        if (e.which === 90) // CTRL + Z
            ur.undo();
        else if (e.which === 89)  // CTRL + Y
            ur.redo();
        else if (e.which == 86) // CTRL + V
            paste();
        else if (e.which == 65) {
            cy.elements().select();
            e.preventDefault();
        }


});

let CopyID = 0;
function paste() {
    let copyMap = new Map();
    let tobePaste = cy.$(':selected');

    for (let key in tobePaste.jsons()) {
        let element = tobePaste.jsons()[key];
        if (element.group == "nodes") {
            node_id++;
            let id = 'node_' + node_id;
            copyMap.set(element.data.id, id);
            let type = element.data.type;
            // add node
            cy.add({
                    data: {
                        id: id,
                        type: type,
                        parent: ""
                    }
                }
            );

            // add listener for setting priority
            addPriorityListener(id);
        }
    }

    for (let key in tobePaste.jsons()) {
        let element = tobePaste.jsons()[key];
        if (element.group == "edges") {
            CopyID++;
            let id = element.data.id + CopyID;
            let source = copyMap.get(element.data.source);
            let target = copyMap.get(element.data.target);
            if (source && target){
                // add node
                cy.add({
                        data: {
                            id: id,
                            source: source,
                            target: target
                        }
                    }
                );

                // add listener for setting priority
                addPriorityListener(id);
            }

        }
    }

    cy.layout({name: 'cola', colasetting}).run();
}