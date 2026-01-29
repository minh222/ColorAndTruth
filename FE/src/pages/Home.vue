<template>
  <!-- LOGOUT -->
  <button class="logout-btn" @click="logout">🚪 Logout</button>

  <!-- OPEN MODAL -->
  <button class="open-btn" @click="showModal = true">
    🪟 Mở cửa sổ phân tích
  </button>

  <!-- COMMENT LIST -->
  <div class="comment-list">
    <h3>💬 Comments</h3>

    <Node
      v-for="c in comments"
      :key="c.id"
      :comment="c"
    />

    <button
      v-if="!noMore && !loading"
      @click="loadComments"
      class="load-more"
    >
      Load more
    </button>

    <p v-if="loading">Đang load...</p>
    <p v-if="noMore">Hết comment</p>
  </div>

  <!-- MODAL -->
  <div
    v-if="showModal"
    class="overlay"
    @click.self="showModal = false"
  >
    <ClaimEmotionConfirm
      @close="showModal = false"
      @submitted="onReload"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from "vue";
import { useRouter } from "vue-router";
import Node from "./Node.vue";
import ClaimEmotionConfirm from "../components/ClaimEmotionConfirm.vue";

const { proxy } = getCurrentInstance();
const authFetch = proxy.$authFetch;

const router = useRouter();
const logout = () => {
  localStorage.removeItem("token");
  router.push("/auth");
};

/* MODAL */
const showModal = ref(false);

/* COMMENT STATE */
const comments = ref([]);
const lastId = ref(null);
const loading = ref(false);
const noMore = ref(false);
const LIMIT = 5;

const loadComments = async () => {
  if (loading.value || noMore.value) return;
  loading.value = true;

  let url = `/api/v1/loadComment?limit=${LIMIT}`;
  if (lastId.value) url += `&lastId=${lastId.value}`;

  const res = await authFetch(url);
  const data = await res.json();

  if (data.length) {
    comments.value.push(...data);
    lastId.value = data[data.length - 1].id;
    if (data.length < LIMIT) noMore.value = true;
  } else {
    noMore.value = true;
  }

  loading.value = false;
};

const onReload = async () => {
  comments.value = [];
  lastId.value = null;
  noMore.value = false;
  await loadComments();
};

onMounted(loadComments);
</script>

<style scoped>
.open-btn {
  margin: 20px auto;
  display: block;
  padding: 10px 18px;
  border-radius: 10px;
  border: none;
  background: #212529;
  color: #fff;
  cursor: pointer;
}

.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.55);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.comment-list {
  max-width: 720px;
  margin: 20px auto;
  padding: 16px;
  background: #fff;
  border-radius: 12px;
}

.logout-btn {
  position: fixed;
  top: 16px;
  right: 16px;
  padding: 8px 14px;
  border-radius: 8px;
  border: none;
  background: crimson;
  color: #fff;
  cursor: pointer;
}
</style>