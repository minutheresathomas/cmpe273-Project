$(":button").click(function () {
var question = $("#question").val();
alert(question);

var sessionname = sessionStorage.getItem("username");
alert("user id from search is :"+sessionname);

var choicetxt="[";
var valueText=document.getElementsByName('DynamicTextBox');
alert(valueText);
for(var i=0;i<valueText.length;i++){
//alert("length:"+valueText.length);
//alert(valueText[i].value);
var vartext=valueText[i].value;

choicetxt=choicetxt+"{\"option\":\""+vartext+"\"}";
if(i<valueText.length-1){
choicetxt=choicetxt+","; 
}
else
choicetxt=choicetxt+"]";

//alert("option :"+choicetxt);

}
var obj=JSON.parse(choicetxt);
//alert("obj"+obj);



var createdate=$("#datepicker1").val();
alert(createdate);
var expirydate=$("#datepicker2").val();
jQuery.ajax({
type: "POST",
url: "http://localhost:8080/sms-voting/v1/polls/"+sessionname+"/createPoll",
//url: "http://ec2-54-193-13-149.us-west-1.compute.amazonaws.com:8080/sms-voting/v1/polls/"+val+"/createPoll",
contentType: "application/json",
dataType: "json",
data: JSON.stringify({
"question": $("#question").val(),
"Choices": obj,
"StartDate":$("#datepicker1").val(),
"EndDate":	$("#datepicker2").val()
}),
success: function (data) {
alert("success");
},
error: function () {
alert("failed");
}
});
});