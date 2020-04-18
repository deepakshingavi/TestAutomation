$(document).ready(function() {
	var today = new Date().toLocaleString();
	today = today.substring(0,today.length-3).replace("/","_").replace("/","_").replace(", ","_");
	today = replaceAll(today, "-", "_");
	today = replaceAll(today, ":", "_");
	today = readCookie("TAuname")+"_"+today;
	$("input[name=execution_name]").val(today);
	var browserNames, environmentNames, logicalOptions, selectEnv, selectEnvOps, selectUserRole="";
	$.ajax({
		url: base_url+"/browser/allByCompany", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(dataBrowser) {
			browserNames = "";
			$.each(dataBrowser, function(key, value) {	
				browserNames += '<option value="'+value.browserId+'">'+value.browserName+'</option>';
			});
			$("select[name=browser_id]").append(browserNames);			
		}
	});
	$.ajax({
		url: base_url+"/environment/all", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data) {
			environmentNames = '<option value="" disabled="">---Select Environment---</option>';
			selectEnv = "";
			$.each(data, function(key, value) {	
				environmentNames += '<option value="'+value.environmentId+'">'+value.environmentName+'</option>';
				selectEnv += '<option value="'+value.environmentId+'">'+value.environmentName+'</option>';
			});
			
			$("select.logicalEnv").html(environmentNames);
		}
	});
	//User Role
	$.ajax({
		url: base_url+"/executionUser/allByCompany",
		type: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data)
		{ 
			var payload = '<option value="" disabled="">---Select User Role---</option>';
			
			$.each(data, function(index, value) {
				payload += '<option value="'+value.executionUserId+'">'+value.name+'- '+value.role+'</option>';
				selectUserRole += '<option value="'+value.executionUserId+'">'+value.name+' - '+value.role+'</option>';
			});
			$("select.logicalUserRole").html(payload);
		}
	});
	$.ajax({
		url: base_url+"/logicalGroup/allByCompany", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data) {
			logicalOptions = "";
			var logicalTR = "";
			$.each(data, function(key, value) {	
				logicalTR += '<tr><td><input type="checkbox" name="logical_group_id[]" value="'+value.id+'"  style="margin-left:  10px;width: 16px;height: 16px;"></td>';
				logicalTR += '<td><span class="grp_name'+key+'">'+value.name+'</span><div class="grp_edit'+key+'" style="display:none">';
				logicalTR += '<input type="hidden" class="test'+key+'" value="[428,521]"/><input class="form-control group_name'+key+'" type="text" value="'+value.name+'" name="logical_group_name"></div></td>';
				logicalTR += '<td><span class="env_name'+key+'">'+value.environmentName+'</span><div class="env_edit'+key+'" style="display:none">';
				//logicalTR += '<select class="form-control logicalEnv" name="environment_id'+key+'" id="environment_id'+key+'"  required onchange="show_url_datashow_url_data(this.value)">';
				logicalTR += '<select class="form-control logicalEnv" name="environment_id'+key+'" id="environment_id'+key+'"  required="">';
				logicalTR += '<option value="" disabled="">---Select Environment---</option>';
				logicalTR += selectEnv;
				logicalTR += '</select>';
				logicalTR += '</div></td>';
				
				logicalTR += '<td><span class="user_role'+key+'">'+value.executionUserName+' - '+ value.executionUserRole +'</span><div class="userrole_edit'+key+'" style="display:none">';
				logicalTR += '<input type="hidden" class="userroleid'+key+'" value="'+value.executionUserId+'"/><select class="form-control logicalUserRole" name="userrole'+key+'" required id="userrole'+key+'">';
				logicalTR += selectUserRole;
				logicalTR += '</select>';
				logicalTR += '</div></td>';
				
				logicalTR += '<td>'+value.executedBy+'</td><td>';             	
				logicalTR += '<a style="cursor:pointer" title="View Test Set" onclick="showAjaxModal('+value.id+', \''+value.name+'\');"  class="table-link info">';
				logicalTR += '<span class="fa-stack"><i class="fa fa-square fa-stack-2x"></i><i class="fa fa-eye fa-stack-1x fa-inverse" style="background-color:#f2e185;border-radius:3px"></i></span></a>';
				logicalTR += '<a style="cursor:pointer" title="Clone Test Set" class="table-link clone'+key+'" onclick="showClone('+key+', \''+value.environmentName+'\');" >';
				logicalTR += '<span class="fa-stack"><i class="fa fa-square fa-stack-2x"></i><i class="fa fa-clone fa-stack-1x fa-inverse" style="background-color:#ffa000;border-radius:3px"></i></span></a>';
				logicalTR += '<a style="cursor:pointer;display:none" title="Save Test Set" class="table-link save'+key+'" onclick="saveClone('+key+');" >';
				logicalTR += '<span class="fa-stack"><i class="fa fa-square fa-stack-2x"></i><i class="fa fa-save fa-stack-1x fa-inverse" style="background-color:#3c763d;border-radius:3px"></i></span></a>';
				logicalTR += '<a style="cursor:pointer;display:none" class="reset'+key+'"  title="Reset" onclick="reset('+key+');"><i class="fa fa-undo" aria-hidden="true"></i></a>';
				logicalTR += '<a  style="cursor:pointer" title="Delete Test Set" class="table-link danger">';
				logicalTR += '<span class="fa-stack" onclick="return checkDelete('+value.id+');">';
				logicalTR += '<i class="fa fa-square fa-stack-2x"></i><i class="fa fa-trash-o fa-stack-1x fa-inverse" style="background-color:#ff0000;border-radius:3px"></i></span></a></td></tr>';
				
				logicalOptions += '<option value="'+value.id+'">'+value.name+'</option>';				
			});
			$("select[name=logical_group]").append(logicalOptions);	
			$("#table-example tbody").html(logicalTR);
			$('#table-example').DataTable();			
		}
	});
	$("#get_schedule").click(function() {
		if ($(this).is(':checked') == true) {
			//$("#msg").hide();
			$("#show_schedule_content").show();
		} else {
			$("#show_schedule_content").hide();
		}

	});
	
	$("input[name=execute]").click(function() {
		if($("#execution_name").val()==""){
			alert("Please enter execution name");
			return false;
		}
		if($("select[name=browser_id] option:selected").val()==""){
			alert("Please select browser");
			return false;
		}
		if ($("#table-example input[type=checkbox]:checked").length > 0) {
			var arr = [];
			$.each($("#table-example input[type=checkbox]:checked"), function(k,v){
				arr.push($(v).val());
			});
			var dataObj = {};
			var url = "";
			$browser_id = $("select[name=browser_id] option:selected").val();
			$browser_name = $("select[name=browser_id] option:selected").text();
			dataObj["name"] = $("#execution_name").val();
			dataObj["logicalTest SetId"] = arr;
			if($browser_id == ""){
				$browser_id = null;
			}
			dataObj["browserId"] = $browser_id;
			dataObj["browserName"] = $browser_name;
			if($("#get_schedule").is(":checked")) {
				var timezone_offset_min = new Date().getTimezoneOffset(),
				offset_hrs = parseInt(Math.abs(timezone_offset_min/60)),
				offset_min = Math.abs(timezone_offset_min%60),
				timezone_standard;

				if(offset_hrs < 10)
				offset_hrs = '0' + offset_hrs;

				if(offset_min < 10)
				offset_min = '0' + offset_min;

				// Add an opposite sign to the offset
				// If offset is 0, it means timezone is UTC
				if(timezone_offset_min < 0)
				timezone_standard = '+' + offset_hrs + offset_min;
				else if(timezone_offset_min > 0)
				timezone_standard = '-' + offset_hrs + offset_min;
				else if(timezone_offset_min == 0)
				timezone_standard = 'Z';
				dataObj["scheduledDate"] = $("input#date").val()+"T"+$("input#time").val()+":00.000"+timezone_standard;
			
			
				/*var a = new Date($("input#date").val()+"T"+$("input#time").val());
				var schdDate = new Date(a.getTime() - (a.getTimezoneOffset() * 60000));
				dataObj["scheduledDate"] = schdDate.toJSON();*/
				url = "schedule";
			}
			else {
				url = "execute";
			}
			$.ajax({
				type: 'POST',
				data: JSON.stringify(dataObj),
				contentType: 'application/json',
				dataType: 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
				},
				url: base_url+"/logicalGroup/"+url,
				success: function(response)
				{
					if(url == "execute") {
						window.location.href = "reports.html";
					}
					else {
						window.location.href = "scheduled.html";
					}
				}
			});
		} else {
			alert("Please select Test set to execute");
			return false;
		}

	});
});

