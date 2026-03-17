const server = require('http').createServer();
const io = require('socket.io') (server);

io.on('connection', socket => {
console.log('Usuário conectado:', socket.id);
// Escuta mensagens vindas de um cliente
socket.on('send_message', (data) => {
// Envia para todos os outros conectados (broadcast)
io.emit('receive_message', data);
});
socket.on('disconnect', () => console.log('Usuário saiu'));
});
server.listen(3000, () => console.log('Servidor rodando na porta 3000'));