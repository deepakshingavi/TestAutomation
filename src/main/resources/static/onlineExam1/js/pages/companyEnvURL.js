var comEnvURL, aldta = {}, envData = {};
$(document).ready(function() {
    $.ajax({
		url: base_url+"/application/allByCompany", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data) {
			var options = "";
			$.each(data, function(key, value) {
				options += '<option value="'+value.applicationId+'">'+value.applicationName+'</option>';
			});
			$("select[name=application_id]").append(options);
		}
	});
	$.ajax({
		url: base_url+"/companyEnvironUrl/findAllByCompanyId", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success:function(data) {
			var payload = "";
			var appOptions = "";
			comEnvURL = data.map;
			$.each(data.map, function(key, value) {			
				payload += '<tr>';
				payload += '<td><h6>' + value.applicationName + '</h6></td>';	
				payload += '<td>';
				payload += '<a style="cursor:pointer" onclick="showViewModal('+key+');" class="table-link">';
				payload += '<button  class="btn btn-primary"><i class="fa fa-eye"></i> View URL</button>';            
				payload += '</a>';
				payload += '</td>';
				payload += '<td>';
				payload += '<a style="cursor:pointer" onclick="showUpdateModal('+key+');" class="table-link danger">';
				payload += '<button  class="btn btn-warning"><i class="fa fa-pencil"></i> Edit URL</button>';
				payload += '</a>';
				payload += '</td>';
				payload += '<td>';
				payload += '<a style="cursor:pointer" class="table-link danger">';
				payload += '<button  class="btn btn-danger" onclick="return checkDelete('+key+');"><i class="fa fa-trash-o"></i> Delete</button>';
				payload += '</a>';
				payload += '</td>';
				payload += '</tr>';
				//appOptions += '<option value="'+key+'">' + value.applicationName + '</option>';
			});
			//$("select[name=application_id]").append(appOptions);
			$("#companyAllTable tbody").html(payload);
			$('#companyAllTable').DataTable();
		}
	});
	
	$.ajax({
		url: base_url+"/environment/all", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data) {
			envData = data;
			var payload = "";
			$.each(data, function(key, value) {
				payload += '<div class="form-group">';
				payload += '<label for="exampleInputEmail1">'+value.environmentName+'</label>';
				payload += '<input class="form-control" data-envurl-id="" data-env-id="'+value.environmentId+'" name="'+value.environmentName+'" placeholder="'+value.environmentName+'" type="text">';
				payload += '</div>';
			});
			$('#modal_ajax .panel-body .modal-body .col-md-12').append(payload);
			$("select[name=application_id]").on("change", function(){
				$('#modal_ajax input').val('');
				$('#modal_ajax textarea').val('');
				if(this.value!=""){
					$.ajax({
						url: base_url+"/companyEnvironUrl/getAllByCompanyId/"+$("select[name=application_id]").val(), 
						method: "get",
						beforeSend: function (xhr) {
							xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
						},
						success: function(data) {
							aldta = data;
							var payload = "";
							$.each(data.companyEnvironUrls, function(key, value) {
								$('#modal_ajax .panel-body .modal-body .col-md-12 input[name='+value.environment.environmentName+']').val(value.envUrl);
								$('#modal_ajax .panel-body .modal-body .col-md-12 input[name='+value.environment.environmentName+']').attr("data-envurl-id", value.companyEnvironUrlId);
							});
						}
					});
				}
			});
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
    var tableFixed = $('#table-example-fixed').dataTable({
        'info': false,
        'pageLength': 50
    });

    new $.fn.dataTable.FixedHeader(tableFixed);
});
function addCompanyEnvironUrlId(companyEnvironUrlId=0){
	if($("select[name=application_id]:visible").val() == ""){
		showError();
		return false;
	}
	/*var data = {};
	if(aldta.companyEnvironUrls.length > 0) {
		$.each(aldta.companyEnvironUrls, function(k,v){
			v.envUrl = $('input[name='+v.environment.environmentName+']:visible').val();
			v.environment.environmentId = $('input[name='+v.environment.environmentName+']:visible').attr("data-env-id");
		});
	}
	else {*/
		//data = envData;
		//aldta.companyEnvironUrls = [];
		$.each(envData, function(k,v){ 
			if(aldta.companyEnvironUrls[k] == undefined) {
				aldta.companyEnvironUrls[k] = {};
			}																							
			aldta.companyEnvironUrls[k].envUrl = $('input[name='+v.environmentName+']:visible').val();
			aldta.companyEnvironUrls[k].environment = {};
			aldta.companyEnvironUrls[k].environment.environmentId = $('input[name='+v.environmentName+']:visible').attr("data-env-id");
		});
	//}
	$.ajax({
		type: 'POST',
		data: JSON.stringify(aldta),
		contentType: 'application/json',
		dataType: 'json',
		url: base_url+"/companyEnvironUrl/saveAll",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(msg){
			$('.modal').modal('hide');
			if(!alert(successMsg)) {
				window.location.href= window.location.href;
			}
		}
	});

	return false;
}
/*function addCompanyEnvironUrlId(companyEnvironUrlId=0){
	$applicationId = $('select[name=application_id]:visible').val();
	$environmentName = $('input[name=environment]:visible').val();
	if($environmentName == '')
	{
	   $('#res').html("<span style='color:red;text-transform:capitalize;font-size:14px'>Enter Environment Name..!</span>");
	   return false;
	}
	var dataObj = {};
	dataObj["environmentName"]= $environmentName;
	dataObj["applicationId"]= $applicationId;
	if(companyEnvironUrlId!==0){
		dataObj["companyEnvironUrlId"] = companyEnvironUrlId;
	}
	$.ajax({
		type: 'POST',
		data: JSON.stringify(dataObj),
		contentType: 'application/json',
		dataType: 'json',
		url: base_url+"/companyEnvironUrl/save",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(msg){
			$('.modal').modal('hide');
			window.location.href= window.location.href;
		}
	});

	return false;
}
*/
function showViewModal(id)
{
	$.ajax({
		url: base_url+"/companyEnvironUrl/getAllByCompanyId/"+id, 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data) {
			var payload = "";
			$.each(data.companyEnvironUrls, function(index, value) {
				payload += 	'<tr>';
				payload += 	'<td>'+(index+1)+'</td>';
				payload += 	'<td>'+value.environment.environmentName+'</td>';
				payload += 	'<td>'+value.envUrl+'</td>';
				payload += 	'</tr>';
			});
			$(".panel-title h5 b").html("Application Name : "+comEnvURL[id].applicationName);
			$('#view_modal table#viewTabale tbody').html(payload);
			$('#view_modal').modal('show', {backdrop: 'true'});
		}
	});
}
function showUpdateModal(url)
{	
	$('#modal_ajax').modal('show', {backdrop: 'true'});
	$("select[name=application_id]").val(url).change();
}
function showAddModal(url) {
	jQuery('#modal_ajax input').val('');
    // LOADING THE AJAX MODAL
    jQuery('#modal_ajax').modal('show', {
        backdrop: 'true'
    });

}
function showAjaxModal() {
    // LOADING THE AJAX MODAL
    jQuery('#modal_ajax').modal('show', {
        backdrop: 'true'
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

function checkDelete(applicationId) {
    var chk = confirm("Are You Sure To Delete This !");
    if (chk) {
		$.ajax({
			url: base_url+"/companyEnvironUrl/byApplication/"+applicationId,
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