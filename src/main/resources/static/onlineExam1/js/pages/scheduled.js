$(document).ready(function() {
	$.ajax({
		url: base_url+"/scheduledExecution/allByCompany", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(dataBrowser) {
			var appOptions = "";
			var payload = "";
			var index = 0;
			$.each(dataBrowser, function(key, value) {
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

				//var dt = "2018-10-11T19:30:00.000-0530";
				var dateArray = "-";
				if(value.executedDate!=null)
				{
					var dt = value.executedDate.split("+")[0]+"-"+(timezone_standard.slice(1));
					var d = new Date(dt);
					dateArray = d.toJSON().split(".")[0].replace("T", " ");
				}
				//var dateArray = value.executedDate==null?"-":value.executedDate.split(".")[0].replace("T", " ");
				$pas= value.progess;
				var div = "";
				var isDisabled = 'disabled="disabled"';
				if($pas >=0 && $pas <=30){
					$cls = 'progress progress-bar-danger';
					div = '<div class="progress" style="margin-bottom:0px;color:#000;"><div class="progress-bar '+$cls+' progress-bar-striped" title="'+$pas+'%" role="progressbar" aria-valuenow="'+$pas+'"  aria-valuemin="0" aria-valuemax="100" style="width:'+$pas+'%"></div>';
				}else if($pas >=30 && $pas <=50){
					 $cls = 'progress progress-bar-warning';
					 div = '<div class="progress" style="margin-bottom:0px;color:#000;"><div class="progress-bar '+$cls+' progress-bar-striped" title="'+$pas+'%" role="progressbar" aria-valuenow="'+$pas+'"  aria-valuemin="0" aria-valuemax="100" style="width:'+$pas+'%"></div>';
				}else if($pas >=50 && $pas <=99){
					 $cls = 'progress progress-bar-info';
					 div = '<div class="progress" style="margin-bottom:0px;color:#000;"><div class="progress-bar '+$cls+' progress-bar-striped" title="'+$pas+'%" role="progressbar" aria-valuenow="'+$pas+'"  aria-valuemin="0" aria-valuemax="100" style="width:'+$pas+'%"></div>';
				} else if($pas ==100){
					 $cls = '';
					 isDisabled = "";
					 div = '<div>';
				}
				payload += '<tr>';
				payload += '<td>'+(++index)+'</td>';
				payload += '<td>'+value.name+'</td>';
				payload += '<td>'+value.executedBy+'</td>';
				payload += '<td>';
				payload += div;
				payload += '</div>';
				payload += '<div>'+dateArray+'</div>';
				payload += '</td>';
				payload += '<td>';
				var ids = value.id;
				var schdld = false;
				var runId = value.id;
				if(value.scheduled) {
					schdld = true;
					runId = (value.runnerId==null) ? 0 : value.runnerId;
					payload += '<button onclick="showAjaxModal('+ids+', true, \''+runId+'\', \''+value.name+'\');" class="btn btn-info">';
				}
				else {
					payload += '<button onclick="showAjaxModal('+ids+', false, 0, \''+value.name+'\');" class="btn btn-info">';
				}
				payload += 'View';
				payload += '</button>';
				payload += '</td>';
				payload += '<td>';
				payload += '<button onclick="downloadFile(\'excel\', '+runId+', \''+value.name+'\', '+schdld+');" style="cursor:pointer;background-color: green" class="btn btn-success mar5">';
				payload += '<i class="fa fa-file-excel-o"></i>';
				payload += '</button>';
				payload += '<button onclick="downloadFile(\'pdf\', '+runId+', \''+value.name+'\', '+schdld+');" style="cursor:pointer;background-color: #E13300" class="btn btn-info">';
				payload += '<i class="fa fa-file-pdf-o"></i>';
				payload += '</button>';
				payload += '</td>';
				payload += '<td>';
				payload += '<button '+isDisabled+' onclick="runReport('+runId+');" class="btn btn-info">';
				payload += 'Run';
				payload += '</button>';
				payload += '</td>';
				payload += '<td>';
				payload += '<a onclick="return false;" class="table-link danger">';
				payload += '<span class="fa-stack" onclick="return checkDelete('+value.id+', '+schdld+');">';
				payload += '<i class="fa fa-square fa-stack-2x"></i>';
				payload += '<i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>';
				payload += '</span>';
				payload += '</a>';
				payload += '</td>';
				payload += '</tr>'; 
				appOptions += '<option value="'+value.id+'">'+value.name+'</option>';
			});
			$("select[name=executed_testCases]").append(appOptions);			
			$("#table-example tbody").html(payload);
			//$('#table-example').DataTable();
			var table = $('#table-example').dataTable({
				'info': false,
				'sDom': 'lTfr<"clearfix">tip',
				'oTableTools': {
					'aButtons': [{
						'sExtends': 'collection',
						'sButtonText': '<i class="fa fa-cloud-download"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-caret-down"></i>',
						'aButtons': ['csv', 'xls', 'pdf', 'copy', 'print']
					}]
				}
			});

			var tt = new $.fn.dataTable.TableTools(table);
			$(tt.fnContainer()).insertBefore('div.dataTables_wrapper');

			var tableFixed = $('#table-example-fixed').dataTable({
				'info': false,
				'pageLength': 50
			});

			new $.fn.dataTable.FixedHeader(tableFixed);			
		}
	});

    
});

