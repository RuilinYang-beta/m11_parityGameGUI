baseURL = "http://localhost:8080/rest"

window.onload = function () {
    // http GET
    let req = new XMLHttpRequest();

    // handle response
    req.onreadystatechange = function(){
        if (this.readyState === 4 && this.status === 200) {
            let algorithms = JSON.parse(this.responseText);
            let elem = document.getElementById("select_algorithm");
            elem.innerHTML = "";
            for (let k = 0; k < algorithms.length; k ++) {
                elem.innerHTML += "<li>\n" +
                    "<a href=\"#\" class=\"algorithm\" data-toggle=\"modal\" data-target=\"#attributes_modal\" onclick=\"select_algorithm(this)\">" + algorithms[k] + "</a>\n" +
                    "</li>";
            }
        } else if (this.readyState === 4 && this.status !== 200 && this.status !== 0) {
            alert(`Server returned error code ${this.status}.`);
        }
    };

    // send http GET request
    req.open("GET", baseURL + "/algorithm/all", true);
    req.setRequestHeader("Content-type", 'application/json; charset=UTF-8');
    req.send(null);
}