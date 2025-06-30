document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Bạn cần đăng nhập để truy cập trang này!");
        window.location.href = "/login";
        return;
    }

    // Lấy thông tin người dùng
    fetch("/api/customer", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (response.status === 401) {
            alert("Token không hợp lệ hoặc đã hết hạn. Vui lòng đăng nhập lại.");
            localStorage.removeItem("token");
            window.location.href = "/login";
            return;
        }
        return response.json();
    })
    .then(data => {
        if (data) {
            const nameDisplay = document.getElementById("user-name-display");
            const fullName = data.name || data.username;
            if (nameDisplay) nameDisplay.textContent = fullName;
        }
    })
    .catch(error => {
        console.error("Lỗi khi lấy thông tin người dùng:", error);
    });

    // Lấy chu kỳ gần nhất và cập nhật UI
    loadLatestCycle();

    // Gán tháng/năm hiện tại
    window.currentDate = new Date();
    window.currentMonth = currentDate.getMonth();
    window.currentYear = currentDate.getFullYear();

    const prevMonthButton = document.querySelector(".month-nav.prev");
    const nextMonthButton = document.querySelector(".month-nav.next");

    prevMonthButton.addEventListener("click", () => {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        generateCalendar(currentMonth, currentYear);
    });

    nextMonthButton.addEventListener("click", () => {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        generateCalendar(currentMonth, currentYear);
    });

    const logoutBtn = document.getElementById("logout-button");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", function (e) {
            e.preventDefault();
            localStorage.removeItem("token");
            window.location.href = "/home";
        });
    }
});

function formatDate(date) {
    if (!date || date === "null") return "--";
    const d = new Date(date);
    return d.toLocaleDateString("vi-VN", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric"
    });
}

function loadLatestCycle() {
    const token = localStorage.getItem("token");
    if (!token) return;

    fetch("/api/customer/cycle/latest", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) throw new Error("Không thể lấy chu kỳ gần nhất");
        return response.json();
    })
    .then(data => {
        if (data.lastPeriodDate) {
            const lastPeriodDate = new Date(data.lastPeriodDate);
            const cycleLength = data.cycleLength;
            const periodLength = data.periodLength;

            const ovulationDate = new Date(lastPeriodDate);
            ovulationDate.setDate(lastPeriodDate.getDate() + cycleLength - 14);

            const fertileStart = new Date(ovulationDate);
            fertileStart.setDate(ovulationDate.getDate() - 3);

            const fertileEnd = new Date(ovulationDate);
            fertileEnd.setDate(ovulationDate.getDate() + 2);

            const nextMenstruationDate = new Date(lastPeriodDate);
            nextMenstruationDate.setDate(lastPeriodDate.getDate() + cycleLength);

            let fertilityStatus = "Thấp";
            if (cycleLength >= 26 && cycleLength <= 32) fertilityStatus = "Cao";
            else if (cycleLength > 32) fertilityStatus = "Trung bình";

            let healthScore = 80;
            if (cycleLength >= 26 && cycleLength <= 32) healthScore = 90;
            else if (cycleLength > 32) healthScore = 85;
            else if (cycleLength < 26) healthScore = 75;

            // ✅ Lưu ISO để đảm bảo new Date() parse được
            localStorage.setItem("cycleLength", cycleLength);
            localStorage.setItem("periodLength", periodLength);
            localStorage.setItem("ovulationDate", ovulationDate.toISOString());
            localStorage.setItem("fertileStart", fertileStart.toISOString());
            localStorage.setItem("fertileEnd", fertileEnd.toISOString());
            localStorage.setItem("nextMenstruationDate", nextMenstruationDate.toISOString());
            localStorage.setItem("fertilityStatus", fertilityStatus);
            localStorage.setItem("healthScore", healthScore);

            // Cập nhật UI
            document.getElementById("cycle-length-display").textContent = cycleLength + " ngày";
            document.getElementById("ovulation-date-display").textContent = formatDate(ovulationDate);
            document.getElementById("fertile-period-display").textContent = fertilityStatus;
            document.getElementById("health-score-display").textContent = healthScore + "/100";
            document.getElementById("menstruation-date-display").textContent = formatDate(nextMenstruationDate);
            document.getElementById("fertile-start-display").textContent = formatDate(fertileStart);
            document.getElementById("fertile-end-display").textContent = formatDate(fertileEnd);

            generateCalendar(currentMonth, currentYear);
        }
    })
    .catch(err => console.warn("Không tìm thấy chu kỳ gần nhất:", err.message));
}

function generateCalendar(month, year) {
    const calendarContainer = document.querySelector(".calendar");
    calendarContainer.innerHTML = "";

    const firstDay = new Date(year, month, 1).getDay();
    const lastDate = new Date(year, month + 1, 0).getDate();

    document.querySelector(".current-month").textContent = `Tháng ${month + 1}, ${year}`;

    const daysOfWeek = ["CN", "T2", "T3", "T4", "T5", "T6", "T7"];
    daysOfWeek.forEach(day => {
        const dayHeader = document.createElement("div");
        dayHeader.classList.add("calendar-header");
        dayHeader.textContent = day;
        calendarContainer.appendChild(dayHeader);
    });

    for (let i = 0; i < firstDay; i++) {
        const emptyDay = document.createElement("div");
        emptyDay.classList.add("calendar-day", "other-month");
        calendarContainer.appendChild(emptyDay);
    }

    const menstruationDate = localStorage.getItem("nextMenstruationDate") ? new Date(localStorage.getItem("nextMenstruationDate")) : null;
    const fertileStart = localStorage.getItem("fertileStart") ? new Date(localStorage.getItem("fertileStart")) : null;
    const fertileEnd = localStorage.getItem("fertileEnd") ? new Date(localStorage.getItem("fertileEnd")) : null;
    const ovulationDate = localStorage.getItem("ovulationDate") ? new Date(localStorage.getItem("ovulationDate")) : null;
    const periodLength = localStorage.getItem("periodLength") ? parseInt(localStorage.getItem("periodLength")) : 5;

    for (let day = 1; day <= lastDate; day++) {
        const dayElement = document.createElement("div");
        dayElement.classList.add("calendar-day");
        dayElement.textContent = day;

        const fullDate = new Date(year, month, day);

        if (fullDate.toDateString() === new Date().toDateString()) {
            dayElement.classList.add("today");
        }

        if (menstruationDate && fullDate >= menstruationDate && fullDate < new Date(menstruationDate.getTime() + periodLength * 86400000)) {
            dayElement.classList.add("menstruation");
        }

        if (fertileStart && fertileEnd && fullDate >= fertileStart && fullDate <= fertileEnd) {
            dayElement.classList.add("fertile");
        }

        if (ovulationDate && fullDate.toDateString() === ovulationDate.toDateString()) {
            dayElement.classList.add("ovulation");
        }

        calendarContainer.appendChild(dayElement);
    }
}
