function toggle_arrow(elem) {

    if (elem.getAttribute("aria-expanded") == "false") {
        elem.getElementsByTagName("i")[0].setAttribute("class", "arrow down");
    }
    else {
        elem.getElementsByTagName("i")[0].setAttribute("class", "arrow right");
    }
}