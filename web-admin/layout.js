const adminRole = localStorage.getItem('admin_role') || 'manager';

let superAdminMenu = '';
if (adminRole === 'superadmin') {
    superAdminMenu = `<li class="nav-item">
        <a class="nav-link text-white" href="admin-admins.html">Quản trị viên</a>
    </li>`;
}

// Header (Navbar)
const adminHeaderHTML = `
<div class="header-wrapper bg-dark text-white p-3 mb-4">
    <div class="header-container container d-flex justify-content-between align-items-center">
        
        <div class="logo">
            <a href="admin-dashboard.html" class="text-white text-decoration-none fs-4 fw-bold">
                ShoeStore Admin
            </a>
        </div>

        <nav class="main-nav">
            <ul class="nav">
                <li class="nav-item">
                    <a class="nav-link text-white" href="admin-dashboard.html">Bảng điều khiển</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link text-white" href="admin-products.html">Sản phẩm</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link text-white" href="admin-orders.html">Đơn hàng</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link text-white" href="admin-users.html">Người dùng</a>
                </li>

                ${superAdminMenu}

                <li class="nav-item">
                    <a class="nav-link text-danger" href="#" onclick="handleLogout()">Đăng xuất</a>
                </li>
            </ul>
        </nav>
    </div>
</div>
`;

// Footer
const adminFooterHTML = `
<footer class="bg-dark text-white pt-5 pb-3 mt-auto">
    <div class="container">
        <div class="row">
            <div class="col-md-4 mb-3">
                <h5 class="text-uppercase text-primary">Về chúng tôi</h5>
                <p class="small text-light">
                    BizFlow Admin - Hệ thống quản lý cửa hàng giày uy tín, chất lượng hàng đầu Việt Nam.
                </p>
            </div>
            <div class="col-md-4 mb-3">
                <h5 class="text-uppercase text-primary">Liên hệ</h5>
                <p class="small text-light mb-1">- Email: admin@bizflow.com</p>
                <p class="small text-light">- Điện thoại: 0123 456 789</p>
            </div>
            <div class="col-md-4 mb-3">
                <h5 class="text-uppercase text-primary">Địa chỉ</h5>
                <p class="small text-light">
                    - 123 Đường ABC, TP. Hồ Chí Minh
                </p>
            </div>
        </div>

        <hr class="border-secondary my-3">

        <div class="text-center small text-light">
            <p class="mb-0">&copy; 2026 BizFlow Admin. All rights reserved.</p>
        </div>
    </div>
</footer>
`;

// Load Header + Footer
function loadLayout() {
    const headerEl = document.getElementById('admin-header');
    const footerEl = document.getElementById('admin-footer');

    if (headerEl) headerEl.innerHTML = adminHeaderHTML;
    if (footerEl) footerEl.innerHTML = adminFooterHTML;

    // Highlight menu active
    const currentPath = window.location.pathname.split('/').pop();

    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });
}

// Logout
function handleLogout() {
    if (confirm("Bạn có chắc chắn muốn đăng xuất?")) {
        localStorage.removeItem('admin_role');
        localStorage.removeItem('admin_id');
        localStorage.removeItem('admin_token');

        window.location.href = 'admin-login.html';
    }
}

// 7. Auto load
document.addEventListener('DOMContentLoaded', loadLayout);