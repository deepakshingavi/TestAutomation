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
					url: base_url+"/company/emailSetting/"+this.value, 
					method: "get",
					beforeSend: function (xhr) {
						xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
					},
					success:function(data) {
						$('input[name="hostname"]:visible').val(data.hostName);
						$('input[name="portnumber"]:visible').val(data.port);
						$('input[name="username"]:visible').val(data.username);
						if(data.username!=""){
							$('input[name="username"]:visible').attr("readonly", true);
						}
						$('input[name="email"]:visible').val(data.email);
						$('input[name="password"]:visible').val(data.password);
						$('input[name="securityprotocol"]:visible').val(data.secuirityProtocol);
					}
				});
			}
		});
	});
	
	$("button[name=submitEmail]").click(function(){
		$('#res span').hide();
		$company_id = $('select[name=company_id]:visible').val();
		$hostname = $('input[name=hostname]:visible').val();
		$email = $('input[name=email]:visible').val();
		$portnumber = $('input[name=portnumber]:visible').val();
		$username = $('input[name=username]:visible').val();
		$password = $('input[name=password]:visible').val();
		$securityprotocol = $('input[name=securityprotocol]:visible').val();
		if($company_id == '')
		{
		   showError();
		   return false;
		}
		
		var dataObj = {};
		dataObj["hostName"]= $hostname;
		dataObj["companyId"]= $company_id;
		dataObj["port"]= $portnumber;
		dataObj["email"]= $email;
		dataObj["username"]= $username;
		dataObj["password"]= $password;
		dataObj["secuirityProtocol"]= $securityprotocol;
		//**************************HARD CODED VALUES***************************//
		
		$.ajax({
			type: 'POST',
			data: JSON.stringify(dataObj),
			contentType: 'application/json',
			dataType: 'json',
			url: base_url+"/company/save/emailSetting",
			beforeSend: function (xhr) {
				xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
			},
			success: function(msg){
				$('#res span').show();
			}
		});
	});
});