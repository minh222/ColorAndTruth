<template>
    <div class="comment-node">
      <div class="comment-box">
        <div class="content">
          <!-- USER INFO -->
          <div class="user-row">
            <img
              class="avatar"
              :src="comment.avatar || '/default-avatar.jpg'"
              alt="avatar"
            />
            <span class="username">
              {{ comment.name || 'Ẩn danh' }}
            </span>
            <span v-if="comment.time" class="comment-time">
              · {{ formatTime(comment.time) }}
            </span>
          </div>

          <p class="claim">{{ comment.claim }}</p>

          <div class="actions">
            <button @click="toggleReplies">
              💬 <span v-if="comment.count">({{ comment.count }})</span>
            </button>

            <div class="reply-wrapper">
              <button
                class="reply-btn"
                @click="$emit('quote', {
                  id: comment.id,
                  name: comment.name || 'Ẩn danh',
                  claim: comment.claim
                })"
                
              >
                ↩ Trả lời
              </button>

              <!-- overlay tên user -->
              <div
                v-if="comment.isDebateClaim === false"
                class="reply-lock-overlay"
              >
                🚫 {{ comment.name || "Ẩn danh" }}
              </div>
            </div>

            <button
              v-if="comment.emotion"
              @click="toggleEmotion"
            >
              <span
                class="emotion-toggle"
                :class="{ open: showEmotion }"
              >
                {{ showEmotion ? "👁" : "🕶 (secret true emotion)" }}
              </span>
            </button>

            <button
              v-if="Number(comment.userId) === Number(currentUserId)"
              class="delete-btn"
              @click="confirmDelete"
            >
              🗑
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
          :dayAgo="dayAgo"
          @reply="$emit('reply', $event)"
          @quote="$emit('quote', $event)"    
          @deleted="removeChild"
        />
        <div class="children-actions">
          <div class="children-actions">
            <button v-if="children.length > CHILD_LIMIT" class="collapse-children" @click="collapseReplies">🔼</button>
            <button v-if="!noMoreChildren && !loading" class="load-more-children" @click="loadMoreChildren">🔽</button>
          </div>
        </div>
        </div>
      </div>

      <!-- children -->

    </div>
    
    <!-- POPUP CONFIRM -->
    <Teleport to="body">
      <div v-if="showDebateConfirm" class="debate-popup">
        <div class="popup-card">
          <p>
            🗣  Không đc phản biện phần secret true emotion nếu muốn xem 
          </p>

          <div class="popup-actions">
            <button @click="confirmSubmit(true)">Oke</button>
            <button @click="confirmSubmit(false)">Không xem nữa</button>
          </div>
        </div>
      </div>

      <div v-if="popup.show" class="debate-popup">
        <div class="popup-card">
          <button class="popup-close" @click="closePopup">✖</button>

          <p>{{ popup.message }}</p>

          <div class="popup-actions">
            <button
              @click="() => { popup.onYes?.(); closePopup() }"
            >
              Có
            </button>

            <button
              @click="() => { popup.onNo?.(); closePopup() }"
            >
              Không
            </button>
          </div>
        </div>
      </div>
    </Teleport>
  </template>

  <script setup>
  import { ref, getCurrentInstance, watch } from "vue";

  const emit = defineEmits(["quote", "reply", "deleted"]);
  const defaultAvatar = '/default-avatar.jpg'
  const popup = ref({
    show: false,
    message: "",
    onYes: null,
    onNo: null,
  })

  const showConfirmPopup = (msg, onYes, onNo = null) => {
    popup.value.message = msg
    popup.value.onYes = onYes
    popup.value.onNo = onNo
    popup.value.show = true
  }

  const closePopup = () => {
    popup.value.show = false
    popup.value.onYes = null
    popup.value.onNo = null
  }

  const formatTime = (time) => {
    if (!time) return "";

    // cắt chuỗi, không dùng Date
    const [date, clock] = time.split("T");
    const [y, m, d] = date.split("-");
    const hhmm = clock?.slice(0, 5) || "";

    return `${d}/${m}/${y} · ${hhmm}`;
  };

  const handleReply = () => {
    emit("reply", {
      id: props.comment.id,
      name: props.comment.name || "Ẩn danh",
      claim: props.comment.claim
    })
  }

  const confirmDelete = () => {
    showConfirmPopup(
      "Xóa comment này nha? Bay là bay luôn đó 😬",
      async () => {
        await authFetch(
          `/api/v1/remove/${props.comment.id}`,
          { method: "POST" }
        );

        const delta = 1 + (props.comment.count || 0);

        emit("deleted", {
          id: props.comment.id,
          delta
        });
      }
    );
  };

  const removeChild = ({ id, delta }) => {
    children.value = children.value.filter(c => c.id !== id);

    if (typeof delta === "number") {
      props.comment.count -= delta;
      emit("deleted", { id, delta });
    }
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
  const lastChildId = ref(null);
  const noMoreChildren = ref(false);

  /* auth */
  const { proxy } = getCurrentInstance();
  const authFetch = proxy.$authFetch;

  const onReply = () => {
    emit("reply", props.comment);
  };

  const props = defineProps({
    comment: { type: Object, required: true },
    currentUserId: {
      type: Number,
      default: null
    },
    dayAgo: {
      type: Number,
      default: 0
    }
  });

  const cancelDebatePopup = () => {
    showDebateConfirm.value = false;
    pendingShowEmotion.value = false;
  };

  const confirmSubmit = async (allowReply) => {
    showDebateConfirm.value = false;
    pendingShowEmotion.value = false;

    if (!allowReply) return;

    const res = await authFetch(`/api/v1/loadComment/${props.comment.id}`);
    const data = await res.json(); // 👈 QUAN TRỌNG

    // show emotion
    emotion.value = data.emotion;
    showEmotion.value = true;

    // 🔒 lock / unlock reply
    props.comment.isDebateClaim = data.isDebateClaim;
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

    // 🚫 không có reply thì khỏi mở
    if (!props.comment.count || props.comment.count === 0) {
      opened.value = true;   // vẫn mở để UI nhất quán
      children.value = [];  // chắc chắn rỗng
      return;
    }

    opened.value = true;

    // đã load rồi thì thôi
    if (children.value.length) return;

    await loadMoreChildren();
  };

  const CHILD_LIMIT = 2;

  const loadMoreChildren = async () => {
    if (loading.value || noMoreChildren.value) return;

    loading.value = true;

    try {
      const params = new URLSearchParams({
        id: props.comment.id,
        limit: String(CHILD_LIMIT),
      });

      params.append("dayAgo", String(props.dayAgo ?? 0));

      if (lastChildId.value !== null) {
        params.append("lastId", lastChildId.value);
      }

      const res = await authFetch(`/api/v1/loadChildren?${params.toString()}`);
      const data = await res.json();

      if (data.length) {
        children.value.push(...data);
        lastChildId.value = data[data.length - 1].id;

        // Nếu số trả về < limit thì hết
        if (data.length < CHILD_LIMIT) {
          noMoreChildren.value = true;
        }
      } else {
        noMoreChildren.value = true;
      }
    } finally {
      loading.value = false;
    }
  };

  const collapseReplies = () => {
    // Giữ lại đúng CHILD_LIMIT
    if (children.value.length > CHILD_LIMIT) {
      children.value.splice(CHILD_LIMIT);
      noMoreChildren.value = false; // có thể load tiếp
      lastChildId.value =
        children.value.length > 0
          ? children.value[children.value.length - 1].id
          : null;
    }
  };

  watch(() => props.dayAgo, () => {
    children.value = []
    lastChildId.value = null
    noMoreChildren.value = false
    if (opened.value) {
      loadMoreChildren()
    }
  });

  </script>


  <style scoped>
  .collapse-children {
    margin: 8px auto;
    display: block;
    padding: 6px 12px;
    border-radius: 6px;
    border: none;
    background: #f3f4f6;
    color: #374151;
    cursor: pointer;
    font-size: 13px;
  }

  .collapse-children:hover {
    background: #e5e7eb;
  }
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
  .comment-time {
    font-size: 12px;
    color: #999;
    margin-left: 4px;
  }
  .reply-wrapper {
    position: relative;
    display: inline-block;
  }

  .reply-btn {
    position: relative;
  }

  .reply-btn:disabled {
    opacity: 0.4;
    cursor: not-allowed;
  }

  .reply-lock-overlay {
    
    position: absolute;
    inset: 0;
    background: rgba(255, 255, 255, 0.85);

    display: flex;
    align-items: center;
    justify-content: center;

    font-size: 12px;
    font-weight: 600;
    color: #b42318;
    pointer-events: auto;   /* 👈 CHẶN CLICK */
    border-radius: 6px;
    text-align: center;
    padding: 0 6px;
  }

  .load-more-children:hover {
    background: #cbd5e1;
  }

  .children-actions {
    display: flex;
    justify-content: flex-end;  
    gap: 8px;
    margin-top: 8px;
  }

  .load-more-children,
  .collapse-children {
    padding: 6px 12px;
    border-radius: 6px;
    border: none;
    font-size: 13px;
    cursor: pointer;
  }
  

  </style>
