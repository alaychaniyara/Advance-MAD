const http = require('http');
const app = require('./app');

const port = process.env.PORT || 80;

const server = http.createServer(app);



//server.listen("dev.local", 300 );

server.listen(3000);

