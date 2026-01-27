import { auth, db } from './firebase.js';
import { createUserWithEmailAndPassword } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-auth.js';
import { doc, setDoc } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-firestore.js';

const form = document.querySelector('form');
const username = document.getElementById('username');
const email = document.getElementById('email');
const password = document.getElementById('password');
const confirmPassword = document.getElementById('confirm_password');
const tipoCheckbox = document.getElementById('tipo');
const switchLabel = document.getElementById('switch-label');

// Actualizar etiqueta del switch
tipoCheckbox.addEventListener('change', () => {
    switchLabel.textContent = tipoCheckbox.checked ? 'Cliente' : 'Mecánico';
});

// Inicializar etiqueta
switchLabel.textContent = tipoCheckbox.checked ? 'Cliente' : 'Mecánico';
form.addEventListener('submit', (e) => {
    e.preventDefault();

    // VALIDACIONES
    if (!email.value || !username.value || !password.value || !confirmPassword.value) {
        alert('Por favor, completa todos los campos.');
        return;
    }

    if (password.value !== confirmPassword.value) {
        alert('Las contraseñas no coinciden.');
        return;
    }

    if (password.value.length < 6) {
        alert('La contraseña debe tener al menos 6 caracteres.');
        return;
    }

    // CREAR USUARIO CON AUTH DE FIREBASE
    createUserWithEmailAndPassword(auth, email.value, password.value)
    .then(async (userCredential) => {
        const user = userCredential.user;

        // Guardar datos adicionales en Firestore
        await setDoc(doc(db, "users", user.uid), {
            email: user.email,
            username: username.value,
            tipo: tipoCheckbox.checked ? 'mecanico' : 'cliente',
            createdAt: new Date()
        });

        alert('Usuario registrado exitosamente. Bienvenido, ' + user.email + '!');
        window.location.href = './../screens/home.html';
    })
    .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.error('Error en registro:', errorCode, errorMessage);
        // Mostrar mensaje de error al usuario
        if (errorCode === 'auth/email-already-in-use') {
            alert('El correo electrónico ya está registrado.');
        } else if (errorCode === 'auth/weak-password') {
            alert('La contraseña es demasiado débil.');
        } else {
            alert('Error en el registro: ' + errorMessage);
        }
    });
});