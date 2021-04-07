/* add label */

/* priority */
// the element #inputPriorityGroup need to be a text input field that only takes digits
$(document).ready(function() {
    $("#inputPriorityGroup").inputFilter(function(value) {
        return /^\d*$/.test(value);    // Allow digits only, using a RegExp
    });
});

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

/**
 * When a node is created (by button, by drag-drop, by import file),
 * a listener is attached to the node such that when the node is selected,
 * a hidden input field gets focus. Depending on whether it's a single selection or
 * group selection, different input field gets triggered. When the node is unselected,
 * the hidden field clears its value.
 * @param id: the id of a node
 */
function addPriorityListener(id) {
    cy.$(`#${id}`).on('select', function(e1) {
        let intended = cy.$(':selected').filter(e => e.data().type === "even" || e.data().type === "odd");
        if (intended.length === 1) {  // single selection
            if (!$("#inputPrioritySingle").is(':focus')){
                $("#inputPrioritySingle").focus();
            }
            let intendedNode = intended[0];
            if (intendedNode.style().label !== "") {
                $("#inputPrioritySingle").val(parseInt(intendedNode.style().label));
            }
        } else {  // group selection
            if (!$("#inputPriorityGroup").is(':focus')){
                $("#inputPriorityGroup").focus();
            }
        }
    })

    // clear the value of the input field when the node is unselected
    cy.$(`#${id}`).on('unselect', function (){
        $('#inputPrioritySingle').val('');
        $('#inputPriorityGroup').val('');
    });
}

// handle single node selection
$('#inputPrioritySingle').keyup(function () {
    let priority = $('#inputPrioritySingle').val();
    let style = {
        "label": priority,
        "text-wrap": "wrap",
        "text-valign": "center",
        "text-halign": "center"
    }
    let selected = cy.$(':selected');
    selected.style(style);
});

// handle group node selection
$('#inputPriorityGroup').keyup(function (evt) {
    let intended = cy.$(':selected').filter(e => e.data().type === "even" || e.data().type === "odd");

    if (evt.code === 'ArrowUp' || evt.code === 'ArrowDown' ||
        evt.key === "ArrowUp" || evt.key === "ArrowDown") {   // group increment/decrement
        intended.forEach(function(element){
            let pri = parseInt(element.style().label);
            if (isNaN(pri)){
                pri = 0;
            }
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
        let priority = $('#inputPriorityGroup').val();

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