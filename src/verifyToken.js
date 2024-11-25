const admin = require('firebase-admin');

// Função para verificar o token e retornar o userId
const verifyToken = async (token) => {
    try {
        // Verifica o token com o firebase-admin
        const decodedToken = await admin.auth().verifyIdToken(token);
        return decodedToken.uid;  // Retorna o UID do usuário
    } catch (error) {
        console.error('Token verification failed:', error);
        return null;  // Se o token for inválido ou expirado
    }
};

module.exports = verifyToken;
