const electron = require('electron');
const { app, BrowserWindow } = electron;

function createWindow() {
    const win = new BrowserWindow({
        show: false,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false
        }
    });

    win.loadFile('index.html');
    win.setMenu(null);

    // Pantalla Completa
    win.maximize(); 
    

    // Mostramos la ventana cuando esté lista
    win.once('ready-to-show', () => {
        win.show();
        win.webContents.openDevTools();
    });
}

app.on('ready', createWindow);
app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') app.quit();    
});