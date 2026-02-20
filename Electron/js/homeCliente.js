// Credenciales de autenticación para la API
const APP_USER = "autolog";
const APP_PASS = "X9#mK2$vQpL7@nRw";
const AUTH = 'Basic ' + btoa(`${APP_USER}:${APP_PASS}`);

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
      const resp = await fetch('https://autolog-0mnd.onrender.com/api/vehiculos', {
        headers: { 'Authorization': AUTH }
      });
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

      // Generar HTML para mostrar los vehículos
      let html = '';
      vehiculosFiltrados.forEach((vehiculo) => {
        const fotoSrc = vehiculo.imagenBase64
          ? `data:image/jpeg;base64,${vehiculo.imagenBase64}`
          : '../icons/coche.svg';
        const estadoRaw = (vehiculo.estadoRevision || '').toString();
        let estadoSlug = estadoRaw
          .normalize('NFD')
          .replace(/[\u0300-\u036f]/g, '')
          .toLowerCase()
          .replace(/\s+/g,'-')
          .replace(/[^a-z0-9\-]/g,'');

        // Map common variations to canonical slugs used in CSS
        if (/repar/.test(estadoSlug)) {
          estadoSlug = 'reparacion';
        } else if (/pend/.test(estadoSlug)) {
          estadoSlug = 'pendiente';
        } else if (/final/.test(estadoSlug)) {
          estadoSlug = 'finalizado';
        } else if (/esper/.test(estadoSlug)) {
          estadoSlug = 'espera';
        }
        html += `
          <div class="car-card ${estadoSlug === 'finalizado' ? 'car-card-finalizado' : ''}">
            <img class="car-photo" src="${fotoSrc}" alt="Foto del coche">
            <p><strong>Matrícula:</strong> ${vehiculo.matricula || 'N/A'}</p>
            <div class="card-actions" style="align-items:center;">
              <button type="button" class="btn-ver-mas" data-id="${vehiculo.idVehiculo || ''}">Ver más</button>
              <span class="estado-revision ${estadoSlug ? 'estado-'+estadoSlug : ''}" aria-hidden="false">${escapeHTML(estadoRaw)}</span>
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
      const resp = await fetch(`https://autolog-0mnd.onrender.com/api/vehiculos/${encodeURIComponent(id)}`, {
        headers: { 'Authorization': AUTH }
      });
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

    // Apartados a mostrar (puedes personalizar el orden y los campos)
    const apartados = [
      {
        titulo: 'Datos del Vehículo',
        campos: [
          { key: 'matricula', label: 'Matrícula' },
          { key: 'marca', label: 'Marca' },
          { key: 'modelo', label: 'Modelo' },
          { key: 'anio', label: 'Año' },
          { key: 'color', label: 'Color' },
          { key: 'kilometraje', label: 'Kilometraje' }
        ]
      },
      {
        titulo: 'Estado y Observaciones',
        campos: [
          { key: 'estadoRevision', label: 'Estado revisión' },
          { key: 'observaciones', label: 'Observaciones' },
          { key: 'medidasTomadas', label: 'Medidas tomadas' }
        ]
      },
      {
        titulo: 'Datos del Cliente y Mecánico',
        campos: [
          { key: 'nombreCliente', label: 'Nombre cliente' },
          { key: 'nombreMecanico', label: 'Nombre mecánico' },
          { key: 'emailMecanico', label: 'Email mecánico' }
        ]
      }
    ];

    let hayDatos = false;
    html += '<div class="modal-apartados">';
    apartados.forEach(apartado => {
      // Solo mostrar el apartado si hay algún campo con valor
      const camposConValor = apartado.campos.filter(c => veh[c.key] != null && veh[c.key] !== '');
      if (camposConValor.length > 0) {
        hayDatos = true;
        html += `<div class="modal-apartado"><h3 class="modal-apartado-titulo">${apartado.titulo}</h3><div class="modal-grid">`;
        for (let i = 0; i < camposConValor.length; i += 2) {
          html += '<div class="modal-row">';
          for (let j = i; j < i + 2 && j < camposConValor.length; j++) {
            const campo = camposConValor[j];
            html += `<div class=\"modal-block\"><span class=\"modal-label\">${campo.label}:</span> <span class=\"modal-value\">${escapeHTML(String(veh[campo.key] ?? ''))}</span></div>`;
          }
          html += '</div>';
        }
        html += '</div></div>';
      }
    });
    html += '</div>';
    if (!hayDatos) {
      html += '<p>No hay información adicional.</p>';
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