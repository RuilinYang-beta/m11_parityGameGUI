// a variable storing current file name
let currentFile = null;

function upload(){
    $("#import_file").click();
}

$("#import_file").change(readSingleFile);

/**
 * From a event read the content of the file.
 */
function readSingleFile(e) {
    currentFile = null;
    let file = e.target.files[0];
    if (!file) {
        console.log("not file");
        return;
    }
    if (!file.name.endsWith(".pg")){
        alert("please choose a .pg file");
        return;
    }
    currentFile = file.name.split(".")[0];
    var reader = new FileReader();
    reader.readAsText(file);
    reader.onload = function(e) {
        console.log(reader.result.trim());
        addGraphFromString(reader.result.trim());
    };
}


/**
 * From a gameString as in .pg file, to construct a graph model and display it.
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
        edgeMap[quasiId] = nodeArray[3].match(/\d+/g);
        // TODO: sometimes there's label in the .pg file, need to support it

        // add nodes
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
 * Add the game on page 119/205 of the slides http://tvandijk.nl/pdf/2019softwarescience.pdf
 * to the graph. This acts as a benchmark to compare our visualization with.
 */
function dummyGraph(){
    addGraphFromString(testGameString);
}
