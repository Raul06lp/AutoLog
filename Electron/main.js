const electron = require('electron');
const { app, BrowserWindow, Menu } = electron;
const remoteMain = require('@electron/remote/main');
const path = require('path');

remoteMain.initialize();

const templateMenu = [
  {
    label: 'Opciones',
    submenu: [
      {
        label: 'Abrir DevTools',
        accelerator: 'Ctrl+Shift+I', // Atajo de teclado
        click(item, focusedWindow) {
          focusedWindow.toggleDevTools(); // Abre/Cierra las herramientas
        }
      },
      { type: 'separator' },
      { label: 'Salir', role: 'quit' }
    ]
  },
  {
    label: 'Ver',
    submenu: [
      { role: 'reload' },
      { role: 'forceReload' }
    ]
  }
];

try {
  require('electron-reloader')(module);
} catch (_) {}

const mainMenu = Menu.buildFromTemplate(templateMenu);
Menu.setApplicationMenu(mainMenu);

function createWindow() {
    const win = new BrowserWindow({
        show: false,
        icon: path.join(__dirname, 'icons/logo.ico'),
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false
        }
    });

    win.loadFile('index.html');

    // Pantalla Completa
    win.maximize(); 
    

    // Mostramos la ventana cuando esté lista
    win.once('ready-to-show', () => {
        win.show();
        // win.webContents.openDevTools();
    });
    //remote 
    remoteMain.enable(win.webContents);
}

app.on('ready', createWindow);
app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') app.quit();    
});