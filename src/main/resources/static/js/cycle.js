document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("cycle-form");
    const lastPeriodDateInput = document.getElementById("lastPeriodDate");
    const cycleLengthInput = document.getElementById("cycleLength");

    form.addEventListener("submit", function(event) {
        const lastPeriodDate = new Date(lastPeriodDateInput.value);
        const cycleLength = parseInt(cycleLengthInput.value);

        if (isNaN(cycleLength) || cycleLength < 20 || cycleLength > 40) {
            alert("Độ dài chu kỳ không hợp lệ! Vui lòng nhập giá trị từ 20 đến 40 ngày.");
            event.preventDefault();
        } else {
            const ovulationDate = new Date(lastPeriodDate);
            ovulationDate.setDate(lastPeriodDate.getDate() + cycleLength - 14);

            alert(`Dự đoán ngày rụng trứng: ${ovulationDate.toISOString().split("T")[0]}`);
        }
    });
});
