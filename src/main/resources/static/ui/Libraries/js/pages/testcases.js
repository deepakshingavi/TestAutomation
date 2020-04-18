var tcs = [];
var appOp = {};
var mapping;
var comp = {};
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
	}
	else{
		return false;
	}
}
function showAjaxModal(id=0)
{
	if(id){
		$('#testcase_name').val(tcs[id].testcaseName);
		$('#class_name').val(tcs[id].className);
		$('#testcaseId').val(tcs[id].testcasesId);
		$('#description').val(tcs[id].description);
		//$('#appMap option[value='+tcs[id].companyId+']').attr("selected", "selected");
		//$('#application_id option[value='+tcs[id].applicationId+']').attr("selected", "selected");
		
		$('#modal_ajax').modal('show', {backdrop: 'true'});
		
		$('#appMap').val(tcs[id].companyId).attr("selected", true).change();
		tcs[id].isPerfSuite ? $('#isPerfSuite').attr("checked","checked") : $('#isPerfSuite').attr("checked",false)
		$("#addButton").text("Update TestCase");
	}
	else {
		$('#testcase_name').val("");
		$('#class_name').val("");
		$('#testcaseId').val("");
		$('#description').val("");
		$('#application_id').val("");
		$('#appMap').val("");
		$('#isPerfSuite').attr("checked",false);
		$("#addButton").text("Add TestCase");
		$('#modal_ajax .modal-body .panel-title h5 b').html("Add New TestCase");
		$('#modal_ajax').modal('show', {backdrop: 'true'});
	}
}
function confirm_modal(delete_url , post_refresh_url)
{
	$('#preloader-delete').html('');
	jQuery('#modal_delete').modal('show', {backdrop: 'static'});
	document.getElementById('delete_link').setAttribute("onClick" , "delete_data('" + delete_url + "' , '" + post_refresh_url + "')" );
	document.getElementById('delete_link').focus();
}
$(document).ready(function() {	
	$.ajax({
		url: base_url+"/application/mapByCompany", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data) {
			var appMap="";
			mapping = data;
			appMap += '<option value="">---Select Company---</option>';
			$.each(data.companies, function(key, value) {
				comp[value.companyId] = value.companyName;
				appMap += '<option value="'+value.companyId+'">'+value.companyName+'</option>';
			});
			$("select[name=appMap]").html(appMap);			
		}
	});
	$("select[name=appMap]").on("change", function(e){
		e.preventDefault();
		var options="";
		options += '<option value="" disabled="">---Select Application---</option>';
		if(mapping.map[this.value] != undefined) {		
			$.each(mapping.map[this.value], function(key,value){
				options += '<option value="'+value.applicationId+'">'+value.applicationName+'</option>';
			});
		}
		else {
			alert("There is no application created for "+$("select[name=appMap] option:selected").text());
		}
		$("select[name=application_id]").html(options);
	});
	$.ajax({
		url: base_url+"/application/all", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data) {
			$.each(data, function(key, value) {
				appOp[value.applicationId] = value.applicationName;
			});
		},
		complete: function(data){
			$.ajax({
				url: base_url+"/testcases/all", 
				method: "get",
				beforeSend: function (xhr) {
					xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
				},
				success: function(data) {	
					var tcData = "";		
					$.each(data, function(key, value) {
						var chkBox = "";
						if(value.isPerfSuite){
							chkBox = '<input class="col-md-3" value="'+value.isPerfSuite+'" checked="checked" disabled="disabled" type="checkbox">';
						} else {
							chkBox = '<input class="col-md-3" value="'+value.isPerfSuite+'" disabled="disabled" type="checkbox">';
						}
						var appName = appOp[value.applicationId];
						tcData += '<tr>';
						tcData += '<td>'+(key+1)+'</td>';
						tcData += '<td>'+comp[value.companyId]+'</td>';
						tcData += '<td>'+value.testcaseName+'</td>';
						tcData += '<td>'+chkBox+'</td>';
						tcData += '<td>'+appName+'</td>';
						tcData += '<td>';
						tcData += '<a style="cursor:pointer" onclick="showAjaxModal('+value.testcasesId+');" class="table-link">';
						tcData += '<span  class="fa-stack">';
						tcData += '<i class="fa fa-square fa-stack-2x"></i>';
						tcData += '<i class="fa fa-pencil fa-stack-1x fa-inverse"></i>';
						tcData += '</span>';
						tcData += '</a>';
						tcData += '<a href="" class="table-link danger" onclick="checkDelete(\''+base_url+'/testcases/' + value.testcasesId + '\');">';
						tcData += '<span class="fa-stack">';
						tcData += '<i class="fa fa-square fa-stack-2x"></i>';
						tcData += '<i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>';
						tcData += '</span>';
						tcData += '</a>';
						tcData += '</td>';
						tcData += '</tr>';
						tcs[value.testcasesId] = value;
					});
					$("table#table-example").append(tcData);
				},
				complete: function(){
					$('table#table-example').DataTable();
				}
			});
		}
	});
	
	
	$('#addButton').click(function(e){
		e.preventDefault();
		$testcase_name = $('#testcase_name').val();
		$class_name = $('#class_name').val();
		$description = $('#description').val();
		$testcaseId = $('#testcaseId').val();
		$application_id = $('#application_id').val();
		$companyId = $('#appMap').val();
		$isPerfSuite = $("#isPerfSuite[type=checkbox]").is(":checked")?1:0;
		var dataObj = {};
		dataObj["testcaseName"] = $testcase_name
		dataObj["className"] = $class_name
		dataObj["description"] = $description
		dataObj["applicationId"] = $application_id;
		//dataObj["companyId"] = $companyId;
		
		if($companyId == "" || $application_id == "" || $testcase_name == "" || $class_name == "" || $description == "") {
			showError();
			return false;
		}
		
		if($testcaseId!="") {
			dataObj["testcasesId"] = $testcaseId;
		}
		dataObj["isPerfSuite"] = $isPerfSuite;
		$.ajax({
			type: 'POST',
			data: JSON.stringify(dataObj),
			contentType: 'application/json',
			dataType: 'json',
			beforeSend: function (xhr) {
				xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
			},
			url: base_url+"/testcases/save",
				success: function(msg){
					$('#modal_ajax').modal('hide');
					if(!alert(successMsg)) {
						window.location.href= window.location.href;
					}
				}
		});
		return false;
	});
});


