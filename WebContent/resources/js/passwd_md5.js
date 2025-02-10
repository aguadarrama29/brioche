/* 
 * Rozvo Ware Solutions.
 */

/**
 * Encripta nuestro password en md5 y lo envía encriptado.
 * 
 * @returns {null}
 */
function passwd_md5() {
    var password = $("#login_form\\:password").val();
    var passwd_md5;
    passwd_md5 = hex_md5(password);
    $("#login_form\\:password").val(passwd_md5);
}