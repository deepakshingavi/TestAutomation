var specialKeys = new Array();
specialKeys.push(8); //Backspace
specialKeys.push(9); //Tab
specialKeys.push(46); //Delete
specialKeys.push(36); //Home
specialKeys.push(35); //End
specialKeys.push(37); //Left
specialKeys.push(39); //Right
function IsAlphaNumeric(e) {
	var keyCode = e.keyCode == 0 ? e.charCode : e.keyCode;
	var ret = ((keyCode >= 48 && keyCode <= 57) || (keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122) || (specialKeys.indexOf(e.keyCode) != -1 && e.charCode != e.keyCode));
	document.getElementById("username_error").style.display = ret ? "none" : "inline";
	return ret;
}	
function addUser(){
	$(".errmsg").hide();
	$password = $('input[name=password]').val();
	$old_password = $('input[name=old_password]').val();
	$confirm = $('input[name=confirm]').val();
	if($old_password == '' || $password == '')
	{
	   showError(1);
	   return false;
	} 
	else if($confirm != $password)
	{
	   showError(1);
	   return false;
	} 
	else {
		$.ajax({
			type: 'POST',
			contentType: 'application/json',
			dataType: 'json',
			beforeSend: function (xhr) {
				xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
			},
			url: base_url+"/user/changePassword?newPassword="+$password+"&oldPassword="+$old_password,
			success: function(msg){
				//window.location.href= window.location.href;
				$('#res').html("<span style='color:red;text-transform:capitalize;font-size:14px'>Password updated successfully..!</span>");
				$('#res span').fadeIn().fadeOut(3000);
			}
		});
	}
}

$(document).ready(function() {
	$(window).keydown(function(event){
		if(event.keyCode == 13) {
			event.preventDefault();
			return false;
		}
	});
});