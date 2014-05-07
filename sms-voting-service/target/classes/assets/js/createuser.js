$(":button").click(function () {
	
	var pass1 = document.getElementById("passwordsignup").value;
    var pass2 = document.getElementById("passwordsignup_confirm").value;
    if (pass1 != pass2) {
        alert("Passwords Do not match");
        //document.getElementById("pass1").style.borderColor = "#E34234";
        //document.getElementById("pass2").style.borderColor = "#E34234";
    }
    else {
        alert("Passwords Match!!!");
        document.location.href="login"
    }

	
	jQuery.ajax({
		type: "POST",
		url: "http://ec2-54-193-13-149.us-west-1.compute.amazonaws.com:8080/sms-voting/v1/polls/createUser",
		contentType: "application/json",
		dataType: "json",
		data: JSON.stringify({
			"user_id": $("#emailsignup").val(),
			"password": $("#passwordsignup").val(),
			"name": $("#usernamesignup").val(),
			"country": $("#countrysignup").val()
		}),
		success:function (data) {
			alert("success");
			document.location.href="login";
		},
		failure: function () {
			alert("failed");
		}
	});
});