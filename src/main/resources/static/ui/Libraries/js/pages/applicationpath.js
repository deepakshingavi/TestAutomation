//url: "../companyEnvironUrl/findAllByCompanyId",
$(document).ready(function() {
    $.ajax({
		url: base_url+"/company/all", 
		method: "get",
		beforeSend: function (xhr) {
			xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
		},
		success:function(data) {
			var payload = "";
			$.each(data, function(index, value) {
				payload += '<option value="'+value.companyId+'">'+value.companyName+'</option>';
			});
			$("select[name=company_id]").append(payload);
		}
	}).complete(function( data ) {
		$("select[name=company_id]").on('change', function() {
			if(this.value!=""){
				$.ajax({
					url: base_url+"/company/applicationPath/"+this.value, 
					method: "get",
					beforeSend: function (xhr) {
						xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
					},
					success:function(data) {
						$('input[name="selenium_home"]:visible').val(data.seleniumHome);
						$('input[name="testdata_home"]:visible').val(data.testDataHome);
						$('input[name="screenshot_home"]:visible').val(data.screenShotsHome);
						$('input[name="batch_home"]:visible').val(data.batchFileHome);
						$('input[name="logs_home"]:visible').val(data.logsHome);
					}
				});
			}
		});
	});
	
	$("button[name=submitAppPath]").click(function(){
		$('#res span').hide();
		$company_id = $('select[name=company_id]:visible').val();
		$companyName = $('select[name=company_id]:visible option:selected').text();
		$selenium_home = $('input[name=selenium_home]:visible').val();
		$testdata_home = $('input[name=testdata_home]:visible').val();
		$screenshot_home = $('input[name=screenshot_home]:visible').val();
		$batch_home = $('input[name=batch_home]:visible').val();
		$logs_home = $('input[name=logs_home]:visible').val();
		if($company_id == '')
		{
		   showError();
		   return false;
		}
		
		var dataObj = {};
		dataObj["seleniumHome"]= $selenium_home;
		dataObj["companyId"]= $company_id;
		dataObj["companyName"]= $companyName;
		dataObj["testDataHome"]= $testdata_home;
		dataObj["screenShotsHome"]= $screenshot_home;
		dataObj["batchFileHome"]= $batch_home;
		dataObj["logsHome"]= $logs_home;
		//**************************HARD CODED VALUES***************************//
		$.ajax({
			type: 'POST',
			data: JSON.stringify(dataObj),
			contentType: 'application/json',
			dataType: 'json',
			url: base_url+"/company/save/applicationPath",
			beforeSend: function (xhr) {
				xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
			},
			success: function(msg){
				$('#res span').show();
			}
		});
	});
});