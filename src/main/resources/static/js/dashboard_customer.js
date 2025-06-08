document.addEventListener('DOMContentLoaded', () => {
    // Example: Sidebar toggle functionality
    const sidebar = document.querySelector('.sidebar');
    const toggleButton = document.querySelector('.sidebar-toggle');

    if (toggleButton) {
        toggleButton.addEventListener('click', () => {
            sidebar.classList.toggle('open');
        });
    }
});