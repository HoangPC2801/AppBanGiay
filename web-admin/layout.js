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
                HoangShoes
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
                    <a class="nav-link text-white" href="admin-app.html">Quản lý app</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link text-white" href="admin-chat.html">Chat</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link text-white" href="admin-users-firebase.html">Người dùng Firebase</a>
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
                    HoangShoes - Hệ thống quản lý cửa hàng giày uy tín, chất lượng hàng đầu Việt Nam.
                </p>
            </div>
            <div class="col-md-4 mb-3">
                <h5 class="text-uppercase text-primary">Liên hệ</h5>
                <p class="small text-light mb-1">- Email: admin@hoangshowes.com</p>
                <p class="small text-light">- Điện thoại: 0123 456 789</p>
            </div>
            <div class="col-md-4 mb-3">
                <h5 class="text-uppercase text-primary">Địa chỉ</h5>
                <p class="small text-light">
                    - Tân Bình, TP. Hồ Chí Minh
                </p>
            </div>
        </div>

        <hr class="border-secondary my-3">

        <div class="text-center small text-light">
            <p class="mb-0">&copy; 2026 HoangShoes Admin. Phạm Công Hoàng.</p>
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


async function loadDashboard() {
    try {
        const token = localStorage.getItem('admin_token');
        const response = await fetch('http://127.0.0.1:8000/dashboard/stats', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (!response.ok) throw new Error("Lỗi tải dashboard");
        const data = await response.json();
        
        // Đổ 4 con số thống kê (bạn nhớ gắn ID tương ứng vào các thẻ HTML nhé)
        document.getElementById('stat-orders').innerText = data.total_orders;
        document.getElementById('stat-revenue').innerText = data.total_revenue.toLocaleString() + ' VNĐ';
        document.getElementById('stat-products').innerText = data.total_products;
        document.getElementById('stat-users').innerText = data.total_users;
        
        // Đổ bảng 10 đơn hàng gần nhất
        const tbody = document.getElementById('recent-orders-table');
        tbody.innerHTML = '';
        data.recent_orders.forEach(order => {
            const date = new Date(order.created_at).toLocaleString('vi-VN');
            tbody.innerHTML += `
                <tr>
                    <td>${order.id}</td>
                    <td>${date}</td>
                    <td>${order.username}</td>
                    <td>${order.total.toLocaleString()} VNĐ</td>
                    <td><span class="badge bg-info">${order.status}</span></td>
                    <td><button class="btn btn-sm btn-primary">Xem</button></td>
                </tr>
            `;
        });
    } catch (error) {
        console.error(error);
    }
}

// Gọi hàm này khi đang ở trang admin-dashboard.html
if (window.location.pathname.includes('admin-dashboard.html')) {
    loadDashboard();
}

async function loadOrders(statusFilter = '') {
    try {
        const token = localStorage.getItem('admin_token');
        const url = statusFilter ? `http://127.0.0.1:8000/orders/?status=${statusFilter}` : 'http://127.0.0.1:8000/orders/';
        
        const response = await fetch(url, { headers: { 'Authorization': `Bearer ${token}` } });
        const orders = await response.json();
        
        // Dòng này giúp bạn nhìn thấy rốt cuộc API đang trả về cái gì
        console.log("Dữ liệu danh sách đơn hàng:", orders); 

        const tbody = document.getElementById('orders-table');
        if(!tbody) return;
        tbody.innerHTML = '';

        orders.forEach(order => {
            // Lấy id an toàn
            const orderId = order.id || order.order_id;
            const date = new Date(order.created_at).toLocaleString('vi-VN');
            
            // CHÚ Ý: orderId được bọc trong dấu nháy đơn ('${orderId}') để tránh lỗi undefined
            let cancelBtn = order.status === 'pending' 
                ? `<button class="btn btn-sm btn-danger ms-1" onclick="updateOrderStatus('${orderId}', 'cancelled')">Hủy</button>` 
                : '';
                
            let statusSelect = `
                <select class="form-select form-select-sm" onchange="updateOrderStatus('${orderId}', this.value)">
                    <option value="pending" ${order.status==='pending'?'selected':''}>Chờ xử lý</option>
                    <option value="processing" ${order.status==='processing'?'selected':''}>Đang xử lý</option>
                    <option value="shipped" ${order.status==='shipped'?'selected':''}>Đang giao</option>
                    <option value="completed" ${order.status==='completed'?'selected':''}>Hoàn thành</option>
                    <option value="cancelled" ${order.status==='cancelled'?'selected':''}>Đã hủy</option>
                </select>
            `;

            tbody.innerHTML += `
                <tr>
                    <td>${orderId}</td>
                    <td>${date}</td>
                    <td>${order.username}</td>
                    <td>${order.total.toLocaleString()} VNĐ</td>
                    <td>${statusSelect}</td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="viewOrderDetail('${orderId}')">Xem</button>
                        ${cancelBtn}
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        console.error("Lỗi load orders:", error);
    }
}

// Hàm gửi API cập nhật trạng thái
async function updateOrderStatus(orderId, newStatus) {
    // Ngăn chặn việc bấm nhầm nếu giá trị truyền vào bị lỗi undefined
    if (!orderId || !newStatus || orderId === "undefined" || newStatus === "undefined") {
        console.error("Lỗi Frontend: ID hoặc Trạng thái đang bị undefined!", { orderId, newStatus });
        alert("Không thể cập nhật: Lỗi dữ liệu giao diện (undefined).");
        return;
    }

    if (newStatus === 'cancelled' && !confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')) {
        window.location.reload(); // Tải lại để reset select box về trạng thái cũ nếu bấm hủy xác nhận
        return;
    }
    
    try {
        const token = localStorage.getItem('admin_token');
        const response = await fetch(`http://127.0.0.1:8000/orders/${orderId}/status?status=${newStatus}`, {
            method: 'PUT',
            headers: { 
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            alert('Cập nhật trạng thái đơn hàng thành công!');
            window.location.reload(); 
        } else {
            const errData = await response.json();
            // Sửa lỗi [object Object] bằng cách lấy trường .detail từ FastAPI trả về
            alert('Lỗi từ hệ thống: ' + (errData.detail || JSON.stringify(errData)));
            window.location.reload();
        }
    } catch (error) {
        console.error('Network Error:', error);
        alert('Có lỗi xảy ra khi kết nối tới máy chủ API!');
    }
}

// Bắt sự kiện Lọc đơn hàng
function handleFilterSubmit(event) {
    event.preventDefault(); // Ngăn form load lại trang
    const status = document.getElementById('status-filter').value;
    loadOrders(status);
}

// Gọi tự động khi ở trang Orders
if (window.location.pathname.includes('admin-orders.html')) {
    loadOrders();
    // Lắng nghe form Lọc (Giả sử thẻ form có id="filter-form")
    document.getElementById('filter-form')?.addEventListener('submit', handleFilterSubmit);
}

async function viewOrderDetail(orderId) {
    // Kiểm tra an toàn phòng trường hợp biến truyền vào bị undefined
    if (!orderId || orderId === "undefined") {
        console.error("Lỗi Frontend: orderId truyền vào bị undefined!");
        alert("Không thể xem chi tiết: Lỗi xác định mã đơn hàng.");
        return;
    }

    try {
        const token = localStorage.getItem('admin_token');
        
        // Gọi API Backend lấy chi tiết đơn hàng theo ID
        const response = await fetch(`http://127.0.0.1:8000/orders/${orderId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        // Nếu Backend trả về lỗi (404, 500...), bóc tách thông báo lỗi ra hiển thị
        if (!response.ok) {
            const errData = await response.json();
            alert("Lỗi từ máy chủ: " + (errData.detail || "Không thể tải chi tiết đơn hàng"));
            return;
        }

        const order = await response.json();

        // Đổ thông tin cơ bản của đơn hàng lên Modal
        document.getElementById('detail-order-id').innerText = order.id;
        document.getElementById('detail-order-date').innerText = new Date(order.created_at).toLocaleString('vi-VN');
        document.getElementById('detail-order-total').innerText = order.total.toLocaleString() + ' VNĐ';
        document.getElementById('detail-order-status').innerText = order.status.toUpperCase();

        // Tìm bảng chứa danh sách sản phẩm trong Modal
        const tbody = document.getElementById('order-items-detail-body');
        if (tbody) {
            tbody.innerHTML = ''; // Xóa sạch dữ liệu cũ trước khi đổ mới

            // Nếu không có chi tiết món hàng hoặc mảng trống
            if (!order.items || order.items.length === 0) {
                tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted py-3">Đơn hàng này không có chi tiết sản phẩm.</td></tr>`;
            } else {
                // Duyệt qua từng món hàng để hiển thị lên bảng
                order.items.forEach((item, index) => {
                    const imgUrl = item.image_url || 'https://via.placeholder.com/50';
                    tbody.innerHTML += `
                        <tr>
                            <td class="text-center align-middle">${index + 1}</td>
                            <td class="align-middle">
                                <div class="d-flex align-items-center">
                                    <img src="${imgUrl}" alt="${item.product_name}" style="width: 50px; height: 50px; object-fit: cover; border-radius: 4px;" class="me-2">
                                    <span>${item.product_name}</span>
                                </div>
                            </td>
                            <td class="text-end align-middle">${item.price.toLocaleString()} VNĐ</td>
                            <td class="text-center align-middle">${item.quantity}</td>
                            <td class="text-end align-middle fw-bold">${item.total_price.toLocaleString()} VNĐ</td>
                        </tr>
                    `;
                });
            }
        }

        // Kích hoạt hiển thị Bootstrap Modal bằng JavaScript
        const detailModalEl = document.getElementById('orderDetailModal');
        if (detailModalEl) {
            const detailModal = new bootstrap.Modal(detailModalEl);
            detailModal.show();
        } else {
            alert("Lỗi giao diện: Không tìm thấy thẻ HTML Modal '#orderDetailModal' trong trang!");
        }

    } catch (error) {
        console.error('Fetch Error:', error);
        alert("Lỗi kết nối mạng: Không thể kết nối tới máy chủ API.");
    }
}