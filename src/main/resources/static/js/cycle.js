document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("cycle-form");
    const lastPeriodDateInput = document.getElementById("lastPeriodDate");
    const cycleLengthInput = document.getElementById("cycleLength");
    const periodLengthInput = document.getElementById("periodLength");

    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const token = localStorage.getItem("token");
        if (!token) {
            alert("Bạn cần đăng nhập để thực hiện hành động này.");
            window.location.href = "/login";
            return;
        }

        const lastPeriodDateValue = lastPeriodDateInput.value;
        const cycleLength = parseInt(cycleLengthInput.value);
        const periodLength = parseInt(periodLengthInput.value);

        if (!lastPeriodDateValue || isNaN(cycleLength) || isNaN(periodLength)) {
            alert("⚠️ Vui lòng điền đầy đủ thông tin.");
            return;
        }

        if (cycleLength < 20 || cycleLength > 40) {
            alert("⛔ Độ dài chu kỳ không hợp lệ! Nhập từ 20 đến 40 ngày.");
            return;
        }

        const lastPeriodDate = new Date(lastPeriodDateValue);
        const ovulationDate = new Date(lastPeriodDate);
        ovulationDate.setDate(ovulationDate.getDate() + cycleLength - 14);
        alert(`📅 Dự đoán ngày rụng trứng: ${formatDate(ovulationDate)}`);

        fetch("/api/customer/cycle/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "Authorization": "Bearer " + token
            },
            body: new URLSearchParams({
                lastPeriodDate: lastPeriodDateValue,
                cycleLength,
                periodLength
            })
        })
        .then(response => {
            if (!response.ok) throw new Error("Không thể lưu chu kỳ.");
            return response.text();
        })
        .then(() => {
            alert("✅ Chu kỳ đã được lưu!");
            form.reset();
            setTimeout(() => {
            loadLatestCycle(); // ⚠️ Load lại sau 300ms thay vì reload toàn trang
        }, 300);
            window.location.href = "/customer";

            
        })
        .catch(error => {
            console.error("Lỗi khi lưu:", error);
            alert("❌ Lỗi khi lưu chu kỳ. Vui lòng thử lại.");
        });
    });

    function formatDate(date) {
        return date.toLocaleDateString("vi-VN", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric"
        });
    }
});
