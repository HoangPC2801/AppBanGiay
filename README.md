# Tổng quan đồ án

## 1. Mô tả tổng quan đồ án
**Tên dự án kiến nghị:** Hệ thống Thương mại điện tử Bán Giày (Kiến trúc Client-Server)

### Mô hình Kiến trúc:

#### Backend API (Server-side):
- Công nghệ: Python với framework FastAPI.
- Nhiệm vụ: Xử lý toàn bộ logic nghiệp vụ (thêm/sửa/xóa sản phẩm, tính toán giá tiền, quản lý đơn hàng). FastAPI sẽ kết nối trực tiếp với cơ sở dữ liệu và cung cấp các endpoint API.

#### Mobile App (Client - Dành cho khách hàng):
- Công nghệ: Kotlin, sử dụng Jetpack Compose để xây dựng giao diện UI/UX.
- Nhiệm vụ: Là nơi khách hàng xem danh sách giày, tìm kiếm, xem chi tiết, thêm vào giỏ hàng và tiến hành đặt hàng.
- Bảo mật: Tích hợp Firebase Authentication để xử lý luồng đăng nhập/đăng ký (Google SignIn, Email/Password).

#### Web Admin (Client - Dành cho người quản trị):
- Công nghệ: HTML/CSS/JS.
- Nhiệm vụ: Gọi các API từ FastAPI Backend để hiển thị Dashboard, quản lý danh sách sản phẩm, phê duyệt hoặc hủy đơn hàng, quản lý người dùng.

---

## 2. Kế hoạch thực hiện

### Tuần 1: Phân tích, Thiết kế hệ thống & Khởi tạo dự án
- Database: Migrate cơ sở dữ liệu từ project PHP cũ. Chuẩn hóa lại các bảng (Users, Products, Categories, Orders, Order_Details) cho phù hợp với ORM của Python.
- Thiết kế UML: Vẽ các biểu đồ thiết kế phần mềm cốt lõi.
- Khởi tạo Git: Tạo repository mới với 3 thư mục riêng biệt: backend-api, android-app, web-admin. Setup cấu trúc cơ bản cho FastAPI.

### Tuần 2: Xây dựng Core API & Setup Mobile App
#### FastAPI Backend:
- Kết nối Database.
- Viết các API GET lấy danh sách sản phẩm, danh mục, chi tiết giày.
- Dùng Postman hoặc Swagger UI để test API.

#### Android App:
- Khởi tạo project Kotlin. Thiết lập Navigation, cấu trúc thư mục (MVVM architecture).
- Tích hợp Firebase Authentication. Hoàn thiện luồng UI/UX cho màn hình Đăng nhập/Đăng ký.

### Tuần 3: Giao diện Jetpack Compose & Kết nối API
#### Android App:
- Sử dụng Jetpack Compose để code giao diện: Màn hình Home (hiển thị banner, danh sách giày), Màn hình Chi tiết sản phẩm.
- Sử dụng thư viện Retrofit kết hợp Coroutines để gọi API từ Server, parse chuỗi JSON và hiển thị giày lên UI app.

#### FastAPI Backend:
- Viết tiếp các API cho luồng Giỏ hàng và Đặt hàng.

### Tuần 4: Hoàn thiện tính năng Mobile & Xử lý Giỏ hàng
#### Android App:
- Xây dựng luồng Giỏ hàng. Có thể dùng Room Database lưu giỏ hàng local tạm thời hoặc đẩy thẳng lên Backend.
- Xây dựng màn hình Thanh toán và xác nhận đơn hàng.
- Gửi payload (danh sách món hàng, thông tin người đặt) từ App lên API tạo đơn hàng của Backend.

#### FastAPI Backend:
- Viết API quản lý đơn hàng (Lấy danh sách đơn hàng cho Admin, cập nhật trạng thái đơn).

### Tuần 5: Web Admin & Tích hợp hệ thống
- Web Admin: Xây dựng giao diện trang quản trị. Dùng Fetch API để gọi dữ liệu từ FastAPI hiển thị lên bảng danh sách giày, danh sách đơn đặt hàng từ Mobile.
- Testing: Thực hiện kịch bản test end-to-end: Đăng nhập trên App -> Chọn giày -> Đặt hàng -> Đăng nhập Web Admin -> Kiểm tra đơn hàng vừa đổ về -> Chuyển trạng thái đơn -> App hiển thị trạng thái mới.

### Tuần 6: Deployment, Đóng gói & Hoàn thiện báo cáo
- Deployment: Viết các file Dockerfile và docker-compose.yml để đóng gói FastAPI backend và Database thành các container độc lập.
- Hoàn thiện: Bắt lỗi dữ liệu đầu vào trên app, hiển thị Toast/Snackbar báo lỗi.
- Chốt lại các biểu đồ UML khớp với code thực tế và hoàn thiện slide báo cáo tổng kết.