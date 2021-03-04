/* menu */
var contextMenu = cy.contextMenus({
    menuItems: [
        {
            id: 'color',
            content: 'change color',
            tooltipText: 'change color',
            selector: 'node',
            hasTrailingDivider: true,
            submenu: [
                {
                    id: 'color-blue',
                    content: 'blue',
                    tooltipText: 'blue',
                    onClickFunction: function (event) {
                        let target = event.target || event.cyTarget;
                        target.style('background-color', '#258fea');
                        target.style('type', 'even')
                        target.style('shape', `ellipse`)
                    },

                },
                {
                    id: 'color-red',
                    content: 'red',
                    tooltipText: 'red',
                    onClickFunction: function (event) {
                        let target = event.target || event.cyTarget;
                        target.style('background-color', '#e73413')
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

