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


/**
 * When the play button is clicked, this function is triggered, the current graph is serialized
 * into a string of .pg file format and send to server.
 */
function post(){
    let nodes = cy.json().elements.nodes;

    if (typeof nodes !== undefined) {
        let gameString = getGameString(cy.nodes());

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
            }
        };

        // send http POST request
        req.open("POST", baseURL + "/vertex", true);
        req.setRequestHeader("Content-type", "text/plain");
        console.log(gameString);
        req.send(gameString);

    } else {
        console.log("add node first!");
    }
}


/**
 * Turn the current graph into a string representation, depending on the usage of the gameString,
 * the id of each node might need to change. If it is for posting to server, no change needed;
 * if it is for save to a file at client side, the ids will change to fill any hole. For example,
 * if the ids of the nodes are [77,78,83,101], the exported file will have ids [0,1,2,3].
 * @returns {string}
 */
function getGameString(nodesCy, forPost=true){

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

        // ids.push(id);
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

let algorithm;
let vis_attributes;

/**
 * Set the algorithm to be run;
 * Get the attributes of the algorithm as a list of json object;
 * @param algorithm
 * @returns {string}
 */
function set_algorithm(choice) {
    algorithm = choice;

    // todo: post algorithm choice to server

    // get visualization attributes
    get_attributes(algorithm);

    // todo: allow user to choose color for attributes
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

            console.log(vis_attributes);
        }
    };

    req.open("POST", baseURL + "/algorithm", true);
    req.setRequestHeader("Content-type", "text/plain");
    req.send(algorithm);
}

/**
 * Helper function of getGameString()
 */
function getIndex(wanted, array) {
    let idx = array.findIndex(e => e === wanted);
    if (idx === -1) {
        throw "it's not in the array!"
    }
    return idx;
}
