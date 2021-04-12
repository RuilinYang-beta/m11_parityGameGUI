/**
 * set priority
 * the element #inputPriorityGroup need to be a text input field that only takes digits
 * */
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
    cy.$(`#${id}`).on('select', function() {

        // all use group
        if (!$("#inputPriorityGroup").is(':focus')) {
            $("#inputPriorityGroup").focus();
        }
    })

    // clear the value of the input field when the node is unselected
    cy.$(`#${id}`).on('unselect', function (){
        // $('#inputPrioritySingle').val('');
        $('#inputPriorityGroup').val('');
    });
}

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
            if (evt.code === 'ArrowUp' || evt.key === "ArrowUp" ) {
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
    } else if (isLegalPriority(evt)) {  // set group priority in one go
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

function isLegalPriority(evt) {
    let cond1 = ['1','2','3','4','5','6','7','8','9','0',
                 'Backspace'].includes(evt.key);
    let cond2 = ['Digit1','Digit2','Digit3','Digit4','Digit5',
                 'Digit6','Digit7','Digit8','Digit9','Digit0',
                 'Backspace'].includes(evt.code);
    return cond1 || cond2;
}