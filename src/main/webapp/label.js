/* add label */

/* priority */
/**
 * When a node is created (by button, by drag-drop, by import file),
 * a listener is attached to the node (and its parent node),
 * such that when the node/parent node is selected, a hidden number input field has focus;
 * and when the node/parent is unselected, the hidden number field clears its value.
 * @param i: the number part of the id of the selected node and its parent
 */
function addPriorityListener(i) {
    cy.$(`#pnode${i}, #node${i}`).on('select', function (){
        $("#inputPriority").focus();

        let selected = cy.$(':selected');

        // if the selected is parent node, update to point to child node
        if (selected.data().type === "compound") {
            let i = parseInt(cy.$(':selected').data().id.match(/\d+/g)[0]);
            selected = cy.$(`#node${i}`);
        }

        // if the node already has priority, set it as the value of #inputPriority field
        if (selected.style().label !== "") {
            $("#inputPriority").val(parseInt(selected.style().label));
        }
    });

    // clear the value of the input field when the node is unselected
    cy.$(`#pnode${i}, #node${i}`).on('unselect', function (){
        $('#inputPriority').val('');
    });
}

// whenever the hidden input field experienced a keyup,
// update the priority of the selected node / its child node
$('#inputPriority').keyup(function () {
    let priority = $('#inputPriority').val();
    let style = {
        "label": priority,
        "text-wrap": "wrap",
        "text-valign": "center",
        "text-halign": "center"
    }

    let selected = cy.$(':selected');
    // if the parent of the compound node is selected, update to point to child node
    if (selected.data().type === "compound") {
        let i = parseInt(cy.$(':selected').data().id.match(/\d+/g)[0]);
        selected = cy.$(`#node${i}`);
    }
    selected.style(style);
});

// function set_priority() {
//     let priority = $('#priority').val();
//     var style = {
//         "label": priority,
//         "text-wrap": "wrap",
//         "text-valign": "center",
//         "text-halign": "center"
//     }
//     cy.$(':selected').style(style);
// }