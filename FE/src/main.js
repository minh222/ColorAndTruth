// main.js
import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

import "./style.css";
import "./main.css";

const app = createApp(App);

app.config.globalProperties.$authFetch = async (url, options = {}) => {
  const token = localStorage.getItem("token");

  try {
    const res = await fetch(url, {
      ...options,
      headers: {
        ...(options.headers || {}),
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
    });

    if (res.status === 401 || res.status === 403) {
      localStorage.removeItem("token");
      router.push("/auth");
      throw new Error("Unauthorized");
    }

    return res;
  } catch (err) {
    console.error("❌ authFetch failed:", url, err);
    throw err;
  }
};

app.use(router);
app.mount("#app");
