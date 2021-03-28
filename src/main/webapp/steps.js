/**
 * step forward & backward
 * **/
// index of the current step
let step_ptr = 0;
// maximum number of attributes displayed
let max_num_attr = 3;

// todo: temporary selected visualization attributes
// todo: choose attributes first then choose color assignment
// let selected_vis_attr = ["color"];
// todo: temporary color assignment
let selected_attr_colors = {
    "color": {
        "even": "red",
        "odd": "blue"
    }
};

/**
 * dictionary to translate effect attribute to opacity in graph
 * **/
let effect_to_opacity = {
    "highlight": 1,
    "shade": 0.25,
    "neutralize": 0.65,
    "neutral": 0.65
}

/**
 * display the next step of the algorithm
 * **/
function step_forward() {

    // point to the desired step
    if (step_ptr >= steps.length - 1) {
        return ;
    }
    step_ptr += 1;
    let step = steps[step_ptr]["update"];
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
    // todo: do we need to do the stack? I think we do.
    for (let key in step) {
        let node = step[key];
        // set style of the parent node objects
        let curr = cy.$('#node' + node.id).parent();
        let i = 0;
        // get the effect values
        let opacity = effect_to_opacity[node.effect];
        while (curr.length !== 0) {
            console.log(curr);
            // get corresponding style
            let attribute = selected_vis_attr[i];

            let updated_value = node[attribute];
            let color = selected_attr_colors[attribute][updated_value];
            // Restore default style if the effect is neutralize.
            if (node.effect == "neutral") {
                // set style
                curr.style({
                    "background-color": null,
                    "opacity": null
                });
            }
            // Otherwise, set the style.
            else {
                curr.style({
                    "background-color": ""+color,
                    "opacity": opacity
                });
            }
            // next parent node object
            curr = curr.parent();
            i++;
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
        listItems[j].className = "" + (j == stepNumber ? "list-group-item selected" : "list-group-item");
    }
}

/**
 * clear/reset
 * everything
 * **/
function clear_all() {
    steps = null;
    step_ptr = 0;
    document.getElementById("slider").value = "0";
    cy.$("node").remove();
    i = 0;
    let elem = document.getElementById("steps_display");
    elem.innerHTML = "";
}