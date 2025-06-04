document.addEventListener('DOMContentLoaded', () => {
    const adminActions = document.querySelectorAll('.admin-action');

    adminActions.forEach(action => {
        action.addEventListener('click', () => {
            alert(`You clicked on ${action.textContent}`);
        });
    });
});