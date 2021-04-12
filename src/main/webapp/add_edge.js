/**
set edge handler extension
*/
let defaults = {
    handleNodes: 'node[type="even"], node[type="odd"]', // selector/filter function for whether edges can be made from a given node

    loopAllowed: function( node ){
        // for the specified node, return whether edges from itself to itself are allowed
        return true;
    },
}
let eh = cy.edgehandles(defaults);
