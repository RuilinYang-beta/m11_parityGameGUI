/**
 * trigger auto-layout when nodes in the current core graph is dragged/freed
 * */
cy.on("drag", function(evt) {
    if (auto_organizing)
        cy.layout({name: 'cola', colasetting}).run();
});
cy.on("dragfree", function(evt) {
    if (auto_organizing)
        cy.layout({name: 'cola', colasetting}).run();
});
