$("#search").click(function() {
	
	var val = sessionStorage.getItem("username");
//	document.location.href=val+"/searchPolls";
	var que = $("#queSubString").val();
	
	var url = "http://localhost:8080/sms-voting/v1/polls/"+val+"/searchPolls?question="+que;
//	var url = "http://ec2-54-193-13-149.us-west-1.compute.amazonaws.com:8080/sms-voting/v1/polls/"+val+"/searchPolls?question="+que;
	jQuery.ajax({
		type: "GET",
	    url: url,
	    contentType: "application/json",
	    success : function ()
	    {
	    	alert('success');
	    	document.location.href = "http://localhost:8080/sms-voting/web/polls/"+val+"/searchPolls?question="+que;
//	    	document.location.href = "http://ec2-54-193-13-149.us-west-1.compute.amazonaws.com:8080/sms-voting/web/polls/"+val+"/searchPolls?question="+que;
	    },
		failure : function ()
		{
			alert('failure...');
		}
	});
});