import { createRouter, createWebHistory } from "vue-router";
import Auth from "./pages/Auth.vue";
import Home from "./pages/Home.vue";
import Draw from "./pages/Draw.vue";

const routes = [
  { path: "/", redirect: "/auth" },
  { path: "/auth", component: Auth },
  {
    path: "/home",
    component: Home,
    meta: { requiresAuth: true },
  },
  {
    path: "/draw",
    component: Draw,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
