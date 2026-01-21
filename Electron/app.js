const fs=require('fs');

const data=fs.readFileSync('ejemplo.json','utf-8');

const entries=JSON.parse(data);

//mostrar cada entrada en una tarjeta
let mostrarentradas=()=>{
    let wrapper=document.getElementById('wrapper');
    entries.forEach(entry=>{
        wrapper.innerHTML+=`
            <div class="car-card">
                <p><strong>Cliente:</strong> ${entry.cliente}</p>
                <p><strong>Marca:</strong> ${entry.marca}</p>
                <p><strong>Modelo:</strong> ${entry.modelo}</p>
            </div>
        `;
    }
    );
}
 mostrarentradas();