/*

$(document).ready(function() {
$('#sel2').select2();
$("#searchtable").hide();
$("#showid").click(function(){
$("#searchtable").show();
$('#table-example').hide();
$("#table-example_wrapper").hide();
$(".DTTT").hide();
});
var table = $('.table-example').dataTable({
'info': false,
'sDom': 'lTfr<"clearfix">tip',
'oTableTools': {
'aButtons': [
{
'sExtends':    'collection',
'sButtonText': '<i class="fa fa-cloud-download"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-caret-down"></i>',
'aButtons':    [ 'csv', 'xls', 'pdf', 'copy', 'print' ]
}
]
}
});



var tt = new $.fn.dataTable.TableTools( table );
$( tt.fnContainer() ).insertBefore('div.dataTables_wrapper');

var tableFixed = $('#table-example-fixed').dataTable({
'info': false,
'pageLength': 50
});

new $.fn.dataTable.FixedHeader( tableFixed );
});
*/


/*$(document).ready(function() {
    //$('#sel2').select2();
    $("#searchtable").hide();
    $("#showid").click(function() {
        //$("#searchtable").show();
        $('#table-example').hide();
        $("#table-example_wrapper").hide();
        $(".DTTT").hide();
    });
});*/
function downloadFile(type, id, name, schdld=false) {
	var URL;	
	if(schdld != null && schdld){
		URL = base_url+"/scheduledExecutionBk/download";
	}
	else {
		URL = base_url+"/scheduledExecution/download";
	}
	var downloadType = "";
	var ext = name+".xlsx";
	var contentType = "application/octet-stream";
	var hrefContentType = 'data:application/octet-stream;base64,';
	if(type.toUpperCase()=="EXCEL"){
		downloadType = URL+"Excel/"+id;
	}
	else {
		downloadType = URL+"Pdf/"+id;
		contentType = "application/pdf";
		hrefContentType = 'data:application/pdf;base64,';
		ext = name+".pdf";
	}
	
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
function show_test_case(no, one) {
    if (no == "0") {
        $execution_id = $("#execution_test_id").val();

    } else {
        $execution_id = no;
    }
    if ($execution_id != '') {
        showAjaxModal($execution_id);
    } else {
        alert("Please Select Execution Name");
		return false;
    }
}

function reset_test_case(no) {
    $('#executionReport').css("display", "none");
    $("#displayNoneMain").css("display", "block");

}

function runReport(runID) {
	$.ajax({
		type: 'POST',
		url: base_url+"/executionResults/reRun/"+runID,
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(msg){
			if(msg) {
				window.location.href= window.location.href;
			}
			else {
				alert("No failed test case in selected Execution");
			}
		}
	});
}

function checkDelete(id, schdld=false) {
	var URL;
	
	if(schdld != null && schdld){
		URL = base_url+"/scheduledExecutionBk/"+id;
	}
	else {
		URL = base_url+"/scheduledExecution/"+id;
	}
    var chk = confirm("Are You Sure To Delete This Scheduled Report!");
    if (chk) {
		$.ajax({
			url: URL,
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

function confirm_modal(delete_url, post_refresh_url) {
    $('#preloader-delete').html('');
    jQuery('#modal_delete').modal('show', {
        backdrop: 'static'
    });
    document.getElementById('delete_link').setAttribute("onClick", "delete_data('" + delete_url + "' , '" + post_refresh_url + "')");
    document.getElementById('delete_link').focus();
}

function showAjaxModal(id, schdld=false, runId=0, name="") {
	var URL;
	var isBk = true;
	if(schdld != null && schdld == true){
		URL = base_url+"/scheduledExecutionBk/getRunnerDetailsFull/"+runId;
	}
	else {
		URL = base_url+"/scheduledExecution/getRunnerDetailsFull/"+id+"/"+runId;
		isBk = false;
	}
	var testcaseName = "";
    $.ajax({
		url: URL, 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success:function(data) {
			$(".panel-title b.runname").html(": "+name);
			var payload = "";
			$.each(data, function(key, value){
				if(isBk) {
					testcaseName = value.testcases.testcaseName;
				}
				else {
					testcaseName = value.testcaseName;
				}
					
				var logLink = "";
				payload += '<tr>';
				payload += '<td>'+(key+1)+'</td>';
				if(testcaseName != null || testcaseName!= ""){
					payload += '<td>'+testcaseName+'</td>';
				}
				else {
					payload += '<td>'+value.executionName+'</td>';
				}
				if(value.result.toUpperCase() == "FAILED"){
					payload += '<td style="color:red"><b style="cursor:pointer" class="imageLink">Failed</b></td>';
					logLink = '<p class="spanLinks">Log</p>';
				} 
				else if(value.result.toUpperCase() == "PASSED"){
					payload += '<td style="color:green"><b>Passed</b></td>';
					logLink = '<p class="spanLinks">Log</p>';
				}
				else if(value.result.toUpperCase() == "QUEUED"){
					payload += '<td style="color:orange"><b>Queued</b></td>';
				}
				else if(value.result.toUpperCase() == "SUBMITED"){
					payload += '<td style="color:blue"><b>Submited</b></td>';
				}
				payload += '<td>'+value.elapsedTime+'</td>';
				if(value.testcaseExeDetailList==null || value.testcaseExeDetailList.length==0){
					payload += '<td class="accordion-toggle collapsed"  data-toggle="collapse" data-target="#collapse'+(key+1)+'">'+logLink+'</td>';
				}
				else {
					payload += '<td class="accordion-toggle collapsed"  data-toggle="collapse" data-target="#collapse'+(key+1)+'"><span></span>'+logLink+'</td>';
				}
				payload += '</tr>';
				if(value.testcaseExeDetailList!=null && value.testcaseExeDetailList.length > 0){
					$.each(value.testcaseExeDetailList, function(k,v) {
						var output, reason, result;
						if(v.result=="" || v.result==undefined)
						{
							result = '<td><b>-</b></td>';;
						}
						else {
							if(v.result.toUpperCase() == "FAILED"){
								result = '<td style="color:red"><b style="cursor:pointer" class="imageLink">Failed</b></td>';
							} 
							else if(v.result.toUpperCase() == "PASSED"){
								result = '<td style="color:green"><b>Passed</b></td>';
							}
							else if(v.result.toUpperCase() == "QUEUED"){
								result = '<td style="color:orange"><b>Queued</b></td>';
							}
							else if(v.result.toUpperCase() == "SUBMITED"){
								result = '<td style="color:blue"><b>Submited</b></td>';
							}
						}
						if(v.output=="" || v.output==undefined)
						{
							output = "-";
						}
						else {
							output = v.output;
						}
						if(v.reason=="" || v.reason==undefined)
						{
							reason = "-";
						}
						else {
							reason = v.reason;
						}
						payload += '<tr id="collapse'+(key+1)+'" class="collapse"><td class="no-border" colspan="5"><table><thead><th>Row No</th><th>Result</th><th>Output</th><th>Reason</th><th>Elapse Time</th></thead><tbody>';
						payload += '<tr>';
						payload += '<td>'+(key+1)+'.'+(k+1)+'</td>'+result;
						payload += '<td>'+output+' </td>';
						payload += '<td>'+reason+' </td>';
						payload += '<td>'+v.elapsedTime+'</td>';
						payload += '</tr>';
						payload += '</tbody></table></td></tr>';
					});
				}
			});
			$('.modal #table-example-modal tbody').html(payload);
			
			$('#modal_ajax').modal('show', {
				backdrop: 'true'
			});
			
			$(".spanLinks").on("click", function(e){
				e.preventDefault();
				var erName = $(".runname").html().replace(": ","");
				var tcName = $(this).parent().parent().find("td:nth-child(2)").html();		//
				var downloadType = base_url+"/executionResults/downloadTestCaseLog/"+erName+"/"+tcName;
				var ext = tcName+".log";
				var contentType = "text/plain";
				var hrefContentType = 'data:text/plain,';

				var xhr = new XMLHttpRequest();
				xhr.open('GET', downloadType, true);
				var token = "Bearer " + readCookie("TAaccess");
				xhr.setRequestHeader('Authorization', token);
				xhr.setRequestHeader('Content-type', contentType);
				xhr.onload = function(e) {
				  if (this.status == 200) {
					var anchorElement = document.createElement('a');
					anchorElement.setAttribute('download', ext);
					anchorElement.href = hrefContentType + this.response;
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
			
			$(".imageLink").on("click", function(e){
				e.preventDefault();
				$('#image_ajax').modal('show', {
					backdrop: 'true'
				});
				$('#image_ajax .modal-body').html('<div style="text-align:center;margin-top:200px;"><img src="Libraries/img/loader.GIF" style="height:50px;" /></div>');
				var erName = $(".runname").html().replace(": ","");
				var tcName = $(this).parent().parent().find("td:nth-child(2)").html();

				var downloadType = base_url+"/executionResults/downloadTestCaseSnapShot/"+erName+"/"+tcName;
				var ext = tcName+".png";
				var contentType = "image/png";
				var hrefContentType = 'data:image/png;base64,';

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
					
					$('#image_ajax .modal-body').html('<img src="'+hrefContentType + base64+'" />');
				  }
				  else {
					  statusError(this.status, this.responseText);
				  }
				};

				xhr.send();
			});
		}
	});
}

function showTestImage(url) {
    // SHOWING AJAX PRELOADER IMAGE
    jQuery('#image_ajax .modal-body').html('<div style="text-align:center;margin-top:200px;"><img src="./Libraries/img/loader.GIF" style="height:50px;" /></div>');

    // LOADING THE AJAX MODAL
    jQuery('#image_ajax').modal('show', {
        backdrop: 'true'
    });

    // SHOW AJAX RESPONSE ON REQUEST SUCCESS
    $.ajax({
        url: url,
        success: function(response) {
            jQuery('#image_ajax .modal-body').html(response);
        }
    });
}