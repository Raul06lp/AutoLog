const electron = require('electron');
const { app, BrowserWindow } = electron;

function createWindow() {
    const win = new BrowserWindow({
        fullscreen: true,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false
        }
    });
    win.loadFile('screens/home.html');
    win.webContents.openDevTools();
    win.setMenu(null);
}
app.on('ready', createWindow);
app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') app.quit();    
});