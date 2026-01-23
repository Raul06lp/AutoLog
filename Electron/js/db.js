import { initializeApp } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-app.js';
import { getFirestore, collection, addDoc } from 'https://www.gstatic.com/firebasejs/12.8.0/firebase-firestore.js';

// CONEXIÓN A FIREBASE
const firebaseConfig = {
  apiKey: "AIzaSyBXd0Z9bmUZxsAuLwPS-aR3KKTuZvf1iDs",
  authDomain: "autolog-67.firebaseapp.com",
  projectId: "autolog-67",
  storageBucket: "autolog-67.firebasestorage.app",
  messagingSenderId: "105501418916",
  appId: "1:105501418916:web:04a0c821e36f5d287e8d31",
  measurementId: "G-12RVQ6MXXR"
};

// Inicializar Firebase
const app = initializeApp(firebaseConfig);
console.log("Firebase inicializado correctamente.");

const db = getFirestore(app);
console.log("Firestore inicializado correctamente.");

// Función para agregar un documento de ejemplo
async function addExampleDoc() {
  try {
    const docRef = await addDoc(collection(db, "usuario"), {
      contrasena: "67",
      correo: "brandon@email.com",
      nombre: "brandon",
      tipo: "mecanico"
    });
    console.log("Documento escrito con ID: ", docRef.id);
  } catch (e) {
    console.error("Error al agregar documento: ", e);
  }
}

addExampleDoc();