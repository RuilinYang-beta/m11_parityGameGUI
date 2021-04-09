$(document).ready(function () {
    $("#sidebar").mCustomScrollbar({
        theme: "minimal"
    });

    $('#sidebarCollapse').on('click', function () {
        $('#sidebar, #content').toggleClass('active');
        $('.collapse.in').toggleClass('in');
        $('a[aria-expanded=true]').attr('aria-expanded', 'false');
    });
});

function select_algorithm(elem) {
    // handle the highlight of selected algorithm
    $('.algorithm').removeClass('highlight');
    $(elem).addClass('highlight');
    // post algorithm name to get its attributes
    set_algorithm(elem.innerHTML);
}