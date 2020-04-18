var optiontc = [];
var appId = new Array(); 
var tcId = new Array(); 
var tcNames = new Array(); 
var appNames = new Array(); 
var comEnvURL;
$(document).ready(function() {
	var today = new Date().toLocaleString();
	today = today.substring(0,today.length-3).replace("/","_").replace("/","_").replace(", ","_");
	today = replaceAll(today, "-", "_");
	today = replaceAll(today, ":", "_");
	today = readCookie("TAuname")+"_"+today;
	$("input[name=execution_name]").val(today);
	$.ajax({
		url: base_url+"/browser/allByCompany", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(dataBrowser) {
			var appOptions = "";
			$.each(dataBrowser, function(key, value) {	
				appOptions += '<option value="'+value.browserId+'">'+value.browserName+'</option>';
			});
			$("select[name=browser_id]").append(appOptions);			
		}
	});	
	$.ajax({
		url: base_url+"/environment/all", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data) {
			var envOptions = "";
			$.each(data, function(key, value) {	
				envOptions += '<option value="'+value.environmentId+'">'+value.environmentName+'</option>';
			});
			$("select[name=environment_id]").append(envOptions);
			$("select[name=select_environment_id]").append(envOptions);
		}
	});	
	$.ajax({
		url: base_url+"/companyEnvironUrl/findAllByCompanyId", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success:function(data) {
			comEnvURL = data.map;
		}
	});
	$.ajax({
		url: base_url+"/application/allByCompany", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(data) {
			var appOptions = "";
			var options = "";
			var appData = [];
			$.each(data, function(key, value) {
				var applicationId = value.applicationId;
				var applicationName = value.applicationName;
				options += '<option value="'+applicationId+'">'+applicationName+'</option>';
				appData[applicationId] = applicationName;
				
				
				// IMPLEMENTED AS PER NEW DESIGN
				
				
				appOptions += '<div class="row selectdiv app'+applicationId+'">';
				appOptions += '<div class="col-md-12 col-sm-12 col-xs-12 selectdiv1">';
				appOptions += '<label class="main mainCB">'+applicationName;
				appOptions += '<input type="checkbox" value="0" class="selectcheck" onclick="onCheckSelect('+applicationId+')" data-id="'+applicationId+'" data-value="'+applicationName+'" name="_'+applicationId+'">'; 
				appOptions += '<span class="geekmark"></span>';
				appOptions += '</label>';
				appOptions += '<i class="fa fa-angle-down showdet" aria-hidden="true"></i>';
				appOptions += '<i class="fa fa-file-text filetest" aria-hidden="true"></i>';
				
				
				appOptions += '<div class="subdiv">';
				appOptions += '<p><i class="fa fa-times btncancel"></i></p>';
				appOptions += '<p>https://cdn.zeplin.io/5e2aef5289c3b99a5f0ac5eb/screens/FD35F34D-5F0E-444F-98EA-72594957D9B3.png</p>';
				appOptions += '<i class="fa fa-download" aria-hidden="true"></i>';
				appOptions += '<a href="#">Download Master Plan</a>';
				appOptions += '<a href="#">User Test Plan</a>';
				appOptions += '<a href="#" class="btncancel">Cancel</a>';
				appOptions += '</div>';
				appOptions += '<div class="col-md-12 col-sm-12 col-xs-12 submaindiv"><ul></ul></div></div></div>';
				
			});
			
			$("div.sel-content").append(appOptions);
			//User Role
			/*$.ajax({
				url: base_url+"/executionUser/allByCompany",
				type: "get",
				beforeSend: function (xhr) {
					xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
				},
				success: function(data)
				{ 
					var payload = "";
					$.each(data, function(index, value) {
						payload += '<option value="'+value.executionUserId+'">'+value.name+'- '+value.role+'</option>';
					});
					$("select[name=userrole]").append(payload);
				}
			});*/
			
			// Test Cases
			$.ajax({
				url: base_url+"/testcases/allByCompany", 
				method: "get",
				beforeSend: function (xhr) {
					xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
				},
				success: function(data) {
					$.each(data, function(key, value) {
						var tcData = "";
						if(optiontc.length == 0){
							optiontc[String(value.applicationId)] = "";
						}
						/*if(value.isPerfSuite) {
							tcData += '<p title="'+value.description+'">';
							tcData += '<input class="checkes subSelection'+value.applicationId+'" name="sub_category'+value.applicationId+'[]" data-id="'+value.testcasesId+'" data-value="'+value.testcaseName+'" value="'+value.className+'" type="checkbox"> <label>'+value.testcaseName+'</label>';
							tcData += '</p>';
							$("div.panel-group.accordion div#collapseOne0 .panel-body .form-group").append(tcData);
							if($("div.panel-group.accordion div#collapseOne0").parent().hasClass("hide")){
								$("div.panel-group.accordion div#collapseOne0").parent().removeClass("hide");
							}
						}
						else {*/
						
						// IMPLEMENTED AS PER NEW DESIGN
						
						
							tcData +='<li>'
							tcData += '<label class="main subCB">'+value.testcaseName;
							tcData += '<input class="checkes subSelection'+value.applicationId+'" name="sub_category'+value.applicationId+'[]" data-id="'+value.testcasesId+'" data-value="'+value.testcaseName+'" value="'+value.className+'" type="checkbox">';
							tcData += '<span class="geekmark"></span> ';
							tcData += '</label>';
							tcData += '<i class="fa fa-caret-down showbtndiv caret" aria-hidden="true"></i>';
							tcData += '<div class="hidediv">';
							tcData += '<button class="btn btn-primary btn-sm"><i class="fa fa-download" aria-hidden="true"></i> Master Test Case</button><button class="btn btn-primary btn-sm"><i class="fa fa-download" aria-hidden="true"></i> User Test Case</button><button class="btn btn-primary btn-sm"><i class="fa fa-folder" aria-hidden="true"></i> Browse</button><button class="btn btn-primary btn-sm"><i class="fa fa-upload" aria-hidden="true"></i> Upload</button>';
							tcData += '</div>';
							tcData += '</li>';
							$(".selectdiv.app"+value.applicationId+" .selectdiv1 .submaindiv ul").append(tcData)
							
							/*tcData += '<p title="'+value.description+'">';
							tcData += '<input class="checkes subSelection'+value.applicationId+'" name="sub_category'+value.applicationId+'[]" data-id="'+value.testcasesId+'" data-value="'+value.testcaseName+'" value="'+value.className+'" type="checkbox"> <label>'+value.testcaseName+'</label>';
							tcData += '</p>';
							$("div.panel-group.accordion div#collapseOne"+value.applicationId+" .panel-body .form-group").append(tcData);
							if($("div.panel-group.accordion div#collapseOne"+value.applicationId).parent().hasClass("hide")){
								$("div.panel-group.accordion div#collapseOne"+value.applicationId).parent().removeClass("hide");
							}
						}*/
						//optiontc[String(value.applicationId)] += '<option value="'+value.testcasesId+'">'+value.testcaseName+'</option>';
						
					});
					/*$('.checkes').on("change", function(){
						if ($(this).is(':checked')== true) {	
							tcId.push($(this).attr("data-id"));
							tcNames.push($(this).attr("data-value"));					
						}
						else {
							tcId.pop($(this).attr("data-id"));
							tcNames.pop($(this).attr("data-value"));
						}
						if (tcId.length == 0 && appId.length == 0) {
							$("#submit_btn").attr("disabled", "disabled");
						} else {
							$("#submit_btn").removeAttr("disabled");
						}
					});*/
				}
			});
		},
		complete: function(data) {
			
			/*$('.selectcheck').on("change", function(){
				$id = $(this).attr("data-id");
				//alert($id);
				if ($(this).is(':checked')== true) {		
					$("#collapseOne"+$id+" input[type=checkbox]").prop('checked', true).trigger("change");
					appId.push($(this).attr("data-id"));
					appNames.push($(this).attr("data-value"));
				} else {
					$("#collapseOne"+$id+" input[type=checkbox]").prop('checked', false).trigger("change");
					appId.pop($(this).attr("data-id"));
					appNames.pop($(this).attr("data-value"));
					elems=[];
				}
				if (appId.length == 0 || tcId.length==0) {
					$("#submit_btn").attr("disabled", "disabled");
				} else {
				  $("#submit_btn").removeAttr("disabled");
				}
			});
			*/
			
			
			
			
			
			
			
			/*
			$("#get_group").click(function(){
				if ($(this).is(':checked')== true) {
						$("#msg").hide();
						$("#show_group_content").show();
				} else {
						$("#show_group_content").hide();
				}	
			});
			$("#get_schedule").click(function(){		
				if ($(this).is(':checked')== true) {
						//$("#msg").hide();
						$("#show_schedule_content").show();
				} else {
						$("#show_schedule_content").hide();
				}	
			});
			
			$("#submit_group").click(function(){
				$group_name = $("#logical_group_name").val();
				$environment_id = $("#environment_id").val();
				$myCheckboxes = new Array();
				$(".checkes:checked").each(function() {
				   $myCheckboxes.push($(this).attr('data-id'));
				});
				if($("select[name=userrole] option:selected").val() == "") {
					$("select[name=userrole] option:selected").parent().parent().find("span.errmsg").html(errMsg).show();
				}
				if($("select[name=environment_id] option:selected").val() == "") {
					$("select[name=environment_id] option:selected").parent().parent().find("span.errmsg").html(errMsg).show();
				}
				if($myCheckboxes.length == 0) {
					$("#accordion").parent().find("span.errmsg").html(errMsg).show();
				}
				if($("select[name=userrole] option:selected").val() == "" || $("select[name=environment_id] option:selected").val() == "" || $(".checkes:checked").length == 0) {
					return false;
				}
				var dataObj = {};
				var arr = new Array();
				$.each(appNames, function(key, val){
					arr.push({"applicationId": appId[key], "applicationName": val});
				});
				dataObj["logicalGroupName"] = $group_name;
				dataObj["environment"] = {};
				dataObj["environment"]["environmentId"] = $("select[name=environment_id] option:selected").val();
				//dataObj["browser"] = {};
				//dataObj["browser"]["browserId"] = $("select[name=browser_id] option:selected").val();
				dataObj["executionUserId"] = $("select[name=userrole] option:selected").val();
				tcId = [];
				tcLogicalObj = []
				$(".checkes:checked").each(function() {
					tcId.push($(this).attr("data-id"));
					tcNames.push($(this).attr("data-value"));
					var obj = {};
					obj["testcasesId"] = $(this).attr("data-id");
					tcLogicalObj.push(obj);
				});
				dataObj["testCaseList"] = tcLogicalObj;
				$.ajax({
					url: base_url+"/logicalGroup/save/false", 
					method: "post",
					contentType: "application/json",
					data: JSON.stringify(dataObj),
					beforeSend: function (xhr) {
						xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
					},
					success: function(data) {
						$("#show_group_content").hide();
						$("#msg").html('<p id="mssg" style="margin-bottom:-10px;margin-top:5px;color: green;text-align: center;background-color: #f5f5f5; width: 80%;margin-left: 50px;padding: 7px; border: 1px solid #8bc34a;border-radius: 2px;">Test Set Created Successfully.</p>');
						$("#msg").fadeIn().fadeOut(3000);
						$('#get_group').prop('checked', false);
						$("#logical_group_name").val('');
					},
					complete: function(){
						$('#get_group').prop('checked', false);
						$("#logical_group_name").val('');
					}
				});
			});
			
			$("#submit_btn").on("click", function(){
				$("#submit_btn").attr("disabled", "disabled");
				var dataObj = {};
				var arr = new Array();
				$.each(appNames, function(key, val){
					arr.push({"applicationId": appId[key], "applicationName": val});
				});				
				dataObj["browserName"] = $("select[name=browser_id] option:selected").text();
				dataObj["browserId"] = $("select[name=browser_id] option:selected").val();
				dataObj["environmentName"] = $("select[name=environment_id] option:selected").text();
				dataObj["environmentId"] = $("select[name=environment_id] option:selected").val();
				dataObj["executionName"] = $("input[name=execution_name]").val();
				dataObj["executionUserId"] = $("select[name=userrole] option:selected").val();
				tcId = [];
				tcNames = [];
				$myCheckboxes = new Array();
				tcSchdldObj = []
				$(".checkes:checked").each(function() {
					tcId.push($(this).attr("data-id"));
					tcNames.push($(this).attr("data-value"));
					var obj = {};
					obj["testcasesId"] = $(this).attr("data-id");
					tcSchdldObj.push(obj);
				});
				dataObj["testcaseIds"] = tcId;
				dataObj["testcaseNames"] = tcNames;
				dataObj["scheduled"] = false;
				dataObj["applications"] = arr;
				if($("#show_schedule_content").is(":visible")) {
					if($("input#time").val() == "" || $("input#date").val() == "")
					{
						alert("Please select scheduled execution date and time");
						return false;
					}
					var schObj = {};
					schObj["scheduledExecutionName"] = $("input[name=execution_name]").val();
					schObj["executionUserId"] = $("select[name=userrole] option:selected").val();
					schObj["browserId"] = $("select[name=browser_id] option:selected").val();
					schObj["testCaseList"] = tcSchdldObj;
					schObj["environment"] = {};
					schObj["environment"]["environmentId"] = $("select[name=environment_id] option:selected").val();
					$(".checkes:checked").each(function() {
					   $myCheckboxes.push($(this).attr('data-id'));
					});
					if($("select[name=userrole] option:selected").val() == "") {
						$("select[name=userrole] option:selected").parent().parent().find("span.errmsg").html(errMsg).show();
					}
					if($("select[name=environment_id] option:selected").val() == "") {
						$("select[name=environment_id] option:selected").parent().parent().find("span.errmsg").html(errMsg).show();
					}
					if(tcSchdldObj.length == 0) {
						$("#accordion").parent().find("span.errmsg").html(errMsg).show();
					}
					if($("select[name=browser_id] option:selected").val() == "") {
						$("select[name=browser_id] option:selected").parent().parent().find("span.errmsg").html(errMsg).show();
					}
					if($("select[name=userrole] option:selected").val() == "" || $("select[name=environment_id] option:selected").val() == "" || tcSchdldObj.length == 0 || $("select[name=browser_id] option:selected").val() == "") {
						return false;
					}
					//var a = new Date($("input#date").val()+"T"+$("input#time").val());
					//var schdDate = new Date(a.getTime() + (a.getTimezoneOffset() * 60000));
					//schObj["scheduledDate"] = schdDate.toJSON();
					
					
					//schObj["scheduledDate"] = $("input#date").val()+"T"+$("input#time").val();
					
					
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

					
					// Timezone difference in hours and minutes
					// String such as +5:30 or -6:00 or Z
					schObj["scheduledDate"] = $("input#date").val()+"T"+$("input#time").val()+":00.000"+timezone_standard;
					
					
					$.ajax({
						url: base_url+"/scheduledExecution/save", 
						method: "post",
						contentType: "application/json",
						data: JSON.stringify(schObj),
						beforeSend: function (xhr) {
							xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
						},
						success: function(data) {
							$("#submit_btn").removeAttr("disabled", "disabled");
						},
						complete: function(){
							$("#show_schedule_content input").val("");
							$('#get_schedule').click();
							$("#submit_btn").removeAttr("disabled", "disabled");
							window.location.href = "scheduled.html";
						}
					});
				}
				else {
					$.ajax({
						url: base_url+"/executionResults/saveAll", 
						method: "post",
						contentType: "application/json",
						data: JSON.stringify(dataObj),
						beforeSend: function (xhr) {
							xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
						},
						success: function(data) {
							$("#submit_btn").removeAttr("disabled", "disabled");
						},
						complete: function(){
							$("#submit_btn").removeAttr("disabled", "disabled");
							window.location.href = "reports.html";
						}
					});
				}
			});
			*/



			// COPIED FROM SELECT TEST SET HTML FILE
			
			$(".mainCB input[type=checkbox]").click(function(e){
				e.stopPropagation();

				if($(this).prop("checked")==true)
				{	$(this).closest(".selectdiv1").find(".subCB input[type=checkbox]").prop("checked", true);	}
				else
				{	$(this).closest(".selectdiv1").find(".subCB input[type=checkbox]").prop("checked", false);	}
			});
			$(".mainCB, .subCB").click(function(e){
				e.stopPropagation();
			});

			/*---Jquery for section display none/block on click icon file-text---*/
			
			$(".filetest").click(function(e){
				e.stopPropagation();
				$('.subdiv').hide();
				$(this).closest(".selectdiv1").find(".subdiv").show();
				$(this).closest(".selectdiv").find(".submaindiv").slideUp();
			});
		   
		   /*---Jquery for section display none on click icon cancel---*/

			$('.btncancel').click(function(e){
				e.stopPropagation();
			  $(this).closest('.subdiv').hide();
			});
			
			/*---Jquery for toggle section on click angle-down icon---*/

			$(".selectdiv i.showdet").click(function(e){
				e.stopPropagation();
				$(this).closest(".selectdiv").find(".submaindiv").slideToggle();
				$(this).toggleClass("caret-rev");
			});

			$(".selectdiv1").click(function(){
				$(this).find(".submaindiv").slideToggle();
				$(this).find("i.showdet").toggleClass("caret-rev");
			});
			
			/*---Jquery for toggle section on click caret-down icon---*/

			$(".submaindiv i.showbtndiv").click(function(e){
				e.stopPropagation();
				$(this).closest("li").find(".hidediv").slideToggle();
				$(this).toggleClass("caret-rev");
			});

			$("aside.sidebar").height($("#main-content").height() + $("#footer").height() + 30);

			$(window).resize(function(){
				$("aside.sidebar").height($("#main-content").height() + $("#footer").height() + 30);
			});

			// For sidebar toggle
			if($(window).width() < 730)
			{
				$("#sidebarToggle").click(function(e){
					e.preventDefault();
					$("aside.sidebar").toggleClass("showSB");
					$(this).toggleClass("toggleIcon");
				});
			}
			


			
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
	$(".icons span").tooltip();
});
function onCheckSelect(appId)
{
	/*if($("input[name='_"+appId+"']").prop("checked")) {
		$("input[name='sub_category"+appId+"[]']").prop("checked", true);
	}
	else {
		$("input[name='sub_category"+appId+"[]']").prop("checked", false);
	}*/
	if($("#collapseOne"+appId+" input[type=checkbox]").prop("checked")) {
		$("#collapseOne"+appId+" input[type=checkbox]").prop("checked", true);
	}
	else {
		$("#collapseOne"+appId+" input[type=checkbox]").prop("checked", false);
	}
}
function populateUrls(id) {
	$.each(comEnvURL, function(key, value){
		//a[href="#collapseOne69"]
		$.each(value.environmentList, function(k,v){
			if(v.environment.environmentId == id) {
				$('a[href="#collapseOne'+key+'"] .value_show1').html('(<b style="font-size:13px;">'+v.environment.environmentName+'</b>:<span style="font-size:11px;"> '+v.envUrl+' </span> )');
			}
		});
	});
}
function gettestcases(id)
{
	$("select[name=selectTestCase]").html('<option value="" selected="selected" disabled="">---Select Test Case---</option>');
	$("select[name=selectTestCase]").append(optiontc[String(id)]);
}
function getZipLinks(id)
{
	if($("select[name=application_id] option:selected").val()==""){
		alert("Please select application");
		return false;
	}
	else {
		if(id==""){
			$(".zipLinks").hide();
			alert("Please select environment");
			return false;
		}
		else {
			$(".zipLinks").show();
		}
	}
}
function downloadUTP()
{
	var downloadType = base_url+"/executionResults/downloadTestcasesZip/"+$("select[name=application_id] option:selected").text()+"/"+$("select[name=select_environment_id] option:selected").text()+"/false";
	var ext = 'UserTestPlan.zip';
	var contentType = "application/octet-stream";
	var hrefContentType = 'data:application/octet-stream;base64,';
	var xhr = new XMLHttpRequest();
	xhr.open('GET', downloadType, true);
	var token = "Bearer " + readCookie("TAaccess");
	xhr.responseType = 'arraybuffer';
	xhr.setRequestHeader('Authorization', token);
	xhr.setRequestHeader('Content-type', contentType);
	xhr.onload = function(e) {
	  if (this.status == 200) {
		var uInt8Array = new Uint8Array(this.response);
		var i = uInt8Array.length;
		var binaryString = new Array(i);
		while (i--)
		{
		  binaryString[i] = String.fromCharCode(uInt8Array[i]);
		}
		var data = binaryString.join('');

		var base64 = window.btoa(data);

		var anchorElement = document.createElement('a');
		anchorElement.setAttribute('download', ext);
		anchorElement.href = hrefContentType + base64;
		document.body.appendChild(anchorElement);
		var mouseEvent = document.createEvent('MouseEvents');
		mouseEvent.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
		anchorElement.dispatchEvent(mouseEvent);		
	  }
	  else {
		  statusError(this.status, this.responseText);;
	  }
	};

	xhr.send();
}
function downloadMTP()
{
	var downloadType = base_url+"/executionResults/downloadTestcasesZip/"+$("select[name=application_id] option:selected").text()+"/"+$("select[name=select_environment_id] option:selected").text()+"/true";
	var ext = 'MasterTestPlan.zip';
	var contentType = "application/octet-stream";
	var hrefContentType = 'data:application/octet-stream;base64,';
	var xhr = new XMLHttpRequest();
	xhr.open('GET', downloadType, true);
	var token = "Bearer " + readCookie("TAaccess");
	xhr.responseType = 'arraybuffer';
	xhr.setRequestHeader('Authorization', token);
	xhr.setRequestHeader('Content-type', contentType);
	xhr.onload = function(e) {
	  if (this.status == 200) {
		var uInt8Array = new Uint8Array(this.response);
		var i = uInt8Array.length;
		var binaryString = new Array(i);
		while (i--)
		{
		  binaryString[i] = String.fromCharCode(uInt8Array[i]);
		}
		var data = binaryString.join('');

		var base64 = window.btoa(data);

		var anchorElement = document.createElement('a');
		anchorElement.setAttribute('download', ext);
		anchorElement.href = hrefContentType + base64;
		document.body.appendChild(anchorElement);
		var mouseEvent = document.createEvent('MouseEvents');
		mouseEvent.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
		anchorElement.dispatchEvent(mouseEvent);		
	  }
	  else {
		  statusError(this.status, this.responseText);;
	  }
	};

	xhr.send();
}
function show_TestCase_data(tcid)
{
	var file_name = "<button type='button' name='masterFile' class='btn btn-primary mar10'><i class='fa fa-download'></i>&nbsp;&nbsp;Master TestCase</button>";
	file_name += "<button type='button' name='downloadFile' class='btn btn-primary mar10'><i class='fa fa-download'></i>&nbsp;&nbsp;User TestCase</button>";
	file_name += '<label class="btn btn-primary mar10"><i class="fa fa-file-excel-o"></i>&nbsp;&nbsp;Browse<input type="file" name="testData" style="display: none;"></label>';
	file_name += "<button type='button' name='saveFile' class='btn btn-primary'><i class='fa fa-upload'></i>&nbsp;&nbsp;Upload</button>";
	$("#filenameshow").html(file_name);
	$("#filenameshow").show();
	$("button[name=masterFile]").on("click", function(){
		var downloadType = base_url+"/testcases/downloadTestCase?applicationName="+$("select[name=application_id] option:selected").text()+"&testCaseName="+$("select[name=selectTestCase] option:selected").text()+"&isMasterFile=true&environmentName="+$("select[name=select_environment_id] option:selected").text();
		var ext = $("select[name=selectTestCase] option:selected").text()+".xlsx";
		var contentType = "application/octet-stream";
		var hrefContentType = 'data:application/octet-stream;base64,';
		var xhr = new XMLHttpRequest();
		xhr.open('GET', downloadType, true);
		var token = "Bearer " + readCookie("TAaccess");
		xhr.responseType = 'arraybuffer';
		xhr.setRequestHeader('Authorization', token);
		xhr.setRequestHeader('Content-type', contentType);
		xhr.onload = function(e) {
		  if (this.status == 200) {
			var uInt8Array = new Uint8Array(this.response);
			var i = uInt8Array.length;
			var binaryString = new Array(i);
			while (i--)
			{
			  binaryString[i] = String.fromCharCode(uInt8Array[i]);
			}
			var data = binaryString.join('');

			var base64 = window.btoa(data);

			var anchorElement = document.createElement('a');
			anchorElement.setAttribute('download', ext);
			anchorElement.href = hrefContentType + base64;
			document.body.appendChild(anchorElement);
			var mouseEvent = document.createEvent('MouseEvents');
			mouseEvent.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
			anchorElement.dispatchEvent(mouseEvent);		
		  }
		  else {
			  statusError(this.status, this.responseText);;
		  }
		};

		xhr.send();
	});
	$("button[name=downloadFile]").on("click", function(){
		var downloadType = base_url+"/testcases/downloadTestCase?applicationName="+$("select[name=application_id] option:selected").text()+"&testCaseName="+$("select[name=selectTestCase] option:selected").text()+"&isMasterFile=false&environmentName="+$("select[name=select_environment_id] option:selected").text();
		var ext = $("select[name=selectTestCase] option:selected").text()+".xlsx";
		var contentType = "application/octet-stream";
		var hrefContentType = 'data:application/octet-stream;base64,';
		var xhr = new XMLHttpRequest();
		xhr.open('GET', downloadType, true);
		var token = "Bearer " + readCookie("TAaccess");
		xhr.responseType = 'arraybuffer';
		xhr.setRequestHeader('Authorization', token);
		xhr.setRequestHeader('Content-type', contentType);
		xhr.onload = function(e) {
		  if (this.status == 200) {
			var uInt8Array = new Uint8Array(this.response);
			var i = uInt8Array.length;
			var binaryString = new Array(i);
			while (i--)
			{
			  binaryString[i] = String.fromCharCode(uInt8Array[i]);
			}
			var data = binaryString.join('');

			var base64 = window.btoa(data);

			var anchorElement = document.createElement('a');
			anchorElement.setAttribute('download', ext);
			anchorElement.href = hrefContentType + base64;
			document.body.appendChild(anchorElement);
			var mouseEvent = document.createEvent('MouseEvents');
			mouseEvent.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
			anchorElement.dispatchEvent(mouseEvent);		
		  }
		  else {
			  statusError(this.status, this.responseText);;
		  }
		};

		xhr.send();
	});
	$("button[name=saveFile]").on("click", function(){
		var form = $('#uploadFile form')[0];
        var data = new FormData(form);
		data.append('testData', $('input[type=file]')[0].files[0]); 
		data.append('applicationName', $("select[name=application_id] option:selected").text()); 
		data.append('environmentName', $("select[name=select_environment_id] option:selected").text()); 
		data.append('testCaseName', $("select[name=selectTestCase] option:selected").text()); 
		$.ajax({
			type: "POST",
            contentType: false,
			enctype: 'multipart/form-data',
            processData: false,
            cache: false,
            timeout: 600000,
			url: base_url+"/testcases/uploadTestCaseFile",
			data: data,
			beforeSend: function (xhr) {
				xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
			},
			success: function(data) {
				$("#filemsg p").html("File uploaded successfully!");
				$("#filemsg").fadeIn().fadeOut(3000);
			}
		});
	});
}