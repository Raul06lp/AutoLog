// Credenciales de autenticación para la API
const APP_USER = "autolog";
const APP_PASS = "X9#mK2$vQpL7@nRw";
const AUTH = 'Basic ' + btoa(`${APP_USER}:${APP_PASS}`);

console.log("Formulario de registro de vehículo listo.");

document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('form');
    const correoCliente = document.getElementById('correoCliente');
    const marca = document.getElementById('marca');
    const modelo = document.getElementById('modelo');
    const matricula = document.getElementById('matricula');
    const anyo = document.getElementById('anyo');
    const color = document.getElementById('color');
    const kilometraje = document.getElementById('kilometraje');
    const foto = document.getElementById('foto');
    const observaciones = document.getElementById('observaciones');

    // Poblar el desplegable de correos de clientes
    fetch('https://autolog-0mnd.onrender.com/api/clientes', {
        headers: { 'Authorization': AUTH }
    })
    .then(resp => resp.json())
    .then(clientes => {
        if (Array.isArray(clientes)) {
            clientes.forEach(cliente => {
                const email = cliente.email || cliente.correo || cliente.mail;
                if (email) {
                    const option = document.createElement('option');
                    option.value = email;
                    option.textContent = email;
                    correoCliente.appendChild(option);
                }
            });
        }
    })
    .catch(() => {
        // Si falla, no poblar nada
    });

    console.log("Formulario de registro de vehículo listo.");

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        try {
            const userRaw = localStorage.getItem('autolog_user');
            const user = userRaw ? JSON.parse(userRaw) : null;
            const idMecanico = user?.role === 'mecanico' ? user.id : null;

            const errorElement = document.getElementById('error-message');
            if (errorElement) errorElement.style.display = 'none';
            if (!idMecanico) {
                if (errorElement) {
                    errorElement.textContent = 'No se encontró el mecánico logeado. Inicia sesión nuevamente.';
                    errorElement.style.display = 'block';
                }
                return;
            }

            const clientesResp = await fetch('https://autolog-0mnd.onrender.com/api/clientes', {
                headers: { 'Authorization': AUTH }
            });
            const clientesData = await clientesResp.json().catch(() => ([]));

            if (!clientesResp.ok || !Array.isArray(clientesData)) {
                if (errorElement) {
                    errorElement.textContent = 'No se pudo obtener la lista de clientes.';
                    errorElement.style.display = 'block';
                }
                return;
            }

            const correo = correoCliente.value.trim().toLowerCase();
            const cliente = clientesData.find((c) => {
                const email = (c.email || c.correo || c.mail || '').toString().toLowerCase();
                return email === correo;
            });

            if (!cliente) {
                if (errorElement) {
                    errorElement.textContent = 'No se encontró un cliente con ese correo.';
                    errorElement.style.display = 'block';
                }
                return;
            }

            const idCliente = cliente.id ?? cliente.idCliente;
            if (!idCliente) {
                if (errorElement) {
                    errorElement.textContent = 'No se pudo obtener el ID del cliente.';
                    errorElement.style.display = 'block';
                }
                return;
            }

            const anioValue = parseInt(anyo.value, 10);
            if (!matricula.value || !marca.value || !modelo.value || !anioValue) {
                if (errorElement) {
                    errorElement.textContent = 'Completa matrícula, marca, modelo y año.';
                    errorElement.style.display = 'block';
                }
                return;
            }

            const formData = new FormData();
            formData.append('matricula', matricula.value);
            formData.append('marca', marca.value);
            formData.append('modelo', modelo.value);
            formData.append('anio', anioValue.toString());
            formData.append('idCliente', idCliente.toString());
            formData.append('idMecanico', idMecanico.toString());

            if (color.value) formData.append('color', color.value);
            if (kilometraje.value) formData.append('kilometraje', kilometraje.value);
            if (observaciones.value) formData.append('observaciones', observaciones.value);
            // Estado: usar el valor seleccionado en el formulario; por defecto 'pendiente'
            const estadoEl = document.getElementById('estado');
            const estadoVal = estadoEl && estadoEl.value ? estadoEl.value : 'pendiente';
            formData.append('estado', estadoVal);
            if (foto?.files && foto.files[0]) {
                formData.append('imagen', foto.files[0]);
            }

            console.log("Datos a enviar (multipart):", Object.fromEntries(formData.entries()));

            const resp = await fetch('https://autolog-0mnd.onrender.com/api/vehiculos/con-imagen', {
                method: 'POST',
                body: formData,
                headers: {
                    'Authorization': AUTH
                }
            });

            const data = await resp.json().catch(() => ({}));

            if (!resp.ok) {
                const msg = data.message || data.error || 'Error al registrar el vehículo.';
                if (errorElement) {
                    errorElement.textContent = msg;
                    errorElement.style.display = 'block';
                }
                return;
            }

            form.reset();
            window.location.href = "home.html";
        } catch (e) {
            console.error("✗ Error al registrar vehículo: ", e);
            const errorElement = document.getElementById('error-message');
            if (errorElement) {
                errorElement.textContent = "Error: " + (e.message || 'Error desconocido.');
                errorElement.style.display = 'block';
            }
        }
    })
});