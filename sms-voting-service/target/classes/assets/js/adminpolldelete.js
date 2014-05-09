$(":button").click(function() {
var pollid=$(this).closest("tr").find("td").eq(0).text();
alert(pollid);
var val = sessionStorage.getItem("username");
alert("user id from search is :"+val);
jQuery.ajax({
type: "DELETE",
//url: "http://ec2-54-193-13-149.us-west-1.compute.amazonaws.com:8080/sms-voting/v1/polls/"+val+"/"+pollid,
url: "localhost:8080/sms-voting/v1/polls/"+val+"/"+pollid,
contentType: "application/json",
success : function ()
{

alert('Success.');
location.reload();
},
failure : function ()
{
alert('failure...');
}
});
});
