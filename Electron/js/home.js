import { auth, getAuth } from './firebase.js';
import { signOut } from "https://www.gstatic.com/firebasejs/12.8.0/firebase-auth.js";

const auth = getAuth();
const logOut = document.getElementById("logOut");

document.addEventListener("DOMContentLoaded", () => {
  logOut.addEventListener("click", cerrarSesion);

  function cerrarSesion() {
    signOut(auth).then(() => {
    // El cierre de sesión fue exitoso.
    console.log("Sesión cerrada correctamente");
    }).catch((error) => {
    // Ocurrió un error al cerrar la sesión.
    console.error("Error al cerrar sesión:", error);
    });
    window.location.href = "../index.html";
}
});