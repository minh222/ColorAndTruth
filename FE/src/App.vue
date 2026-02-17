<script setup>
import { useRoute } from "vue-router"
import BackgroundEffect from "./components/BackgroundEffect.vue"
import NpcWalker from "./components/NpcWalker.vue"

import "./assets/auth-bg.css"  

const route = useRoute()
</script>

<template>
  <div v-if="route.path === '/'" class="auth-bg"></div>

  <BackgroundEffect v-if="route.path !== '/'" />

  <NpcWalker />
  <router-view />
</template>
