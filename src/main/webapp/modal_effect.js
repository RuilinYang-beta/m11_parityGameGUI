function toggle_arrow(elem) {
    // toggle arrow
    if (elem.getAttribute("aria-expanded") == "false") {
        elem.getElementsByTagName("i")[0].setAttribute("class", "arrow down");
    }
    else {
        elem.getElementsByTagName("i")[0].setAttribute("class", "arrow right");
    }

    // enable apply
    // document.getElementById("apply_attribute_setting").disabled = false;
}

function enable_apply() {
    document.getElementById("apply_attribute_setting").disabled = false;
};
