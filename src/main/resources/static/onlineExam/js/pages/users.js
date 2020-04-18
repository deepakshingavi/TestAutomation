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
	//document.getElementById("username_error").style.display = ret ? "none" : "inline";
	$("#username_error").html("* Special symbols and spaces not allowed").show();
	return ret;
}

function showUpdateModal(url)
{
	// SHOW AJAX RESPONSE ON REQUEST SUCCESS
	$.ajax({
		url: url,
		type: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(response)
		{
			$('#update_modal .modal-body input[name=firstname]').val(response.fName);
			$('#update_modal .modal-body input[name=lastname]').val(response.lName);
			$('#update_modal .modal-body input[name=contact]').val(response.contact);
			//$('#update_modal .modal-body select[name=user_type] option[value='+response.userType+']').attr('selected', 'selected');
			$('#update_modal .modal-body input[name=email]').val(response.email);
			$('#update_modal .modal-body input[name=loginid]').val(response.userName);
			$('#update_modal .modal-body textarea[name=address]').val(response.address);
			$('#update_modal .modal-body input[name=button]').attr("onclick","addUser("+response.userId+")");
			
			// LOADING THE AJAX MODAL
			$('#update_modal').modal('show', {backdrop: 'true'});
		}
	});
}
function showAjaxModal(url)
{	
	$('#modal_ajax input').val('');
	$('#modal_ajax textarea').val('');
	// LOADING THE AJAX MODAL
	$('#modal_ajax').modal('show', {backdrop: 'true'});
	
	// SHOW AJAX RESPONSE ON REQUEST SUCCESS
	$.ajax({
		url: url,
		type: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(response)
		{
			$('#modal_ajax .modal-body').html(response);
		}
	});
}
function showTestImage(url)
{
	// SHOWING AJAX PRELOADER IMAGE
	$('#image_ajax .modal-body').html('<div style="text-align:center;margin-top:200px;"><img src="Libraries/img/loader.GIF" style="height:50px;" /></div>');
	
	// LOADING THE AJAX MODAL
	$('#image_ajax').modal('show', {backdrop: 'true'});
	
	// SHOW AJAX RESPONSE ON REQUEST SUCCESS
	$.ajax({
		url: url,
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		type: "get",
		success: function(response)
		{
			$('#image_ajax .modal-body').html(response);
		}
	});
}

//Normal AJAX
function confirm_modal(delete_url , post_refresh_url)
{
	$('#preloader-delete').html('');
	$('#modal_delete').modal('show', {backdrop: 'static'});
	document.getElementById('delete_link').setAttribute("onClick" , "delete_data('" + delete_url + "' , '" + post_refresh_url + "')" );
	document.getElementById('delete_link').focus();
}

function checkDelete(url)
{
	var chk=confirm("Are You Sure To Delete This !");
	if(chk)
	{
		$.ajax({
			url: url,
			type: 'DELETE',
			beforeSend: function (xhr) {
				xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
			},
			success: function(response)
			{
				window.location.href= window.location.href;
			}
		});
	  //return true;  
	}
	else{
		return false;
	}
}

