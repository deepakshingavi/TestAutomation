$(document).ready(function() {
	$.ajax({
		url: base_url+"/company/all", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success:function(data) {
			var companydetails = "";
			var arr = [];
			$.each(data, function(index, value) {
				companydetails += '<option value="'+value.companyId+'" disabled="disabled">'+value.companyName+'</option>';
				arr[value.companyId] = value.companyName;
			});
			$("select[name=company_id]").append(companydetails);
			$.ajax({
				url: base_url+"/browser/all", 
				method: "get",
				beforeSend: function (xhr) {
					xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
				},
				success: function(dataBrowser) {
					var payload = "";
					$.each(dataBrowser, function(index, value) {
						var dateArray = value.createdAt.split(".");
						var role = "";
						switch (value.userType) {
							case 1:
								role = "Administrator";
								break;
							case 2:
								role = "Company";
								break;
							case 3:
								role = "Tester";
								break;
						}
						payload += '<tr>';
						payload += '<td>' + (index + 1) + '</td>';
						payload += '<td>' + arr[value.companyId] + '</td>';
						payload += '<td>' + value.browserName + '</td>';
						payload += '<td>' + dateArray[0].replace("T", " ") + '</td>';
						payload += '<td>';
						payload += '<a style="cursor:pointer" onclick="showUpdateModal(\''+base_url+'/browser/' + value.browserId + '\');" class="table-link">';
						payload += '<span class="fa-stack">';
						payload += '<i class="fa fa-square fa-stack-2x"></i>';
						payload += '<i class="fa fa-pencil fa-stack-1x fa-inverse"></i>';
						payload += '</span>';
						payload += '</a>';
						payload += '<a style="cursor:pointer" onclick="checkDelete(\''+base_url+'/browser/' + value.browserId + '\');" class="table-link danger">';
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
		}
	})
	
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
    var tableFixed = $('#table-example-fixed').dataTable({
        'info': false,
        'pageLength': 50
    });

    new $.fn.dataTable.FixedHeader(tableFixed);
});
function addBrowser(browserId=0){
	$browserName = $('input[name=browser]:visible').val();
	$companyId = $("select[name=company_id]:visible option:selected").val();
	if($browserName == '' || $companyId == "")
	{
	   showError();
	   return false;
	}
	//               $(this).attr('disabled','disabled');

	//var dataObj = {"email": $useremail,"address": $Address,"contact": $usercontact,"fName": $firstname,"lName": $lastname,"loginId": $loginid,"password": $password,"userType": $user_type};

	var dataObj = {};
	dataObj["browserName"]= $browserName;
	//dataObj["companyId"] = $companyId;
	
	//**************************HARD CODED VALUES***************************//
	//dataObj["userId"]= 162;
	//dataObj["addedBy"]= 0;
	//dataObj["addedByName"] = "administrator";
	//dataObj["status"] = 0;
	
	if(browserId!==0){
		dataObj["browserId"] = browserId;
	}
	//**************************HARD CODED VALUES***************************//
	
	$.ajax({
		type: 'POST',
		data: JSON.stringify(dataObj),
		contentType: 'application/json',
		dataType: 'json',
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		url: base_url+"/browser/save/"+$companyId,
		success: function(msg){
			$('.modal').modal('hide');
			if(!alert(successMsg)) {
				window.location.href= window.location.href;
			}
		}
	});

	return false;
}
function showUpdateModal(url)
{
	if($("select[name=company_id] option:disabled").length != 0) {
		$("select[name=company_id] option").attr("disabled", "disabled");
	}
	// SHOW AJAX RESPONSE ON REQUEST SUCCESS
	$.ajax({
		url: url,
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(response)
		{
			$('#update_modal .modal-body input[name=browser]').val(response.browserName);
			$('#update_modal .modal-body select[name=company_id] option[value='+response.companyId+']').prop("selected", true);
			$('#update_modal .modal-body input[name=update_button]').attr("onclick","addBrowser("+response.browserId+")");
			// LOADING THE AJAX MODAL
			$('#update_modal').modal('show', {backdrop: 'true'});
		}
	});
}
function showAddModal(url) {
	jQuery('#modal_ajax input').val('');
	$("select[name=company_id]").val("");
	$("select[name=company_id] option").removeAttr("disabled", "disabled");
    // LOADING THE AJAX MODAL
    jQuery('#modal_ajax').modal('show', {
        backdrop: 'true'
    });

}
function showAjaxModal(url) {
    // SHOWING AJAX PRELOADER IMAGE
    jQuery('#modal_ajax .modal-body').html('<div style="text-align:center;margin-top:200px;"><img src="Libraries/img/loader.GIF" style="height:50px;" /></div>');

    // LOADING THE AJAX MODAL
    jQuery('#modal_ajax').modal('show', {
        backdrop: 'true'
    });

    // SHOW AJAX RESPONSE ON REQUEST SUCCESS
    $.ajax({
        url: url,
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
        success: function(response) {
            jQuery('#modal_ajax .modal-body').html(response);
        }
    });
}

function showTestImage(url) {
    // SHOWING AJAX PRELOADER IMAGE
    jQuery('#image_ajax .modal-body').html('<div style="text-align:center;margin-top:200px;"><img src="Libraries/img/loader.GIF" style="height:50px;" /></div>');

    // LOADING THE AJAX MODAL
    jQuery('#image_ajax').modal('show', {
        backdrop: 'true'
    });

    // SHOW AJAX RESPONSE ON REQUEST SUCCESS
    $.ajax({
        url: url,
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
        success: function(response) {
            jQuery('#image_ajax .modal-body').html(response);
        }
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

function checkDelete(url) {
    var chk = confirm("Are You Sure To Delete This !");
    if (chk) {
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
        return true;
    } else {
        return false;
    }
}