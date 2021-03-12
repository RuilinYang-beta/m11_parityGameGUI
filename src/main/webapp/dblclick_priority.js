let doubleClicked = null;

cy.dblclick();

cy.on('dblclick', function(evt) {
    $('#inputPriority').removeAttr('disabled');
    $("#inputPriority").focus();

    doubleClicked = evt.target;
    doubleClicked.on('unselect', function(){
        $('#inputPriority').val('');
        $('#inputPriority').attr('disabled','disabled');
    });

    // if the node already has priority, set it as the value of inputPriority
    if (doubleClicked.style().label !== "") {
        $("#inputPriority").val(parseInt(doubleClicked.style().label));
    }
});


$('#inputPriority').keypress(function (e) {
    // if 'enter' is hit
    if (e.which === 13) {
        // set the priority of selected node to this number
        let priority = $('#inputPriority').val();
        let style = {
            "label": priority,
            "text-wrap": "wrap",
            "text-valign": "center",
            "text-halign": "center"
        }
        doubleClicked.style(style);
        return false;
    }
});



