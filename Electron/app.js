const fs = require('fs');

let entries = [];
try {
    if (fs.existsSync('ejemplo.json')) {
        const data = fs.readFileSync('ejemplo.json', 'utf-8');
        entries = JSON.parse(data);
    } else {
        console.warn('ejemplo.json no encontrado — usando lista vacía');
        entries = [];
    }
} catch (err) {
    console.error('Error leyendo ejemplo.json:', err);
    entries = [];
}

//mostrar cada entrada en una tarjeta
let mostrarentradas = () => {
    let wrapper = document.getElementById('wrapper');
    entries.forEach((entry, index) => {
        wrapper.innerHTML += `
                    <div class="car-card">
                        <button class="btn-edit" onclick="location.href='./edit.html'"><img src="../icons/edit.png"></button>
                        <p>${entry.img}</p>
                        <p><strong>Cliente:</strong> ${entry.cliente}</p>
                        <p><strong>Marca:</strong> ${entry.marca}</p>
                        <p><strong>Modelo:</strong> ${entry.modelo}</p>
                        <div class="buttons-bottom">
                            <button class="btn-details" onclick="location.href='./details.html'"><img src="../icons/add.png"></button>
                            <button class="btn-remove"><img src="../icons/check.png"></button>
                        </div>
                    </div>
                `;
    });
}
mostrarentradas();