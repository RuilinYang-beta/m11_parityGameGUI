function toggle_arrow(elem) {
    alert(elem.getAttribute("aria-expanded"));
    if (elem.getAttribute("aria-expanded") == "false") {
        console.log(elem.getAttribute("aria-expanded"));
        console.log(elem.getElementsByTagName("i")[0]);
        elem.getElementsByTagName("i")[0].setAttribute("class", "arrow down");
        console.log(elem.getElementsByTagName("i")[0]);

    }
    else {
        console.log(elem.getAttribute("aria-expanded"));
        console.log(elem.getElementsByTagName("i")[0]);
        elem.getElementsByTagName("i")[0].setAttribute("class", "arrow right");
        console.log(elem.getElementsByTagName("i")[0]);
    }
}