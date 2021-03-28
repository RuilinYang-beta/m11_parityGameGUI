/* user graph */
let cy = cytoscape({
    container: document.getElementById("cy"),

    style: [
        {
            selector: ':selected',
            style: {
                'border-width': 18,
                'border-opacity': 0.2
            }
        },

        {
            selector: 'edge',
            style: {
                'curve-style': 'bezier',
                'target-arrow-shape': 'triangle',
                'background-color': 'red',
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
                'background-color': 'lightsteelblue'
            },
        },
        {
            selector: 'node[type="odd"]',
            style: {
                'shape': 'pentagon',
                'background-color': 'lightsteelblue',
            }
        },

        // position of priority number
        {
            selector: "node",
            style: {
                "text-halign": "center",
                "text-valign": "center",
                "border-style": "solid",
                "border-color": "steelblue",
                "border-opacity": 1,
            }
        },

        // default opacity of compound node
        {
            selector: 'node[type="compound"]',
            style: {
                "opacity": 0.65
            }
        },
    ],
});

