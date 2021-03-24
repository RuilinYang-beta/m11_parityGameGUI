/**
 * step forward & backward
 * **/
// index of the current step
let step_ptr = 0;
// maximum number of attributes displayed
let max_num_attr = 3;

// todo: temporary selected visualization attributes
// todo: choose attributes first then choose color assignment
let selected_vis_attr = ["color"];
// todo: temporary color assignment
let selected_attr_colors = {
    "color": {
        "even": "red",
        "odd": "blue"
    }
};

let effect_to_opacity = {
    "highlight": 1,
    "shade": 0.25,
    "neutralize": 0.65,
    "neutral": 0.65
}

/**
 * Displays the next step of the algorithm.
 * **/
function step_forward() {
    // point to the desired step
    if (step_ptr >= steps.length - 1) {
        return ;
    }
    step_ptr += 1;
    let step = steps[step_ptr]["update"];
    update_style(step);

}

function step_backward() {
    // point to the desired step
    if (step_ptr <= 0) {
        return ;
    }
    step_ptr -= 1;
    let step = steps[step_ptr]["game"];
    update_style(step);

}

function update_style(step) {
    // pop previous states of the nodes from stack
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

function jump_to() {

}