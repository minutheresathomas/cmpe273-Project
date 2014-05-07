$(document).ready(function() {
	$(":button").click(function() {
		var id = this.id;
		var selectedVal = "";
		var selected = $("input[type='radio'][name='option']:checked");
		if (selected.length > 0) {
		    selectedVal = selected.val();
		    localStorage.clear();
		    localStorage.setItem('selectedOpt', selectedVal);
		}
		var user_id = sessionStorage.getItem("username");
		var destinationUrl = "http://ec2-54-193-13-149.us-west-1.compute.amazonaws.com:8080/sms-voting/v1/polls/"+user_id+"/participate/"+id+"?option="+selectedVal;
		var button = ':button#'+id;
		jQuery.ajax({
		    type: "PUT",
		    url: destinationUrl,
//		    contentType: "application/json",
		    dataType: 'html',
		    success : function ()
		    {
		    	alert('success');
		    	location.reload();
		    	
		    },
			failure : function ()
			{
				alert('failure...');
			}
		});
		$(button).attr("disabled", true);
	});
	
	 // Retrieve the radio button value from the local storage
	 var savedOption = localStorage.getItem('selectedOpt');
	 $('input[name=option][value=' + savedOption + ']').attr('checked', 'checked');
}); 

