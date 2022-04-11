function eliminarAnuncio(id, idDueno) {
	swal({
		  title: "Esta seguro de eliminar su anuncio?",
		  text: "Al eliminar su cuenta se le borra todo lo que tiene guardado en la base de datos",
		  icon: "warning",
		  buttons: true,
		  dangerMode: true,
		}).then((ok) => {
		  if (ok) {
			  $.ajax({
				  url:"/user/eliminarAnuncio/"+id,
			  	  success: function() {
					  console.log(res);
				  }
			  });
		    swal("Se elimino con exito el anuncio", {
		      icon: "success",
		    }).then((ok) => {
		    	if(ok) {
		    		location.href="/user/anuncios";
		    	}
		    });
		  } else {
		    swal("ooooooo");
		  }
		});
};