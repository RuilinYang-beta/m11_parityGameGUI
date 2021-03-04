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