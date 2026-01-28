import { collection, addDoc } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-firestore.js';

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

    form.addEventListener('submit', (e) => {
        e.preventDefault();


        async function agregarVehiculo() {
            try {
                const docRef = await addDoc(collection(db, "vehiculo"))
            } catch (e) {

            }
        }
    })
});