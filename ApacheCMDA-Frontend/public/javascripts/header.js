
$(document).ready(load());

function load(){
	$.ajax({
		url: '/network/search/keyword/get',
		type: 'get',
		dataType: 'json',
		success: function (data) {
			appendOption(JSON.stringify(data))
		}
	});
}

function appendOption(x) {
	var input = document.getElementById('srch-term')
	var data = $.parseJSON(x);
	var arry = new Array();
	for (var i = 0; i < data.length; i++) {
		arry[i]= data[i].keyword ;
	}
	new Awesomplete(input, {list: arry});
}