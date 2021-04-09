
cy.on("drag", function(evt) {
    if (auto_organizing)
        cy.layout({name: 'cola', colasetting}).run();
});
cy.on("dragfree", function(evt) {
    if (auto_organizing)
        cy.layout({name: 'cola', colasetting}).run();
});
