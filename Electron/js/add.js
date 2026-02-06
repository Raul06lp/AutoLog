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
    const estadoRevision = document.getElementById('estadoRevision');
    const foto = document.getElementById('foto');
    const observaciones = document.getElementById('observaciones');

    console.log("Formulario de registro de vehículo listo.");

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        try {
            const userRaw = localStorage.getItem('autolog_user');
            const user = userRaw ? JSON.parse(userRaw) : null;
            const idMecanico = user?.role === 'mecanico' ? user.id : null;

            if (!idMecanico) {
                alert('No se encontró el mecánico logeado. Inicia sesión nuevamente.');
                return;
            }

            const clientesResp = await fetch('https://autolog-0mnd.onrender.com/api/clientes');
            const clientesData = await clientesResp.json().catch(() => ([]));

            if (!clientesResp.ok || !Array.isArray(clientesData)) {
                alert('No se pudo obtener la lista de clientes.');
                return;
            }

            const correo = correoCliente.value.trim().toLowerCase();
            const cliente = clientesData.find((c) => {
                const email = (c.email || c.correo || c.mail || '').toString().toLowerCase();
                return email === correo;
            });

            if (!cliente) {
                alert('No se encontró un cliente con ese correo.');
                return;
            }

            const idCliente = cliente.id ?? cliente.idCliente;
            if (!idCliente) {
                alert('No se pudo obtener el ID del cliente.');
                return;
            }

            const anioValue = parseInt(anyo.value, 10);
            if (!matricula.value || !marca.value || !modelo.value || !anioValue) {
                alert('Completa matrícula, marca, modelo y año.');
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
            if (estadoRevision.value) formData.append('estadoRevision', estadoRevision.value);
            if (foto?.files && foto.files[0]) {
                formData.append('imagen', foto.files[0]);
            }

            console.log("Datos a enviar (multipart):", Object.fromEntries(formData.entries()));

            const resp = await fetch('https://autolog-0mnd.onrender.com/api/vehiculos/con-imagen', {
                method: 'POST',
                body: formData
            });

            const data = await resp.json().catch(() => ({}));

            if (!resp.ok) {
                const msg = data.message || data.error || 'Error al registrar el vehículo.';
                alert(msg);
                return;
            }

            form.reset();
            window.location.href = "home.html";
        } catch (e) {
            console.error("✗ Error al registrar vehículo: ", e);
            alert("Error: " + e.message);
        }
    })
});