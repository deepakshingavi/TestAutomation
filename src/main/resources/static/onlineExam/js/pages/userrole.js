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
	//document.getElementById("").style.display = ret ? "none" : "inline";
	$("#username_error").html("* Special symbols and spaces not allowed").show();
	return ret;
}
function showUpdateModal(url)
{
	// SHOW AJAX RESPONSE ON REQUEST SUCCESS
	$.ajax({
		url: base_url+url,
		type: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(response)
		{
			$('#update_modal .modal-body input[name=password]').val(response.password);
			$('#update_modal .modal-body input[name=confirm]').val(response.password);
			$('#update_modal .modal-body input[name=user_type]').val(response.role);
			$('#update_modal .modal-body input[name=loginid]').val(response.name);
			$('#update_modal .modal-body input[name=button]').attr("onclick","addUserRole("+response.executionUserId+")");
			// LOADING THE AJAX MODAL
			$('#update_modal').modal('show', {backdrop: 'true'});
		}
	});
}
function showAjaxModal(url)
{	
	$('#modal_ajax input').val('');
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
			url: base_url+url,
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

function addUserRole(executionUserId=0){
	if($('input[name=password]:visible').val() == "" || $('input[name=confirm]:visible').val() == "" || $('input[name=loginid]:visible').val() == "" || $("input[name=user_type]:visible").val() == ""){
		showError();
		return false;
	}
	else if($('input[name=password]:visible').val() != $('input[name=confirm]:visible').val()){
		$(".errmsg").hide();
		showError(1);
		return false;
	}
	
	$(".errmsg").hide();
	if(executionUserId==0){
		$user_type = $("input[name=user_type]:visible").val();
		$password = $('input[name=password]:visible').val();
		$loginid = $('input[name=loginid]:visible').val();
		$confirm = $('input[name=confirm]:visible').val();
	}
	else {
		$user_type = $("#update_modal .modal-body input[name=user_type]:visible").val();
		$password = $('#update_modal .modal-body input[name=password]:visible').val();
		$confirm = $('#update_modal .modal-body input[name=confirm]:visible').val();
		$loginid = $('#update_modal .modal-body input[name=loginid]:visible').val();
	}
	var dataObj = {};
	dataObj["name"] = $loginid;
	dataObj["password"] = $password;
	dataObj["role"] = $user_type;
	if(executionUserId!=0){
		dataObj["executionUserId"] = executionUserId;
	}
	$.ajax({
		type: 'POST',
		data: JSON.stringify(dataObj),
		contentType: 'application/json',
		dataType: 'json',
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		url: base_url+"/executionUser/save",
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
		url: base_url+"/executionUser/allByCompany",
		type: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data)
		{ 
			var payload = "";
			$.each(data, function(index, value) {
				payload += '<tr>';
				payload += '<td>'+(index+1)+'</td>';
				payload += '<td>'+value.name+'</td>';
				payload += '<td>'+value.role+'</td>';
				payload += '<td>'+value.addedBy.userName+'</td>';
				payload += '<td>';
				payload += '<a style="cursor:pointer" onclick="showUpdateModal(\'/executionUser/'+value.executionUserId+'\');" class="table-link">';
				payload += '<span class="fa-stack">';
				payload += '<i class="fa fa-square fa-stack-2x"></i>';
				payload += '<i class="fa fa-pencil fa-stack-1x fa-inverse"></i>';
				payload += '</span>';
				payload += '</a>';
				payload += '<a style="cursor:pointer" onclick="checkDelete(\'/executionUser/'+value.executionUserId+'\');" class="table-link danger">';
				payload += '<span class="fa-stack">';
				payload += '<i class="fa fa-square fa-stack-2x"></i>';
				payload += '<i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>';
				payload += '</span>';
				payload += '</a>';
				payload += '</td>';
				payload += '</tr>';
			});
			$("#table-example tbody").html(payload);
			$('#table-example').DataTable();
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