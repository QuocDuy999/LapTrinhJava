AOS.init();

    // Navbar scroll
    window.addEventListener('scroll', () => {
        const navbar = document.getElementById('navbar');
        navbar.classList.toggle('scrolled', window.scrollY > 50);
        document.getElementById("backToTop").style.display = window.scrollY > 300 ? "block" : "none";
    });

    // Back to top
    document.getElementById("backToTop").onclick = function () {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    // Mobile menu
    const mobileMenu = document.querySelector('.mobile-menu');
    const navMenu = document.querySelector('.nav-menu');
    mobileMenu.addEventListener('click', () => {
        navMenu.classList.toggle('active');
        mobileMenu.classList.toggle('active');
    });