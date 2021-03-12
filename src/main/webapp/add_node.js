/* add node by button*/
let i = 0;
function addEven(){
    cy.add({
            data: {
                id: 'node' + i,
                type: "even"
            }
        }
    );
    i = i + 1;
    cy.layout({name: 'cola', colasetting}).run();
}

function addOdd(){
    cy.add({
            data: {
                id: 'node' + i,
                type: "odd"
            }
        }
    );
    i = i + 1;
    cy.layout({name: 'cola', colasetting}).run();
}


/**
 * Add a node with given owner and priority.
 * Make use of the addEven and addOdd function, in addition set the priority in the style of the node.
 * @returns {string} the id of the newly created node.
 */
function addNodeWithPriority(owner, priority){
    let thisId = 'node' + i;
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
    cy.add({
        data: {
            id: "node" + i,
            type: "even"
        },
    });
    let top = $("#cy").position().top;
    console.log(top);
    let left = $("#cy").position().left;
    cy.$("#node" + i).renderedPosition({
            x: evt.target.renderedPosition('x') - left,
            y: evt.target.renderedPosition('y') - top
        }
    );
    // cy.$("#node" + i).style("label", "1");
    i = i + 1;

    evt.target.position(
        panel.$("#a").position()
    );
});



panel.on("drag", "#b_drag", function(evt) {
    evt.target.style().opacity = 0.4;
});
panel.on("dragfree", "#b_drag", function(evt) {
    cy.add({
        data: {
            id: "node" + i,
            type: "odd"
        },
    });
    let top = $("#cy").position().top;
    let left = $("#cy").position().left;

    console.log(`user graph -- top: ${top}; left: ${left}`);
    console.log(`free position -- x: ${evt.target.renderedPosition('x')}; y: ${evt.target.renderedPosition('y')}`);

    cy.$("#node" + i).renderedPosition({
            x: evt.target.renderedPosition('x') - left,
            y: evt.target.renderedPosition('y') - top
        }
    );

    console.log(`actual position -- x: ${cy.$("#node" + i).renderedPosition('x')}; y: ${cy.$("#node" + i).renderedPosition('y')}`);

    i = i + 1;

    evt.target.position(
        panel.$("#b").position()
    );
});