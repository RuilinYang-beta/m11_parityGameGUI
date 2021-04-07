/**
 * Extract the current game, and save it in a file of .pg format.
 * The file is saved to the default download folder of the browser.
 * If the import file have holes in the id, for example the id of the nodes are [1,2,6,7,10],
 * the export will fill the holes;
 * If user change the graph by hand such that the ids has holes, the export will also fill any existing hole.
 */
function exportGame(){
    // getGameString is defined in crud.js
    let gameString = getGameString(cy.nodes(), false);

    downloadFile(gameString, currentFile + ".pg");
}

function exportSolution(){
    if (steps === undefined || steps === null) {
        // TODO: make export unclickable if algorithm not ran before
        console.log("post the gameString to server first!");
        return;
    }

    let last = steps[steps.length - 1].game;
    let ids = Object.keys(last);


    let solString = [];
    jQuery.each(last, function(key, val){
        let nodeString = "";
        // id
        let id = val.id;
        let idForExp = getIndex(id, ids);
        nodeString += idForExp;
        // winner
        let win = val.winner;
        nodeString += " " + win;
        // only write strategy if a node has strategy
        if (val.strategy !== "null") {
            let strategy = val.strategy;
            let strForExp = getIndex(strategy, ids);
            nodeString += " " + strForExp;
        }
        solString.push(nodeString);
    })


    // sort gameString by id, (in some cases the order of the id will be messed up)
    solString = solString.sort((a, b) => {
        return +(a.split(" ")[0]) - +(b.split(" ")[0])
    });

    solString.unshift("paritysol " + solString.length + ";");
    solString = solString.join("\n");

    downloadFile(solString, currentFile + ".pgsol");
}


function downloadFile(content, filename){
    const a = document.createElement('a');
    const file = new Blob([content], {type: 'text/plain'});
    a.href= URL.createObjectURL(file);
    a.download = filename;
    a.click();

    URL.revokeObjectURL(a.href);
}