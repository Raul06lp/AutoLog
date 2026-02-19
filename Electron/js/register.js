// Credenciales de autenticación para la API
const APP_USER = "autolog";
const APP_PASS = "X9#mK2$vQpL7@nRw";
const AUTH = 'Basic ' + btoa(`${APP_USER}:${APP_PASS}`);

const form = document.querySelector('form');
const username = document.getElementById('username');
const email = document.getElementById('email');
const password = document.getElementById('password');
const confirmPassword = document.getElementById('confirm_password');
const tipoCheckbox = document.getElementById('tipo');

// Toggle visibility for password fields (eye icon)
document.addEventListener('click', (e) => {
    if (e.target.closest && e.target.closest('.toggle-pass')) {
        const btn = e.target.closest('.toggle-pass');
        const wrapper = btn.parentElement;
        const input = wrapper.querySelector('input');
        if (!input) return;
        if (input.type === 'password') {
            input.type = 'text';
            btn.classList.add('active');
            btn.setAttribute('aria-label', 'Ocultar contraseña');
        } else {
            input.type = 'password';
            btn.classList.remove('active');
            btn.setAttribute('aria-label', 'Mostrar contraseña');
        }
    }
});


form.addEventListener('submit', (e) => {
    e.preventDefault();

    // Limpiar mensaje de error anterior
    const errorElement = document.getElementById('error-message');
    if (errorElement) errorElement.style.display = 'none';

    // VALIDACIONES
    if (!email.value || !username.value || !password.value || !confirmPassword.value) {
        errorElement.textContent = 'Por favor, completa todos los campos.';
        errorElement.style.display = 'block';
        return;
    }

    if (password.value !== confirmPassword.value) {
        errorElement.textContent = 'Las contraseñas no coinciden.';
        errorElement.style.display = 'block';
        return;
    }

    if (password.value.length < 6) {
        errorElement.textContent = 'La contraseña debe tener al menos 6 caracteres.';
        errorElement.style.display = 'block';
        return;
    }

    // CREAR USUARIO USANDO API REST
    (async () => {
        const tipo = tipoCheckbox.checked; // false = cliente, true = mecanico
        const endpoint = tipo
            ? 'https://autolog-0mnd.onrender.com/api/mecanicos/registro'
            : 'https://autolog-0mnd.onrender.com/api/clientes/registro';

        const datos = {
            nombre: username.value,
            email: email.value,
            contrasena: password.value
        };

        try {
            const resp = await fetch(endpoint, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    'Authorization': AUTH
                },
                body: JSON.stringify(datos)
            });

            const data = await resp.json().catch(() => ({}));

            if (!resp.ok) {
                const errorElement = document.getElementById('error-message');
                const msg = data.message || data.error || 'Error en el registro.';
                errorElement.textContent = msg;
                errorElement.style.display = 'block';
                return;
            }

            // Registro exitoso: redirigir según tipo
            if (tipo === false) {
                window.location.href = '../screensCliente/home.html';
            } else {
                window.location.href = '../screensMecanico/home.html';
            }
        } catch (err) {
            console.error('Error en fetch registro:', err);
            const errorElement = document.getElementById('error-message');
            errorElement.textContent = 'No se pudo conectar con el servidor.';
            errorElement.style.display = 'block';
        }
    })();
});