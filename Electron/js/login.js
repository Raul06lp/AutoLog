import { auth } from './firebase.js';
import { signInWithEmailAndPassword } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-auth.js';

const email = document.getElementById('email');
const password = document.getElementById('password');
const form = document.querySelector('form');

form.addEventListener('submit', (e) => {
    e.preventDefault();

    signInWithEmailAndPassword(auth, email.value, password.value)
    .then((userCredential) => {
        const user = userCredential.user;

        user.
        window.location.href = './screens/home.html';
    })
    .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.error('Error en login:', errorCode, errorMessage);
        if (errorCode === 'auth/user-not-found') {
            alert('Usuario no encontrado.');
        } else if (errorCode === 'auth/wrong-password') {
            alert('Contraseña incorrecta.');
        } else {
            alert('Error en login: ' + errorMessage);
        }
    });
});