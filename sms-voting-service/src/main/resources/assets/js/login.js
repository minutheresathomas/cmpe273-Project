$(":button").click(function() {
	// to check the user id in mongodb
//	var name = "username=";
	alert('inside login');
	
	var user_id = document.getElementById("usernamep").value;
	if(user_id != null)
		{
		alert('user is i: '+user_id);
		sessionStorage.setItem("username", user_id);
		}
		
	
	
	$.ajax({
		type : 'GET',
		url : 'http://localhost:8080/sms-voting/v1/polls/'+user_id,
//		url : 'http://ec2-54-193-13-149.us-west-1.compute.amazonaws.com:8080/sms-voting/v1/polls/'+user_id,
		dataType: 'html',
		success:function(data) {
			//alert(data);
			alert("success");
			document.location.href=user_id;
		},
		failure:function () {
			alert('failed');
		}
	});
});