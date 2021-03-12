//undoRedo
var ur = cy.undoRedo({
    isDebug: true
});

cy.on("afterUndo", function (e, name) {
    document.getElementById("undos").innerHTML += "<span style='color: darkred; font-weight: bold'>Undo: </span> " + name  +"</br>";
});

cy.on("afterRedo", function (e, name) {
    document.getElementById("undos").innerHTML += "<span style='color: darkblue; font-weight: bold'>Redo: </span>" + name  +"</br>";
});

cy.on("afterDo", function (e, name) {
    document.getElementById("undos").innerHTML += "<span style='color: darkmagenta; font-weight: bold'>Do: </span>" + name  +"</br>";
});

document.addEventListener("keydown", function (e) {
    if(e.which === 46) {
        var selecteds = cy.$(":selected");
        if (selecteds.length > 0)
            ur.do("remove", selecteds);
    }
    else if (e.ctrlKey && e.target.nodeName === 'BODY')
        if (e.which === 90)
            ur.undo();
        else if (e.which === 89)
            ur.redo();
        else if (e.which == 67) // CTRL + C
            cy.clipboard().copy(cy.$(":selected"));
        else if (e.which == 86) // CTRL + V
            ur.do("paste");
        else if (e.which == 65) {
            cy.elements().select();
            e.preventDefault();
        }


});

document.getElementById('batchButton').addEventListener("click", function(e){
    actions = [];

    actions.push({
        name: "remove",
        param: cy.$(":selected")
    });
    ur.do("batch", actions);
});