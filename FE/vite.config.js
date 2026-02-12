// vite.config.js
import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      "/api": {
        target: "https://colorandtruth-1.onrender.com",
        changeOrigin: true,
        secure: true,
        rewrite: (path) => path,
      },
    },
  },
});
