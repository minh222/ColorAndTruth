<template>
  <div class="register-box">
    <h3>📝 Đăng ký</h3>

    <input v-model="name" placeholder="Tên đăng nhập" />

    <input v-model="password" type="password" placeholder="Mật khẩu" />

    <button type="button" @click="register" :disabled="loading">Đăng ký</button>

    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script setup>
import { ref } from "vue";

const name = ref("");
const password = ref("");
const loading = ref(false);
const error = ref("");

const register = async () => {
  if (!name.value || !password.value) {
    alert("Thiếu thông tin");
    return;
  }

  try {
    const url =
      `/api/v1/register` +
      `?name=${encodeURIComponent(name.value)}` +
      `&password=${encodeURIComponent(password.value)}`;


    const res = await fetch(url, {
      method: "POST",
    });

    if (!res.ok) throw new Error("Register failed");

    const token = await res.text(); // backend trả JWT string
    localStorage.setItem("token", token);

    alert("🎉 Đăng ký thành công");
  } catch (e) {
    console.error(e);
    alert("❌ Đăng ký lỗi");
  }
};
</script>

<style scoped>
.register-box {
  width: 320px;
  margin: 40px auto;
  padding: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

input {
  width: 100%;
  padding: 10px;
  margin-bottom: 12px;
  border-radius: 8px;
  border: 1px solid #ddd;
}

button {
  width: 100%;
  padding: 10px;
  border-radius: 8px;
  border: none;
  background: #212529;
  color: #fff;
  cursor: pointer;
}

button:disabled {
  opacity: 0.6;
}

.error {
  margin-top: 10px;
  color: red;
  text-align: center;
}
</style>
