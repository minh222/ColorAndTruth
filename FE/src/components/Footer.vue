<template>
  <footer class="app-footer">
    <div class="footer-container">

      <!-- LEFT -->
      <div class="footer-col">
        <h4>About Me</h4>
        <p>
          Đây là nền tảng chia sẻ cảm xúc thật (true emotion).
          Mục tiêu là tạo không gian nơi con người có thể bộc lộ
          suy nghĩ mà không bị phản biện hay công kích.
        </p>
      </div>

      <!-- CENTER -->
      <div class="footer-col">
        <h4>Quick Links</h4>
        <ul>
          <li><a href="#">Trang chủ</a></li>
          <li><a href="#">Bình luận</a></li>
          <li><a href="#">Chính sách</a></li>
        </ul>
      </div>

      <!-- RIGHT -->
      <div class="footer-col">
        <h4>Contact</h4>
        <p>Email: your@email.com</p>
        <p>© {{ year }} True Emotion</p>
      </div>

    </div>
  </footer>
</template>

<script setup>
const year = new Date().getFullYear();
</script>

<style scoped>
.app-footer {
  margin-top: 60px;
  padding: 40px 20px;
  background: #111827;
  color: #f9fafb;
}

.footer-container {
  max-width: 1100px;
  margin: auto;
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
  justify-content: space-between;
}

.footer-col {
  flex: 1;
  min-width: 220px;
}

.footer-col h4 {
  margin-bottom: 12px;
  font-size: 16px;
  font-weight: 600;
}

.footer-col p,
.footer-col li {
  font-size: 14px;
  color: #d1d5db;
  line-height: 1.6;
}

.footer-col ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-col a {
  text-decoration: none;
  color: #9ca3af;
  transition: 0.2s ease;
}

.footer-col a:hover {
  color: #fff;
}
.app-footer {
  width: 100%;
}
</style>