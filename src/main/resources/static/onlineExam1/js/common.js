
//GET BASE URL
var l = window.location;
var base_url = l.protocol + "//" + l.host + "/" + l.pathname.split('/')[1];
var statuscode = {
				403: function() {	
				   // Only if your server returns a 403 status code can it come in this block. :-)
					$('#res').html("<h5><span style='color:red;text-transform:capitalize;font-size:14px'>Invalid login Details.</span></h5>");
				},
				401: function() {
				   // Only if your server returns a 403 status code can it come in this block. :-)
					$('#res').html("<h5><span style='color:green;text-transform:capitalize;font-size:14px'>Booh! You are not authorized..</span></h5>");
				}
			};
var successMsg = "Submitted Successfully";
var errMsg = "* This field is mandatory";
var pwdMsg = "* Passwords do not match";

function showError(pwd=0){
	var msg = errMsg;
	if(pwd){
		$(".pwd").html(pwdMsg).show();
	}
	else {
		$.each($('input,textarea,select').filter('[required]:visible'), function(k,v){
			if($(this).val()=="") { 
				$(this).parent().find("span.errmsg").html(msg).show();
			}
			else {
				$(this).parent().find("span.errmsg").hide();
			}
		});
	}
}
// Cookies
function createCookie(name, value, minutes) {
	if (minutes) {
		var date = new Date();
		date.setTime(date.getTime() + (minutes * 60 * 1000));
		var expires = "; expires=" + date.toGMTString();
	}
	else var expires = "";               

	document.cookie = name + "=" + value + expires + "; path=/";
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') c = c.substring(1, c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
	}
	return null;
}
function replaceAll(str, replacing, replacewith){
	var i = 0, strLength = str.length;
	for(i; i < strLength; i++) {
		str = str.replace(replacing, replacewith);
	}
	return str;
}

function eraseCookie(name) {
	createCookie(name, "", -1);
}
$(document).ready(function() {
	$("header.navbar").load(base_url+"/ui/includes/header.html", function(responseTxt, statusTxt, xhr){
		$("div#nav-col").load(base_url+"/ui/includes/leftNav.html", function(responseTxt, statusTxt, xhr){
			$("footer#footer-bar").load(base_url+"/ui/includes/footer.html", function(responseTxt, statusTxt, xhr){
				if(readCookie("TAurole")!=null){
					$("."+ readCookie("TAurole").toLowerCase()).show();
				}
				$("span.hidden-xs").html(readCookie("TAuname").toUpperCase());
			});
		});
	});
	$.ajaxSetup({
		cache: false,
        error: function (x, status, error) {
            if (x.status == 403) {
                alert("You are not authorized!");
                //window.location.href = "login.html?msg=notauth";
            }
			else if (x.status == 401) {
                alert("You are not authenticated!");
                window.location.href = "login.html?msg=notauth";
            }
			else if (x.status == 409) {
                alert("Error: "+ x.responseText);
                //window.location.href = "login.html?msg=notauth";
            }
			else if (x.status == 500) {
                alert("Error: Serverside error");
                //window.location.href = "login.html?msg=notauth";
            }
			else if (x.status == "") {
            }
			else if (x.status == 304) {
            }
            else {
                //alert("An error occurred: " + status + "nError: " + error);
				alert("Booh! It seems some error on page. Please contact to administrator");
				//return false;
            }
        }
    });
	
	$('header.navbar').on('click', ".hidden-xxs", function(e) {
        e.preventDefault();
		$.ajax({
			url: base_url+"/secured/users/logout",
			type: 'DELETE',
			beforeSend: function (xhr) {
				xhr.setRequestHeader('Authorization', "Bearer " + readCookie("TAaccess"));
			},
			success: function(data){
			}
		}).always(function() {
			eraseCookie("TAaccess");
			eraseCookie("TAuid");
			eraseCookie("TAuname");
			eraseCookie("TAurole");
			window.location.href = base_url+"/ui/login.html";
		});
	});
});
function statusError(statusCode, responseText="") {
	if (statusCode == 403) {
		alert("You are not authorized!");
		//window.location.href = "login.html?msg=notauth";
	}
	else if (statusCode == 401) {
		alert("You are not authenticated!");
		window.location.href = "login.html?msg=notauth";
	}
	else if (statusCode == 409) {
		alert("Error: "+ responseText);
		//window.location.href = "login.html?msg=notauth";
	}
	else if (statusCode == 500) {
		alert("Error: Serverside error");
		//window.location.href = "login.html?msg=notauth";
	}
	else if (statusCode == "") {
	}
	else if (statusCode == 304) {
	}
	else {
		//alert("An error occurred: " + status + "nError: " + error);
		alert("Booh! It seems some error on page. Please contact to administrator");
		//return false;
	}
}