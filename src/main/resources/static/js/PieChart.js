// For a pie chart
//first chart data str is var here second one is coming from encrypted thing from our model
var chartDataStr=decodeHtml(pieData);
var chartJsonArray=JSON.parse(chartDataStr);

var arrayLenght=chartJsonArray.length;
var numericData=[];
var labelData=[];

for(var i=0;i<arrayLenght;i++){
    numericData[i]=chartJsonArray[i].value;
    labelData[i]=chartJsonArray[i].label;
}
new Chart(document.getElementById("myPieChart"), {
    type: 'pie',
    // The data for our dataset
    data: {
        labels: labelData,
        datasets: [{
            label: 'My First dataset',
            backgroundColor: ["#cd3e3e","#dfff00","#06c100"],

            data: numericData
        }]
    },

    // Configuration options go here
    options: {
        title:{
            display: true,
            text: 'Your Projects Statuses'
        }
    }
});
// decryption for our chart data to make it understandable for js
function decodeHtml(html) {
    var txt= document.createElement("textarea");
    txt.innerHTML=html;
    return txt.value;
}
