const { app, BrowserWindow} = require('electron');
const path = require('path');

function createWindow() {
  const win = new BrowserWindow({
    width: 1200,
    height: 800,
    title: "Admin Dashboard - Petitii",
    webPreferences: {
      nodeIntegration: false
    }
  });


win.loadURL
('http://localhost:4200/admin-dashboard');

win.setMenuBarVisibility(false);
}

app.whenReady().then(createWindow);


app.on('window-all-closed', () => {
    if(process.platform !== 'darwin') app.quit();
});


