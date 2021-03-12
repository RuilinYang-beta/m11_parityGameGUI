/* user graph */
let cy = cytoscape({
    container: document.getElementById("cy"),

    // test graph
    elements: [
        // compound nodes
        { data: { id: 'pnode0', type: 'compound'} },
        { data: { id: 'pnode1', type: 'compound'}  },
        { data: { id: 'pnode2', type: 'compound'} },
        { data: { id: 'pnode3', type: 'compound'}  },
        { data: { id: 'pnode4', type: 'compound'} },
        { data: { id: 'pnode5', type: 'compound'} },
        { data: { id: 'pnode6', type: 'compound'} },
        { data: { id: 'pnode7', type: 'compound'} },
        { data: { id: 'pnode8', type: 'compound'} },
        // nodes
        { data: { id: 'node0', type: 'even', parent: 'pnode0'} },
        { data: { id: 'node1', type: "odd", parent: 'pnode1'}  },
        { data: { id: 'node2', type: 'even', parent: 'pnode2'} },
        { data: { id: 'node3', type: 'odd', parent: 'pnode3'}  },
        { data: { id: 'node4', type: 'even', parent: 'pnode4'} },
        { data: { id: 'node5', type: 'even', parent: 'pnode5'} },
        { data: { id: 'node6', type: 'even', parent: 'pnode6'} },
        { data: { id: 'node7', type: 'even', parent: 'pnode7'} },
        { data: { id: 'node8', type: 'even', parent: 'pnode8'} },
        { data: { id: '0-1', source: 'node0', target: 'node1' } },
        { data: { id: '1-0', source: 'node1', target: 'node0' } },
        { data: { id: '1-5', source: 'node1', target: 'node5' } },
        { data: { id: '2-1', source: 'node2', target: 'node1' } },
        { data: { id: '2-6', source: 'node2', target: 'node6' } },
        { data: { id: '3-2', source: 'node3', target: 'node2' } },
        { data: { id: '3-4', source: 'node3', target: 'node4' } },
        { data: { id: '4-3', source: 'node4', target: 'node3' } },
        { data: { id: '4-8', source: 'node4', target: 'node8' } },
        { data: { id: '5-6', source: 'node5', target: 'node6' } },
        { data: { id: '6-7', source: 'node6', target: 'node7' } },
        { data: { id: '7-3', source: 'node7', target: 'node3' } },
        { data: { id: '7-8', source: 'node7', target: 'node8' } },
        { data: { id: '8-4', source: 'node8', target: 'node4' } },
        { data: { id: '8-7', source: 'node8', target: 'node7' } },
    ],

    style: [
        // styles for test graph

        { selector: '#node0', style: { 'label': '0'} },
        { selector: '#node1', style: { 'label': '2'} },
        { selector: '#node2', style: { 'label': '7'} },
        { selector: '#node3', style: { 'label': '1'} },
        { selector: '#node4', style: { 'label': '5'} },
        { selector: '#node5', style: { 'label': '8'} },
        { selector: '#node6', style: { 'label': '6'} },
        { selector: '#node7', style: { 'label': '2'} },
        { selector: '#node8', style: { 'label': '3'} },

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
                'background-color': '#ffffff'
            },
        },
        {
            selector: 'node[type="odd"]',
            style: {
                'shape': 'pentagon',
                'background-color': '#ffffff',
            }
        },

        // position of priority number
        {
            selector: "node",
            style: {
                "text-halign": "center",
                "text-valign": "center",
            }
        },
    ],
});

let steps;
let step_ptr = 0;