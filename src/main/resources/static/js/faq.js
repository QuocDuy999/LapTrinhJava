document.addEventListener('DOMContentLoaded', () => {
    // Dữ liệu mẫu các câu hỏi thường gặp
    const faqData = [
        {
            id: 1,
            question: "Chu kỳ kinh nguyệt bình thường kéo dài bao nhiêu ngày?",
            answer: "Chu kỳ kinh nguyệt bình thường thường kéo dài từ 21 đến 35 ngày, tính từ ngày đầu tiên của kỳ kinh này đến ngày đầu tiên của kỳ kinh tiếp theo. Số ngày hành kinh thường từ 3 đến 7 ngày. Tuy nhiên, mỗi người có thể có chu kỳ khác nhau tùy thuộc vào cơ địa và sức khỏe.",
            category: "cycle",
            likes: 42
        },
        {
            id: 2,
            question: "Làm thế nào để tính ngày rụng trứng?",
            answer: "Có một số phương pháp để tính ngày rụng trứng:<br><br>1. Theo dõi chu kỳ kinh: Ngày rụng trứng thường rơi vào khoảng ngày thứ 14 trước kỳ kinh tiếp theo.<br>2. Đo nhiệt độ cơ thể: Nhiệt độ cơ thể tăng nhẹ (0.3-0.5°C) sau khi rụng trứng.<br>3. Dùng que thử rụng trứng: Phát hiện sự gia tăng hormone LH trước khi rụng trứng 24-36 giờ.<br>4. Quan sát chất nhầy cổ tử cung: Trong những ngày rụng trứng, chất nhầy thường trong, dai và co giãn như lòng trắng trứng.",
            category: "cycle",
            likes: 38
        },
        {
            id: 3,
            question: "Các dấu hiệu mang thai sớm nhất là gì?",
            answer: "Các dấu hiệu mang thai sớm có thể bao gồm:<br><br>- Chậm kinh<br>- Buồn nôn, đặc biệt vào buổi sáng<br>- Mệt mỏi bất thường<br>- Đau ngực, căng tức ngực<br>- Đi tiểu thường xuyên hơn<br>- Thay đổi khẩu vị, thèm ăn hoặc chán ăn<br>- Nhạy cảm với mùi<br>- Ra máu báo thai (lượng ít, màu hồng hoặc nâu nhạt)",
            category: "health",
            likes: 56
        },
        {
            id: 4,
            question: "Khi nào nên đi xét nghiệm STIs?",
            answer: "Bạn nên đi xét nghiệm STIs trong các trường hợp sau:<br><br>1. Khi bắt đầu quan hệ tình dục với bạn tình mới<br>2. Khi có nhiều bạn tình<br>3. Khi bạn tình của bạn có nhiều bạn tình khác<br>4. Khi có triệu chứng nghi ngờ: dịch bất thường, ngứa, nóng rát khi đi tiểu, đau khi quan hệ, mụn hoặc vết loét ở vùng kín<br>5. Định kỳ 6 tháng đến 1 năm nếu có nguy cơ<br><br>Nhiều bệnh STIs không có triệu chứng rõ ràng, vì vậy xét nghiệm định kỳ là cách tốt nhất để bảo vệ sức khỏe.",
            category: "sti",
            likes: 29
        },
        {
            id: 5,
            question: "Các phương pháp tránh thai phổ biến nào hiệu quả nhất?",
            answer: "Các phương pháp tránh thai hiệu quả cao (>99% khi sử dụng đúng cách):<br><br>- Que cấy tránh thai<br>- Vòng tránh thai (IUD)<br>- Thuốc tiêm tránh thai<br>- Triệt sản<br><br>Các phương pháp hiệu quả cao (91-94%):<br>- Thuốc tránh thai hàng ngày<br>- Miếng dán tránh thai<br>- Vòng âm đạo<br><br>Các phương pháp hiệu quả trung bình (85-88%):<br>- Bao cao su nam/nữ<br>- Màng ngăn âm đạo<br>- Thuốc diệt tinh trùng",
            category: "contraception",
            likes: 47
        },
        {
            id: 6,
            question: "Tôi có thể đặt lịch tư vấn trực tuyến như thế nào?",
            answer: "Bạn có thể đặt lịch tư vấn trực tuyến theo các bước sau:<br><br>1. Truy cập mục 'Tư vấn trực tuyến' trên trang chủ<br>2. Chọn chuyên khoa và bác sĩ bạn muốn tư vấn<br>3. Xem lịch trống của bác sĩ và chọn khung giờ phù hợp<br>4. Điền thông tin cá nhân và mô tả ngắn về vấn đề sức khỏe<br>5. Xác nhận đặt lịch và thanh toán (nếu có)<br><br>Bạn sẽ nhận được email xác nhận và thông báo nhắc lịch trước giờ hẹn 30 phút.",
            category: "consultation",
            likes: 31
        }
    ];

    const faqList = document.getElementById('faq-list');
    const searchInput = document.getElementById('faq-search');
    const categoryButtons = document.querySelectorAll('.category-btn');
    const askForm = document.getElementById('ask-form');
    const askQuestionBtn = document.getElementById('ask-question-btn');
    const cancelQuestionBtn = document.getElementById('cancel-question-btn');
    const submitQuestionBtn = document.getElementById('submit-question-btn');
    
    let currentCategory = 'all';
    let currentSearch = '';
    
    // Hiển thị danh sách câu hỏi
    function renderFAQs() {
        faqList.innerHTML = '';
        
        const filteredFAQs = faqData.filter(faq => {
            const matchesCategory = currentCategory === 'all' || faq.category === currentCategory;
            const matchesSearch = currentSearch === '' || 
                faq.question.toLowerCase().includes(currentSearch.toLowerCase()) ||
                faq.answer.toLowerCase().includes(currentSearch.toLowerCase());
            
            return matchesCategory && matchesSearch;
        });
        
        if (filteredFAQs.length === 0) {
            faqList.innerHTML = `
                <div class="no-results">
                    <i class="fas fa-search" style="font-size: 3rem; margin-bottom: 1rem; opacity: 0.3;"></i>
                    <h3>Không tìm thấy câu hỏi phù hợp</h3>
                    <p>Hãy thử từ khóa khác hoặc đặt câu hỏi mới</p>
                </div>
            `;
            return;
        }
        
        filteredFAQs.forEach(faq => {
            const faqItem = document.createElement('div');
            faqItem.className = 'faq-item';
            faqItem.dataset.id = faq.id;
            faqItem.dataset.category = faq.category;
            
            faqItem.innerHTML = `
                <div class="faq-question">
                    <span>${faq.question}</span>
                    <i class="fas fa-chevron-down faq-icon"></i>
                </div>
                <div class="faq-answer">
                    ${faq.answer}
                    <div class="faq-meta" style="margin-top: 1rem; display: flex; justify-content: space-between; align-items: center;">
                        <span class="faq-category" style="background: #f0f4ff; color: #6a67ce; padding: 0.25rem 0.75rem; border-radius: 20px; font-size: 0.85rem;">
                            ${getCategoryName(faq.category)}
                        </span>
                        <div class="faq-actions">
                            <button class="btn-like" style="background: none; border: none; cursor: pointer;">
                                <i class="far fa-thumbs-up"></i> ${faq.likes}
                            </button>
                        </div>
                    </div>
                </div>
            `;
            
            faqList.appendChild(faqItem);
            
            // Thêm sự kiện mở rộng câu hỏi
            const questionElement = faqItem.querySelector('.faq-question');
            questionElement.addEventListener('click', () => {
                faqItem.classList.toggle('expanded');
            });
        });
    }
    
    // Lấy tên hiển thị của danh mục
    function getCategoryName(categoryKey) {
        const categories = {
            'all': 'Tất cả',
            'cycle': 'Chu kỳ kinh nguyệt',
            'health': 'Sức khỏe sinh sản',
            'sti': 'Xét nghiệm STIs',
            'contraception': 'Biện pháp tránh thai',
            'consultation': 'Tư vấn trực tuyến'
        };
        return categories[categoryKey] || 'Khác';
    }
    
    // Xử lý tìm kiếm
    searchInput.addEventListener('input', (e) => {
        currentSearch = e.target.value;
        renderFAQs();
    });
    
    // Xử lý chọn danh mục
    categoryButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Xóa active khỏi tất cả các nút
            categoryButtons.forEach(btn => btn.classList.remove('active'));
            
            // Thêm active cho nút được chọn
            button.classList.add('active');
            
            // Cập nhật danh mục hiện tại
            currentCategory = button.dataset.category;
            renderFAQs();
        });
    });
    
    // Hiển thị form đặt câu hỏi
    askQuestionBtn.addEventListener('click', () => {
        askForm.style.display = 'block';
        window.scrollTo({
            top: askForm.offsetTop - 100,
            behavior: 'smooth'
        });
    });
    
    // Ẩn form đặt câu hỏi
    cancelQuestionBtn.addEventListener('click', () => {
        askForm.style.display = 'none';
    });
    
    // Xử lý gửi câu hỏi mới
    submitQuestionBtn.addEventListener('click', () => {
        const title = document.getElementById('question-title').value.trim();
        const category = document.getElementById('question-category').value;
        const detail = document.getElementById('question-detail').value.trim();
        
        if (!title || !detail) {
            alert('Vui lòng nhập đầy đủ tiêu đề và nội dung câu hỏi');
            return;
        }
        
        // Tạo câu hỏi mới (trong thực tế sẽ gửi đến server)
        const newQuestion = {
            id: faqData.length + 1,
            question: title,
            answer: "Câu hỏi của bạn đang được các chuyên gia xem xét. Chúng tôi sẽ cập nhật câu trả lời sớm nhất!",
            category: category,
            likes: 0
        };
        
        // Thêm vào đầu danh sách
        faqData.unshift(newQuestion);
        
        // Reset form
        document.getElementById('question-title').value = '';
        document.getElementById('question-detail').value = '';
        askForm.style.display = 'none';
        
        // Hiển thị lại danh sách
        currentCategory = 'all';
        categoryButtons.forEach(btn => {
            btn.classList.remove('active');
            if (btn.dataset.category === 'all') btn.classList.add('active');
        });
        
        renderFAQs();
        
        // Cuộn đến câu hỏi mới
        setTimeout(() => {
            const newQuestionElement = document.querySelector(`.faq-item[data-id="${newQuestion.id}"]`);
            if (newQuestionElement) {
                newQuestionElement.classList.add('expanded');
                window.scrollTo({
                    top: newQuestionElement.offsetTop - 100,
                    behavior: 'smooth'
                });
            }
        }, 100);
        
        alert('Câu hỏi của bạn đã được gửi thành công!');
    });
    
    // Khởi tạo hiển thị
    renderFAQs();
});