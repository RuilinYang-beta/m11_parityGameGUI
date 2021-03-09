/* add label */

/* priority */
function set_priority() {
    let priority = $('#priority').val();
    var style = {
        "label": priority,
        "text-wrap": "wrap",
        "text-valign": "center",
        "text-halign": "center"
    }
    cy.$(':selected').style(style);

}