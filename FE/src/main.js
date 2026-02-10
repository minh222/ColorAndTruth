// main.js
import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

import "./style.css";
import "./main.css";

const app = createApp(App);
 
// main.js
let serverDead = false;

 

app.config.globalProperties.$authFetch = async (url, options = {}) => {
  const token = localStorage.getItem("token");

  try {
    const res = await fetch(url, {
      ...options,
      headers: {
        ...(options.headers || {}),
        ...(token ? { Authorization: `Bearer ${token}` } : {})
      }
    });

    // 🔒 auth fail → redirect, KHÔNG throw
    if (res.status === 401 || res.status === 403) {
      localStorage.removeItem("token");
      router.push("/auth");
      return res;
    }

    // 🚧 server lỗi thật
    if (res.status >= 500) {
      window.dispatchEvent(
        new CustomEvent("server-error", {
          detail: { type: "maintenance" }
        })
      );
      throw new Error("SERVER_MAINTENANCE");
    }

    return res;
  } catch (err) {
    // 💥 chỉ rơi vào đây khi server chết hẳn / ECONNREFUSED
    window.dispatchEvent(
      new CustomEvent("server-error", {
        detail: { type: "unreachable" }
      })
    );
    throw err;
  }
};


app.use(router);
app.mount("#app");

 