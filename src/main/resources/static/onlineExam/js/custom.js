$(function($){setTimeout(function(){$('#content-wrapper > .row').css({opacity:1});},200);$('#nav-col').on('click','.dropdown-toggle',function(e){e.preventDefault();var $item=$(this).parent();if(!$item.hasClass('open')){$item.parent().find('.open .submenu').slideUp('fast');$item.parent().find('.open').toggleClass('open');}
$item.toggleClass('open');if($item.hasClass('open')){$item.children('.submenu').slideDown('fast');}
else{$item.children('.submenu').slideUp('fast');}});$('body').on('mouseenter','#page-wrapper.nav-small #sidebar-nav .dropdown-toggle',function(e){if($(document).width()>=992){var $item=$(this).parent();if($('body').hasClass('fixed-leftmenu')){var topPosition=$item.position().top;if((topPosition+ 4*$(this).outerHeight())>=$(window).height()){topPosition-=6*$(this).outerHeight();}
$('#nav-col-submenu').html($item.children('.submenu').clone());$('#nav-col-submenu > .submenu').css({'top':topPosition});}
$item.addClass('open');$item.children('.submenu').slideDown('fast');}});$('body').on('mouseleave','#page-wrapper.nav-small #sidebar-nav > .nav-pills > li',function(e){if($(document).width()>=992){var $item=$(this);if($item.hasClass('open')){$item.find('.open .submenu').slideUp('fast');$item.find('.open').removeClass('open');$item.children('.submenu').slideUp('fast');}
$item.removeClass('open');}});$('body').on('mouseenter','#page-wrapper.nav-small #sidebar-nav a:not(.dropdown-toggle)',function(e){if($('body').hasClass('fixed-leftmenu')){$('#nav-col-submenu').html('');}});$('body').on('mouseleave','#page-wrapper.nav-small #nav-col',function(e){if($('body').hasClass('fixed-leftmenu')){$('#nav-col-submenu').html('');}});$('#make-small-nav').click(function(e){$('#page-wrapper').toggleClass('nav-small');});$(window).smartresize(function(){if($(document).width()<=991){$('#page-wrapper').removeClass('nav-small');}});$('.mobile-search').click(function(e){e.preventDefault();$('.mobile-search').addClass('active');$('.mobile-search form input.form-control').focus();});$(document).mouseup(function(e){var container=$('.mobile-search');if(!container.is(e.target)&&container.has(e.target).length===0)
{container.removeClass('active');}});$('.fixed-leftmenu #col-left').nanoScroller({alwaysVisible:false,iOSNativeScrolling:false,preventPageScrolling:true,contentClass:'col-left-nano-content'});$("[data-toggle='tooltip']").each(function(index,el){$(el).tooltip({placement:$(this).data("placement")||'top'});});});$.fn.removeClassPrefix=function(prefix){this.each(function(i,el){var classes=el.className.split(" ").filter(function(c){return c.lastIndexOf(prefix,0)!==0;});el.className=classes.join(" ");});return this;};(function($,sr){var debounce=function(func,threshold,execAsap){var timeout;return function debounced(){var obj=this,args=arguments;function delayed(){if(!execAsap)
func.apply(obj,args);timeout=null;};if(timeout)
clearTimeout(timeout);else if(execAsap)
func.apply(obj,args);timeout=setTimeout(delayed,threshold||100);};}
jQuery.fn[sr]=function(fn){return fn?this.bind('resize',debounce(fn)):this.trigger(sr);};})(jQuery,'smartresize');


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
	$("header.navbar").load(base_url+"/onlineExam1/includes/header.html", function(responseTxt, statusTxt, xhr){
		$("aside.sidebar").load(base_url+"/onlineExam1/includes/leftNav.html", function(responseTxt, statusTxt, xhr){
			$("footer#footer").load(base_url+"/onlineExam1/includes/footer.html", function(responseTxt, statusTxt, xhr){
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
			window.location.href = base_url+"/onlineExam1/login.html";
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

$(".mainbtn input[type=checkbox]").click(function(){
  if($(this).prop("checked")==true)
    {
     $(".card").css("background-color", "Black"); 
     $("form#profileform .form-group label").css("color", "White !important");
	}
  else
    {
     $(".card").css("background-color", "White"); 
     $("form#profileform .form-group label").css("color", "Black !important");
	}
});