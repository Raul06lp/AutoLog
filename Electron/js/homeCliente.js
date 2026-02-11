const logOut = document.getElementById("logOut");
const wrapper = document.getElementById("wrapper");

document.addEventListener("DOMContentLoaded", () => {
  const titleEl = document.querySelector('#title h1');
  const userRaw = localStorage.getItem('autolog_user');
  const user = userRaw ? JSON.parse(userRaw) : null;
  const nombre = user?.nombre || user?.name;

  if (titleEl) {
    titleEl.textContent = nombre ? `${nombre}, tus coches` : '¡Tus Coches!';
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
            <p><strong>Matrícula:</strong> ${vehiculo.matricula || 'N/A'}</p>
            <div class="card-actions">
              <button type="button" class="btn-ver-mas" data-id="${vehiculo.idVehiculo || ''}">Ver más</button>
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

  // Delegación para botones "Ver más" — carga detalle desde API y muestra modal
  wrapper.addEventListener('click', async (ev) => {
    const btn = ev.target.closest('.btn-ver-mas');
    if (!btn) return;
    const id = btn.dataset.id;
    if (!id) return;
    try {
      showModalLoading();
      const resp = await fetch(`https://autolog-0mnd.onrender.com/api/vehiculos/${encodeURIComponent(id)}`);
      const veh = await resp.json().catch(() => null);
      if (!resp.ok || !veh) throw new Error('No se pudo obtener el vehículo');
      renderModal(veh);
    } catch (err) {
      console.error('Error al cargar detalle:', err);
      renderModal({ error: 'Error al cargar información del vehículo.' });
    }
  });

  // Modal helpers
  function showModal() {
    const modal = document.getElementById('modalDetalle');
    if (modal) modal.classList.remove('hidden');
  }
  function hideModal() {
    const modal = document.getElementById('modalDetalle');
    if (modal) modal.classList.add('hidden');
  }
  function showModalLoading() {
    const body = document.getElementById('modalBody');
    if (body) body.innerHTML = '<p>Cargando...</p>';
    showModal();
  }
  function renderModal(veh) {
    const body = document.getElementById('modalBody');
    if (!body) return;
    if (veh && veh.error) {
      body.innerHTML = `<p>${veh.error}</p>`;
      return;
    }

    let html = '';
    if (veh.imagenBase64) {
      html += `<img src="data:image/jpeg;base64,${veh.imagenBase64}" style="width:100%;max-height:260px;object-fit:cover;border-radius:8px;margin-bottom:12px;">`;
    }

    const skip = new Set(['imagenBase64', '__v', '_id']);
    const entries = Object.entries(veh || {}).filter(([k]) => !skip.has(k));
    if (entries.length === 0) {
      html += '<p>No hay información adicional.</p>';
    } else {
      html += entries.map(([k, v]) => `<p><strong>${formatKey(k)}:</strong> ${escapeHTML(String(v ?? ''))}</p>`).join('');
    }

    body.innerHTML = html;
  }

  function formatKey(key) {
    return key.replace(/([A-Z])/g, ' $1').replace(/_/g, ' ').replace(/^./, s => s.toUpperCase());
  }

  function escapeHTML(str) {
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/\"/g, '&quot;').replace(/'/g, '&#39;');
  }

  // Eventos de cierre del modal
  const modalCloseButton = document.getElementById('modalClose');
  if (modalCloseButton) modalCloseButton.addEventListener('click', hideModal);
  const modalOverlay = document.getElementById('modalDetalle');
  if (modalOverlay) modalOverlay.addEventListener('click', (e) => { if (e.target === modalOverlay) hideModal(); });


  logOut.addEventListener("click", cerrarSesion);

  function cerrarSesion() {
    localStorage.removeItem('autolog_user');
    window.location.href = "../index.html";
  }
});