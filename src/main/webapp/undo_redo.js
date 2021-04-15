//undoRedo
var ur = cy.undoRedo({
    isDebug: true
});

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


// Paste nodes with priority/edges functionality
let CopyID = 0;
function paste() {
    let copyMap = new Map();
    let tobePaste = cy.$(':selected');
    for (let key = 0; key < tobePaste.length; key++) {
        let element = tobePaste[key];
        let data = element.data();
        let label = element.style().label;
        // copy nodes.
        if (data.type == "even" || data.type == "odd") {
            node_id++;
            let id = 'node_' + node_id;
            copyMap.set(data.id + "", id);
            let type = data.type;
            // add node
            cy.add({
                    data: {
                        id: id,
                        type: type,
                        parent: ""
                    }
                }
            );
            cy.$('#' + id).style("label", label);
            // add listener for setting priority
            addPriorityListener(id);
        }
    }

    for (let key = 0; key < tobePaste.length; key++) {
        let element = tobePaste[key];
        let data = element.data();
        // copy edges.
        if (data.source) {
            CopyID++;
            let id = data.id + CopyID;
            let source = copyMap.get(data.source);
            let target = copyMap.get(data.target);
            if (source && target) {
                // add edge
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