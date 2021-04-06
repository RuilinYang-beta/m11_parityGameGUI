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
    cy.$(`#${id}`).on('select', function(e1) {
        console.log("being selected");
        console.log(e1.target);

        if (!$("#inputPriority").is(':focus')){
            $("#inputPriority").focus();
        }

        let selectedArr = cy.$(':selected').map(e => e.data()).filter(e => e.type === "even" || e.type === "odd");
        // single selection
        if (selectedArr.length === 1) {
            let selected = cy.$(':selected');
            // update the value of input field as the priority of the node
            if (selected.style().label !== "") {
                $("#inputPriority").val(parseInt(selected.style().label));
            }
        }
        // group selection
    })

    // clear the value of the input field when the node is unselected
    cy.$(`#${id}`).on('unselect', function (){
        console.log("in unselect");
        $('#inputPriority').val('');
    });
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
$('#inputPriority').keyup(function (evt) {
    // // single selection
    // let priority = $('#inputPriority').val();
    // let style = {
    //     "label": priority,
    //     "text-wrap": "wrap",
    //     "text-valign": "center",
    //     "text-halign": "center"
    // }
    // let selected = cy.$(':selected');   // TODO: should filter to only change even/odd node
    // selected.style(style);

    // group selection
    console.log(evt.code);
    console.log(evt.key);
    if (evt.code === 'ArrowUp' || evt.code === 'ArrowDown' ||
        evt.key === "ArrowUp" || evt.key === "ArrowDown") {   // group increment/decrement
        // each selected node should incre/decre priority by 1
        let intended = cy.$(':selected').filter(e => e.data().type === "even" || e.data().type === "odd");

        intended.forEach(function(element){
            let pri = parseInt(element.style().label);
            if (evt.code === 'ArrowUp' || evt.key === "ArrowUp" ){
                pri = pri + 1;
            } else {
                pri = pri - 1;
            }
            let style = {
                "label": pri,
                "text-wrap": "wrap",
                "text-valign": "center",
                "text-halign": "center"
            }
            element.style(style);
        });
    } else if (evt.code === 'Control' || evt.key === 'Control') {
        // do nothing
    } else {  // set group priority in one go
        let intended = cy.$(':selected').filter(e => e.data().type === "even" || e.data().type === "odd");
        let priority = $('#inputPriority').val();

        intended.forEach(function(element){
            let style = {
                "label": priority,
                "text-wrap": "wrap",
                "text-valign": "center",
                "text-halign": "center"
            }
            element.style(style);
        });
    }


});


// Restricts input for the set of matched elements to the given inputFilter function.
(function($) {
    $.fn.inputFilter = function(inputFilter) {
        return this.on("input keydown keyup mousedown mouseup select contextmenu drop", function() {
            if (inputFilter(this.value)) {
                this.oldValue = this.value;
                this.oldSelectionStart = this.selectionStart;
                this.oldSelectionEnd = this.selectionEnd;
            } else if (this.hasOwnProperty("oldValue")) {
                this.value = this.oldValue;
                this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
            } else {
                this.value = "";
            }
        });
    };
}(jQuery));

$(document).ready(function() {
    $("#inputPriority").inputFilter(function(value) {
        return /^\d*$/.test(value);    // Allow digits only, using a RegExp
    });
});