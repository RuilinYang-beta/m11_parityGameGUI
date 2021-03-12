/* auto-layout */
var colasetting = {
    refresh: 0.1,
    fit: false,
    nodeSpacing: function( node ){ return 0; },
}
cy.on("drag", function(evt) {
    cy.layout({name: 'cola', colasetting}).run();
});
cy.on("dragfree", function(evt) {
    cy.layout({name: 'cola', colasetting}).run();
});