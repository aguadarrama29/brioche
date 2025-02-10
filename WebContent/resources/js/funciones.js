function playError() {
	var sound = new Audio("../../resources/sounds/error.wav");
	sound.play();
}

function playNotify() {
	var sound = new Audio("../../resources/sounds/solemn.mp3");
	sound.play();
}

function numerosEnteros(evt) {
	var theEvent = evt || window.event;
	var key = theEvent.which;
	if (key < 48 || key > 57) {
		if (key != 8) {
			theEvent.returnValue = false;
			// playError();
			if (theEvent.preventDefault)
				theEvent.preventDefault();
		}
	}
}

function soloNumeros(e) {
	tecla = (document.all) ? e.keyCode : e.which;
	if (tecla == 8)
		return true;
	patron = /\d/; // Solo acepta números
	te = String.fromCharCode(tecla);
	return patron.test(te);
}

// numeros decimales
function SoloNumeroDecimal(e, field) {
	key = e.keyCode ? e.keyCode : e.which
	// backspace
	if (key == 8)
		return true

		// 0-9 a partir del .decimal
	if (field.value != "") {
		if ((field.value.indexOf(".")) > 0) {
			// si tiene un punto valida dos digitos en la parte decimal
			if (key > 47 && key < 58) {
				if (field.value == "")
					return true
					// regexp = /[0-9]{1,10}[\.][0-9]{1,3}$/
				regexp = /[0-9]{2}$/
				return !(regexp.test(field.value))
			}
		}
	}
	// 0-9
	if (key > 47 && key < 58) {
		if (field.value == "")
			return true
			// cuantos decimales antes del punto lo que esta en las llaves
		regexp = /[0-9]{5}/
		return !(regexp.test(field.value))
	}
	// .
	if (key == 46) {
		if (field.value == "")
			return false
		regexp = /^[0-9]+$/
		return regexp.test(field.value)
	}
	// other key
	return false
}

function metodo() {
	document.getElementById('formApplet:pk').value = appletFE.panel.pkByte;
	document.getElementById('formApplet:cer').value = appletFE.panel.cerByte;
}

function metodo2() {
	document.getElementById('formApplet2:pk').value = appletFE.panel.pkByte;
	document.getElementById('formApplet2:cer').value = appletFE.panel.cerByte;
}

function delayFirma() {

}

function desactivaBtn() {
	document.getElementById('divBtn').style.display = "none";
}

function activaBtn() {
	document.getElementById('divBtn').style.display = "block";
}

function delay1() {

}

// Firma JS

function procesaFirma() {

	firmar(function(error_code) {

		if (error_code == 0) {

			validaBean();

		} else {

			PF('btnFirmarDoc').disable();

		}

	});

}

function procesaFirmaM() {

	firmarM(function(error_code) {

		if (error_code == 0) {

			validaBeanM();

		} else {

			PF('btnFirmarDocM').disable();

		}

	});

}

function firmar(callback) {

	muestraProceso();

	var passUser = document.getElementById('pwdLlavePriv').value;
	var llavePrivadaUser = document.getElementById('fileInput');
	var certificadoUser = document.getElementById('fileInputCer');

	var readerCertificado = new FileReader();

	readerCertificado.onload = function(e) {

		document.getElementById('formVF:certi').value = e.target.result
				.split("base64,")[1];

	};

	readerCertificado.readAsDataURL(certificadoUser.files[0]);

	validarFiel(llavePrivadaUser, passUser, certificadoUser, function(
			error_code, certificado) {

		if (error_code != 0) {
			callback(1)
		} else {
			callback(0)
		}

	});

}

function firmarM(callback) {

	muestraProceso();

	var passUser = document.getElementById('pwdLlavePrivM').value;
	var llavePrivadaUser = document.getElementById('fileInputM');
	var certificadoUser = document.getElementById('fileInputCerM');

	var readerCertificado = new FileReader();

	readerCertificado.onload = function(e) {

		document.getElementById('formVFM:certiM').value = e.target.result
				.split("base64,")[1];

	};

	readerCertificado.readAsDataURL(certificadoUser.files[0]);

	validarFiel(llavePrivadaUser, passUser, certificadoUser, function(
			error_code, certificado) {

		if (error_code != 0) {
			callback(1)
		} else {
			callback(0)
		}

	});

}

function procesaFirmaXML() {

	muestraProceso();

	var passUser = document.getElementById('pwdLlavePriv').value;
	var llavePrivadaUser = document.getElementById('fileInput');

	leeLlavePrivada(llavePrivadaUser, passUser, function(error_code, rsakey) {

		firmarBean();

	});

}

function procesaFirmaXMLM() {

	muestraProceso();

	var passUser = document.getElementById('pwdLlavePrivM').value;
	var llavePrivadaUser = document.getElementById('fileInputM');

	leeLlavePrivadaM(llavePrivadaUser, passUser, function(error_code, rsakey) {

		firmarBeanM();

	});

}
