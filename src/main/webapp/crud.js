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

    if (typeof nodes != 'undefined') {
        let gameString = getGameString(cy.nodes());

        // http POST
        let req = new XMLHttpRequest();
        req.onreadystatechange = function(){
            if (this.readyState === 4 && this.status === 200){
                console.log(this.responseText);
                console.log(JSON.parse(this.responseText));
            }
        }
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
        if (focus.data().type === undefined) {
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

/**
 * Add the game on page 119/205 of the slides http://tvandijk.nl/pdf/2019softwarescience.pdf
 * to the graph. This acts as a benchmark to compare our visualization with.
 */
let testGameString = "parity 9;\n" +
    "0 0 0 1;\n" +
    "1 2 1 0,5;\n" +
    "2 7 0 1,6;\n" +
    "3 1 1 2,4;\n" +
    "4 5 0 3,8;\n" +
    "5 8 0 6;\n" +
    "6 6 0 7;\n" +
    "7 2 0 3,8;\n" +
    "8 3 0 4,7;"

/**
 * From a gameString as in .pg file, to a graph model displayed.
 * @param gameString
 */
function addGraphFromString(gameString){
    // a map from the id in the gameString, to the actual id assigned
    let idMap = {};
    // a map from a node's id in the gameString, to the ids of its out nodes in the gameString
    let edgeMap = {};
    let gameArray = gameString.split("\n");
    gameArray.shift();

    for (let i = 0; i < gameArray.length; i++) {
        let nodeArray = gameArray[i].split(" ");

        let quasiId = nodeArray[0];
        let priority = nodeArray[1];
        let owner = nodeArray[2];
        // record in the map from quasiId to quasiOuts
        edgeMap[quasiId] = nodeArray[3].match(/\d+/g);    // TODO: sometimes there's label in the .pg file, need to support it

        let realId = addNodeWithPriority(owner, priority);
        idMap[quasiId] = realId;
    }

    // add edge
    for (let quasiId in edgeMap){                     // iterate over map, for .. in
        let realId = idMap[quasiId];
        for (let quasiOut of edgeMap[quasiId]) {      // iterate over array, for .. of
            let realOut = idMap[quasiOut];
            cy.add({
                data: { id: `${realId}-${realOut}`, source: realId, target: realOut}
            });
        }
    }
}


function dummyGraph(){
    addGraphFromString(testGameString);
}


