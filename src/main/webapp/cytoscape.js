/* user graph */


let cy = cytoscape({
    container: document.getElementById("cy"),
    elements: [
        {data: {id: "node0"}},
        {data: {id: "node1"}},
        {
            data: {
                id: "01",
                source: "node0",
                target: "node1"
            }
        }],
    style: [
        {
            selector: 'node[name]',
            style: {
                'content': 'data(name)'
            }
        },

        {
            selector: ':selected',
            style: {

            }
        },

        {
            selector: 'edge',
            style: {
                'curve-style': 'bezier',
                'target-arrow-shape': 'triangle'
            }
        },

        // some style for the extension
        {
            selector: '.eh-handle',
            style: {
                'background-color': 'red',
                'width': 12,
                'height': 12,
                'shape': 'ellipse',
                'overlay-opacity': 0,
                'border-width': 12, // makes the handle easier to hit
                'border-opacity': 0
            }
        },

        {
            selector: '.eh-hover',
            style: {
                'background-color': 'red'
            }
        },

        {
            selector: '.eh-source',
            style: {
                'border-width': 2,
                'border-color': 'red'
            }
        },

        {
            selector: '.eh-target',
            style: {
                'border-width': 2,
                'border-color': 'red'
            }
        },

        {
            selector: '.eh-preview, .eh-ghost-edge',
            style: {
                'background-color': 'red',
                'line-color': 'red',
                'target-arrow-color': 'red',
                'source-arrow-color': 'red'
            }
        },

        {
            selector: '.eh-ghost-edge.eh-preview-active',
            style: {
                'opacity': 0
            }
        },

        // even and odd node
        {
            selector: 'node[type="even"]',
            style: {
                'background-color': '#258fea'
            },
        },
        {
            selector: 'node[type="odd"]',
            style: {
                'shape': 'pentagon',
                'background-color': '#e73413',
            }
        }
    ],
});


/* fixed node panel */
let panel = cytoscape({
    container: document.getElementById("panel"),
    style: [
        {
            selector: 'node[type="even"]',
            style: {
                'background-color': '#258fea'
            },
        },
        {
            selector: 'node[type="odd"]',
            style: {
                'shape': 'pentagon',
                'background-color': '#e73413',
            }
        },
        {
            selector: "node",
            style: {
                "text-halign": "center",
                "text-valign": "bottom",
            }
        },
        {
            selector: '#a',
            style: {
                'label': 'even'
            }
        },
        {
            selector: '#b',
            style: {
                'label': 'odd'
            }
        }
    ],
    layout: {name: 'preset'},
});

panel.add({
    data: {id: "a", type: "even"},
    renderedPosition: {x: 50, y: 100}
})

panel.add({
    data: {id: "b", type: "odd"},
    renderedPosition: {x: 50, y: 200}
})

panel.add({ data: { id: "a_drag", type: "even" } });
panel.add({ data: { id: "b_drag", type: "odd" } });

panel.$("#a_drag").renderedPosition(
    panel.$("#a").renderedPosition()
);
panel.$("#b_drag").renderedPosition(
    panel.$("#b").renderedPosition()
)


panel.$("#a").lock();
panel.$("#b").lock();
panel.userZoomingEnabled(false);
panel.userPanningEnabled(false);
cy.userZoomingEnabled(true);
cy.userPanningEnabled(true);

/* add node by button*/
let i = 2;
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

/* edge handlers */
let eh = cy.edgehandles();

/* auto-layout */
var colasetting = {
    refresh: 0.1,
    fit: false
}
cy.on("drag", function(evt) {
    cy.layout({name: 'cola', colasetting}).run();
});
cy.on("dragfree", function(evt) {
    cy.layout({name: 'cola', colasetting}).run();
});

//undoRedo
var ur = cy.undoRedo({
    isDebug: true
});

cy.on("afterUndo", function (e, name) {
    document.getElementById("undos").innerHTML += "<span style='color: darkred; font-weight: bold'>Undo: </span> " + name  +"</br>";
});

cy.on("afterRedo", function (e, name) {
    document.getElementById("undos").innerHTML += "<span style='color: darkblue; font-weight: bold'>Redo: </span>" + name  +"</br>";
});

cy.on("afterDo", function (e, name) {
    document.getElementById("undos").innerHTML += "<span style='color: darkmagenta; font-weight: bold'>Do: </span>" + name  +"</br>";
});

document.addEventListener("keydown", function (e) {
    if(e.which === 46) {
        var selecteds = cy.$(":selected");
        if (selecteds.length > 0)
            ur.do("remove", selecteds);
    }
    else if (e.ctrlKey && e.target.nodeName === 'BODY')
        if (e.which === 90)
            ur.undo();
        else if (e.which === 89)
            ur.redo();

});

document.getElementById('batchButton').addEventListener("click", function(e){
    actions = [];

    actions.push({
        name: "remove",
        param: cy.$(":selected")
    });
    ur.do("batch", actions);
});


//menu


var contextMenu = cy.contextMenus({
    menuItems: [
        {
            id: 'color',
            content: 'change color',
            tooltipText: 'change color',
            selector: 'node',
            hasTrailingDivider: true,
            submenu: [
                {
                    id: 'color-blue',
                    content: 'blue',
                    tooltipText: 'blue',
                    onClickFunction: function (event) {
                        let target = event.target || event.cyTarget;
                        target.style('background-color', '#258fea');
                        target.style('type', 'even')
                        target.style('shape', `ellipse`)
                    },

                },
                {
                    id: 'color-red',
                    content: 'red',
                    tooltipText: 'red',
                    onClickFunction: function (event) {
                        let target = event.target || event.cyTarget;
                        target.style('background-color', '#e73413')
                        target.style('type', 'odd')
                        target.style('shape', `pentagon`)
                    },
                },
            ]
        },

    ],
    menuItemClasses: ['custom-menu-item'],
    contextMenuClasses: ['custom-context-menu']
});

