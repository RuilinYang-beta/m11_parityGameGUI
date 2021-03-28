function search_attributes() {
    var input, filter, table, tr, td, i;
    input = document.getElementById("search_attributes");
    filter = input.value.toUpperCase();
    table = document.getElementById("attributes_list");
    tr = table.getElementsByTagName("li");

    for (i = 0; i < tr.length; i++) {
        // td = tr[i].getElementsByTagName("td")[0];
        td = tr[i].textContent;
        if (td) {
            if (td.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function remove_attribute(parent_id) {
    let attribute_name = parent_id.substring(9);
    let list_id = attribute_name + "_list";
    console.log(list_id);
    console.log(attribute_name);

    document.getElementById(attribute_name).checked = false;
    document.getElementById(list_id).remove();

}

let selected_vis_attr = [];
let maximum_selected_attr = 3;
function handleChange(checkbox, attribute_id, attribute_name) {
    if (selected_vis_attr.length == 3) {
        checkbox.checked = false;

        // disable all checkboxes
        let attributes_list = document.getElementById("attributes_list");
        let checkbox_list = attributes_list.getElementsByTagName("input");

        for (let i = 0; i < checkbox_list.length; i++) {
            checkbox_list[i].disabled = true;
        }

    }
    let index = parseInt(attribute_id.substring(10), 10);
    if(checkbox.checked == true){
        // add the attribute object to selected_vis_attr
        selected_vis_attr.push(vis_attributes[index]);


        // modify html
        let selected_attr_list = document.getElementById("selected_attr_list");
        selected_attr_list.innerHTML += "                            <li id=\"attribute_"+ index +"_list\">\n" +
            "                                <div class=\"list-group-item dropdown-toggle selected_attribute\" id=\"selected_attribute_" + index + "\"" +
            "                                   href=\"#attribute_"+ index +"_values\"\n" +
            "                                   data-toggle=\"collapse\" aria-expanded=\"false\" onclick=\"toggle_arrow(this)\">" +
            "                                   <div class=\"arrow_container\"><i class=\"arrow right\"></i></div><div class=\"attr_name_container\">" + attribute_name + "</div>\n" +
            "                                   <a class=\"close\" onclick=\"remove_attribute(this.parentNode.id)\"></a>\n" +
            "                                </div>\n" +
            "\n" +
            "                                <ul class=\"collapse list-unstyled\" id=\"attribute_"+ index +"_values\">\n" +
            "                                </ul>\n" +
            "                            </li>";

        // get selected attribute_values
        let values = vis_attributes[index].values;
        let selected_attribute_values = document.getElementById("attribute_" + index + "_values");
        for (let j = 0; j < values.length; j++) {
            let value = values[j];
            selected_attribute_values.innerHTML += "<li class=\"list-group-item\">" + value +
                "<input class=\"attr_color_picker\" type=\"color\" id=\"attribute_" + index + "_value_" + j +"\"></li>";
        }

    }else{
        // remove the index-th selected attribute_list.
        document.getElementById("attribute_" + index + "_list").remove();
    }
}