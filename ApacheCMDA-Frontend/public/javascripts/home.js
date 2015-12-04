
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
	
	var ckb = document.getElementById('cbox1');
	if(ckb!=null){
	ckb.onclick = function() {
		if ($( "#cbox1" ).is(":checked")){
		$( "#map" ).css( "display", "block" );
		document.getElementById("includeL").value = "yes";
		}
		else{
			$('#map').css("display", "none");
			document.getElementById("includeL").value = "no";
		}
	};
	}
});

function changeSecurity(postID, sel) {
	$.get("/network/post/changeSecurity/"+postID+"/"+sel.value, function(data, status) {
	});
}

function initMap() {
	var ctlMap = document.getElementById('map');
	if(ctlMap!=null){
	  var map = new google.maps.Map(document.getElementById('map'), {
	    center: {lat: -34.397, lng: 150.644},
	    zoom: 6
	  });
	  var infoWindow = new google.maps.InfoWindow({map: map});

	  // Try HTML5 geolocation.
	  if (navigator.geolocation) {
		  var geocoder = new google.maps.Geocoder(); 
	    navigator.geolocation.getCurrentPosition(function(position) {
	      var pos = {
	        lat: position.coords.latitude,
	        lng: position.coords.longitude
	      };

	     
	      infoWindow.setPosition(pos);
	      
	      var latlng = new google.maps.LatLng(pos.lat, pos.lng);
	      geocoder.geocode({'latLng': latlng}, function(results, status) {
	        if (status == google.maps.GeocoderStatus.OK) {
	        console.log(results);
	          if (results[1]) {
	          var indice=0;
	          for (var j=0; j<results.length; j++)
	          {
	              if (results[j].types[0]=='locality'||results[j].types[0]=='route' )
	                  {
	                      indice=j;
	                      break;
	                  }
	              }
	          //alert('The good number is: '+j);
	          console.log(results[j]);
	          for (var i=0; i<results[j].address_components.length; i++)
	              {
	                  if (results[j].address_components[i].types[0] == "locality") {
	                          //this is the object you are looking for
	                          city = results[j].address_components[i];
	                      }
	                  if (results[j].address_components[i].types[0] == "administrative_area_level_1") {
	                          //this is the object you are looking for
	                          region = results[j].address_components[i];
	                      }
	                  if (results[j].address_components[i].types[0] == "country") {
	                          //this is the object you are looking for
	                          country = results[j].address_components[i];
	                      }
	              }

	              //city data
	          infoWindow.setContent(city.long_name + ", " + region.long_name + ", " + country.short_name);
	          document.getElementById("location").value = city.long_name + ", " + region.long_name + ", " + country.short_name;

	              } else {
	                alert("No results found");
	              }
	          //}
	        } else {
	          alert("Geocoder failed due to: " + status);
	        }
	      });
	      
	      map.setCenter(pos);
	    }, function() {
	      handleLocationError(true, infoWindow, map.getCenter());
	    });
	  } else {
		    // Browser doesn't support Geolocation
		    handleLocationError(false, infoWindow, map.getCenter());
		  }
		}
}
		function handleLocationError(browserHasGeolocation, infoWindow, pos) {
		  infoWindow.setPosition(pos);
		  infoWindow.setContent(browserHasGeolocation ?
		                        'Error: The Geolocation service failed.' :
		                        'Error: Your browser doesn\'t support geolocation.');
		}

		  
		    
	    
