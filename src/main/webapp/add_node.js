/* add node by button*/
let node_id = 0;
function addEven(){
    // // initialize region
    // cy.add({
    //         data: {
    //             id: 'pnode' + i,
    //             type: "compound",
    //             selectable: false
    //         }
    //     }
    // );

    // add node
    cy.add({
            data: {
                id: 'node_' + node_id,
                type: 'even',
                parent: ''
            }
        }
    );

    // add listener for setting priority
    addPriorityListener(node_id);

    // id counter ++
    node_id ++;
    if (auto_organizing)
        cy.layout({name: 'cola', colasetting}).run();
}

function addOdd(){
    // // initialize region
    // cy.add({
    //         data: {
    //             id: 'pnode' + i,
    //             type: "compound",
    //             selectable: false,
    //
    //         }
    //     }
    // );

    // add node
    cy.add({
            data: {
                id: 'node_' + node_id,
                type: 'odd',
                parent: ''
            }
        },
    );


    // add listener for setting priority
    addPriorityListener(node_id);

    // id counter ++
    node_id ++;
    if (auto_organizing)
        cy.layout({name: 'cola', colasetting}).run();
}


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

/* add node by drag-and-drop */
panel.on("drag", "#a_drag", function(evt) {
    evt.target.style().opacity = 0.4;
});
panel.on("dragfree", "#a_drag", function(evt) {
    // // initialize region
    // cy.add({
    //     data: {
    //         id: "pnode" + node_id,
    //         type: "compound",
    //         selectable: false
    //     }
    // });

    // add node
    cy.add({
        data: {
            id: 'node_' + node_id,
            type: 'even',
            parent: ''
        },
    });

    // add listener for setting priority
    addPriorityListener(node_id);

    // set position of the node
    let top = $("#cy").position().top;
    console.log(top);
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



panel.on("drag", "#b_drag", function(evt) {
    evt.target.style().opacity = 0.4;
});
panel.on("dragfree", "#b_drag", function(evt) {
    // // initialize region
    // cy.add({
    //     data: {
    //         id: "pnode" + node_id,
    //         type: "compound",
    //         selectable: false
    //     },
    // });

    // add node
    cy.add({
        data: {
            id: 'node_' + node_id,
            type: 'odd',
            parent: ''
        },
    });

    // add listener for setting priority
    addPriorityListener(node_id);

    // set position of the node
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