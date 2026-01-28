import { auth, db } from './firebase.js';
import { signInWithEmailAndPassword } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-auth.js';
import { doc, getDoc } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-firestore.js';

const email = document.getElementById('email');
const password = document.getElementById('password');
const form = document.querySelector('form');

form.addEventListener('submit', (e) => {
    e.preventDefault();

    // Limpiar mensaje de error anterior
    document.getElementById('error-message').style.display = 'none';

    signInWithEmailAndPassword(auth, email.value, password.value)
    .then(async (userCredential) => {
        const user = userCredential.user;

        // Obtener datos del usuario desde Firestore
        const userDoc = await getDoc(doc(db, "usuario", user.uid));
        if (userDoc.exists()) {
            const userData = userDoc.data();
            const tipo = userData.tipo;

            console.log('Usuario logueado:', user.email);
            console.log('tipo:', tipo);

            // Redirigir según el tipo de usuario
            if (tipo === 'cliente') {
                window.location.href = 'screensCliente/home.html';
            } else {
                window.location.href = 'screensMecanico/home.html';
            }
        } else {
            console.error('No se encontraron datos del usuario en Firestore');
            errorElement.textContent = 'No se pudieron obtener los datos del usuario.';
        }
    })
    .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.error('Error en login:', errorCode, errorMessage);
        const errorElement = document.getElementById('error-message');
        if (errorCode === 'auth/user-not-found') {
            errorElement.textContent = 'Usuario no encontrado.';
            errorElement.style.display = 'block';
        } else if (errorCode === 'auth/wrong-password') {
            errorElement.textContent = 'Contraseña incorrecta.';
            errorElement.style.display = 'block';
        } else {
            console.error('Error en login:', errorCode, errorMessage);
            errorElement.textContent = 'El usuario o la contraseña son incorrectos.';
            errorElement.style.display = 'block';
        }
    });
});