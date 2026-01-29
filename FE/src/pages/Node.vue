<template>
  <div class="comment-node">
    <div class="comment-box">
      <p class="claim">{{ comment.claim }}</p>

      <div class="actions">
        <button @click="toggleEmotion">
          {{ showEmotion ? "👁️‍🗨️" : "👁️" }}
        </button>

        <button @click="toggleReplies">
          💬
        </button>
      </div>
    </div>

    <!-- emotion -->
    <small v-if="showEmotion" class="emotion">
      {{ emotion || "Đang tải..." }}
    </small>

    <!-- children -->
    <div v-if="opened" class="children">
      <p v-if="loading">⏳ Đang tải trả lời...</p>

      <Node
        v-for="c in children"
        :key="c.id"
        :comment="c"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance } from "vue";

defineOptions({
  name: "Node"
});

const props = defineProps({
  comment: { type: Object, required: true }
});

const { proxy } = getCurrentInstance();
const authFetch = proxy.$authFetch;

/* emotion */
const showEmotion = ref(false);
const emotion = ref(null);

/* children */
const opened = ref(false);
const loading = ref(false);
const children = ref([]);

const toggleEmotion = async () => {
  if (showEmotion.value) {
    showEmotion.value = false;
    return;
  }

  if (!emotion.value) {
    const ok = window.confirm("Bạn phải tôn trọng cảm xúc người viết.");
    if (!ok) return;

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
}

.comment-box {
  background: #fff;
  padding: 10px;
  border-radius: 10px;
}

.actions {
  display: flex;
  gap: 6px;
}

.children {
  margin-left: 18px;
  padding-left: 10px;
  border-left: 2px solid #ddd;
}

.claim {
  font-weight: bold;
}

.emotion {
  color: #666;
  font-size: 13px;
}
</style>
