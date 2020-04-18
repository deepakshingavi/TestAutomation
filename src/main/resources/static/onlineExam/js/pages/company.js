var cData;
$(document).ready(function() {	
    $.ajax({
		url: base_url+"/company/all", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success:function(data) {
			cData = data;
			var payload = "";
			$.each(data, function(index, value) {
				var dateArray = value.createdAt.split(".");
				payload += '<tr role="row" class="odd">';
				payload += '<td class="sorting_1">'+(index+1)+'</td>';	
				payload += '<td>'+value.companyName+'</td>';	
				payload += '<td>'+value.email+'</td>';	
				payload += '<td>'+dateArray[0].replace("T", " ")+'</td>';	
				payload += '<td>';
				payload += '<a style="cursor:pointer" onclick="showAjaxModal('+value.companyId+');" class="table-link">';
				payload += '<span class="fa-stack">';
				payload += '<i class="fa fa-square fa-stack-2x"></i>';
				payload += '<i class="fa fa-pencil fa-stack-1x fa-inverse"></i>';
				payload += '</span>';
				payload += '</a>';
				payload += '<a href="" class="table-link danger">';
				payload += '<span class="fa-stack" onclick="return checkDelete('+value.companyId+');">';
				payload += '<i class="fa fa-square fa-stack-2x"></i>';
				payload += '<i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>';
				payload += '</span>';
				payload += '</a></td>';
				payload += '</tr>';
			});
			$("#companyAllTable tbody").html(payload);
			$('#companyAllTable').DataTable();
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
		success: callback,
		data: data,
		contentType: type
	  });
	}
    var tableFixed = $('#table-example-fixed').dataTable({
        'info': false,
        'pageLength': 50
    });

    new $.fn.dataTable.FixedHeader(tableFixed);
});

/*function addCompany(){
	$environmentName = $('select[name=application_id]:visible').val();
	$environmentName = $('input[name=environment]:visible').val();
	$environmentName = $('input[name=environment]:visible').val();
	if($environmentName == '')
	{
	   $('#res').html("<span style='color:red;text-transform:capitalize;font-size:14px'>Enter Environment Name..!</span>");
	   return false;
	}
	//               $(this).attr('disabled','disabled');

	//var dataObj = {"email": $useremail,"address": $Address,"contact": $usercontact,"fName": $firstname,"lName": $lastname,"loginId": $loginid,"password": $password,"userType": $user_type};

	var dataObj = {};
	dataObj["environmentName"]= $environmentName;
	
	$.ajax({
		type: 'POST',
		data: JSON.stringify(dataObj),
		contentType: 'application/json',
		dataType: 'json',
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		url: base_url+"/company/save",
		success: function(msg){
			$('.modal').modal('hide');
			window.location.href= window.location.href;
		}
	});

	return false;
}*/
function showUpdateModal(url)
{
	
	$('#modal_ajax').modal('show', {backdrop: 'true'});
	
	// SHOW AJAX RESPONSE ON REQUEST SUCCESS
	/*$.ajax({
		url: url,
		success: function(response)
		{
			$('#modal_ajax .modal-body input[name=environment]').val(response.environmentName);
			$('#modal_ajax .modal-body input[name=update_button]').attr("onclick","addEnv("+response.environmentId+")");
			// LOADING THE AJAX MODAL
			$('#modal_ajax').modal('show', {backdrop: 'true'});
		}
	});*/
}
function showAddModal(url) {
	$('input').val('');
    // LOADING THE AJAX MODAL
    $('#modal_ajax').modal('show', {
        backdrop: 'true'
    });

}
function showAddAjaxModal(){
	$('input').val('');
	$('textarea').val('');
    // LOADING THE AJAX MODAL    
	$('#add_modal_ajax').modal('show', {
        backdrop: 'true'
    });
	
	$("button[name=addCompany]").click(function(){
		if($("input[name=company_name]:visible").val() == "" || $("input[name=email]:visible").val() == "" || $("input[name=username]:visible").val() == "" || $("input[name=password]:visible").val() == "" || $("input[name=confirm]:visible").val() == "") {
			showError();
			return false;
		}
		else if($("input[name=password]:visible").val() !== $("input[name=confirm]:visible").val()){
			$(".errmsg").hide();
			showError(1);
			return false;
		}
		else if(/^[a-z0-9][a-z0-9-_\.]+@([a-z]|[a-z0-9]?[a-z0-9-]+[a-z0-9])\.[a-z0-9]{2,10}(?:\.[a-z]{2,10})?$/.test($('input[name=email]:visible').val())) {
		   console.log('passed');
		}
		else {
			$('input[name=email]:visible').parent().find("span.errmsg").html("Enter valid email address").show();
			return false;
		}
		
		var dataObj = {};
		dataObj["companyName"] = $("input[name=company_name]:visible").val();
		dataObj["email"] = $("input[name=email]:visible").val();
		dataObj["username"] = $("input[name=username]:visible").val();
		dataObj["password"] = $("input[name=password]:visible").val();
		dataObj["contact"] = $("input[name=contact]:visible").val();
		dataObj["address"] = $("textarea[name=address]:visible").val();
		
		$.ajax({
			url: base_url+"/company/save",
			type: "POST",
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify(dataObj),
			beforeSend: function (xhr) {
				xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
			},
			success: function(response)
			{
				// LOADING THE AJAX MODAL
				$('#add_modal_ajax').modal('hide');
				if(!alert(successMsg)) {
					window.location.href= window.location.href;
				}
			}
		});
	});
}
function showAjaxModal(id) {
	
	$.each(cData, function(index, value){
		if(value.companyId == id){
			$("input[name=company_name]").val(value.companyName);
			$("input[name=email]").val(value.email);
			$("input[name=company_id]").val(value.companyId);
			$("input[name=contact]").val(value.contact);
			$("input[name=username]").val(value.username);
			$("textarea[name=address]").val(value.address);
		}
	});
    // LOADING THE AJAX MODAL
    
	jQuery('#modal_ajax').modal('show', {
        backdrop: 'true'
    });
	
	$("button[name=updateCompany]").click(function(){
		var dataObj = {};
		dataObj["companyId"] = $("input[name=company_id]").val();
		dataObj["contact"] = $("input[name=contact]").val();
		dataObj["address"] = $("textarea[name=address]").val();
		if($("input[name=company_id]").val()=="") {
			showError();
			return false;
		}
		$.ajax({
			url: base_url+"/company/update",
			type: "POST",
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify(dataObj),
			beforeSend: function (xhr) {
				xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
			},
			success: function(response)
			{
				// LOADING THE AJAX MODAL
				$('#modal_ajax').modal('hide');
				if(!alert(successMsg)) {
					window.location.href= window.location.href;
				}
			}
		});
	});
}
function confirm_modal(delete_url, post_refresh_url) {
    $('#preloader-delete').html('');
    jQuery('#modal_delete').modal('show', {
        backdrop: 'static'
    });
    document.getElementById('delete_link').setAttribute("onClick", "delete_data('" + delete_url + "' , '" + post_refresh_url + "')");
    document.getElementById('delete_link').focus();
}

function checkDelete(id) {
    var chk = confirm("Are You Sure To Delete This !");
    if (chk) {
		$.ajax({
			url: base_url+"/company/"+id,
			type: 'DELETE',
			beforeSend: function (xhr) {
				xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
			},
			success: function(response)
			{
				window.location.href= window.location.href;
			}
		});
        return true;
    } else {
        return false;
    }
}