document.addEventListener('DOMContentLoaded', () => {
    const images = document.querySelectorAll('.gallery img');
    images.forEach(img => {
        img.addEventListener('click', () => {
            images.forEach(i => i.classList.remove('highlight'));
            img.classList.add('highlight');
        });
    });
});