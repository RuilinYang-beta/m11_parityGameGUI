$(document).ready(function () {
    $("#sidebar").mCustomScrollbar({
        theme: "minimal"
    });

    $('#sidebarCollapse').on('click', function () {
        $('#sidebar, #content').toggleClass('active');
        $('.collapse.in').toggleClass('in');
        $('a[aria-expanded=true]').attr('aria-expanded', 'false');
    });


    $('.algorithm').on('click', function() {
        // clear any existing attributes in current attribute list
        let ul = document.querySelector('#attributes_list');
        while (ul.firstChild) {
            ul.removeChild(ul.firstChild);
        }
        // the highlight of selected algorithm
        $('.algorithm').removeClass('highlight');
        $(this).addClass('highlight');
        // post algorithm name to get its attributes
        set_algorithm(this.innerHTML);
    });
});

