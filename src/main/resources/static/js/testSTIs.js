document.addEventListener('DOMContentLoaded', () => {
    // Xử lý chọn gói xét nghiệm
    const selectButtons = document.querySelectorAll('.btn-select');
    const testTypeSelect = document.getElementById('test-type');
    
    selectButtons.forEach(button => {
        button.addEventListener('click', () => {
            const testCard = button.closest('.test-card');
            const testName = testCard.querySelector('h4').textContent;
            
            // Cập nhật dropdown chọn gói
            Array.from(testTypeSelect.options).forEach(option => {
                if (option.text.includes(testName)) {
                    option.selected = true;
                }
            });
            
            // Highlight gói được chọn
            document.querySelectorAll('.test-card').forEach(card => {
                card.classList.remove('selected');
            });
            testCard.classList.add('selected');
            
            // Cuộn đến form đặt lịch
            document.querySelector('.booking-section').scrollIntoView({ 
                behavior: 'smooth', 
                block: 'start'
            });
        });
    });
    
    // Xử lý đặt lịch
    const bookingForm = document.querySelector('.booking-form');
    const submitBtn = document.getElementById('submit-booking');
    const resetBtn = document.getElementById('reset-btn');
    
    submitBtn.addEventListener('click', (e) => {
        e.preventDefault();
        
        const testType = testTypeSelect.value;
        const testDate = document.getElementById('test-date').value;
        const testTime = document.getElementById('test-time').value;
        const location = document.getElementById('location').value;
        
        if (!testType || !testDate || !testTime || !location) {
            alert('Vui lòng điền đầy đủ thông tin đặt lịch');
            return;
        }
        
        // Tạo đối tượng booking
        const booking = {
            testType,
            testDate,
            testTime,
            location,
            payment: document.getElementById('payment').value,
            notes: document.getElementById('notes').value,
            timestamp: new Date().toISOString()
        };
        
        // Lưu vào localStorage (trong thực tế sẽ gửi đến server)
        saveBooking(booking);
        
        alert('Đã đặt lịch xét nghiệm thành công!');
        resetForm();
    });
    
    resetBtn.addEventListener('click', resetForm);
    
    // Lịch sử xét nghiệm
    const historyBtn = document.getElementById('view-history-btn');
    historyBtn.addEventListener('click', () => {
        const history = getBookingHistory();
        if (history.length === 0) {
            alert('Bạn chưa có lịch sử xét nghiệm nào');
            return;
        }
        
        // Hiển thị lịch sử (trong thực tế có thể mở modal hoặc trang mới)
        let historyText = "Lịch sử xét nghiệm:\n\n";
        history.forEach((booking, index) => {
            historyText += `#${index + 1}: ${formatBooking(booking)}\n\n`;
        });
        alert(historyText);
    });
    
    // Helper functions
    function saveBooking(booking) {
        const history = getBookingHistory();
        history.push(booking);
        localStorage.setItem('stisBookingHistory', JSON.stringify(history));
    }
    
    function getBookingHistory() {
        return JSON.parse(localStorage.getItem('stisBookingHistory') || [];
    }
    
    function resetForm() {
        bookingForm.reset();
        document.querySelectorAll('.test-card').forEach(card => {
            card.classList.remove('selected');
        });
    }
    
    function formatBooking(booking) {
        const date = new Date(booking.testDate).toLocaleDateString('vi-VN');
        let locationText = '';
        
        switch(booking.location) {
            case 'hanoi': locationText = 'Hà Nội'; break;
            case 'hcm': locationText = 'TP.HCM'; break;
            case 'danang': locationText = 'Đà Nẵng'; break;
            default: locationText = booking.location;
        }
        
        return `Gói: ${getTestTypeName(booking.testType)}
Ngày: ${date}
Giờ: ${getTimeSlot(booking.testTime)}
Địa điểm: ${locationText}`;
    }
    
    function getTestTypeName(type) {
        switch(type) {
            case 'basic': return 'Gói cơ bản';
            case 'standard': return 'Gói tiêu chuẩn';
            case 'premium': return 'Gói toàn diện';
            default: return type;
        }
    }
    
    function getTimeSlot(time) {
        switch(time) {
            case 'morning': return 'Sáng (8:00 - 11:00)';
            case 'afternoon': return 'Chiều (13:00 - 16:00)';
            case 'evening': return 'Tối (17:00 - 20:00)';
            default: return time;
        }
    }
});