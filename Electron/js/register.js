import { auth, db } from './firebase.js';
import { createUserWithEmailAndPassword } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-auth.js';
import { doc, setDoc } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-firestore.js';

const form = document.querySelector('form');
const username = document.getElementById('username');
const email = document.getElementById('email');
const password = document.getElementById('password');
const confirmPassword = document.getElementById('confirm_password');
const tipoCheckbox = document.getElementById('tipo');
// const switchLabel = document.getElementById('switch-label');


form.addEventListener('submit', (e) => {
    e.preventDefault();

    // Limpiar mensaje de error anterior
    document.getElementById('error-message').style.display = 'none';

    // VALIDACIONES
    const errorElement = document.getElementById('error-message');
    if (!email.value || !username.value || !password.value || !confirmPassword.value) {
        errorElement.textContent = 'Por favor, completa todos los campos.';
        errorElement.style.display = 'block';
        return;
    }

    if (password.value !== confirmPassword.value) {
        errorElement.textContent = 'Las contraseñas no coinciden.';
        errorElement.style.display = 'block';
        return;
    }

    if (password.value.length < 6) {
        errorElement.textContent = 'La contraseña debe tener al menos 6 caracteres.';
        errorElement.style.display = 'block';
        return;
    }

    // CREAR USUARIO CON AUTH DE FIREBASE
    createUserWithEmailAndPassword(auth, email.value, password.value)
    .then(async (userCredential) => {
        const user = userCredential.user;

        // Guardar datos adicionales en Firestore
        const tipo = tipoCheckbox.checked;
        await setDoc(doc(db, "usuario", user.uid), {
            email: user.email,
            username: username.value,
            esMecanico: tipo,
            createdAt: new Date()
        });

        // Redirigir según el tipo de usuario
        if (tipo === false) {
            window.location.href = '../screensCliente/home.html';
        } else {
            window.location.href = '../screensMecanico/home.html';
        }
    })
    .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.error('Error en registro:', errorCode, errorMessage);
        const errorElement = document.getElementById('error-message');
        // Mostrar mensaje de error al usuario
        if (errorCode === 'auth/email-already-in-use') {
            errorElement.textContent = 'El correo electrónico ya está registrado.';
        } else if (errorCode === 'auth/weak-password') {
            errorElement.textContent = 'La contraseña es demasiado débil.';
        } else {
            errorElement.textContent = 'Error en el registro: ' + errorMessage;
        }
        errorElement.style.display = 'block';
    });
});