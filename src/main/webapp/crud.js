baseURL = "http://localhost:8080/rest"

// id:          cy.$(`#${node}`).data().id     --> trim
// owner:       cy.$(`#${node}`).data().type
// priority:    cy.$(`#${node}`).style().label
// out:         cy.$("#node0").neighborhood("node").forEach( e => console.log(e.data().id))

/**
 * When the play button is clicked, this function is triggered, the current graph is serialized
 * into a .pg file format and send to server.
 */
function tempFunc(){
    // only send relevant data to server!
    let nodes = cy.json().elements.nodes;

    if (typeof nodes !== undefined) {
        let gameString = getGameString(cy.nodes());

        // http POST
        let req = new XMLHttpRequest();
        req.onreadystatechange = function(){
            if (this.readyState === 4 && this.status === 200) {
                steps = JSON.parse(this.responseText);
                console.log(steps);
            }
        };
        req.open("POST", baseURL + "/vertex", true);
        req.setRequestHeader("Content-type", "text/plain");
        req.send(gameString);

    } else {
        console.log("add node first!");
    }


}

/**
 * Turn the current nodes on the graph into a string representation.
 * @param nodesCy
 * @returns {string}
 */
function getGameString(nodesCy){

    let gameString = [];

    // populate nodesJSON
    for (let i = 0; i < nodesCy.length; i++){
        let focus = nodesCy[i];
        // in case this is not a node
        if (focus.data().type === undefined || focus.data().type === 'compound') {
            continue;
        }
        let nodeString = "";
        let focusId = focus.data().id;

        // id
        nodeString += focusId.slice(4) + " ";
        // priority
        nodeString += focus.style().label + " ";
        // owner
        nodeString += (focus.data().type === "even") ? "0 " : "1 ";
        // outs
        nodeString += focus.neighborhood("edge").filter(e => e.data().source === focusId).map(e => e.data().target.slice(4)).join(",") + ";";

        gameString.push(nodeString);

        // TODO: sometimes a node has label in .pg files, deal with it (need enable setting txt label in UI)
    }

    gameString.unshift("parity " + gameString.length + ";");
    gameString = gameString.join("\n");

    console.log(gameString);
    return gameString;
}



