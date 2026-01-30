<template>
  <div class="comment-node">
    <div class="comment-box">
      <div class="content">
        <p class="claim">{{ comment.claim }}</p>

        <div class="actions">
          <button @click="toggleReplies">💬</button>
          <button @click="$emit('reply', comment)">↩ Reply</button>
          <button @click="toggleEmotion">
            {{ showEmotion ? "👁️‍🗨️" : "👁️" }}
          </button>
        </div>
      </div>

      <div
        class="emotion-panel"
        :class="{ show: showEmotion }"
      >
        {{ emotion }}
      </div>
    </div>

    <!-- children -->
    <div v-if="opened" class="children">
      <p v-if="loading">⏳ Đang tải trả lời...</p>

      <Node
        v-for="c in children"
        :key="c.id"
        :comment="c"
        @reply="$emit('reply', $event)"  
      />
    </div>
  </div>
</template>

<script setup>

import { ref, getCurrentInstance } from "vue";

defineOptions({
  name: "Node"
});

/* reply */
const emit = defineEmits(["reply"]);

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
  comment: { type: Object, required: true }
});


const toggleEmotion = async () => {
  if (showEmotion.value) {
    showEmotion.value = false;
    return;
  }

  const ok = window.confirm("Bạn ko đc phản biện vào phần này (có thể công nhận) vì nó là cảm xúc/ niềm tin con người");
  if (!ok) return;

  if (!emotion.value) {
    const res = await authFetch(`/api/v1/loadComment/${props.comment.id}`);
    emotion.value = await res.text();
  }

  showEmotion.value = true;
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
  right: 0;

  width: 260px;
  height: 100%;

  background: #f1f3f5;
  border-left: 2px solid #ddd;

  transform: translateX(100%);
  transition: transform 0.25s ease;
}

.emotion-panel.show {
  transform: translateX(0);
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

</style>
