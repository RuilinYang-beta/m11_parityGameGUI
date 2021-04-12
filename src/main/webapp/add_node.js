/**
 * node id of the next added node
 */
let node_id = 0;

/**
 * Add a Even node by clicking on the button.
 */
function addEven(){
    // add node
    cy.add({
            data: {
                id: 'node_' + node_id,
                type: 'even',
                parent: ""
            }
        }
    );

    // add listener for setting priority
    addPriorityListener('node_' + node_id);

    // id counter ++
    node_id ++;
    if (auto_organizing)
        cy.layout({name: 'cola', colasetting}).run();
}

/**
 * Add an Odd node by clicking on the button.
 */
function addOdd(){
    // add node
    cy.add({
            data: {
                id: 'node_' + node_id,
                type: 'odd',
                parent: ""
            }
        },
    );


    // add listener for setting priority
    addPriorityListener('node_' + node_id);

    // id counter ++
    node_id ++;
    if (auto_organizing)
        cy.layout({name: 'cola', colasetting}).run();
}

/**
 * Add a node through drag and drop.
 */
/* even node in the panel */
panel.on("drag", "#a_drag", function(evt) {
    evt.target.style().opacity = 0.4;
});
panel.on("dragfree", "#a_drag", function(evt) {
    // add node
    cy.add({
            data: {
                id: 'node_' + node_id,
                type: 'even',
                parent: ""
            }
        }
    );
    cy.$('#node_' + node_id).style("label", 0);

    // add listener for setting priority
    addPriorityListener('node_' + node_id);

    // set position of the added node
    let top = $("#cy").position().top;
    let left = $("#cy").position().left;
    cy.$("#node_" + node_id).renderedPosition({
            x: evt.target.renderedPosition('x') - left,
            y: evt.target.renderedPosition('y') - top
        }
    );

    // id counter ++
    node_id ++;

    // reset position of a-drag
    evt.target.position(
        panel.$("#a").position()
    );
});

/* odd node in the panel */
panel.on("drag", "#b_drag", function(evt) {
    evt.target.style().opacity = 0.4;
});
panel.on("dragfree", "#b_drag", function(evt) {
    // add node
    cy.add({
            data: {
                id: 'node_' + node_id,
                type: 'odd',
                parent: ""
            }
        }
    );
    cy.$('#node_' + node_id).style("label", 0);

    // add listener for setting priority
    addPriorityListener('node_' + node_id);

    // set position of the added node
    let top = $("#cy").position().top;
    let left = $("#cy").position().left;
    cy.$("#node_" + node_id).renderedPosition({
            x: evt.target.renderedPosition('x') - left,
            y: evt.target.renderedPosition('y') - top
        }
    );

    // id counter ++
    node_id ++;

    // reset position of b-drag
    evt.target.position(
        panel.$("#b").position()
    );
});

/**
 * Add a node with given owner and priority.
 * Make use of the addEven and addOdd function, in addition set the priority in the style of the node.
 * @returns {string} the id of the newly created node.
 */
function addNodeWithPriority(owner, priority){
    let thisId = 'node_' + node_id;
    if (owner % 2 === 0) {
        addEven();
    } else {
        addOdd();
    }
    cy.$(`#${thisId}`).style("label", priority);
    return thisId;
}