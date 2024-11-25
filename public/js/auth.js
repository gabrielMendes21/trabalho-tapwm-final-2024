// Verificar se o usuário está autenticado
function checkUserAuthentication() {
    const userUID = localStorage.getItem('userUID');

    if (!userUID) {
        // Se não houver UID no localStorage, o usuário não está autenticado
        alert("You must be logged in to access this page.");
        window.location.href = "/login"; // Redireciona para o login
    } else {
        console.log("User is authenticated with UID:", userUID);
        // O usuário está autenticado, você pode carregar os dados ou o conteúdo da página
    }
}

// Chame a função de verificação ao carregar a página
document.addEventListener("DOMContentLoaded", checkUserAuthentication);
