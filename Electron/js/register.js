const form = document.querySelector('form');
const username = document.getElementById('username');
const email = document.getElementById('email');
const password = document.getElementById('password');
const confirmPassword = document.getElementById('confirm_password');
const tipoCheckbox = document.getElementById('tipo');


form.addEventListener('submit', (e) => {
    e.preventDefault();

    // Limpiar mensaje de error anterior
    // document.getElementById('error-message').style.display = 'none';

    // VALIDACIONES
    const errorElement = document.getElementById('error-message');
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
                headers: { 'Content-Type': 'application/json' },
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