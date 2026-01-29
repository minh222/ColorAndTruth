<template>
  <div class="auth-box">
    <h2>{{ isLoginMode ? "🔐 Đăng nhập" : "📝 Đăng ký" }}</h2>

    <input v-model="name" placeholder="Tên đăng nhập" />
    <input
      v-model="password"
      type="password"
      placeholder="Mật khẩu"
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
import { ref } from "vue";
import { useRouter } from "vue-router"; // 👈 THÊM

const router = useRouter(); // 👈 THÊM

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
  loading.value = true;
  error.value = "";

  try {
    const endpoint = isLoginMode.value
      ? "/api/v1/login"
      : "/api/v1/register";

    const url =
      `${endpoint}?name=${encodeURIComponent(name.value)}&password=${encodeURIComponent(password.value)}`;

    const res = await fetch(url, { method: "POST" });

    // ❌ FAIL → đọc message backend
    if (!res.ok) {
      const err = await res.json();
      throw new Error(err.message || "Login failed");
    }

    // ✅ OK → đọc token
    const token = await res.text();
    localStorage.setItem("token", token);
    router.push("/home");

  } catch (e) {
    error.value = "❌ " + e.message;
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
