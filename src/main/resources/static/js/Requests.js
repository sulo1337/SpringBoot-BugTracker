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
function setInputFilter(textbox, inputFilter) {
    ["input", "keydown", "keyup", "mousedown", "mouseup", "select", "contextmenu", "drop"].forEach(function(event) {
        textbox.addEventListener(event, function() {
            if (inputFilter(this.value)) {
                this.oldValue = this.value;
                this.oldSelectionStart = this.selectionStart;
                this.oldSelectionEnd = this.selectionEnd;
            } else if (this.hasOwnProperty("oldValue")) {
                this.value = this.oldValue;
                this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
            } else {
                this.value = "";
            }
        });
    });
}