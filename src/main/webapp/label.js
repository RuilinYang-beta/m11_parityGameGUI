/* add label */

/* priority */
/**
 * When a node is created (by button, by drag-drop, by import file),
 * a listener is attached to the node (and its parent node),
 * such that when the node/parent node is selected, a hidden number input field has focus;
 * and when the node/parent is unselected, the hidden number field clears its value.
 * @param id: the id of a node
 */
// new version
function addPriorityListener(id) {
    cy.$(`#${id}`).on('select', function(evt) {
        // handle key up/down


        // handle digit and backspace

    })
}


// old version: can't handle group operation
// function addPriorityListener(id) {
//     cy.$(`#${id}`).on('select', function (evt){
//
//         console.log("target is");
//         console.log(evt.target);
//         console.log(evt.target.data());
//
//         let selectedArr = cy.$(':selected').map(e => e.data()).filter(e => e.type === "even" || e.type === "odd");
//
//         /*
//          * It's a single selection or a group selection?
//          * If a node is selected as a single selection:
//          * - (point to the innermost child if it's the parent being selected),
//          * - (write the current priority to the hidden input field)
//          * - and update priority of node based on the input field whenever keyup.
//          * If the node is selected as a part of group selection:
//          * - (do nothing if it's a compound node or edge),
//          * - listen for arrow up/down, and incr/decr priority by hand,
//          * - listen for number/backspace, trigger input field and update priority.
//          */
//         if (selectedArr.length <= 1) {   // single selection, there's 0 or 1 innermost node
//             let selected = cy.$(':selected');
//             if (selected.data().type === "compound") {
//                 id = id.split("_p_")[0];
//                 selected = cy.$(`#${id}`);
//             }
//             $("#inputPriority").focus();
//             if (selected.style().label !== "") {
//                 $("#inputPriority").val(parseInt(selected.style().label));
//             }
//         } else {   // group selection
//             console.log("group selection!!");
//             if (evt.target.data().type !== "even" && evt.target.data().type !== "odd"){
//                 return;
//             }
//             // on arrow up/down: priority = priority +/-1
//
//             // on key in number: hidden field get this number and set it as priority
//         }
//     });
//
//     // clear the value of the input field when the node is unselected
//     cy.$(`#${id}`).on('unselect', function (){
//         console.log("in unselect");
//         $('#inputPriority').val('');
//     });
// }

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

    let selected = cy.$(':selected');   // TODO: should filter to only change even/odd node
    // if the parent of the compound node is selected, update to point to child node
    if (selected.data().type === "compound") {
        let i = parseInt(cy.$(':selected').data().id.match(/\d+/g)[0]);
        selected = cy.$(`#node${i}`);
    }
    selected.style(style);
});