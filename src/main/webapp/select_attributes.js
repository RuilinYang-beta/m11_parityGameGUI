function search_attributes() {
    var input, filter, table, tr, td, i;
    input = document.getElementById("search_attributes");
    filter = input.value.toUpperCase();
    table = document.getElementById("attributes_list");
    tr = table.getElementsByTagName("li");
    console.log(tr);
    for (i = 0; i < tr.length; i++) {
        // td = tr[i].getElementsByTagName("td")[0];
        td = tr[i].textContent;
        if (td) {
            if (td.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}