function showClone(sr, envName) {
    var userRole = $(".user_role" + sr+":visible").text();
	$(".grp_name" + sr).hide();
    $(".clone" + sr).hide();
    $(".env_name" + sr).hide();
    $(".user_role" + sr).hide();	
	$(".grp_edit" + sr).show();
    $(".env_edit" + sr).show();
    $(".userrole_edit" + sr).show();
	var env = $("select[name=environment_id"+sr+"] option:contains('"+envName+"')").val();
    $("select[name=environment_id"+sr+"] option[value="+env+"]").attr("selected", "selected");
	var role = $("select[name=userrole"+sr+"] option:contains('"+userRole+"')").val();
	$("select[name=userrole"+sr+"] option[value="+role+"]").attr("selected", true);
	$(".save" + sr).show();
    $(".reset" + sr).show();
    //alert(id);

}

function reset(sr) {
    $(".grp_name" + sr).show();
    $(".clone" + sr).show();
    $(".env_name" + sr).show();
	$(".user_role"+ sr).show();
	$(".userrole_edit" + sr).hide();
    $(".grp_edit" + sr).hide();
    $(".env_edit" + sr).hide();
    $(".save" + sr).hide();
    $(".reset" + sr).hide();
}

function saveClone(sr) {
    $group_name = $(".group_name" + sr +":visible").val();
	$id = $('input[name="logical_group_id[]"]:visible').eq(sr).val()
    $environment_id = $("select#environment_id"+sr+":visible option:selected").val();
    $userroleid = $("select#userrole"+sr+":visible option:selected").val();
    $browser_id = $("select[name=browser_id] option:selected").val();
    $myCheckboxes = new Array();
    $myCheckboxes = $(".test" + sr +":visible").val();
	if($environment_id == "") {
		alert("Environment is mandatory");
		return false;
	}
	var dataObj = {};
	dataObj["logicalTest SetId"] = $id;
	dataObj["logicalTest SetName"] = $group_name;
	dataObj["executionUserId"] = $userroleid;
	/*if($browser_id != ""){
		dataObj["browser"] = {};
		dataObj["browser"]["browserId"] = $browser_id;
	}*/
	dataObj["environment"] = {};
	dataObj["environment"]["environmentId"] = $environment_id;
    //alert($myCheckboxes);
	$.ajax({
		url: base_url+"/logicalGroup/save/true", 
		method: "post",
		contentType: "application/json",
		data: JSON.stringify(dataObj),
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data) {
			window.location.reload();
		},
		complete: function(){
			$('#get_group').prop('checked', false);
			$("#logical_group_name").val('');
		}
	});
    /*$.post('createCloneTestSet', {
        group_name: $group_name,
        environment_id: $environment_id,
        testcase_id: $myCheckboxes
    }, function(data) {
        if (data == 1) {
            window.location.reload();
        } else {
            $("#msg").html('<div id="mssg" style="margin-bottom:0px;margin-top:5px;color: red;text-align: center;background-color: #fff; width: 75%;margin-left: 100px;padding: 4px; border: 1px solid red;border-radius: 2px;"><button type="button" class="close" data-dismiss="alert" aria-hidden="true"><i class="fa fa-times-circle fa-fw fa-lg" style="color:red"></i></button>' + data + '</div>');
            $("#msg").show();

        }
    });*/

}

