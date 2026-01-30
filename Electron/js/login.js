import { auth, db } from './firebase.js';
import {
    signInWithEmailAndPassword,
    setPersistence,
    browserLocalPersistence,
    onAuthStateChanged
} from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-auth.js';
import { doc, getDoc } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-firestore.js';

const email = document.getElementById('email');
const password = document.getElementById('password');
const form = document.querySelector('form');

// Si ya hay una sesión activa, redirigir automáticamente según el rol
onAuthStateChanged(auth, async (user) => {
    console.log('[onAuthStateChanged] user:', user);
    if (!user) return; // no hay sesión
    try {
        const userDoc = await getDoc(doc(db, "usuario", user.uid));
        if (userDoc.exists()) {
            const tipo = userDoc.data().esMecanico;
            if (tipo == false) {
                window.location.href = 'screensCliente/home.html';
            } else {
                window.location.href = 'screensMecanico/home.html';
            }
        }
    } catch (err) {
        console.error('Error al obtener datos de usuario en onAuthStateChanged:', err);
    }
});

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Limpiar mensaje de error anterior
    const errorElement = document.getElementById('error-message');
    if (errorElement) errorElement.style.display = 'none';

    try {
        // Asegurar que la sesión se persista entre reinicios de la app
        await setPersistence(auth, browserLocalPersistence);

        const userCredential = await signInWithEmailAndPassword(auth, email.value, password.value);
        const user = userCredential.user;

        // Obtener datos del usuario desde Firestore
        const userDoc = await getDoc(doc(db, "usuario", user.uid));
        if (userDoc.exists()) {
            const tipo = userDoc.data().esMecanico;
            if (tipo == false) {
                window.location.href = 'screensCliente/home.html';
            } else {
                window.location.href = 'screensMecanico/home.html';
            }
        } else {
            console.error('No se encontraron datos del usuario en Firestore');
            if (errorElement) {
                errorElement.textContent = 'No se pudieron obtener los datos del usuario.';
                errorElement.style.display = 'block';
            }
        }
    } catch (error) {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.error('Error en login:', errorCode, errorMessage);
        if (errorElement) {
            if (errorCode === 'auth/user-not-found') {
                errorElement.textContent = 'Usuario no encontrado.';
            } else if (errorCode === 'auth/wrong-password') {
                errorElement.textContent = 'Contraseña incorrecta.';
            } else {
                errorElement.textContent = 'El usuario o la contraseña son incorrectos.';
            }
            errorElement.style.display = 'block';
        } else {
            alert(errorMessage);
        }
    }
});