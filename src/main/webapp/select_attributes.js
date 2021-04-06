/**
 * variables
 */
// selected attributes to be visualized
    // json Object: index -> attribute object
let selected_vis_attr = {};
// maximum number of selected attributes
let max_selected_attr = 3;

/**
 * pattern matching search
 */
function search_attributes() {
    let input, filter, table, tr, td, i;
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

/**
 * When the cross icon of a selected attribute is clicked,
 * remove the attribute from selected_vis_attr,
 * set the corresponding checkbox.checked to false,
 * remove the attribute from selected_attr_list in HTML,
 * and enable all checkboxes.
 * @param selected_vis_attr: index -> attribute object.
 */
function remove_attribute(attribute_id) {
    let index = parseInt(attribute_id.split("_")[2], 10);

    // remove the attribute from selected_vis_attr
    delete selected_vis_attr[index];
    console.log(selected_vis_attr);

    // set the corresponding checkbox.checked to false
    let checkbox = document.getElementById("attribute_" + index);
    checkbox.checked = false;

    // remove the attribute from selected_attr_list in HTML
    document.getElementById("attribute_"+ index +"_list").remove();

    // enable all checkboxes
    // set checkbox.checked to false
    checkbox.checked = false;

    // disable all checkboxes
    let attributes_list = document.getElementById("attributes_list");
    let checkbox_list = attributes_list.getElementsByTagName("input");
    for (let i = 0; i < checkbox_list.length; i++) {
        checkbox_list[i].disabled = false;
    }
}

/**
 * When an attribute is selected or de-selected,
 * add or remove attribute to/from HTML and selected_vis_attr,
 * and add or remove attribute to/from accordingly.
 * Furthermore, the number of selected attributes should not exceed max_selected_attr.
 * @param selected_vis_attr: index -> attribute object.
 * @param max_selected_attr: maximum number of selected attributes.
 */
function handleChange(checkbox, attribute_id, attribute_name) {
    // if the number of selected attributes is equal to max_selected_attr,
    // and checkbox.checked === true,
    // set checkbox.checked in HTML to false, disable all checkboxes and return
    if (Object.keys(selected_vis_attr).length === max_selected_attr && checkbox.checked === true) {
        // set checkbox.checked to false
        checkbox.checked = false;

        // disable all checkboxes
        let attributes_list = document.getElementById("attributes_list");
        let checkbox_list = attributes_list.getElementsByTagName("input");
        for (let i = 0; i < checkbox_list.length; i++) {
            checkbox_list[i].disabled = true;
        }

        return ;
    }

    // else, add the selected attribute to selected_vis_attr,
    // and add the selected attribute to selected_attr_list in HTML
    let index = parseInt(attribute_id.substring(10), 10);
    if(checkbox.checked === true){
        // add the attribute object to selected_vis_attr
        selected_vis_attr[index] = vis_attributes[index];

        // add the selected attribute to selected_attr_list in HTML
        let selected_attr_list = document.getElementById("selected_attr_list");
        selected_attr_list.innerHTML += "                            <li id=\"attribute_"+ index +"_list\">\n" +
            "                                <div class=\"list-group-item dropdown-toggle selected_attribute\" id=\"selected_attribute_" + index + "\"" +
            "                                   href=\"#attribute_"+ index +"_values\"\n" +
            "                                   data-toggle=\"collapse\" aria-expanded=\"false\" onclick=\"toggle_arrow(this)\">" +
            "                                   <div class=\"arrow_container\"><i class=\"arrow right\"></i></div><div class=\"attr_name_container\">" + attribute_name + "</div>\n" +
            "                                   <a class=\"close\" onclick=\"remove_attribute(this.parentNode.id)\"></a>\n" +
            "                                </div>\n" +
            "                                <ul class=\"collapse list-unstyled\" id=\"attribute_"+ index +"_values\">\n" +
            "                                </ul>\n" +
            "                            </li>";

        // add the values of the selected attribute to selected_attr_list in HTML
        // save attribute type as a class of <input> elements
        let name = vis_attributes[index].name;
        let values = vis_attributes[index].values;
        let selected_attribute_values = document.getElementById("attribute_" + index + "_values");
        let type = vis_attributes[index].type;
        for (let j = 0; j < values.length; j++) {
            let value = values[j];
            if (type === "color") {
                if (name === "color" && value === "even") {
                    selected_attribute_values.innerHTML += "<li class=\"list-group-item\">" + value +
                        "<input class=\"attr_color_picker\" type=\"color\" id=\"attribute_" + index + "_value_" + j +"\" value = \"#ff0000\"></li>";
                } else if (name === "color" && value === "odd") {
                    selected_attribute_values.innerHTML += "<li class=\"list-group-item\">" + value +
                        "<input class=\"attr_color_picker\" type=\"color\" id=\"attribute_" + index + "_value_" + j +"\" value = \"#0000ff\"></li>";
                } else {
                    selected_attribute_values.innerHTML += "<li class=\"list-group-item\">" + value +
                        "<input class=\"attr_color_picker\" type=\"color\" id=\"attribute_" + index + "_value_" + j +"\"></li>";
                }
            }
            // if the attribute_type is text
            else if (type === "text") {
                selected_attribute_values.innerHTML += "<li class=\"list-group-item\">"+ value +"</li>";
            }
        }

    }else{
        // remove the attribute from selected_vis_attr
        delete selected_vis_attr[index];
        // remove the attribute from selected_attr_list in HTML
        document.getElementById("attribute_" + index + "_list").remove();
    }
}

/**
 * When the user click on "Save" of the modal,
 * save the setting of selected attributes.
 */
let selected_attr_colors = {};
function save_selected_attributes() {
    let evens = cy.$('node[type="even"]');
    let odds = cy.$('node[type="odd"]');

    evens.move({
        parent: null
    });
    odds.move({
        parent: null
    })
    let compounds = cy.$('node[type="compound"]');
    compounds.remove();

    // map compound node to attribute
    let nodes = cy.$("node");
    for (let j = 0; j < nodes.length; j ++) {
        // current node
        let node = nodes[j];
        if (node.data("type") !== "even" && node.data("type") !== "odd") {
            continue ;
        }
        let node_id = node.data("id");
        console.log(node_id);

        // add nested compound nodes to the current node
        let current = node;
        let have_text = false;

        for (let j in selected_vis_attr) {
            let attribute = selected_vis_attr[j];

            let type = attribute.type;
            if (type === "text") {
                have_text = true;
            }
            if (type === "color") {
                // create a compound node
                cy.add({
                        data: {
                            id: node_id + "_p_" + j,
                            type: "compound",
                            parent: ''
                        }
                    }
                );
                // add the newly created compound node to current
                current.move({
                    parent: node_id + "_p_" + j
                });
                // shift current
                current = current.parent();
            }
        }

        if (have_text) {
            cy.add({
                    data: {
                        id: node_id + "_p_" + "text",
                        type: "compound",
                        parent: ''
                    }
                }
            );
            current.move({
                parent: node_id + "_p_" + "text"
            });
        }

    }

    // save colors for each value and each attribute
    console.log(selected_vis_attr);
    for (let j in selected_vis_attr) {
        let attribute = selected_vis_attr[j]
        let attribute_name = attribute["name"];
        selected_attr_colors[attribute_name] = {};
        let attribute_values = document.getElementById("attribute_" + j + "_values");

        // iterate the values
        let values = attribute_values.getElementsByTagName("li");
        let colors = attribute_values.getElementsByTagName("input");
        for (let k = 0; k < attribute["values"].length; k ++) {
            let value = values[k].textContent;
            // check attribute type
            if (attribute["type"] === "color") {
                let color = colors[k].value;
                selected_attr_colors[attribute_name][value] = color;
                console.log(color);
            }
        }
    }
    let step = steps[step_ptr]["game"];
    update_style(step);
}

/**
* When the user click on "Attributes" to open the modal,
* clear the previously added compound nodes.
*/
// todo: fix this
// function clear_selected_attributes() {
//     let nodes = cy.$("node");
//     for (let j = 0; j < nodes.length; j ++) {
//         let node = nodes[j];
//         node.ancestors().remove();
//     }
// }