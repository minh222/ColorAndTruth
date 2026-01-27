<template>
  <!-- OPEN MODAL BUTTON -->
  <button class="open-btn" @click="showModal = true">
    🪟 Mở cửa sổ phân tích
  </button>

  <!-- COMMENT LIST -->
  <div ref="listRef" class="comment-list">
    <h3>💬 Comments</h3>

    <div
      v-for="c in comments"
      :key="c.id"
      class="comment-item"
    >
      <div class="comment-top">
        <p class="claim">{{ c.claim }}</p>

        <!-- 👁️ TOGGLE PER COMMENT -->
        <button
          class="eye-toggle"
          @click="toggleEmotion(c.id)"
          :title="openEmotionMap[c.id] ? 'Ẩn emotion' : 'Hiện emotion'"
        >
          {{ openEmotionMap[c.id] ? "👁️‍🗨️" : "👁️" }}
        </button>
      </div>

      <small
        v-if="openEmotionMap[c.id]"
        class="emotion"
      >
        {{ c.emotion }}
      </small>
    </div>

    <!-- AUTO LOAD TRIGGER -->
    <div
      v-show="canAutoLoad"
      ref="loadTrigger"
      class="load-trigger"
    ></div>

    <!-- MANUAL LOAD -->
    <button
      v-if="!canAutoLoad && !noMore"
      class="load-more"
      @click="loadComments"
    >
      Load more
    </button>

    <p v-if="loading" class="loading">Đang load...</p>
    <p v-if="noMore" class="end">Hết comment</p>
  </div>

  <!-- MODAL -->
  <div
    v-show="showModal"
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
import { ref, onMounted, onBeforeUnmount } from "vue";
import ClaimEmotionConfirm from "../components/ClaimEmotionConfirm.vue";

/* MODAL */
const showModal = ref(false);
const emotionConsent = ref(
  localStorage.getItem("emotionConsent") === "true"
);

/* COMMENT STATE */
const comments = ref([]);
const lastId = ref(null);
const loading = ref(false);
const noMore = ref(false);
const canAutoLoad = ref(false);
const LIMIT = 5;

/* 👁️ EMOTION STATE (PER COMMENT) */
const openEmotionMap = ref({});

/* DOM */
const listRef = ref(null);
const loadTrigger = ref(null);
let observer = null;

/* TOGGLE EMOTION */
const toggleEmotion = (id) => {
  // đang mở → cho đóng
  if (openEmotionMap.value[id]) {
    openEmotionMap.value[id] = false;
    return;
  }

  const ok = window.confirm(
    "Đây là tâm trạng & niềm tin của người viết, bạn phải tôn trọng và ko đc phản biện phần này mới có thể xem, nếu ko đồng ý bạn chỉ đc bình luận trong phạm vi câu nói đã mở (bạn có thể công nhận hoặc động viên)"
  );

  if (!ok) return;

  openEmotionMap.value[id] = true;
};

/* LOAD COMMENT */
const loadComments = async () => {
  if (loading.value || noMore.value) return;
  loading.value = true;

  try {
    let url = `/api/v1/loadComment?limit=${LIMIT}`;
    if (lastId.value !== null) {
      url += `&lastId=${lastId.value}`;
    }

    const res = await fetch(url);
    const data = await res.json();

    if (Array.isArray(data) && data.length) {
      comments.value.push(...data);
      lastId.value = data[data.length - 1].id;

      // init eye state
      data.forEach((c) => {
        if (!(c.id in openEmotionMap.value)) {
          openEmotionMap.value[c.id] = false;
        }
      });

      if (data.length < LIMIT) {
        noMore.value = true;
      }

      requestAnimationFrame(checkScrollable);
    } else {
      noMore.value = true;
    }
  } catch (e) {
    console.error("Load comment failed", e);
  } finally {
    loading.value = false;
  }
};

/* ON RELOAD */
const onReload = async () => {
  // reset state
  comments.value = [];
  lastId.value = null;
  noMore.value = false;

  await loadComments();
};

/* CHECK SCROLL */
const checkScrollable = () => {
  const el = listRef.value;
  if (!el) return;

  canAutoLoad.value = el.scrollHeight > el.clientHeight;

  if (canAutoLoad.value && !observer && loadTrigger.value) {
    setupObserver();
  }
};

/* OBSERVER */
const setupObserver = () => {
  observer = new IntersectionObserver(
    ([entry]) => {
      if (entry.isIntersecting && !loading.value && !noMore.value) {
        loadComments();
      }
    },
    {
      root: listRef.value,
      threshold: 0.1,
    }
  );

  observer.observe(loadTrigger.value);

  // kick phát đầu
  loadComments();
};

/* LIFECYCLE */
onMounted(() => {
  loadComments();
});

onBeforeUnmount(() => {
  if (observer && loadTrigger.value) {
    observer.unobserve(loadTrigger.value);
  }
});
</script>

<style scoped>
.comment-item {
  padding: 14px 12px;
  margin-bottom: 10px;

  background: #fff;
  border-radius: 12px;
  border-bottom: 1px solid #eee;

  /* hiệu ứng vào */
  opacity: 0;
  transform: translateY(10px);
  animation: commentIn 0.45s ease forwards;

  /* hover */
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.comment-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 22px rgba(0, 0, 0, 0.08);
}

@keyframes commentIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.overlay {
  pointer-events: none;   /* 🔥 không nhận click */
}
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

.comment-list {
  max-width: 720px;
  margin: 20px auto;
  padding: 16px;
  background: #fff;
  border-radius: 12px;
  height: 60vh;
  overflow-y: auto;
}

.comment-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.claim {
  font-weight: bold;
}

.emotion {
  color: #555;
  margin-left: 6px;
}

.eye-toggle {
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 16px;
}

.eye-toggle:hover {
  opacity: 0.7;
}

.load-trigger {
  height: 1px;
}

.load-more {
  width: 100%;
  padding: 10px;
  margin-top: 10px;
}

.loading,
.end {
  text-align: center;
  margin: 12px 0;
  color: #888;
}

/* MODAL */
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
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(8px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
}
 
</style>
