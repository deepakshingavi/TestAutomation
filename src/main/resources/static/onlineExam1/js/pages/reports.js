$(document).ready(function() {
	$.ajax({
		url: base_url+"/executionResults/getAllRunnerByCompany", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(dataBrowser) {
			var appOptions = "";
			var payload = "";
			var index = 0;
			$.each(dataBrowser, function(key, value) {
				var dateArray = value.executedOn==null?"-":value.executedOn.split(".")[0].replace("T", " ");
				$pas= value.progess;
				var isDisabled = 'disabled="disabled"';
				var div = "";
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
				payload += '<td scope="col" class="bucketcheck">';
				payload += '<label class="main subCB">';
				payload += '<input type="checkbox"> ';
				payload += '<span class="geekmark"></span> ';
				payload += '</label>';
				payload += '</td>';
				payload += '<td></td>';
				payload += '<td>'+value.runName+'</td>';
				payload += '<td>'+value.executedBy+'</td>';
				payload += '<td><span id="hundred">100</span>  '+dateArray+'</td>';
				payload += '<td>';
				payload += '<img src="img/visibility-24-px.png" alt="Imgtime"  onclick="showAjaxModal('+value.runId+', \''+value.runName+'\');" title="View">  '; 
				payload += '<img src="img/refresh-24-px.png" alt="Imgrefresh" title="Retry">  ';
				payload += '<img src="img/down-arrow.png" onclick="downloadFile(\'excel\', '+value.runId+', \''+value.runName+'\');" class="Imgdownload" alt="Imgdownload" title="Download">';
				payload += '<div class="showicons" style="display: none;">';
				payload += '<img src="img/pdf-1.png" alt="Imgpdf">';
				payload += '<img src="img/excel.png" alt="Imgexcel">';
				payload += '</div> ';
				payload += '</td>';
				payload += '</tr>';
				payload += '<tr>';
				
				/*payload += '<tr>';
				payload += '<td>'+(++index)+'</td>';
				payload += '<td>'+value.runName+'</td>';
				payload += '<td>'+value.executedBy+'</td>';
				payload += '<td>';
				payload += div;
				payload += '</div>';
				payload += '<div>'+dateArray+'</div>';
				payload += '</td>';
				payload += '<td>';
				payload += '<button onclick="showAjaxModal('+value.runId+', \''+value.runName+'\');" class="btn btn-info">';
				payload += 'View';
				payload += '</button>';
				payload += '</td>';
				payload += '<td>';
				payload += '<button onclick="downloadFile(\'excel\', '+value.runId+', \''+value.runName+'\');" style="cursor:pointer;background-color: green" class="btn btn-success mar5">';
				payload += '<i class="fa fa-file-excel-o"></i>';
				payload += '</button>';
				payload += '<button onclick="downloadFile(\'pdf\', '+value.runId+', \''+value.runName+'\');" style="cursor:pointer;background-color: #E13300" class="btn btn-info">';
				payload += '<i class="fa fa-file-pdf-o"></i>';
				payload += '</button>';
				payload += '</td>';
				payload += '<td>';
				payload += '<button '+isDisabled+' onclick="runReport('+value.runId+');" class="btn btn-info">';
				payload += 'Run';
				payload += '</button>';
				payload += '</td>';
				payload += '<td>';
				payload += '<a onclick="return false;" class="table-link danger">';
				payload += '<span class="fa-stack" onclick="return checkDelete('+value.runId+');">';
				payload += '<i class="fa fa-square fa-stack-2x"></i>';
				payload += '<i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>';
				payload += '</span>';
				payload += '</a>';
				payload += '</td>';
				payload += '</tr>'; 
				appOptions += '<option value="'+value.runId+'">'+value.runName+'</option>';*/
			});
			//$("select[name=executed_testCases]").append(appOptions);			
			$("#bucketList tbody").html(payload);
			
			
			//$('#table-example').DataTable();
			/*var table = $('#table-example').dataTable({
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

			new $.fn.dataTable.FixedHeader(tableFixed);	*/		
		},
		complete: function(){
			var table = $('table#bucketList').DataTable({
				"lengthChange": false,
				"searching": false,   // Search Box will Be Disabled
				"ordering": false,    // Ordering (Sorting on Each Column)will Be Disabled
				"info": true,
			});
			$('.searchboxes').keyup(function(){
				table.search($(this).val()).draw() ;
			});
			$(".searchtext").click(function(e){
				e.preventDefault();
			    $("#bucketList thead tr:last-child").toggle();
			});

			$(".mainCB input[type=checkbox]").click(function(){
				if($(this).prop("checked")==true)
				{	$(this).closest(".selectdiv1").find(".subCB input[type=checkbox]").prop("checked", true);	}
				else
				{	$(this).closest(".selectdiv1").find(".subCB input[type=checkbox]").prop("checked", false);	}
			});

			/*---Jquery for delete row---*/

			$("#deleteRow").click(function(e){
				e.preventDefault();
				var str = '';
				$("table tbody .subCB input:checked").each(function(){
				if(str!='')
					str += ", "+$(this).closest("tr").find("td:nth-child(3)").text();
				else
					str += $(this).closest("tr").find("td:nth-child(3)").text();
				});

				$("#deletedItem").text(str);
				$("#myModal2").modal();
			});

			$(document).on("click", "#yesbtn", function(){
				$("table tbody .subCB input:checked").each(function(){
					$(this).closest("tr").remove();
				});	    
			});
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
function downloadFile(type, id, tcName) {
	var downloadType = "";
	var ext = tcName+".xlsx";
	var contentType = "application/octet-stream";
	var hrefContentType = 'data:application/octet-stream;base64,';
	if(type.toUpperCase()=="EXCEL"){
		downloadType = "getRunnerDetailsExcel/"+id;
		downloadType = base_url+"/executionResults/getRunnerDetailsExcel/"+id;
	}
	else {
		downloadType = base_url+"/executionResults/getRunnerDetailsPdf/"+id;
		contentType = "application/pdf";
		hrefContentType = 'data:application/pdf;base64,';
		ext = tcName+".pdf";
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


function checkDelete(id) {
    var chk = confirm("Are You Sure To Delete This !");
    if (chk) {
		$.ajax({
			url: base_url+"/executionResults/deleteByRunnedId/"+id,
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

function showAjaxModal(id, name="") {
	var totalTC = totalPass = totalFail = totalQueue = 0;
    $.ajax({
		url: base_url+"/executionResults/getRunnerDetailsFull/"+id, 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success:function(data) {
			$(".panel-title b.runname").html("<p><strong>Execution Result : "+name+"</strong>/<p>");
			var payload = "";
			$.each(data, function(key, value){
				totalTC += 1;
				var logLink = "";
				payload += '<tr>';
				payload += '<td scope="col">'+(key+1)+'</td>';
				payload += '<td>'+value.testcases.testcaseName+'</td>';
				if(value.result.toUpperCase() == "FAILED"){
					totalFail += 1;
					payload += '<td class="failed"><b class="imageLink" style="cursor:pointer">Failed</b></td>';
					logLink = '<p class="spanLinks">Download Log</p>';
				} 
				else if(value.result.toUpperCase() == "PASSED"){
					totalPass += 1;
					payload += '<td style="color:green"><b>Passed</b></td>';
					logLink = '<p class="spanLinks">Download Log</p>';
				}
				else if((value.result.toUpperCase() == "QUEUED") || (value.result.toUpperCase() == "SUBMITED")){
					totalQueue += 1;
					payload += '<td style="color:orange"><b>Queued</b></td>';
				}
				// COvered below check in above case
				/*else if(value.result.toUpperCase() == "SUBMITED"){
					payload += '<td style="color:blue"><b>Submited</b></td>';
				}*/
				payload += '<td>'+value.elapsedTime+'</td>';
				payload += '<td class="selectmodeldiv"><img src="img/down-arrow.png" alt="Imgdownload">'+logLink+'<i class="fa fa-angle-down showdet" aria-hidden="true"></i><div class="submodeldiv" style="display: none;">';
				payload += '</td>';
				if(value.testcaseExeDetailList.length==0 || value.testcaseExeDetailList==null){
					payload += '<td class="accordion-toggle collapsed"  data-toggle="collapse" data-target="#collapse'+(key+1)+'">'+logLink+'</td>';
				}
				else {
					payload += '<td class="accordion-toggle collapsed"  data-toggle="collapse" data-target="#collapse'+(key+1)+'"><span></span>'+logLink+'</td>';
				}
				payload += '</tr>';
				if(value.testcaseExeDetailList.length > 0){
					//payload += '<tr id="collapse'+(key+1)+'" class="collapse"><td class="no-border" colspan="5"><table class="table table-hover"><thead><th>Row No</th><th>Result</th><th>Output</th><th>Reason</th><th>Elapse Time</th></thead><tbody>';
					payload += '<table class="table table-hover"><thead><tr><th scope="col">ROW NO.</th><th scope="col">RESULT</th><th scope="col">OUTPUT</th><th scope="col">REASON</th><th scope="col">ELAPSE TIME</th></tr></thead><tbody>';
					$.each(value.testcaseExeDetailList, function(k,v) {
						var output, reason, result;
						if(v.result=="" || v.result==undefined)
						{
							result = "-";
						}
						else {
							result = v.result;
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
						
						payload += '<tr>';
						payload += '<td>'+(key+1)+'.'+(k+1)+'</td>';
						//payload += '<td>'+result+'</td>';
						if(result.toUpperCase() == "FAILED"){
							payload += '<td style="color:red"><b style="cursor:pointer">Failed</b></td>';
						} 
						else if(result.toUpperCase() == "PASSED"){
							payload += '<td style="color:green"><b>Passed</b></td>';
						}
						else if(result.toUpperCase() == "QUEUED"){
							payload += '<td style="color:orange"><b>Queued</b></td>';
						}
						payload += '<td>'+output+' </td>';
						payload += '<td>'+reason+' </td>';
						payload += '<td>'+v.elapsedTime+'</td>';
						payload += '</tr>';						
					});
					payload += '</tbody></table></td></tr>';
				}				
			});
			$(".totalTC").html(totalTC);
			$(".totalPass").html(totalPass);
			$(".totalFail").html(totalFail);
			$(".totalQueue").html(totalQueue);
			$('.modal .Reportmodel tbody').html(payload);
			new Chart(document.getElementById("pie-chart"), {
				type: 'pie',
				data: {
				  // labels: ["TOTAL", "PASSED", "FAILED", "QUEQUD"],
				  datasets: [{
					// label: "Population (millions)",
					backgroundColor: ["#2e8009", "#dbaf11","#bb2424"],
					data: [totalPass,totalQueue,totalFail]
				  }]
				}
			});
			$('#myModal').modal('show', {
				backdrop: 'true'
			});
		},
		complete: function(){
//START OF HTML CODE
			/*---Jquery for search table column---*/

			
            
            /*---Jquery for icon pdf excel toggle---*/

            $(".Imgdownload").click(function(){
                 $(this).closest('td').find(".showicons").toggle();
            });

            /*---Jquery for toggle section on click angle-down icon---*/

			$(".selectmodeldiv i.showdet").click(function(){
				$(".submodeldiv").width($("table.reportmodeltable").width());
				$(this).closest(".selectmodeldiv").find(".submodeldiv").slideToggle();
				$(this).toggleClass("caret-rev");
			});
//END OF HTML CODE

			
			
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
					  statusError(this.status, this.responseText);
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
					  statusError(this.status, this.responseText);;
				  }
				};

				xhr.send();
			});
			
		}
	});
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
/*
function showTestImage() {
	
	///executionResults/downloadTestCaseSnapShot/{executionResultsName}/{testcaseName}
	
	var erName = $(".runname").html().replace(": ","");
	var tcName = $(this).parent().parent().find("td:nth-child(2)").html();		//
	$.ajax({
		url: base_url+"/executionResults/downloadTestCaseSnapShot/"+erName+"/"+tcName, 
		async: true,
		mimeType: "multipart/form-data",
		processData: false,
		responseType: "arraybuffer",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
			xhr.setRequestHeader('Content-type', "application/octet-stream");
		},
		success: function(data) {
			saveAs(new Blob([data], { type: "data:application/octet-stream" }), tcName+'.jpeg');
		}
	});
	
	// LOADING THE AJAX MODAL
    

    // SHOW AJAX RESPONSE ON REQUEST SUCCESS
    $.ajax({
        url: url,
        success: function(response) {
            jQuery('#image_ajax .modal-body').html(response);
        }
    });
}
*/