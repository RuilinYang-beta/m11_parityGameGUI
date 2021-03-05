/* add label */

/* priority */
function set_priority() {
    let priority = $('#priority'). val();
    // var style = {
    //     "label": "2231\n243\n3333\n4334",
    //     "text-wrap": "wrap",
    //     "text-valign": "center",
    //     "text-halign": "left"
    // }
    cy.$(':selected').style("label", priority);

}