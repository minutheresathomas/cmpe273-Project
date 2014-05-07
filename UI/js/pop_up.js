var popup_groupWindow=null;

function popup_group(mypage,myname,w,h,pos,infocus)
{
if (pos == 'random')
{
	LeftPosition=(screen.width)?Math.floor(Math.random()*(screen.width-w)):100;TopPosition=(screen.height)?Math.floor(Math.random()*((screen.height-h)-75)):100;
}

else
{
	LeftPosition=(screen.width)?(screen.width-w)/2:100;TopPosition=(screen.height)?(screen.height-h)/2:100;}
	settings='width='+ w + ',height='+ h + ',top=' + TopPosition + ',left=' + LeftPosition + ',scrollbars=no,location=no,directories=no,status=no,menubar=no,toolbar=no,resizable=no';popup_groupWindow=window.open('',myname,settings);
	
	if(infocus=='front')
	{
		popup_groupWindow.focus();popup_groupWindow.location=mypage;
	}

	if(infocus=='back')
	{
		popup_groupWindow.blur();popup_groupWindow.location=mypage;popup_groupWindow.blur();
	}

}