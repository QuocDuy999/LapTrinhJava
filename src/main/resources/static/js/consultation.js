// Function to handle calendar day selection
document.querySelectorAll('.calendar-day').forEach(day => {
    day.addEventListener('click', function() {
        // Remove selected class from all days
        document.querySelectorAll('.calendar-day').forEach(d => {
            d.classList.remove('selected');
        });
        
        // Add selected class to clicked day
        this.classList.add('selected');
    });
});

// Function to handle time slot selection
document.querySelectorAll('.time-slot').forEach(slot => {
    slot.addEventListener('click', function() {
        // Remove selected class from all slots
        document.querySelectorAll('.time-slot').forEach(s => {
            s.classList.remove('selected');
        });
        
        // Add selected class to clicked slot
        this.classList.add('selected');
    });
});

// Function to toggle consultation type
document.querySelectorAll('.consultation-type .btn').forEach(btn => {
    btn.addEventListener('click', function() {
        alert('Bạn đã chọn hình thức tư vấn này. Vui lòng chọn chuyên gia và thời gian phù hợp.');
    });
});

// Function to handle specialist booking
document.querySelectorAll('.specialist-actions .btn-primary').forEach(btn => {
    btn.addEventListener('click', function() {
        const specialistName = this.closest('.specialist-card').querySelector('.specialist-name').textContent;
        alert(`Bạn đang đặt lịch với ${specialistName}. Vui lòng chọn ngày và giờ phù hợp.`);
        document.querySelector('.schedule-section').scrollIntoView({behavior: 'smooth'});
    });
});