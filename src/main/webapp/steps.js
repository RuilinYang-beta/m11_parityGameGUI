/**
 * step forward & backward
 * **/
// index of the current step
let step_ptr = 0;

/**
 * dictionary to translate effect attribute to opacity in graph
 * **/
let effect_to_opacity = {
    "highlight": 1,
    "shade": 0.25,
    "neutral": 0.65
}
/**
 * dictionary to translate strategy to default color in graph
 * **/
let strategy_to_color = {
    "even": "blue",
    "odd": "red"
}

/**
 * display the next step of the algorithm
 * **/
function step_forward() {
    // point to the desired step
    if (step_ptr >= steps.length - 1) return ;

    step_ptr += 1;
    let step = steps[step_ptr]["game"];
    update_style(step);
    document.getElementById("slider").value = step_ptr + "";
    selectChannel(step_ptr);
}


/**
 * display the previous step of the algorithm
 * **/
function step_backward() {
    // point to the desired step
    if (step_ptr <= 0) return ;

    step_ptr -= 1;
    let step = steps[step_ptr]["game"];
    update_style(step);
    document.getElementById("slider").value = step_ptr + "";
    selectChannel(step_ptr);
}

/**
 * helper function to update style of nodes/edges according to the step
 * **/
function update_style(step) {
    for (let key in step) {
        let node = step[key];

        // set style of strategy edge
        let strategy = node.strategy;
        let owner = cy.$("#node_" + node.id).data("type");
        let edges = cy.edges("[source = \"node_" + node.id + "\"]");
        if (strategy != null) {
            if (selected_attr_colors["color"] !== null) {
                strategy_to_color[owner] = selected_attr_colors["color"][owner];
            }
            for (let i = 0; i < edges.length; i ++) {
                let edge = edges[i];
                if (edge.data("target") === "node_" + strategy) {
                    edge.style({
                        "line-color": strategy_to_color[owner],
                        'target-arrow-color': strategy_to_color[owner]
                    });
                } else {
                    edge.style({
                        "line-color": null,
                        'target-arrow-color': null
                    });
                }
            }
        } else {
            for (let i = 0; i < edges.length; i ++) {
                let edge = edges[i];
                edge.style({
                    "line-color": null,
                    'target-arrow-color': null
                });
            }
        }

        // set style of the parent node objects
        let curr = cy.$('#node_' + node.id).parent();
        // get the effect values
        let opacity = effect_to_opacity[node.effect];
        // update color attributes
        for (let j in selected_vis_attr) {
            // get corresponding style
            let attribute = selected_vis_attr[j];
            let updated_value = node[attribute["name"]];
            let type = attribute.type;

            // restore default style if the effect is neutral.
            if (node.effect === "neutral") {
                // set style
                curr.style({
                    "background-color": null,
                    "opacity": null
                });
            } else {
                // set style according to the type of the attribute
                if (type === "color") {
                    let color = selected_attr_colors[attribute["name"]][updated_value];
                    // set the style.
                    curr.style({
                        "background-color": "" + color,
                        "opacity": opacity
                    });
                    // next parent node object
                    let next = curr.parent();
                    curr = next;
                }
            }
        }

        let have_text = false;
        // update text attributes
        let label_content = "";
        for (let j in selected_vis_attr) {
            // get corresponding style
            let attribute = selected_vis_attr[j];
            let updated_value = node[attribute["name"]];
            let type = attribute.type;

            if (type === "text") {
                have_text = true;
                if (updated_value && updated_value != "null") {
                    label_content += attribute["name"] + ": " + updated_value + "\n";
                }
            }
        }

        // if text attribute exists
        if (have_text) {
            curr = cy.$('#node_' + node.id);
            while (curr != null) {
                if (curr.parent().length == 0) break;
                curr = curr.parent();
            }
            var style = {
                "label": label_content,
                "text-wrap": "wrap",
                "text-valign": "left",
                "text-halign": "left"
            }
            curr.style(style);
        }
    }
}

/**
 * click and jump to step in the right steps_display panel
 * **/
function jump_to(i){
    let value = i.substring(5);
    step_ptr = parseInt(value, 10);
    let step = steps[step_ptr]["game"];
    update_style(step);
    document.getElementById("slider").value = step_ptr + "";
    // search the selected list in the right steps_display panel and highlight it.
    selectChannel(step_ptr);
}

/**
 * update position of the slider
 * **/
function updateSlider(value) {
    if (steps == null) return;
    step_ptr = parseInt(value, 10);
    let step = steps[step_ptr]["game"];
    update_style(step);
    // highlight the selected step in the right steps_display panel
    selectChannel(step_ptr);
}

/**
 * helper function that selects the step list in the right steps_display panel according to step
 * **/
function selectChannel(stepNumber) {
    let listItems = document.getElementById("steps_display").getElementsByTagName("li");
    var length = listItems.length;
    // search the selected list in the right steps_display panel and highlight it.
    for (let j = 0; j < length; j++) {
        listItems[j].className = "" + (j === stepNumber ? "list-group-item selected" : "list-group-item");
    }
}

/**
 * clear/reset
 * everything
 * **/
function clear_all() {
    clear_steps()
    cy.$("node").remove();
    node_id = 0;
}

/**
 * "onclick" event handler for "reset" button
 */
function clear_steps() {
    // clear selected_attr_colors setting
    selected_attr_colors = {};

    // clear step setting
    steps = null;
    step_ptr = 0;
    document.getElementById("steps_display").innerHTML = "";
    document.getElementById("slider").value = "0";

    // clear graph setting
    clear_compounds();
    let elem = document.getElementById("steps_display");
    elem.innerHTML = "";
    cy.edges().style('line-color', null);
    cy.edges().style('target-arrow-color', null);
}

/**
 * clear all parent nodes
 */
function clear_compounds() {
    let evens = cy.$('node[type="even"]');
    let odds = cy.$('node[type="odd"]');

    evens.move({
        parent: null
    });
    odds.move({
        parent: null
    });
    let compounds = cy.$('node[type="compound"]');
    compounds.remove();
}