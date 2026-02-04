<template>
  <div class="comment-node">
    <div class="comment-box">
      <div class="content">
        <!-- USER INFO -->
        <div class="user-row">
          <img
            class="avatar"
            :src="comment.avatar || '/default-avatar.png'"
            alt="avatar"
          />
          <span class="username">
            {{ comment.name || 'Ẩn danh' }}
          </span>
        </div>

        <p class="claim">{{ comment.claim }}</p>

        <div class="actions">
          <button @click="toggleReplies">
            💬 <span v-if="comment.count">({{ comment.count }})</span>
          </button>

          <button @click="$emit('reply', comment)">↩ Reply</button>
          <button @click="toggleEmotion">
            <span
              class="emotion-toggle"
              :class="{ open: showEmotion }"
            >
              {{ showEmotion ? "👁️" : "🕶️ (secret true emotion)" }}
            </span>
          </button>

          <button
            v-if="Number(comment.userId) === Number(currentUserId)"
            class="delete-btn"
            @click="confirmDelete"
          >
            🗑️
          </button>

        </div>
      </div>

      <div
        class="emotion-panel"
        :class="{ show: showEmotion }"
      >
        {{ emotion }}
      </div>
      <div v-if="opened" class="children">
        <p v-if="loading">⏳ Đang tải trả lời...</p>

      <Node
        v-for="c in children"
        :key="c.id"
        :comment="c"
        :currentUserId="currentUserId"
        @reply="$emit('reply', $event)"
        @deleted="removeChild"
      />

      </div>
    </div>

    <!-- children -->

  </div>
  <!-- POPUP CONFIRM -->
  <div v-if="showDebateConfirm" class="debate-popup">
    <div class="popup-card">
      <p>
        🗣️  Không đc phản biện phần secret true emotion nếu muốn xem 
      </p>

      <div class="popup-actions">
        <button @click="confirmSubmit(true)">Oke</button>
        <button @click="confirmSubmit(false)">Không xem nữa</button>
      </div>
    </div>
  </div>

</template>

<script setup>

import { ref, getCurrentInstance } from "vue";
const emit = defineEmits(["reply", "deleted"]);
const confirmDelete = async () => {
  const ok = confirm("Xóa comment này nha? Bay là bay luôn đó 😬");
  if (!ok) return;

  await authFetch(`/api/v1/remove/${props.comment.id}`, {
    method: "POST"
  });

  emit("deleted", props.comment.id);
};

const removeChild = (id) => {
  // 1. remove child khỏi list
  children.value = children.value.filter(c => c.id !== id);

  // 2. trừ count hiển thị
  if (props.comment.count > 0) {
    props.comment.count--;
  }

  // 3. bubble tiếp lên CHA (nếu còn cấp trên)
  emit("deleted", id);
};

defineOptions({
  name: "Node"
});

const showDebateConfirm = ref(false);
const pendingShowEmotion = ref(false); // đánh dấu đang chờ confirm

/* emotion */
const showEmotion = ref(false);
const emotion = ref(null);

/* children */
const opened = ref(false);
const loading = ref(false);
const children = ref([]);

/* auth */
const { proxy } = getCurrentInstance();
const authFetch = proxy.$authFetch;

const onReply = () => {
  emit("reply", props.comment);
};

const props = defineProps({
  comment: { type: Object, required: true },
  currentUserId: { type: Number, required: true }
});

const cancelDebatePopup = () => {
  showDebateConfirm.value = false;
  pendingShowEmotion.value = false;
};

const confirmSubmit = async (allowReply) => {
  showDebateConfirm.value = false;
  pendingShowEmotion.value = false;

  if (!allowReply) return;

  if (!emotion.value) {
    const res = await authFetch(`/api/v1/loadComment/${props.comment.id}`);
    emotion.value = await res.text();
  }

  showEmotion.value = true;
};


const toggleEmotion = async () => {
  if (showEmotion.value) {
    showEmotion.value = false;
    return;
  }
  showDebateConfirm.value = true;
   
};

