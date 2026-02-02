import { auth, db } from './firebase.js';
import { signOut } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-auth.js';
import { collection, getDocs } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-firestore.js';
import { getStorage, ref, uploadBytesResumable, getDownloadURL } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-storage.js';

const logOut = document.getElementById("logOut");
const wrapper = document.getElementById("wrapper");

document.addEventListener("DOMContentLoaded", () => {
  mostrarCoches();

  async function mostrarCoches() {
    try {
      // Obtener la coleección Vehículos
      const vehiculosRef = collection(db, "vehiculo");
      const vehiculos = await getDocs(vehiculosRef);
      
      console.log("Vehículos obtenidos:");
      vehiculos.forEach((doc) => {
        console.log(doc.id, " => ", doc.data());
      });

      // Generar HTML dinámico para mostrar los vehículos
      let html = '';
      vehiculos.forEach((doc) => {
        const vehiculo = doc.data();
        html += `
          <div class="car-card">
            <h3>${vehiculo.marca || 'Sin marca'} ${vehiculo.modelo || ''}</h3>
            <p><strong>Correo :</strong> ${vehiculo.correo || 'N/A'}</p>
            <p><strong>Placa:</strong> ${vehiculo.matricula || 'N/A'}</p>
            <p><strong>Año:</strong> ${vehiculo.anyo || 'N/A'}</p>
            <p><strong>Estado:</strong> ${vehiculo.estadoRevision || 'N/A'}</p>
            <p><strong>Kilometraje:</strong> ${vehiculo.kilometraje || 'N/A'}</p>
            <p><strong>Color:</strong> ${vehiculo.color || 'N/A'}</p>
          </div>
        `;
      });
      
      wrapper.innerHTML = html;
    } catch (error) {
      console.error("Error al obtener vehículos:", error);
      wrapper.innerHTML = '<p>Error al cargar los vehículos</p>';
    }
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