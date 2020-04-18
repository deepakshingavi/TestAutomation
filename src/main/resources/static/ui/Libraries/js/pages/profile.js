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
	$("#username_error").html("* Special symbols and spaces not allowed").show();
	return ret;
}	
function addUser(userId=0){
	$(".errmsg").hide();
	$firstname = $('input[name=fName]').val();
	$lastname = $('input[name=lName]').val();
	$usercontact = $('input[name=contact]').val();
	$user_type = $("select[name=user_type] option:selected").val();
	$useremail = $('input[name=email]').val();
	$Address = $('textarea[name=address]').val();
	$loginid = $('input[name=loginid]:visible').val();
	$password="";
	if($useremail == '' || $firstname == '' || $lastname == '' || $useremail == '')
	{
	   showError();
	   return false;
	}
	var dataObj = {};
	dataObj["fName"]= $firstname;
	dataObj["lName"]= $lastname;
	dataObj["email"]= $useremail;
	dataObj["address"]= $Address;
	dataObj["contact"]= $usercontact;
	dataObj["userName"] = $loginid;
	
	//**************************HARD CODED VALUES***************************//
	/*dataObj["description"] = "";
	dataObj["designation"] = "";
	dataObj["status"] = 1;
	dataObj["passStatus"] = 1;
	//dataObj["addedBy"] = 1;
	
	dataObj["profileImg"] = "";
	//dataObj["companyId"] = 1;
	//dataObj["refUserId"] = 1;
	dataObj["addedByName"] = "";*/
	//**************************HARD CODED VALUES***************************//
	//alert(JSON.stringify(dataObj));
	$.ajax({
		type: 'POST',
		data: JSON.stringify(dataObj),
		contentType: 'application/json',
		dataType: 'json',
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		url: base_url+"/user/updateProfile",
			success: function(msg){
				//window.location.href= window.location.href;
				$('#res').html("<span style='color:red;text-transform:capitalize;font-size:14px'>Profile updated successfully..!</span>").show();
				$('#res span').fadeIn().fadeOut(3000);
			}
	});
}

$(document).ready(function() {
	$(window).keydown(function(event){
		if(event.keyCode == 13) {
			event.preventDefault();
			return false;
		}
	});
	$.ajax({
		url: base_url+"/user/"+readCookie("TAuid"), 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(response) {
			$('input[name=fName]').val(response.fName);
			$('input[name=lName]').val(response.lName);
			$('input[name=contact]').val(response.contact);
			$('input[name=email]').val(response.email);
			$('input[name=loginid]').val(response.userName);
			$('textarea[name=address]').val(response.address);			
		}
	});
});