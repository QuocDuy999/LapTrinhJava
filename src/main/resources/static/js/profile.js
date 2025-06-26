document.addEventListener('DOMContentLoaded', () => {
    // Load user data
    loadUserData();
    
    // Setup event listeners
    document.getElementById('edit-profile-btn').addEventListener('click', enableEditMode);
    document.getElementById('save-profile-btn').addEventListener('click', saveProfile);
    document.getElementById('logout-link').addEventListener('click', logout);
    document.getElementById('avatar-upload-btn').addEventListener('click', uploadAvatar);

    // Load user data from localStorage or API
    function loadUserData() {
        const userData = JSON.parse(localStorage.getItem('userProfile')) || getDefaultProfile();
        
        // Set profile data
        document.getElementById('sidebar-user-name').textContent = userData.fullName;
        document.getElementById('profile-name').textContent = userData.fullName;
        document.getElementById('full-name-display').textContent = userData.fullName;
        document.getElementById('email-display').textContent = userData.email;
        document.getElementById('phone-display').textContent = userData.phone;
        document.getElementById('dob-display').textContent = userData.dob;
        document.getElementById('gender-display').textContent = userData.gender;
        document.getElementById('blood-type-display').textContent = userData.bloodType;
        document.getElementById('height-display').textContent = `${userData.height} cm`;
        document.getElementById('weight-display').textContent = `${userData.weight} kg`;
        document.getElementById('allergies-display').textContent = userData.allergies || 'Không';
        document.getElementById('medical-history-display').textContent = userData.medicalHistory || 'Không';
    }

    function getDefaultProfile() {
        return {
            fullName: "Nguyễn Văn A",
            email: "nguyenvana@example.com",
            phone: "0123456789",
            dob: "01/01/1990",
            gender: "Nữ",
            bloodType: "A+",
            height: 165,
            weight: 55,
            allergies: "",
            medicalHistory: ""
        };
    }

    function enableEditMode() {
        const userData = JSON.parse(localStorage.getItem('userProfile')) || getDefaultProfile();
        
        // Show edit inputs
        document.querySelectorAll('.detail-item').forEach(item => {
            item.classList.add('edit-mode');
        });
        
        // Fill inputs with current data
        document.getElementById('full-name-input').value = userData.fullName;
        document.getElementById('email-input').value = userData.email;
        document.getElementById('phone-input').value = userData.phone;
        document.getElementById('dob-input').value = userData.dob;
        document.getElementById('gender-input').value = userData.gender;
        document.getElementById('blood-type-input').value = userData.bloodType;
        document.getElementById('height-input').value = userData.height;
        document.getElementById('weight-input').value = userData.weight;
        document.getElementById('allergies-input').value = userData.allergies || '';
        document.getElementById('medical-history-input').value = userData.medicalHistory || '';
        
        // Toggle buttons
        document.getElementById('edit-profile-btn').style.display = 'none';
        document.getElementById('save-profile-btn').style.display = 'inline-flex';
    }

    function saveProfile() {
        // Get edited values
        const userData = {
            fullName: document.getElementById('full-name-input').value,
            email: document.getElementById('email-input').value,
            phone: document.getElementById('phone-input').value,
            dob: document.getElementById('dob-input').value,
            gender: document.getElementById('gender-input').value,
            bloodType: document.getElementById('blood-type-input').value,
            height: document.getElementById('height-input').value,
            weight: document.getElementById('weight-input').value,
            allergies: document.getElementById('allergies-input').value,
            medicalHistory: document.getElementById('medical-history-input').value
        };
        
        // Save to localStorage (in real app, send to API)
        localStorage.setItem('userProfile', JSON.stringify(userData));
        
        // Update display
        document.getElementById('sidebar-user-name').textContent = userData.fullName;
        document.getElementById('profile-name').textContent = userData.fullName;
        document.getElementById('full-name-display').textContent = userData.fullName;
        document.getElementById('email-display').textContent = userData.email;
        document.getElementById('phone-display').textContent = userData.phone;
        document.getElementById('dob-display').textContent = userData.dob;
        document.getElementById('gender-display').textContent = userData.gender;
        document.getElementById('blood-type-display').textContent = userData.bloodType;
        document.getElementById('height-display').textContent = `${userData.height} cm`;
        document.getElementById('weight-display').textContent = `${userData.weight} kg`;
        document.getElementById('allergies-display').textContent = userData.allergies || 'Không';
        document.getElementById('medical-history-display').textContent = userData.medicalHistory || 'Không';
        
        // Exit edit mode
        document.querySelectorAll('.detail-item').forEach(item => {
            item.classList.remove('edit-mode');
        });
        
        // Toggle buttons
        document.getElementById('edit-profile-btn').style.display = 'inline-flex';
        document.getElementById('save-profile-btn').style.display = 'none';
        
        alert('Thông tin đã được cập nhật thành công!');
    }

    function logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('userProfile');
        window.location.href = '/login';
    }

    function uploadAvatar() {
        // In a real app, this would open a file dialog and upload to server
        alert('Chức năng cập nhật ảnh đại diện sẽ được thêm trong phiên bản sau!');
    }
});