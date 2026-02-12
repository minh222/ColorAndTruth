<template>
  <div class="auth-box">
    <h2>{{ isLoginMode ? "🔐 Đăng nhập" : "📝 Đăng ký" }}</h2>

    <input v-model="name" placeholder="Tên đăng nhập" />
    <input
      v-model="password"
      type="password"
      placeholder="Mật khẩu"
    />
    <!-- ✅ chỉ hiện khi Đăng ký -->
    <input
      v-if="!isLoginMode"
      v-model="confirmPassword"
      type="password"
      placeholder="Nhập lại mật khẩu"
    />

    <button @click="submit" :disabled="loading">
      {{ loading ? "Đang xử lý..." : isLoginMode ? "Đăng nhập" : "Đăng ký" }}
    </button>

    <p class="switch" @click="toggleMode">
      {{ isLoginMode ? "Chưa có tài khoản? Đăng ký" : "Đã có tài khoản? Đăng nhập" }}
    </p>

    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>
<script setup>
import { ref, getCurrentInstance } from "vue";
import { useRouter } from "vue-router";

const { proxy } = getCurrentInstance();
const authFetch = proxy.$authFetch;
const router = useRouter();
const confirmPassword = ref("");
const name = ref("");
const password = ref("");
const loading = ref(false);
const error = ref("");
const isLoginMode = ref(true);

const toggleMode = () => {
  isLoginMode.value = !isLoginMode.value;
  error.value = "";
};

const submit = async () => {
  if (!name.value || !password.value) {
    error.value = "Thiếu thông tin";
    return;
  }

  if (!isLoginMode.value && password.value.length < 5) {
    error.value = "Mật khẩu phải từ 5 ký tự";
    return;
  }
  
  if (!isLoginMode.value && password.value !== confirmPassword.value) {
    error.value = "Mật khẩu nhập lại không khớp";
    return;
  }

  loading.value = true;
  error.value = "";

  try {
    const endpoint = isLoginMode.value
      ? "/api/v1/login"
      : "/api/v1/register";

    const encodedPassword = btoa(password.value);
    const url =
      `${endpoint}?name=${encodeURIComponent(name.value)}&password=${encodeURIComponent(encodedPassword)}`;

    const res = await authFetch(url, { method: "POST" });

 
    if (!res.ok) {
      const err = await res.json();
      error.value = err.message; // GIỮ NGUYÊN MESSAGE BE
      return;
    }

 
    const token = await res.text();
    localStorage.setItem("token", token);
    router.push("/home");

  } catch (e) {
    // ✅ server chết / bảo trì / ECONNREFUSED
    error.value = "❌ Không thể kết nối server. Vui lòng thử lại sau.";
  } finally {
    loading.value = false;
  }
};
</script>


<style scoped>
.auth-box {
  width: 340px;
  margin: 80px auto;
  padding: 24px;
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.15);
  text-align: center;
}

input {
  width: 100%;
  padding: 12px;
  margin: 10px 0;
  border-radius: 8px;
  border: 1px solid #ddd;
}

button {
  width: 100%;
  padding: 12px;
  margin-top: 10px;
  border-radius: 8px;
  border: none;
  background: #212529;
  color: #fff;
  cursor: pointer;
}

button:disabled {
  opacity: 0.6;
}

.switch {
  margin-top: 14px;
  color: #007bff;
  cursor: pointer;
}

.error {
  margin-top: 10px;
  color: red;
}
</style>
