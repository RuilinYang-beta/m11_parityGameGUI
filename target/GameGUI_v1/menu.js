/* menu */
var contextMenu = cy.contextMenus({
    menuItems: [
        {
            id: 'ownership',
            content: 'change ownership',
            tooltipText: 'change ownership',
            selector: 'node',
            hasTrailingDivider: true,
            submenu: [
                {
                    id: 'shape-even',
                    content: 'even',
                    tooltipText: 'even',
                    onClickFunction: function (event) {
                        let target = event.target || event.cyTarget;

                        target.style('type', 'even')
                        target.style('shape', `ellipse`)
                    },

                },
                {
                    id: 'shape-odd',
                    content: 'odd',
                    tooltipText: 'odd',
                    onClickFunction: function (event) {
                        let target = event.target || event.cyTarget;
                        target.style('type', 'odd')
                        target.style('shape', `pentagon`)
                    },
                },
            ]
        },

    ],
    menuItemClasses: ['custom-menu-item'],
    contextMenuClasses: ['custom-context-menu']
});

