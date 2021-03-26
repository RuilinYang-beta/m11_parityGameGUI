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

function handleChange(checkbox, attribute_name) {

    if(checkbox.checked == true){
        let elem = document.getElementById("selected_attr_list");
        elem.innerHTML += "                            <li id=\""+ attribute_name +"_list\">\n" +
            "                                <div class=\"list-group-item dropdown-toggle\" id=\"selected_" + attribute_name + "\"" +
            "                                   href=\"#"+ attribute_name +"_values\"\n" +
            "                                   data-toggle=\"collapse\" aria-expanded=\"false\">" + attribute_name +"\n" +
            "                                    <a class=\"close\" onclick=\"remove_attribute(this.parentNode.id)\"></a>\n" +
            "                                </div>\n" +
            "\n" +
            "                                <ul class=\"collapse list-unstyled\" id=\""+ attribute_name +"_values\">\n" +
            "                                    <li class=\"list-group-item\"><a>1......</a></li>\n" +
            "                                    <li class=\"list-group-item\"><a>2......</a></li>\n" +
            "                                    <li class=\"list-group-item\"><a>3......</a></li>\n" +
            "                                </ul>\n" +
            "                            </li>";

    }else{
        document.getElementById(attribute_name + "_list").remove();
    }
}