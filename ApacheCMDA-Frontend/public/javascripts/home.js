
$(document).ready(function() {
	var btnFollowers = document.getElementById('btnFollwers');

	btnFollwers.style.cursor = 'pointer';
	btnFollwers.onclick = function() {
		$( "#post" ).css( "display", "none" );
		$( "#followers" ).css( "display", "block" );
	};
	var btnPost = document.getElementById('btnPost');

	btnPost.style.cursor = 'pointer';
	btnPost.onclick = function() {
		$( "#post" ).css( "display", "block" );
		$( "#followers" ).css( "display", "none" );
	};
});

function changeSecurity(postID, sel) {
	$.get("/network/post/changeSecurity/"+postID+"/"+sel.value, function(data, status) {
	});
}