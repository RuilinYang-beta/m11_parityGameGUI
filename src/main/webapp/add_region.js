function add_region() {

    /* add_region(current_step)
        // Create a new region for the node in current_step:

        if current_step.type = 'base'
        then
            regionId = 'regionFor' + current_step.node,
            cy.add({
                data: {
                    id: regionId,
                    type: "region"
                }
            });
            current_step.node.parent = 'regionId';

        //incorporate the attractor into the region (is this the correct def of attractor?)
        else if current_step.type = 'attractor'
        then
            current_step.attractor.parent = current_step.node.parent.id;
     */
}