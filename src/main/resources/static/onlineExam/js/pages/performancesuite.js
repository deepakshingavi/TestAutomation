$(document).ready(function() {
    //executionResults/getPerfResults
	$.ajax({
		url: base_url+"/executionResults/getPerfResults", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success: function(res) {
			//testcaseRunInfoList
			var data = res;
			var appOptions = "";
			$.each(res, function(key,value){
				appOptions += '<option value="'+key+'">'+value.runnerName+'</option>';
			});
			$("select[name=suite1]").append(appOptions);
			
			$("select[name=suite1]").on("change", function(){ 
				 if(this.value != "") {
					$("select[name=suite2]").html($('option', this).not(':selected').clone());
				 }
			});
			
			$("#showid").click(function(){
				var dataObj = {};
				var arr = {};
				var suite1 = $("select[name=suite1]").val();
				var suite2 = $("select[name=suite2]").val();				
				if(suite1 == "" || suite2 == "") {
					showError();
					return false;
				}
				$(".suite1Val").html(suite1);
				$(".suite2Val").html(suite2);
				var payload = "";
				//var dataObj = {};
				$.each(data[suite1].testcaseRunInfoList, function(key,val){
					if(dataObj[val.testcaseName] == undefined) {
						dataObj[val.testcaseName] = {};
						dataObj[val.testcaseName]["infoList"] = [];
						dataObj[val.testcaseName]["infoList"].push({"exName": data[suite1].runnerName, "time": val.elapsedTime, "tcName": val.testcaseName});
					}
					else {
						dataObj[val.testcaseName]["infoList"].push({"exName": data[suite1].runnerName, "time": val.elapsedTime, "tcName": val.testcaseName});
					}
					payload += '<tr class="'+val.testcasesId+'">';
					payload += '<td>'+val.testcaseName+'</td>';
					payload += '<td>'+val.elapsedTime+'</td>';
					payload += '</tr>';
				});
				$("#suite1Tbl tbody").html(payload);
				//$("#suite1Tbl").show();
				var payload1 = "";
				$.each(data[suite2].testcaseRunInfoList, function(key,val){
					if(dataObj[val.testcaseName]== undefined) {
						dataObj[val.testcaseName] = {};
						dataObj[val.testcaseName]["infoList"] = [];
						dataObj[val.testcaseName]["infoList"].push({"exName": data[suite2].runnerName, "time": val.elapsedTime, "tcName": val.testcaseName});
					}
					else {
						dataObj[val.testcaseName]["infoList"].push({"exName": data[suite2].runnerName, "time": val.elapsedTime, "tcName": val.testcaseName});
					}
					payload1 += '<tr class="'+val.testcasesId+'">';
					payload1 += '<td>'+val.testcaseName+'</td>';
					payload1 += '<td>'+val.elapsedTime+'</td>';
					payload1 += '</tr>';
				});
				$("#suite2Tbl tbody").html(payload1);
				//$("#suite2Tbl").show();
				var payload2 = "";
				var index = 0;
				
				
					
				$.each(dataObj, function(k,v){
					payload2 += '<tr>';
					//payload2 += '<td class="col-md-3">'+k+'</td>';
					
					if(v["infoList"].length<2) {
						payload2 += '<td class="col-md-6"></td>';
					}
					else {
						payload2 += '<td class="col-md-6"><div id="container'+index+'"></div></td>';						
					}
					payload2 += '<td class="col-md-3">';
					payload2 += '<table><tbody>';
					if(v["infoList"].length>1) {
						arr[index] = {};
						arr[index]["categories"] = [];
						arr[index]["time"] = [];
						arr[index]["tcName"] = [];
					}
					$.each(v["infoList"], function(i, row){	
						if(v["infoList"].length>1) {
							arr[index]["categories"].push(row.exName);
							arr[index]["time"].push(row.time);
							arr[index]["tcName"].push(row.tcName);
						}
						var nullChk = "";
						if(row.time == null)
						{
							nullChk = "Not Executed";
						}
						else {
							nullChk = row.time + " Sec";
						}
						payload2 += '<tr><td>'+row.exName+'</td>';
						payload2 += '<td>'+nullChk+'</td>';
						payload2 += '</tr>';
					});
					payload2 += '</tbody></table>';
					payload2 += '</td>';
					payload2 += '</tr>';
					if(v["infoList"].length>1) {
						index++;
					}
				});
				$("#chartTbl tbody").html(payload2);
				$("#chartTbl").show(function(){
					for(var i = 0; i<=index; i++){
						if(arr[i] != undefined) {
							Highcharts.chart('container'+i, {
								title: {
									text: arr[i]["tcName"][0]
								},
								yAxis: {
									title: {
										text: 'Elapsed Time'
									}
								},
								xAxis: {
									categories: arr[i]["categories"]
								},
								series: [{
									name: ' ',
									data: arr[i]["time"]
								}],
								responsive: {
									rules: [{
										condition: {
											maxWidth: 100
										},
										chartOptions: {
											legend: {
												layout: 'horizontal',
												align: 'center',
												verticalAlign: 'bottom'
											}
										}
									}]
								}
							});
						}
					}
				});
			});
		}
	});
});



/*


Highcharts.chart('container', {

    xAxis: {
        categories: ['Jan', 'Feb']
    },
    series: [{
        name: ' ',
        data: [43934, 52503]
    }],

    responsive: {
        rules: [{
            condition: {
                maxWidth: 100
            },
            chartOptions: {
                legend: {
                    layout: 'horizontal',
                    align: 'center',
                    verticalAlign: 'bottom'
                }
            }
        }]
    }

});


*/