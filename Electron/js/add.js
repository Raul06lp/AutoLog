import { collection, addDoc } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-firestore.js';
import { getStorage, ref, uploadBytesResumable, getDownloadURL } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-storage.js';
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
    const foto = document.getElementById('foto');
    const observaciones = document.getElementById('observaciones');

    console.log("Formulario de registro de vehículo listo.");

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        try {
            const storage = getStorage();
            let fotoURL = '';

            // Subir imagen a Storage
            if (foto.files && foto.files[0]) {
                const file = foto.files[0];
                const storageRef = ref(storage, 'vehiculos/' + Date.now() + '_' + file.name);
                const uploadTask = uploadBytesResumable(storageRef, file);

                // Esperar a que se complete la carga
                await new Promise((resolve, reject) => {
                    uploadTask.on('state_changed',
                        (snapshot) => {
                            const progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                            console.log('Progreso de carga: ' + progress + '%');
                        },
                        (error) => {
                            console.error("Error en carga:", error);
                            reject(error);
                        },
                        async () => {
                            fotoURL = await getDownloadURL(uploadTask.snapshot.ref);
                            console.log("Imagen subida. URL: ", fotoURL);
                            resolve();
                        }
                    );
                });
            }

            const vehiculoData = {
                correo: correoCliente.value,
                marca: marca.value,
                modelo: modelo.value,
                matricula: matricula.value,
                anyo: parseInt(anyo.value) || 0,
                color: color.value,
                kilometraje: parseInt(kilometraje.value) || 0,
                estadoRevision: estadoRevision.value,
                foto: fotoURL,
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