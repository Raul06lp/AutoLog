const logOut = document.getElementById("logOut");
const wrapper = document.getElementById("wrapper");

document.addEventListener("DOMContentLoaded", () => {
  const titleEl = document.querySelector('#title h1');
  const userRaw = localStorage.getItem('autolog_user');
  const user = userRaw ? JSON.parse(userRaw) : null;
  const nombre = user?.nombre || user?.name;

  if (titleEl) {
    titleEl.textContent = nombre ? `¡Bienvenido ${nombre}!` : '¡Bienvenido!';
  }

  mostrarCoches();

  async function mostrarCoches() {
    try {
      const resp = await fetch('https://autolog-0mnd.onrender.com/api/vehiculos');
      const data = await resp.json().catch(() => ([]));

      if (!resp.ok || !Array.isArray(data)) {
        throw new Error('Respuesta inválida al obtener vehículos');
      }

      const userRaw = localStorage.getItem('autolog_user');
      const user = userRaw ? JSON.parse(userRaw) : null;

      const vehiculosFiltrados = user?.role === 'cliente'
        ? data.filter((v) => v.idCliente === user.id)
        : user?.role === 'mecanico'
          ? data.filter((v) => v.idMecanico === user.id)
          : data;

      // Generar HTML dinámico para mostrar los vehículos
      let html = '';
      vehiculosFiltrados.forEach((vehiculo) => {
        const fotoSrc = vehiculo.imagenBase64
          ? `data:image/jpeg;base64,${vehiculo.imagenBase64}`
          : '../icons/coche.svg';
        html += `
          <div class="car-card">
            <img class="car-photo" src="${fotoSrc}" alt="Foto del coche">
            <p><strong>Correo cliente:</strong> ${vehiculo.emailCliente || 'N/A'}</p>
            <p><strong>Matrícula:</strong> ${vehiculo.matricula || 'N/A'}</p>
            <div class="card-actions">
              <button type="button" class="btn-ver-mas" data-id="${vehiculo.idVehiculo || ''}">Ver más</button>
              <button type="button" class="btn-editar" data-id="${vehiculo.idVehiculo || ''}">Editar</button>
              <button type="button" class="btn-finalizar" data-id="${vehiculo.idVehiculo || ''}">Finalizar</button>
            </div>
          </div>
        `;
      });

      wrapper.innerHTML = html || '<p>No hay vehículos registrados.</p>';
    } catch (error) {
      console.error("Error al obtener vehículos:", error);
      wrapper.innerHTML = '<p>Error al cargar los vehículos</p>';
    }
  }


  logOut.addEventListener("click", cerrarSesion);

  function cerrarSesion() {
    localStorage.removeItem('autolog_user');
    window.location.href = "../index.html";
  }
});