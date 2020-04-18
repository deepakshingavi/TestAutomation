function showAjaxModal(url)
{
	// SHOWING AJAX PRELOADER IMAGE
	jQuery('#modal_ajax .modal-body').html('<div style="text-align:center;margin-top:200px;"><img src="./Libraries/img/loader.GIF" style="height:50px;" /></div>');
	
	// LOADING THE AJAX MODAL
	jQuery('#modal_ajax').modal('show', {backdrop: 'true'});
	
	// SHOW AJAX RESPONSE ON REQUEST SUCCESS
	$.ajax({
		url: url,
		success: function(response)
		{
			jQuery('#modal_ajax .modal-body').html(response);
		}
	});
}
function showTestImage(url)
{
	// SHOWING AJAX PRELOADER IMAGE
	jQuery('#image_ajax .modal-body').html('<div style="text-align:center;margin-top:200px;"><img src="./Libraries/img/loader.GIF" style="height:50px;" /></div>');
	
	// LOADING THE AJAX MODAL
	jQuery('#image_ajax').modal('show', {backdrop: 'true'});
	
	// SHOW AJAX RESPONSE ON REQUEST SUCCESS
	$.ajax({
		url: url,
		success: function(response)
		{
			jQuery('#image_ajax .modal-body').html(response);
		}
	});
}
function confirm_modal(delete_url , post_refresh_url)
{
	$('#preloader-delete').html('');
	jQuery('#modal_delete').modal('show', {backdrop: 'static'});
	document.getElementById('delete_link').setAttribute("onClick" , "delete_data('" + delete_url + "' , '" + post_refresh_url + "')" );
	document.getElementById('delete_link').focus();
}
$(document).ready(function() {
    ///TestApps/dashboard/
/*	$.ajax({
		url: base_url+"/dashboard/getTopTenFailedTestCases", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(res) {
			var payloadTest = "";
			$.each(res, function(key,val){
				if(val.testName >= 5) {
					payloadTest += "<tr><td><span style='color: red; font-size:14px;'>"+val.testName+"</span></td><td>"+val.failedCount+"</td></tr>";
				}
				else {
					payloadTest += "<tr><td><span style='color: #d4bb2e; font-size:14px;'>"+val.testName+"</span></td><td>"+val.failedCount+"</td></tr>";
				}
			});
			$("table.failedTC tbody").html(payloadTest);
		}
	});
*/
	$.ajax({
		url: base_url+"/dashboard/", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(res) {
			var testDetails = [], executionSummary = [], payloadTest = "";
			$.each(res.appTestCaseInfoList, function(key,val){
				testDetails.push([val.applicationName, val.testCaseCount]);
				payloadTest += "<tr><td><h5 style='color: #d4bb2e'>"+val.applicationName+"</h5></td><td>"+val.testCaseCount+"</td></tr>";
			});
			$("table.testDetails tbody").html(payloadTest);
			if(res.latestExecutionSummary != null) {
				executionSummary.push(["Passed", res.latestExecutionSummary.passedTestCasesCount]);
				executionSummary.push(["Failed", res.latestExecutionSummary.failedTestCasesCount]);
				var dateVal = "";
				if(res.latestExecutionSummary.executedOn != null) {
					var dateArr = res.latestExecutionSummary.executedOn.split(".");
					dateVal = dateArr[0].replace("T", " ");
				}
				$("table.exeSumm1 tbody tr:nth-child(1) td:nth-child(2)").html(res.latestExecutionSummary.runName);
				$("table.exeSumm1 tbody tr:nth-child(2) td:nth-child(2)").html(res.latestExecutionSummary.executedBy);
				$("table.exeSumm1 tbody tr:nth-child(3) td:nth-child(2)").html(dateVal);
				$("table.exeSumm2 tbody tr:nth-child(1) td:nth-child(2)").html(res.latestExecutionSummary.testCasesExecutedCount);
				$("table.exeSumm2 tbody tr:nth-child(2) td:nth-child(2)").html(res.latestExecutionSummary.passedTestCasesCount);
				$("table.exeSumm2 tbody tr:nth-child(3) td:nth-child(2)").html(res.latestExecutionSummary.failedTestCasesCount);
			}
			Highcharts.chart('highchartdiv', {
			chart: {
			   type: 'pie',
			   options3d: {
				   enabled: true,
				   alpha: 45,
				   beta: 0
			   }
			},
			title: {
			   text: ""
			},
			tooltip: {
			   pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
			},
			plotOptions: {
			   pie: {
				   allowPointSelect: true,
				   cursor: 'pointer',
				   depth: 35,
				   dataLabels: {
					   enabled: true,
					   format: '{point.name}'
				   }
			   }
			},
					credits: {
			 enabled: false
			},
			series: [{
			   type: 'pie',
			   name: 'Percentage',
			   data: testDetails
			}]
			});


			Highcharts.chart('highchartdiv1', {
			chart: {
			   type: 'pie',
			   options3d: {
				   enabled: true,
				   alpha: 40,
				   beta: 0
			   }
			},
			title: {
			   text: ""
			},
			tooltip: {
			   pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
			},
			plotOptions: {
			   pie: {
				   allowPointSelect: true,
				   cursor: 'pointer',
				   depth: 40,
				   dataLabels: {
					   enabled: true,
					   format: '{point.name}'
				   }
			   }
			},
					credits: {
			 enabled: false
			},
			series: [{
			   type: 'pie',
			   name: 'Percentage',
			   data: executionSummary
			}]
			});



			//CHARTS
			function gd(year, day, month) {
			return new Date(year, month - 1, day).getTime();
			}



			/* SPARKLINE - graph in header */
			var orderValues = [10,8,5,7,4,4,3,8,0,7,10,6,5,4,3,6,8,9];

			$('.spark-orders').sparkline(orderValues, {
			type: 'bar', 
			barColor: '#ced9e2',
			height: 25,
			barWidth: 6
			});

			var revenuesValues = [8,3,2,6,4,9,1,10,8,2,5,8,6,9,3,4,2,3,7];

			$('.spark-revenues').sparkline(revenuesValues, {
			type: 'bar', 
			barColor: '#ced9e2',
			height: 25,
			barWidth: 6
			});

			/* ANIMATED WEATHER */
			var skycons = new Skycons({"color": "#d4bb2e"});
			// on Android, a nasty hack is needed: {"resizeClear": true}

			// you can add a canvas by it's ID...
			skycons.add("current-weather", Skycons.SNOW);

			// start animation!
			skycons.play();
		}
	});
	

});