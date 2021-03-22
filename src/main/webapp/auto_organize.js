
/* auto-layout */
let colasetting = {
    refresh: 0.1,
    fit: false,
    // nodeSpacing: function( node ){ return 0; },
}
let auto_organizing = true;

function auto_organize() {
    // get input of the switch
    auto_organizing = document.getElementById("auto_organize_switch").checked;

}