
const { BrowserWindow, dialog } = require('@electron/remote');
const fs = require('fs');
const path = require('path');

const btnGenerar = document.getElementById('btnGenerar');
const btnAddTrabajo = document.getElementById('btnAddTrabajo');
const tablaBody = document.querySelector('#tablaFormTrabajos tbody');

const nombreCliente = document.getElementById("nombreCliente");
const telefono = document.getElementById("telefono");
const marca = document.getElementById("marca");
const modelo = document.getElementById("modelo");
const matricula = document.getElementById("matricula");
const kilometraje = document.getElementById("kilometraje");
const fecha = document.getElementById("fecha");
const observaciones = document.getElementById("observaciones");
const estadoPago = document.getElementById("estadoPago");

btnAddTrabajo.addEventListener('click', () => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
        <td><input type="text" class="trabajo-nombre" placeholder="Ej: Cambio aceite"></td>
        <td><input type="number" class="trabajo-precio" step="0.01" placeholder="0.00"></td>
        <td><button type="button" class="btnDelete">❌</button></td>
    `;
    tr.querySelector('.btnDelete').onclick = () => tr.remove();
    tablaBody.appendChild(tr);
});

btnGenerar.addEventListener('click', async (e) => {
    e.preventDefault();
    const errorElement = document.getElementById('error-message');
    if (errorElement) errorElement.style.display = 'none';

    const trabajosRealizados = [];
    document.querySelectorAll('#tablaFormTrabajos tbody tr').forEach(row => {
        const nombre = row.querySelector('.trabajo-nombre').value.trim();
        const precio = parseFloat(row.querySelector('.trabajo-precio').value || 0);
        if (nombre) trabajosRealizados.push({ nombre, precio });
    });

    // Validaciones básicas
    if (!nombreCliente.value || !marca.value || !modelo.value || !fecha.value) {
        if (errorElement) {
            errorElement.textContent = 'Completa los campos obligatorios: cliente, marca, modelo y fecha.';
            errorElement.style.display = 'block';
        }
        return;
    }

    if (trabajosRealizados.length === 0) {
        if (errorElement) {
            errorElement.textContent = 'Agrega al menos un trabajo realizado.';
            errorElement.style.display = 'block';
        }
        return;
    }

    const datos = {
        nombreCliente: nombreCliente.value,
        telefono: telefono.value,
        marca: marca.value,
        modelo: modelo.value,
        matricula: matricula.value,
        kilometraje: kilometraje.value,
        fecha: fecha.value,
        observaciones: observaciones.value,
        estadoPago: estadoPago.value,
        trabajosRealizados
    };

    await generarPDF(datos, errorElement);
});


async function generarPDF(datos, errorElement) {
    const pdfWindow = new BrowserWindow({ show: false });

    try {
        const plantillaPath = path.join(__dirname, 'plantilla.html');
        await pdfWindow.loadFile(plantillaPath);

        // Pasar datos al renderer del PDF
        await pdfWindow.webContents.executeJavaScript(`
            document.getElementById('nombreCliente').innerText = \`${datos.nombreCliente}\`;
            document.getElementById('telefono').innerText = \`${datos.telefono}\`;
            document.getElementById('marca').innerText = \`${datos.marca}\`;
            document.getElementById('modelo').innerText = \`${datos.modelo}\`;
            document.getElementById('matricula').innerText = \`${datos.matricula}\`;
            document.getElementById('kilometraje').innerText = \`${datos.kilometraje}\`;
            document.getElementById('fecha').innerText = \`${datos.fecha}\`;
            document.getElementById('observaciones').innerText = \`${datos.observaciones}\`;
            document.getElementById('estadoPago').innerText = \`${datos.estadoPago}\`;

            const tbody = document.querySelector('#tablaTrabajos tbody');
            tbody.innerHTML = '';

            let total = 0;
            const trabajos = ${JSON.stringify(datos.trabajosRealizados)};
            trabajos.forEach(t => {
                const tr = document.createElement('tr');
                tr.innerHTML = \`<td>\${t.nombre}</td><td>\${t.precio.toFixed(2)} €</td>\`;
                tbody.appendChild(tr);
                total += t.precio;
            });

            document.getElementById('total').innerText = total.toFixed(2) + ' €';
        `);

        // Generar PDF
        const buffer = await pdfWindow.webContents.printToPDF({ printBackground: true });

        // Guardar archivo
        const { filePath } = await dialog.showSaveDialog({
            title: 'Guardar Informe',
            defaultPath: 'informe.pdf',
            filters: [{ name: 'PDF', extensions: ['pdf'] }]
        });

        if (!filePath) return;

        fs.writeFileSync(filePath, buffer);
        if (errorElement) {
            errorElement.textContent = 'PDF guardado correctamente';
            errorElement.style.display = 'block';
            errorElement.style.color = '#198754';
            errorElement.style.background = '#eafaf1';
            errorElement.style.border = '1px solid #b6e2cd';
        }
        pdfWindow.close();
    } catch (err) {
        pdfWindow.close();
        if (errorElement) {
            errorElement.textContent = 'Error al generar PDF: ' + (err.message || 'Error desconocido.');
            errorElement.style.display = 'block';
            errorElement.style.color = '#dc2626';
            errorElement.style.background = '#fef2f2';
            errorElement.style.border = '1px solid #fca5a5';
        }
    }
}
