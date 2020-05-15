function getNames() {
    var url = '/board/programmer/name';
    $("#resultsBlock").load(url);
}
function getJob() {
    var url = '/board/manager/job';
    $("#resultsBlock").load(url);
}