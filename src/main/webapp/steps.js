let stack = [];
function step_forward() {
    // point to the desired step
    if (step_ptr >= steps.length - 1) {
        return ;
    }
    step_ptr += 1;
    let step = steps[step_ptr];

    // push current states of the nodes to stack
    let prev_step = []
    step.focus.forEach(function (item) {
        let v = cy.$("#pnode" + item);
        prev_step.push({
            id: item,
            color: v.style("background-color"),
            highlight: v.numericStyle("opacity")
        });
    });
    stack.push(prev_step);

    // choose region color
    let color = "#258fea";
    if (step.type === 1) {
        color = '#e73413';
    }
    // choose highlight or shade
    let highlight = 0.27;
    if (step.action === "HIGHLIGHT") {
        highlight = 0.72;
    }
    // change the style of the corresponding nodes
    step.focus.forEach(function (item) {
        cy.$("#pnode" + item).style({
            "opacity": highlight,
            "background-color": color
        });
    });
}

function step_backward() {
    // point to the desired step
    if (step_ptr <= 0) {
        return ;
    }
    step_ptr -= 1;

    // pop previous states of the nodes from stack
    let prev_step = stack.pop()

    // restore previous states of the nodes
    prev_step.forEach(function (item) {
        console.log(item);
        cy.$("#pnode" + item.id).style({
            "background-color": item.color,
            "opacity": item.highlight
        });
    });
}