function show_test_case(no, one) {
    if (no == "0") {
        $logical_group_id = $("#logical_group_id").val();

    } else {
        $logical_group_id = no;
    }
    if ($logical_group_id != '') {
        showAjaxModal($logical_group_id, $("#logical_group_id option:selected").text());

    } else {
        alert("Select Test set Name");
    }
}

function reset_test_case(no) {
    $('#executionReport').css("display", "none");
    $("#displayNoneMain").css("display", "block");

}
function checkDelete(id) {
    var chk = confirm("Are You Sure To Delete This !");
    if (chk) {
		$.ajax({
			url: base_url+"/logicalGroup/"+id,
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

function showAjaxModal(id, name="") {
	// SHOW AJAX RESPONSE ON REQUEST SUCCESS
    $.ajax({
        url: base_url+"/logicalGroup/view/"+id,
        beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(response) {
			var payload = '';
			$.each(response, function(key, value){
				payload += '<tr>';
				payload += '<td>'+(key+1)+'</td>';
				payload += '<td>'+value.applicationName+'</td>';
				payload += '<td>'+value.executionUserName+' - '+value.executionUserRole+'</td>';
				payload += '<td>'+value.testCaseName+'</td>';
				payload += '</tr>'
			});
			$(".panel-title h5 b").html("Test Set Name : "+name);
            $('#modal_ajax .modal-body table#table-example tbody').html(payload);
			// LOADING THE AJAX MODAL
			$('#modal_ajax').modal('show', {
				backdrop: 'true'
			});
        }
    });
}

function showTestImage(url) {
    // SHOWING AJAX PRELOADER IMAGE
    jQuery('#image_ajax .modal-body').html('<div style="text-align:center;margin-top:200px;"><img src="http://18.217.15.187/TestApps//partnerIT/img/loader.GIF" style="height:50px;" /></div>');

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