function addUser(userId=0){	
	$(".errmsg").hide();
	if(userId==0){
		$firstname = $('input[name=fName]:visible').val();
		$lastname = $('input[name=lName]:visible').val();
		$user_type = $("select[name=userType] option:selected").val();
		$useremail = $('input[name=email]:visible').val();
		$Address = $('textarea[name=address]:visible').val();
		$usercontact = $('input[name=contact]:visible').val();
		$password = $('input[name=password]:visible').val();
		$loginid = $('input[name=loginid]:visible').val();
		$confirm = $('input[name=confirm]:visible').val();
	}
	else {
		$firstname = $('#update_modal .modal-body input[name=firstname]').val();
		$lastname = $('#update_modal .modal-body input[name=lastname]').val();
		$usercontact = $('#update_modal .modal-body input[name=contact]').val();
		$user_type = $("#update_modal .modal-body select[name=user_type] option:selected").val();
		$useremail = $('#update_modal .modal-body input[name=email]').val();
		$Address = $('#update_modal .modal-body textarea[name=address]').val();
		$loginid = $('#update_modal .modal-body input[name=loginid]:visible').val();
		$password="";
	}
	if($firstname == '' || $lastname == '' || $useremail == '' || $loginid == '' || $user_type == '')
	{
		showError();
		return false;
	}
	if($('input[name=password]:visible').val() != $('input[name=confirm]:visible').val()){
		showError(1);
		return false;
	}
	if(/^[a-z0-9][a-z0-9-_\.]+@([a-z]|[a-z0-9]?[a-z0-9-]+[a-z0-9])\.[a-z0-9]{2,10}(?:\.[a-z]{2,10})?$/.test($('input[name=email]:visible').val())) {
	   console.log('passed');
	}
	else {
		$('input[name=email]:visible').parent().find("span.errmsg").html("Enter valid email address").show();
		return false;
	}
	var dataObj = {};
	dataObj["email"]= $useremail;
	dataObj["address"]= $Address;
	dataObj["contact"]= $usercontact;

	dataObj["fName"] = $firstname;
	dataObj["lName"] = $lastname;
	dataObj["userName"] = $loginid;
	dataObj["password"] = $password;
	dataObj["userType"] = $user_type;
	if(userId!=0){
		dataObj["userId"] = userId;
	}
	
	//**************************HARD CODED VALUES***************************//
	dataObj["description"] = "";
	dataObj["designation"] = "";
	dataObj["status"] = 1;
	dataObj["passStatus"] = 1;
	//dataObj["addedBy"] = 1;
	
	dataObj["profileImg"] = "";
	//dataObj["companyId"] = 1;
	//dataObj["refUserId"] = 1;
	dataObj["addedByName"] = "";
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
		url: base_url+"/user/save",
			success: function(msg){
				$('#modal_ajax').modal('hide');
				if(!alert(successMsg)) {
					window.location.href= window.location.href;
				}
			}
	});
}

$(document).ready(function() {	
	$.ajax({
		url: base_url+"/user/allByCompany",
		type: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data)
		{ 
			var payload = "";
			var num = 1;
			$.each(data, function(index, value) {				
				if(readCookie("TAuid") != parseInt(value.userId)) {
					var dateArray = value.createdAt.split(".");
					var role = "";
					switch(value.userType){
						case 1: role = "Administrator"; break;
						case 2: role = "Company"; break;
						case 3: role = "Tester"; break;
					}
					payload += '<tr>';
					payload += '<td>'+num+'</td>';
					payload += '<td>'+value.fName+' '+value.lName+'</td>';
					payload += '<td>'+value.email+'</td>';
					//payload += '<td>'+role+'</td>';
					payload += '<td>'+dateArray[0].replace("T", " ")+'</td>';
					payload += '<td>';
					payload += '<a style="cursor:pointer" onclick="showUpdateModal(\'../user/'+value.userId+'\');" class="table-link">';
					payload += '<span class="fa-stack">';
					payload += '<i class="fa fa-square fa-stack-2x"></i>';
					payload += '<i class="fa fa-pencil fa-stack-1x fa-inverse"></i>';
					payload += '</span>';
					payload += '</a>';
					payload += '<a style="cursor:pointer" onclick="checkDelete(\'../user/'+value.userId+'\');" class="table-link danger">';
					payload += '<span class="fa-stack">';
					payload += '<i class="fa fa-square fa-stack-2x"></i>';
					payload += '<i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>';
					payload += '</span>';
					payload += '</a>';
					payload += '</td>';
					payload += '</tr>';
					num++;
				}
			});
			$("#table-example tbody").html(payload);
			$('#table-example').DataTable();
		}
	});
	
	
	
	$(window).keydown(function(event){
		if(event.keyCode == 13) {
			event.preventDefault();
			return false;
		}
	});
	
	
	//serialize object function
	$.fn.serializeObject = function() {
		var o = {};
		var a = this.serializeArray();
		$.each(a, function() {
			if (o[this.name]) {
				if (!o[this.name].push) {
					o[this.name] = [o[this.name]];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		return o;
	};
	
	$.delete = function(url, data, callback, type){
	 
	  if ( $.isFunction(data) ){
		type = type || callback,
			callback = data,
			data = {}
	  }
	 
	  return $.ajax({
		url: url,
		type: 'DELETE',
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: callback,
		data: data,
		contentType: type
	  });
	}
	
});