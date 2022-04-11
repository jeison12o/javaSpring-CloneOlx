function eliminarUsua() {
	swal({
		  title: "Esta seguro de eliminar su cuenta?",
		  text: "Al eliminar su cuenta se le borra todo lo que tiene guardado en la base de datos",
		  icon: "warning",
		  buttons: true,
		  dangerMode: true,
		}).then((ok) => {
		  if (ok) {
			  var xhr;
			  if (window.XMLHttpRequest) {
			    xhr = new XMLHttpRequest();
			  } else if (window.ActiveXObject) {
			    try {
			      xhr = new ActiveXObject('Msxml2.XMLHTTP');
			    } catch (e) {
			      try {
			        xhr = new ActiveXObject('Microsoft.XMLHTTP');
			      } 
			      catch (e) {}
			    }
			  }
			  
			  xhr.open('GET', "/user/eliminarUsuario", true);
			  xhr.send();
			  
			  xhr.onreadystatechange = function () {
				  if (xhr.status === 200) {
					  swal("Su cuenta se elimino con exito", {
					      icon: "success",
					    }).then((ok) => {
					    	if(ok) {
					    		location.href="/";
					    	}
					    });
					} else {
					}
			  }
		  } else {
		    swal("no se elimino su cuenta");
		  }
		});
};

/*
function eliminarUsua(id) {
	swal({
		  title: "Esta seguro de eliminar su cuenta?",
		  text: "Al eliminar su cuenta se le borra todo lo que tiene guardado en la base de datos",
		  icon: "warning",
		  buttons: true,
		  dangerMode: true,
		}).then((ok) => {
		  if (ok) {
			  $.ajax({
				  url:"/eliminarUsuario/"+id,
			  	  success: function() {
					  console.log(res);
				  }
			  });
		    swal("Poof! Your imaginary file has been deleted!", {
		      icon: "success",
		    }).then((ok) => {
		    	if(ok) {
		    		location.href="/";
		    	}
		    });
		  } else {
		    swal("ooooooo");
		  }
		});
};
*/