const toggleReplies = async () => {
  if (opened.value) {
    opened.value = false;
    return;
  }

  opened.value = true;

  if (children.value.length) return;

  loading.value = true;
  const res = await authFetch(
    `/api/v1/loadChildren?id=${props.comment.id}&limit=5`
  );
  children.value = await res.json();
  loading.value = false;
};
</script>


<style scoped>
.debate-popup {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.popup-card {
  background: white;
  padding: 20px 24px;
  border-radius: 12px;
  text-align: center;
  position: relative;
  text-align: center;
  min-width: 320px;
}


.popup-close {
  position: absolute;
  top: 10px;
  right: 10px;
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 16px;
  opacity: 0.6;
}

.popup-close:hover {
  opacity: 1;
}

.popup-actions {
  margin-top: 16px;
  display: flex;
  gap: 12px;
  justify-content: center;
}

.popup-actions button {
  padding: 8px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
}

.comment-node {
  margin-top: 10px;
  display: block;
}

.comment-box {
  position: relative;
  background: #fff;
  border-radius: 10px;
  overflow: hidden;
}

/* CONTENT LUÔN Ở BÊN TRÁI */
.content {
  padding: 10px;
}

/* PANEL KHÔNG ĐỤNG CONTENT */
.emotion-panel {
  position: absolute;
  top: 0;
  bottom: 0;     /* 👈 quan trọng */
  right: 0;
  
  width: 260px;
  height: 100%;

  background: linear-gradient(135deg, #fff1f2, #fde2e4);
  border-left: 2px solid #f3c6cc;

  padding: 20px;

  transform: translateX(100%);
  transition: transform 0.25s ease;

  /* 👇 nghệ thuật nằm đây */
  font-family: "Playfair Display", serif;
  font-size: 18px;
  line-height: 1.7;
  font-weight: 500;
  color: #7a2e3a;
  letter-spacing: 0.3px;
  font-style: italic;
}


.emotion-panel.show {
  transform: translateX(0);
}

.emotion-panel.show::before {
  opacity: 1;
}

.emotion-panel::before {
  content: "secret true emotion";
  position: absolute;
  inset: 0;

  display: flex;
  align-items: center;
  justify-content: center;

  font-size: 28px;
  font-weight: 700;
  letter-spacing: 2px;

  color: rgba(183, 74, 99, 0.12);
  pointer-events: none;
  user-select: none;
}

/* 🔥 CHỈ CHỪA CHỖ KHI MỞ */
.comment-box.with-emotion {
  padding-right: 260px;
}


.actions {
  display: flex;
  gap: 6px;
}

.children {
  position: relative;
  margin-left: 18px;
  padding-left: 14px;
  border-left: 2px solid #ddd;
}

.children::before {
  content: "";
  position: absolute;
  top: 0;
  left: -2px;
  width: 12px;
  height: 1px;
  background: #ddd;
}
.children .comment-box {
  background: #fafafa;
}

.content-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.claim {
  flex: 1;                 /* 👈 ăn hết bên trái */
  font-weight: bold;
}

.emotion-right {
  max-width: 240px;
  font-size: 13px;
  color: #666;
  text-align: left;
  white-space: normal;
  flex-shrink: 0;
}

.emotion {
  color: #666;
  font-size: 13px;
}
.reply-box {
  margin-top: 8px;
}

.reply-box textarea {
  width: 100%;
  min-height: 60px;
  border-radius: 8px;
  padding: 6px;
}

.reply-actions {
  margin-top: 6px;
  display: flex;
  gap: 6px;
}
.emotion-toggle {
  display: inline-block;
  cursor: pointer;
  font-size: 14px;
  color: rgba(0,0,0,0.6);
  transition: 
    transform 0.35s ease,
    opacity 0.35s ease,
    filter 0.35s ease;
  opacity: 0.6;
  filter: blur(0.3px);
}

/* 🖤 khi mở nội tâm */
.emotion-toggle.open {
  opacity: 1;
  transform: scale(1.08);
  filter: blur(0);
}
 
.user-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}

.avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid #eee;
}

.username {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}


</style>
