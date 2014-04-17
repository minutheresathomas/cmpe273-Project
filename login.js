{\rtf1\ansi\ansicpg1252\cocoartf1265\cocoasubrtf190
{\fonttbl\f0\froman\fcharset0 Times-Roman;}
{\colortbl;\red255\green255\blue255;\red67\green67\blue67;\red252\green252\blue252;}
\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\deftab720
\pard\pardeftab720

\f0\fs24 \cf0 $(document).ready(function()\{\
	$("#add_err").css('display', 'none', 'important');\
	 $("#login").click(function()\{	\
		  username=$("#name").val();\
		  password=$("#word").val();\
		  $.ajax(\{\
		   type: "POST",\
		   url: "login.php",\
			data: "name="+username+"&pwd="+password,\
		   success: function(html)\{    \
			if(html=='true')    \{\
			 //$("#add_err").html("right username or password");\
			 window.location="dashboard.php";\
			\}\
			else    \{\
			$("#add_err").css('display', 'inline', 'important');\
			 $("#add_err").html("<img src='images/alert.png' />Wrong username or password");\
			\}\
		   \},\
		   beforeSend:function()\
		   \{\
			$("#add_err").css('display', 'inline', 'important');\
			$("#add_err").html("<img src='images/ajax-loader.gif' /> Loading...")\
		   \}\
		  \});\
		return false;\
	\});\
\});\cf2 \cb3 \
}