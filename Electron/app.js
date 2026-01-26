const fs = require('fs');

const data = fs.readFileSync('ejemplo.json', 'utf-8');

const entries = JSON.parse(data);

//mostrar cada entrada en una tarjeta
let mostrarentradas = () => {
    let wrapper = document.getElementById('wrapper');
    entries.forEach((entry, index) => {
        wrapper.innerHTML += `
                    <div class="car-card">
                        <button class="btn-edit"><a href="screens/"><img src="../icons/edit.png"></a></button>
                        <p><strong>Cliente:</strong> ${entry.cliente}</p>
                        <p><strong>Marca:</strong> ${entry.marca}</p>
                        <p><strong>Modelo:</strong> ${entry.modelo}</p>
                        <div class="buttons-bottom">
                            <button class="btn-details"><a href="details.html"><img src="../icons/add.png"></a></button>
                            <button class="btn-remove"><img src="../icons/check.png"></button>
                        </div>
                    </div>
                `;
    });
}
mostrarentradas();