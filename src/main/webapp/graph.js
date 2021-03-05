/* user graph */
let cy = cytoscape({
    container: document.getElementById("cy"),
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