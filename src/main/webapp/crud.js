baseURL = "http://localhost:8080/rest"

// id:          cy.$(`#${node}`).data().id     --> trim
// owner:       cy.$(`#${node}`).data().type
// priority:    cy.$(`#${node}`).style().label
// out:         cy.$("#node0").neighborhood("node").forEach( e => console.log(e.data().id))
let steps;


// `cy.nodes()` have access to the style().label (priority) of a node
// `cy.json().elements.nodes` can quickly access id/type of a node but not style

// `cy.nodes()` returns a generator
// `cy.json()` give you all the stuffs

/* post to get steps */
/**
 * When the play button is clicked, this function is triggered, the current graph is serialized
 * into a string of .pg file format and send to server; the response steps is stored and the message
 * of each step is displayed.
 */
function post(){
    save_selected_attributes();
    let nodes = cy.json().elements.nodes;

    if (typeof nodes !== undefined) {
        // http POST
        let req = new XMLHttpRequest();
        req.onreadystatechange = function(){
            if (this.readyState === 4 && this.status === 200) {
                // save steps
                steps = JSON.parse(this.responseText);
                console.log(steps);
                document.getElementById("slider").setAttribute("max", steps.length - 1 + "");
                // display steps
                let elem = document.getElementById("steps_display");
                elem.innerHTML = "";
                for (let key in steps) {
                    elem.innerHTML += "<li class=\"list-group-item\" type=\"button\" id=\"step " + key + "\" onclick=\"jump_to(this.id)\">" +
                        "STEP " + key + ": " +
                        steps[key].msg +
                        "</li>";
                }
                step_forward();
            }
            if (this.readyState === 4 && this.status !== 200) {
                alert(`Server returned error code ${this.status}.\nPlease check if the game is valid and/or the server console message.`);
            }
        };

        // prepare the data to send
        let algorithm = $('.highlight')[0].innerHTML;
        let gameString = getGameString(cy.nodes());
        // abort if the game is illegal
        if (gameString === ""){
            alert("Illegal parity game. Every node should have at least 1 successor.");
            return ;
        }
        let data = {};
        data["algorithm"] = algorithm;
        data["game"] = gameString;
        console.log(JSON.stringify(data));

        // send http POST request
        req.open("POST", baseURL + "/vertex", true);
        req.setRequestHeader("Content-type", 'application/json; charset=UTF-8');
        req.send(JSON.stringify(data));
    } else {
        console.log("add node first!");
    }

}

/**
 * Turn the current graph into a string representation, depending on the usage of the gameString,
 * the id of each node might need to change. If it is for posting to server, no change needed;
 * if it is for save to a file at client side, the ids will change to fill any hole. For example,
 * if the ids of the nodes are [77,78,83,101], the exported file will have ids [0,1,2,3].
 * @param nodesCy: nodes on the graph
 * @param forPost: if true, then the returned gameString is for posting to backend,
 * nodes can keep their id as they are in the graph; if false, then it's for export,
 * nodes ids will start from 0 and will be consecutive.
 * @returns {string}: the string repr of the game; if the game is illegal (there exist a node
 * with no successor, then return empty string.
 */
function getGameString(nodesCy, forPost=true){
    if (!isLegalGame(nodesCy)){
        return "";
    }

    let gameString = [];
    let ids = nodesCy.filter(e => e.data().type === "odd" || e.data().type === "even")
                     .map(e => parseInt(e.data().id.match(/\d+/g)));

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
        let id = parseInt(focusId.match(/\d+/g));
        if (!forPost) {
            id = getIndex(id, ids);
        }
        nodeString += id + " ";
        // priority
        nodeString += focus.style().label + " ";
        // owner
        nodeString += (focus.data().type === "even") ? "0 " : "1 ";
        // outs, a list of number string
        let outs = focus.neighborhood("edge")
                        .filter(e => e.data().source === focusId)
                        .map(e => parseInt(e.data().target.match(/\d+/g)));
        if (!forPost) {
            outs = outs.map(e => getIndex(e, ids));
        }
        nodeString += outs.join(",") + ";";

        gameString.push(nodeString);
    }

    // sort gameString by id, in some cases the order of the id will be messed up
    gameString = gameString.sort((a, b) => {
        return +(a.split(" ")[0]) - +(b.split(" ")[0])
    });

    gameString.unshift("parity " + gameString.length + ";");
    gameString = gameString.join("\n");

    return gameString;
}

/**
 * Check if current game is legal, that means, every node should have at least a successor,
 * and every node should have a priority.
 * @param nodesCy
 * @returns {boolean}: true if the game is legal.
 */
function isLegalGame(nodesCy){
    let nodes = nodesCy.filter(e => e.data().type === "odd" || e.data().type === "even");
    for (let node of nodes) {
        // successor sanity check
        let numSuccessor = node.neighborhood("edge").filter(e => e.data().source === node.data().id).length;
        if (numSuccessor === 0) {
            return false;
        }
        // priority sanity check
        if (node.style().label === "" || isNaN(parseInt(node.style().label))) {
            return false;
        }
    }
    return true;
}

// Helper function of getGameString(), only used when exporting
function getIndex(wanted, array) {
    let idx = array.findIndex(e => e === wanted);
    if (idx === -1) {
        throw "it's not in the array!"
    }
    return idx;
}

/* post to get steps */
let algorithm;
let vis_attributes;

/**
 * Set the algorithm to be run;
 * Get the attributes of the algorithm as a list of json object;
 * @param algorithm
 * @returns {string}
 */
function set_algorithm(choice) {
    // clear all steps
    clear_steps();

    // reset attribute area
    document.getElementById("attributes_list").innerHTML = "";
    document.getElementById("selected_attr_list").innerHTML = "";

    // reset related variables
    vis_attributes = [];
    selected_vis_attr = {};
    selected_attr_colors = {};

    // get visualized attributes
    algorithm = choice;
    get_attributes(algorithm);

    // open attribute modal
    // document.getElementById("attributes_modal").setAttribute("aria-hidden", "false");
}

/**
 * Helper Function;
 * Get the attributes of the algorithm as a list of json object;
 * @param algorithm
 * @returns {string}
 */
function get_attributes(algorithm) {
    // http POST
    let req = new XMLHttpRequest();
    req.onreadystatechange = function(){
        if (this.readyState === 4 && this.status === 200) {
            // the list of attributes
            vis_attributes = JSON.parse(this.responseText);

            // add attributes to the selection modal
            let attributes_list = document.getElementById("attributes_list");
            for (let index = 0; index < vis_attributes.length; index++) {
                let attribute = vis_attributes[index];
                attributes_list.innerHTML += "<li>" + attribute.name + "<input class=\"float-right\" type=\"checkbox\" " +
                                            "id=\"attribute_" + index +"\" onchange=\"handleChange(this, this.id, this.parentNode.textContent)\"></li>";
            }
        }
    };

    req.open("POST", baseURL + "/algorithm", true);
    req.setRequestHeader("Content-type", "text/plain");
    req.send(algorithm);
}


