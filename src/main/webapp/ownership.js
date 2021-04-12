/**
 * Toggle ownership of selected nodes.
 */
function toggle_ownership() {
    let nodes = cy.$(':selected');

    for (let i = 0; i < nodes.length; i++) {
        let node = nodes[i];

        if (node.data("type") === "even") {
            node.data("type", "odd");
            node.style("shape", "pentagon");
        }
        else if(node.data("type") === "odd") {
            node.data("type", "even");
            node.style("shape", "ellipse");
        }
    }
}