import { collection, addDoc } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-firestore.js';
import { db } from './firebase.js';

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
    const observaciones = document.getElementById('observaciones');

    console.log("Formulario de registro de vehículo listo.");

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        try {
            console.log("Iniciando registro de vehículo...");
            
            const vehiculoData = {
                correo: correoCliente.value,
                marca: marca.value,
                modelo: modelo.value,
                matricula: matricula.value,
                anyo: parseInt(anyo.value) || 0,
                color: color.value,
                kilometraje: parseInt(kilometraje.value) || 0,
                estadoRevision: estadoRevision.value,
                observaciones: observaciones.value,
                fechaRegistro: new Date().toISOString()
            };
            
            console.log("Datos a guardar:", vehiculoData);
            
            const docRef = await addDoc(collection(db, "vehiculo"), vehiculoData);
            
            console.log("Vehículo registrado con ID: ", docRef.id);
            form.reset();
            window.location.href = "home.html";
        } catch (e) {
            console.error("✗ Error al agregar documento: ", e);
            alert("Error: " + e.message);
        }
    })
});