function getNames() {
    var url = '/board/programmer/name';
    $("#resultsBlock").load(url);
}
function getJob() {
    var url = '/board/manager/job';
    $("#resultsBlock").load(url);
}
function confirmStart(Form) {
    var id= $("#project-id").val();
    if (confirm("Are you sure ?")) {
        Form.action = '/board/manager/projects/'+id+'/start';
        return true;
    }
    return false;
}
function assign(empId) {
    var id= empId;
    alert(empId);
    var url = '/board/manager/projects/new/assign/'+empId;
    $("#results-assigned").load(url);
}
