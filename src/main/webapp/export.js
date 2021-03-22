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
        console.log("post the gameString to server first!");
        return;
    }

    let last = steps[steps.length - 1].game;

    // TODO: how to make this safer?
    let ids = cy.nodes().filter(e => e.data().type === "odd" || e.data().type === "even")
                        .map(e => parseInt(e.data().id.match(/\d+/g)));

    let solString = [];
    jQuery.each(last, function(key, val){
        // console.log(`${val.id}, ${val.winner}, ${val.strategy}`);

        let nodeString = "";

        let id = parseInt(val.id);
        let idForExp = getIndex(id, ids);
        nodeString += idForExp;

        let win = val.winner;
        nodeString += " " + win;

        // only some of the nodes have strategy
        if (val.hasOwnProperty("strategy")) {
            let strategy = parseInt(val.strategy);
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