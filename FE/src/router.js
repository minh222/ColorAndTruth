import { createRouter, createWebHistory } from "vue-router";
import Auth from "./pages/Auth.vue";
import Home from "./pages/Home.vue";

const routes = [
  { path: "/", redirect: "/auth" },
  { path: "/auth", component: Auth },
  {
    path: "/home",
    component: Home,
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");

  if (to.meta.requiresAuth && !token) {
    next("/auth");
  } else {
    next();
  }
});

export default router;
