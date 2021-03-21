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

    // push current states of the nodes to stack
    // let prev_step = [];
    // step.forEach(function (item) {
    //     // collect colors of compound nodes
    //     let curr = cy.$('#node' + item.id).parent();
    //     let compound_colors = {};
    //     while (curr != null) {
    //         compound_colors.add(curr.style('background-color'));
    //         curr = curr.parent();
    //     }
    //
    //     // push current states of the nodes to stack
    //     prev_step.push({
    //         opacity: cy.$('#node' + item.id).style('opacity'),
    //         id: 'node' + item.id,
    //         compound_colors: compound_colors
    //     });
    // });
    // stack.push(prev_step);

    // update status of the nodes
    for (let key in step) {
        let node = step[key];
        // set style of the parent node objects
        let curr = cy.$('#node' + node.id).parent();
        let i = 0;
        // todo: ensure that parent nodes and selected attributes are consistent
        // iterate the selected attributes and set style according to the updated value.
        while (curr.length != 0) {
            console.log(curr);
            // get corresponding style
            let attribute = selected_vis_attr[i];

            let updated_value = node[attribute];
            let color = selected_attr_colors[attribute][updated_value];
            console.log(color);
            // set style
            curr.style("background-color", ""+color);
            // next parent node object
            curr = curr.parent();
            i++;
        }
    }
}

function step_backward() {
    // point to the desired step
    if (step_ptr <= 0) {
        return ;
    }
    step_ptr -= 1;

    // pop previous states of the nodes from stack
    let prev_step = stack.pop();

    // restore previous states of the nodes
    prev_step.forEach(function (item) {
        console.log(item);
        cy.$("#pnode" + item.id).style({
            "background-color": item.color,
            "opacity": item.highlight
        });
    });
}

function jump_to() {

}