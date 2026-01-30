
import { auth } from './firebase.js';
import { signOut } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-auth.js';

const logOut = document.getElementById("logOut");

document.addEventListener("DOMContentLoaded", () => {

  function mostrarCoches() {
    
  }


  logOut.addEventListener("click", cerrarSesion);

  function cerrarSesion() {
    // Depuración: mostrar usuario antes de signOut
    console.log('[cerrarSesion] auth.currentUser antes:', auth.currentUser);

    // Esperar a que Firebase cierre la sesión antes de redirigir
    signOut(auth).then(() => {
      console.log("Sesión cerrada correctamente");
      console.log('[cerrarSesion] auth.currentUser después:', auth.currentUser);
      window.location.href = "../index.html";
    }).catch((error) => {
      console.error("Error al cerrar sesión:", error);
      alert('Error al cerrar sesión: ' + error.message);
    });
  }
});