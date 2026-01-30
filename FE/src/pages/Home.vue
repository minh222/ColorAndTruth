<template>
  <!-- 🔎 GLOBAL RULE NOTE -->
  <div class="global-rule">
    <p>
      Nếu bạn không xem <b>true emotion</b> bạn có thể bình luận thoải mái,
      không có ràng buộc gì.
    </p>

    <p>
      Nếu bạn xem <b>true emotion</b> của con người, bạn phải chấp nhận
      <b>không được phản biện</b> nó.
    </p>

    <p>
      Một số comment có thể <b>khóa phản hồi</b> sau khi xem true emotion
      nếu người đăng cài đặt thiết lập đó.
    </p>
  </div>

  <!-- LOGOUT -->
  <button class="logout-btn" @click="logout">🚪 Logout</button>

  <!-- OPEN MODAL -->
  <button class="open-btn" @click="openAnalyze">
    🪟 Bình luận ý kiến
  </button>

  <!-- COMMENT LIST -->
  <div class="comment-list">
    <h3>💬 Comments</h3>

    <Node
      v-for="c in comments"
      :key="c.id"
      :comment="c"
      @reply="onReply"
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
    :replyTo="replyingTo"
    @close="() => { replyingTo = null; showModal = false }"
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

/* REPLY STATE */
const replyingTo = ref(null);

const onReply = (comment) => {
  replyingTo.value = comment;
  showModal.value = true;  
};

const openAnalyze = () => {
  replyingTo.value = null; // 👈 chế độ phân tích thường
  showModal.value = true;
};

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
  text-align: left;   /* ✅ */
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
.global-rule {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);

  max-width: 720px;          /* 👈 rất quan trọng cho text dài */
  padding: 12px 16px;

  font-size: 14px;
  line-height: 1.6;
  font-weight: 500;

  color: rgba(0,0,0,0.45);
  font-style: italic;
  text-align: center;

  pointer-events: none;
  z-index: 1;
}
.global-rule p {
  margin: 6px 0;
}

</style>