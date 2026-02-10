<template>
  <div class="register-box">
    <h3>📝 Đăng ký</h3>

    <input v-model="name" placeholder="Tên đăng nhập" />

    <input v-model="password" type="password" placeholder="Mật khẩu" />

    <button type="button" @click="register" :disabled="loading">Đăng ký</button>

    <p v-if="error" class="error">{{ error }}</p>
  </div>

  <!-- POPUP OK -->
  <div v-if="popup.show" class="debate-popup">
    <div class="popup-card">
      <button class="popup-close" @click="closePopup">✖</button>

      <p>{{ popup.message }}</p>

      <div class="popup-actions">
        <button @click="closePopup">OK</button>
      </div>
    </div>
  </div>

</template>

<script setup>
import { ref  , getCurrentInstance } from "vue";

const { proxy } = getCurrentInstance();
const authFetch = proxy.$authFetch;

const name = ref("");
const password = ref("");
const loading = ref(false);
const error = ref("");

const popup = ref({
  show: false,
  message: "",
})

const showPopup = (msg) => {
  popup.value.message = msg
  popup.value.show = true
}

const closePopup = () => {
  popup.value.show = false
}


const register = async () => {
  if (!name.value || !password.value) {
    error.value = "Thiếu thông tin";
    return;
  }

  loading.value = true;
  error.value = "";

  try {
    const url =
      `/api/v1/register` +
      `?name=${encodeURIComponent(name.value)}` +
      `&password=${encodeURIComponent(password.value)}`;

    const res = await authFetch(url, { method: "POST" });

    if (!res.ok) {
      error.value = "Đăng ký thất bại";
      return;
    }

    const token = await res.text();
    localStorage.setItem("token", token);

    showPopup("🎉 Đăng ký thành công")

  } catch (e) { 
    error.value = "❌ Không thể kết nối server. Vui lòng thử lại sau.";
  } finally {
    loading.value = false;
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
.debate-popup {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.popup-card {
  background: white;
  padding: 20px 24px;
  border-radius: 12px;
  text-align: center;
  position: relative;
  min-width: 300px;
}

.popup-close {
  position: absolute;
  top: 10px;
  right: 10px;
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 16px;
  opacity: 0.6;
}

.popup-actions {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

</style>
