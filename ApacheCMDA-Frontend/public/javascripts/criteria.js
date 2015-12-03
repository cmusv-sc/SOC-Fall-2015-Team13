
$(document).ready(loadKeyword());

function loadKeyword(){
	$.ajax({
		url: '/network/search/keyword/get',
		type: 'get',
		dataType: 'json',
		success: function (data) {
			appendKeyword(JSON.stringify(data))
		}
	});
}

function appendKeyword(x) {
	var keywordInput = document.getElementById('srch-term1')
	var data = $.parseJSON(x);
	var arry = new Array();
	for (var i = 0; i < data.length; i++) {
		arry[i]= data[i].keyword ;
	}
	new Awesomplete(keywordInput, {list: arry});
}