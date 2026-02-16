// Credenciales de autenticación para la API
const APP_USER = "autolog";
const APP_PASS = "X9#mK2$vQpL7@nRw";
const AUTH = 'Basic ' + btoa(`${APP_USER}:${APP_PASS}`);

const email = document.getElementById('email');
const password = document.getElementById('password');
const form = document.querySelector('form');

const tryLogin = async (endpoint, payload) => {
    const resp = await fetch(endpoint, {
        method: 'POST',
        headers: { 
            'Authorization': AUTH,
            'Content-Type': 'application/json' 
        },
        body: JSON.stringify(payload)
    });

    const data = await resp.json().catch(() => ({}));
    return { ok: resp.ok, data };
};

const extractUserId = (data) => {
    if (!data) return null;
    return (
        data.id ??
        data.idMecanico ??
        data.idCliente ??
        data.mecanico?.id ??
        data.cliente?.id ??
        data.user?.id ??
        null
    );
};

const extractUserName = (data) => {
    if (!data) return null;
    return (
        data.nombre ??
        data.name ??
        data.nombreMecanico ??
        data.nombreCliente ??
        data.mecanico?.nombre ??
        data.cliente?.nombre ??
        data.user?.nombre ??
        data.user?.name ??
        null
    );
};

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Limpiar mensaje de error anterior
    const errorElement = document.getElementById('error-message');
    if (errorElement) errorElement.style.display = 'none';

    try {
        if (!email.value || !password.value) {
            if (errorElement) {
                errorElement.textContent = 'Por favor, completa todos los campos.';
                errorElement.style.display = 'block';
            }
            return;
        }

        const payload = {
            email: email.value,
            contrasena: password.value
        };

        // Intentar login como mecánico
        const mecanicoEndpoint = 'https://autolog-0mnd.onrender.com/api/mecanicos/login';
        const mecanicoResp = await tryLogin(mecanicoEndpoint, payload);

        if (mecanicoResp.ok) {
            const mecanicoId = extractUserId(mecanicoResp.data);
            const mecanicoNombre = extractUserName(mecanicoResp.data);
            if (mecanicoId) {
                localStorage.setItem(
                    'autolog_user',
                    JSON.stringify({ role: 'mecanico', id: mecanicoId, nombre: mecanicoNombre })
                );
            }
            window.location.href = 'screensMecanico/home.html';
            return;
        }

        // Si falla, intentar login como cliente
        const clienteEndpoint = 'https://autolog-0mnd.onrender.com/api/clientes/login';
        const clienteResp = await tryLogin(clienteEndpoint, payload);

        if (clienteResp.ok) {
            const clienteId = extractUserId(clienteResp.data);
            const clienteNombre = extractUserName(clienteResp.data);
            if (clienteId) {
                localStorage.setItem(
                    'autolog_user',
                    JSON.stringify({ role: 'cliente', id: clienteId, nombre: clienteNombre })
                );
            }
            window.location.href = 'screensCliente/home.html';
            return;
        }

        const msg =
            clienteResp.data?.message ||
            clienteResp.data?.error ||
            mecanicoResp.data?.message ||
            mecanicoResp.data?.error ||
            'El usuario o la contraseña son incorrectos.';

        if (errorElement) {
            errorElement.textContent = msg;
            errorElement.style.display = 'block';
        }
    } catch (error) {
        console.error('Error en login:', error);
        if (errorElement) {
            errorElement.textContent = 'No se pudo conectar con el servidor.';
            errorElement.style.display = 'block';
        }
    }
});