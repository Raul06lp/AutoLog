// Credenciales de autenticación para la API
const APP_USER = "autolog";
const APP_PASS = "X9#mK2$vQpL7@nRw";
const AUTH = 'Basic ' + btoa(`${APP_USER}:${APP_PASS}`);

const logOut = document.getElementById("logOut");
const wrapper = document.getElementById("wrapper");

document.addEventListener("DOMContentLoaded", () => {
  let stateFilter = null; // current estado filter (slug), null = no state filter
  let searchQuery = '';
    // Acción para botón finalizar
    wrapper.addEventListener('click', async (ev) => {
      const btn = ev.target.closest('.btn-finalizar');
      if (!btn) return;
      const id = btn.dataset.id;
      if (!id) return;
      try {
        // 1. Obtener datos actuales del vehículo
        const getResp = await fetch(`https://autolog-0mnd.onrender.com/api/vehiculos/${encodeURIComponent(id)}`, {
          headers: { 'Authorization': AUTH }
        });
        const veh = await getResp.json().catch(() => null);
        if (!getResp.ok || !veh) throw new Error('No se pudo obtener el vehículo');
        // 2. Preparar payload con todos los datos, cambiando solo estadoRevision
        const payload = { ...veh, estadoRevision: 'finalizado' };
        // Elimina campos que no deben enviarse (como _id, __v, imagenBase64)
        delete payload._id;
        delete payload.__v;
        delete payload.imagenBase64;
        // 3. PUT con todos los datos
        const putResp = await fetch(`https://autolog-0mnd.onrender.com/api/vehiculos/${encodeURIComponent(id)}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json', 'Authorization': AUTH },
          body: JSON.stringify(payload)
        });
        if (!putResp.ok) throw new Error('No se pudo finalizar el vehículo');
        await mostrarCoches();
      } catch (err) {
        alert('Error al finalizar el vehículo');
      }
    });

  // Acción para botón revisión (cambiar pendiente -> en_revision)
  wrapper.addEventListener('click', async (ev) => {
    const btn = ev.target.closest('.btn-revision');
    if (!btn) return;
    const id = btn.dataset.id;
    if (!id) return;
    try {
      const getResp = await fetch(`https://autolog-0mnd.onrender.com/api/vehiculos/${encodeURIComponent(id)}`, {
        headers: { 'Authorization': AUTH }
      });
      const veh = await getResp.json().catch(() => null);
      if (!getResp.ok || !veh) throw new Error('No se pudo obtener el vehículo');
      const payload = { ...veh, estadoRevision: 'en_revision' };
      delete payload._id;
      delete payload.__v;
      delete payload.imagenBase64;
      const putResp = await fetch(`https://autolog-0mnd.onrender.com/api/vehiculos/${encodeURIComponent(id)}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', 'Authorization': AUTH },
        body: JSON.stringify(payload)
      });
      if (!putResp.ok) throw new Error('No se pudo actualizar a en_revision');
      await mostrarCoches();
    } catch (err) {
      alert('Error al cambiar a en_revision');
    }
  });

  // Acción para botón restaurar (finalizado -> pendiente + actualizar fechaRegistro)
  wrapper.addEventListener('click', async (ev) => {
    const btn = ev.target.closest('.btn-restaurar');
    if (!btn) return;
    const id = btn.dataset.id;
    if (!id) return;
    try {
      const getResp = await fetch(`https://autolog-0mnd.onrender.com/api/vehiculos/${encodeURIComponent(id)}`, {
        headers: { 'Authorization': AUTH }
      });
      const veh = await getResp.json().catch(() => null);
      if (!getResp.ok || !veh) throw new Error('No se pudo obtener el vehículo');
      const payload = { ...veh, estadoRevision: 'pendiente', fechaRegistro: new Date().toISOString() };
      delete payload._id;
      delete payload.__v;
      delete payload.imagenBase64;
      const putResp = await fetch(`https://autolog-0mnd.onrender.com/api/vehiculos/${encodeURIComponent(id)}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', 'Authorization': AUTH },
        body: JSON.stringify(payload)
      });
      if (!putResp.ok) throw new Error('No se pudo restaurar el vehículo');
      await mostrarCoches();
    } catch (err) {
      alert('Error al restaurar a pendiente');
    }
  });
  const titleEl = document.querySelector('#title h1');
  const userRaw = localStorage.getItem('autolog_user');
  const user = userRaw ? JSON.parse(userRaw) : null;
  const nombre = user?.nombre || user?.name;

  if (titleEl) {
    titleEl.textContent = nombre ? `¡Bienvenido ${nombre}!` : '¡Bienvenido!';
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
      html += `<img src=\"data:image/jpeg;base64,${veh.imagenBase64}\" style=\"width:100%;max-height:260px;object-fit:cover;border-radius:8px;margin-bottom:12px;\">`;
    }

    // Apartados a mostrar (puedes personalizar el orden y los campos)
    const apartados = [
      {
        titulo: 'Datos del Cliente',
        campos: [
          { key: 'emailCliente', label: 'Correo cliente' },
          { key: 'nombreCliente', label: 'Nombre cliente' },
          { key: 'telefono', label: 'Teléfono' }
        ]
      },
      {
        titulo: 'Datos del Vehículo',
        campos: [
          { key: 'marca', label: 'Marca' },
          { key: 'modelo', label: 'Modelo' },
          { key: 'matricula', label: 'Matrícula' },
          { key: 'anyo', label: 'Año' },
          { key: 'color', label: 'Color' },
          { key: 'kilometraje', label: 'Kilometraje' }
        ]
      },
      {
        titulo: 'Estado y Observaciones',
        campos: [
          { key: 'estadoRevision', label: 'Estado revisión' },
          { key: 'observaciones', label: 'Observaciones' }
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
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
  }

  // Eventos de cierre del modal
  const modalCloseButton = document.getElementById('modalClose');
  if (modalCloseButton) modalCloseButton.addEventListener('click', hideModal);
  const modalOverlay = document.getElementById('modalDetalle');
  if (modalOverlay) modalOverlay.addEventListener('click', (e) => { if (e.target === modalOverlay) hideModal(); });

  // --- Edit modal: abrir formulario con valores actuales y enviar PUT ---
  wrapper.addEventListener('click', async (ev) => {
    const btn = ev.target.closest('.btn-editar');
    if (!btn) return;
    const id = btn.dataset.id;
    if (!id) return;
    try {
      // fetch current vehicle (incluye Authorization)
      const resp = await fetch(`https://autolog-0mnd.onrender.com/api/vehiculos/${encodeURIComponent(id)}`, {
        headers: { 'Authorization': AUTH }
      });
      const veh = await resp.json().catch(() => null);
      if (!resp.ok || !veh) throw new Error('No se pudo obtener el vehículo para editar');
      renderEditForm(veh, id);
      showEditModal();
    } catch (err) {
      console.error('Error al obtener vehículo para editar', err);
      // show quick error in detalle modal if edit modal missing
      const editBody = document.getElementById('modalEditBody');
      if (editBody) editBody.innerHTML = '<p>Error al cargar el formulario de edición.</p>';
      showEditModal();
    }
  });

  function showEditModal() {
    const modal = document.getElementById('modalEditar');
    if (modal) modal.classList.remove('hidden');
  }
  function hideEditModal() {
    const modal = document.getElementById('modalEditar');
    if (modal) modal.classList.add('hidden');
  }

  function renderEditForm(veh, id) {
    const body = document.getElementById('modalEditBody');
    if (!body) return;
    // Exclude internal, mechanic-related and non-editable fields from the edit form
    const skip = new Set(['imagenBase64', '__v', '_id', 'idMecanico', 'nombreMecanico', 'emailMecanico', 'fechaIngreso', 'estadoRevision', 'idCliente']);
    const entries = Object.entries(veh || {}).filter(([k]) => !skip.has(k));

    let html = `<form id="editForm">
      <div style="display:flex;flex-direction:column;gap:10px;">
    `;

    // show image if exists
    if (veh.imagenBase64) {
      html += `<img src="data:image/jpeg;base64,${veh.imagenBase64}" style="width:100%;max-height:200px;object-fit:cover;border-radius:8px;margin-bottom:8px;">`;
    }

    // Agrupar inputs en bloques de 2 por fila
    const visibleEntries = entries.filter(([k]) => k !== 'idVehiculo');
    for (let i = 0; i < visibleEntries.length; i += 2) {
      html += '<div class="edit-row">';
      for (let j = i; j < i + 2 && j < visibleEntries.length; j++) {
        const [k, v] = visibleEntries[j];
        const name = escapeHTML(k);
        const value = v == null ? '' : String(v);
        const type = /^\d+$/.test(value) ? 'number' : 'text';
        html += `
          <div class="edit-block">
            <label style="font-weight:700;color:#133;">${formatKey(k)}</label>
            <input name="${name}" placeholder="${escapeHTML(value)}" value="${escapeHTML(value)}" type="${type}" />
          </div>
        `;
      }
      html += '</div>';
    }

    // idVehiculo como hidden
    const idEntry = entries.find(([k]) => k === 'idVehiculo');
    if (idEntry) {
      html += `<input type="hidden" name="${escapeHTML(idEntry[0])}" value="${escapeHTML(String(idEntry[1] ?? ''))}">`;
    }

    html += `
        <div class="modal-edit-actions">
          <button type="submit" class="btn-submit">Guardar</button>
          <button type="button" class="btn-reset" id="cancelEdit">Cancelar</button>
        </div>
      </div>
    </form>`;

    body.innerHTML = html;

    const form = document.getElementById('editForm');
    if (!form) return;
    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const submitBtn = form.querySelector('button[type=submit]');
      if (submitBtn) submitBtn.disabled = true;
      const formData = new FormData(form);
      const payloadFields = {};
      for (const [k, v] of formData.entries()) {
        payloadFields[k] = v;
      }
      // Merge with original vehicle to preserve fields we don't allow editing (mecánico info, etc.)
      const finalPayload = { ...veh, ...payloadFields };
      // Remove server-managed fields
      delete finalPayload._id;
      delete finalPayload.__v;
      delete finalPayload.imagenBase64;
      try {
          const putResp = await fetch(`https://autolog-0mnd.onrender.com/api/vehiculos/${encodeURIComponent(id)}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json', 'Authorization': AUTH },
            body: JSON.stringify(finalPayload)
          });
        const result = await putResp.json().catch(() => ({}));
        if (!putResp.ok) throw new Error(result?.message || 'Error al actualizar');
        // success
        const successNode = document.createElement('p');
        successNode.textContent = 'Vehículo actualizado correctamente.';
        body.prepend(successNode);
        await mostrarCoches();
        setTimeout(() => { hideEditModal(); }, 800);
      } catch (err) {
        console.error('Error en PUT', err);
        const errNode = document.createElement('p');
        errNode.style.color = 'red';
        errNode.textContent = 'Error al actualizar vehículo.';
        body.prepend(errNode);
      } finally {
        if (submitBtn) submitBtn.disabled = false;
      }
    });

    const cancelBtn = document.getElementById('cancelEdit');
    if (cancelBtn) cancelBtn.addEventListener('click', hideEditModal);
  }

  // Close edit modal on overlay or close button
  const modalEditClose = document.getElementById('modalEditClose');
  if (modalEditClose) modalEditClose.addEventListener('click', hideEditModal);
  const modalEditOverlay = document.getElementById('modalEditar');
  if (modalEditOverlay) modalEditOverlay.addEventListener('click', (e) => { if (e.target === modalEditOverlay) hideEditModal(); });

  mostrarCoches();

  function normalizeEstado(raw) {
    const estadoRaw = (raw || '').toString();
    let slug = estadoRaw
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toLowerCase()
      .replace(/\s+/g,'-')
      .replace(/[^a-z0-9\-]/g,'');
    if (/repar|revision/.test(slug)) return 'reparacion';
    if (/pend/.test(slug)) return 'pendiente';
    if (/final/.test(slug)) return 'finalizado';
    if (/esper/.test(slug)) return 'espera';
    return slug;
  }

  function getEstadoLabel(estadoSlug, estadoRaw) {
    if (estadoSlug === 'pendiente') return 'Pendiente';
    if (estadoSlug === 'reparacion') return 'En revisión';
    if (estadoSlug === 'finalizado') return 'Finalizado';
    if (estadoSlug === 'espera') return 'En espera';
    const raw = (estadoRaw || '').toString().trim();
    if (!raw) return '';
    return raw
      .replace(/_/g, ' ')
      .replace(/\s+/g, ' ')
      .replace(/^./, s => s.toUpperCase());
  }

  // Wire up state filter buttons
  const filterBtns = Array.from(document.querySelectorAll('.filter-btn'));
  filterBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      const raw = btn.dataset.estado || '';
      // map variations
      let slug = raw.toString().toLowerCase();
      if (slug === 'reparando' || slug === 'en_revision' || /revision/.test(slug)) slug = 'reparacion';
      if (slug === 'pendiente') slug = 'pendiente';
      if (slug === 'finalizado') slug = 'finalizado';
      // toggle
      if (stateFilter === slug) {
        stateFilter = null;
      } else {
        stateFilter = slug;
      }
      // update active classes
      filterBtns.forEach(b => b.classList.toggle('active', b === btn && stateFilter === slug));
      // Re-render the list so backend data is fetched according to the new filter
      mostrarCoches();
    });
  });

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

      let vehiculosFiltrados = [];
      if (user?.role === 'cliente') {
        vehiculosFiltrados = data.filter((v) => v.idCliente === user.id);
      } else if (user?.role === 'mecanico') {
        // By default mechanics don't see finalizados in the main view, but allow
        // viewing them when the state filter is explicitly 'finalizado'.
        if (stateFilter === 'finalizado') {
          vehiculosFiltrados = data.filter((v) => v.idMecanico === user.id && normalizeEstado(v.estadoRevision) === 'finalizado');
        } else {
          vehiculosFiltrados = data.filter((v) => v.idMecanico === user.id && normalizeEstado(v.estadoRevision) !== 'finalizado');
        }
      } else {
        vehiculosFiltrados = data;
      }

      // Generar HTML dinámico para mostrar los vehículos
      let html = '';
      vehiculosFiltrados.forEach((vehiculo) => {
        const fotoSrc = vehiculo.imagenBase64
          ? `data:image/jpeg;base64,${vehiculo.imagenBase64}`
          : '../icons/coche.svg';
        const dataMat = (vehiculo.matricula || '').toString().replace(/\s+/g, '').toLowerCase();
        const dataEmail = (vehiculo.emailCliente || '').toString().toLowerCase();
        const estadoRaw = (vehiculo.estadoRevision || '').toString();
        let estadoSlug = estadoRaw
          .normalize('NFD')
          .replace(/[\u0300-\u036f]/g, '')
          .toLowerCase()
          .replace(/\s+/g,'-')
          .replace(/[^a-z0-9\-]/g,'');

        // Map common variations to canonical slugs used in CSS
        if (/repar|revision/.test(estadoSlug)) {
          estadoSlug = 'reparacion';
        } else if (/pend/.test(estadoSlug)) {
          estadoSlug = 'pendiente';
        } else if (/final/.test(estadoSlug)) {
          estadoSlug = 'finalizado';
        } else if (/esper/.test(estadoSlug)) {
          estadoSlug = 'espera';
        }
        // Select appropriate action button(s) based on estado
        let actionBtnHtml = '';
        // For pendiente: show Revisión
        if (estadoSlug === 'pendiente') {
          actionBtnHtml = `<button type="button" class="btn-revision" data-id="${vehiculo.idVehiculo || ''}">Revisión</button>`;
        } else if (estadoSlug === 'finalizado') {
          // For finalizado: do NOT show Editar or Finalizar; show Restaurar
          actionBtnHtml = `<button type="button" class="btn-restaurar" data-id="${vehiculo.idVehiculo || ''}">Restaurar</button>`;
        } else {
          // Other states: show Finalizar
          actionBtnHtml = `<button type="button" class="btn-finalizar" data-id="${vehiculo.idVehiculo || ''}">Finalizar</button>`;
        }

        // Only show 'Editar' when not finalizado
        const editarHtml = estadoSlug === 'finalizado' ? '' : `<button type="button" class="btn-editar" data-id="${vehiculo.idVehiculo || ''}">Editar</button>`;

        html += `
          <div class="car-card" data-matricula="${escapeHTML(dataMat)}" data-email="${escapeHTML(dataEmail)}" data-estado="${estadoSlug}">
            <img class="car-photo" src="${fotoSrc}" alt="Foto del coche">
            <p><strong>Correo cliente:</strong> ${vehiculo.emailCliente || 'N/A'}</p>
            <p><strong>Matrícula:</strong> ${vehiculo.matricula || 'N/A'}</p>
            <div class="card-actions">
              <button type="button" class="btn-ver-mas" data-id="${vehiculo.idVehiculo || ''}">Ver más</button>
              ${editarHtml}
              ${actionBtnHtml}
              <span class="estado-revision ${estadoSlug ? 'estado-'+estadoSlug : ''}" aria-hidden="false">${escapeHTML(getEstadoLabel(estadoSlug, estadoRaw))}</span>
            </div>
          </div>
        `;
      });

      wrapper.innerHTML = html || '<p>No hay vehículos registrados.</p>';
      // refresh visibility based on current filters
      refreshDisplay();
    } catch (error) {
      console.error("Error al obtener vehículos:", error);
      wrapper.innerHTML = '<p>Error al cargar los vehículos</p>';
    }
  }

  // Filter function: checks data attributes for quick matching
  function applyFilter(q) {
    searchQuery = (q || '').toString().trim().toLowerCase().replace(/\s+/g, '');
    refreshDisplay();
  }

  function refreshDisplay() {
    const cards = Array.from(document.querySelectorAll('.car-card'));
    cards.forEach(c => {
      const mat = (c.dataset.matricula || '').toString();
      const email = (c.dataset.email || '').toString();
      const estado = (c.dataset.estado || '').toString();
      // search match
      const matchesSearch = !searchQuery || mat.includes(searchQuery) || email.includes(searchQuery);
      // state match
      const matchesState = !stateFilter || estado === stateFilter;
      c.style.display = (matchesSearch && matchesState) ? 'flex' : 'none';
    });
  }

  // Wire up search input
  const searchInput = document.getElementById('buscador-mecanico');
  if (searchInput) {
    searchInput.addEventListener('input', (e) => {
      applyFilter(e.target.value);
    });
  }


  logOut.addEventListener("click", cerrarSesion);

  function cerrarSesion() {
    localStorage.removeItem('autolog_user');
    window.location.href = "../index.html";
  }
});