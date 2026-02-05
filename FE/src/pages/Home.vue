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

  <!-- USER INFO -->
  <div class="user-card">
    <div class="user-info">
      <h4>{{ user.name }}</h4>
      <p>{{ user.email }}</p>
    </div>

    <label class="avatar-wrapper">
      <img
        :src="avatarSrc"
        class="avatar"
        referrerpolicy="no-referrer"
        loading="lazy"
        decoding="async"
      />

      <!-- ⏳ loading overlay -->
      <div v-if="avatarUploading" class="avatar-loading">
        ⏳
      </div>

      <input
        type="file"
        accept="image/*"
        hidden
        :disabled="user.avatar || avatarUploading"
        @change="uploadAvatar"
      />

      <span v-if="!user.avatar &&!avatarUploading" class="edit-avatar">✏️</span>
    </label>

    <div v-if="user.avatar" class="avatar-actions">
      <button
        class="view-avatar"
        @click="showAvatarPreview = true"
      >
        👁️ Xem avatar
      </button>
      <button
        class="remove-avatar"
        @click="removeAvatar"
      >
        🗑️ Xóa avatar
      </button>
    </div>
    <p v-if="user.avatarChangeCount !== undefined" class="avatar-limit">
      Bạn còn {{ 20 - user.avatarChangeCount }} lần xóa avatar trong hôm nay
    </p>
    <div class="user-actions">
      <button class="logout-inline" @click="logout">
        🚪 Logout
      </button>
      <button
        type="button"
        class="yesterday-btn"
        :class="{ active: dayAgo === 1 }"
        @click="toggleYesterday"
      >
        🕰️ Hôm qua
      </button>
    </div>


  </div>

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
      :currentUserId="user.id"
      @reply="onReply"
      @deleted="removeRoot"
    />



    <div class="comment-actions">
      <button
        type="button"
        v-if="!noMore && !loading"
        @click="loadComments"
        class="load-more"
      >
        Load more
      </button>

      <button
        type="button"
        v-if="comments.length > LIMIT"
        class="collapse-btn"
        @click="collapse"
      >
        ⬆️ Thu gọn
      </button>
    </div>

    <p v-if="loading">Đang load...</p> 
  </div>

  <!-- MODAL -->
  <div
    v-if="showModal"
    class="overlay" 
  >

  <ClaimEmotionConfirm
    :replyTo="replyingTo"
    @close="() => { replyingTo = null; showModal = false }"
    @submitted="onReload"
  />

  </div>

  <div
    v-if="showAvatarPreview"
    class="overlay"
    @click.self="showAvatarPreview = false"
  >
    <img
      :src="avatarSrc"
      class="avatar-preview"
    />
  </div>
  <Draw />
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from "vue";
import { useRouter } from "vue-router";
import Node from "./Node.vue";
import ClaimEmotionConfirm from "../components/ClaimEmotionConfirm.vue";
import Draw from "./Draw.vue"
const dayAgo = ref(0); // 0 = hôm nay, 1 = hôm qua
const removeRoot = (id) => {
  comments.value = comments.value.filter(c => c.id !== id);
};

const avatarUploading = ref(false);
const avatarErrored = ref(false);
const showAvatarPreview = ref(false);

const onAvatarError = (e) => {
  if (avatarErrored.value) return;
  avatarErrored.value = true;
  e.target.src = defaultAvatar;
};

const { proxy } = getCurrentInstance();
const authFetch = proxy.$authFetch;

const router = useRouter();
const logout = () => {
  localStorage.removeItem("token");
  router.push("/auth");
};

const user = ref({});
const defaultAvatar = "/default-avatar.png";

/* LOAD USER */
const avatarSrc = ref(defaultAvatar);

/* LOAD USER */
const loadUser = async () => {
  const res = await authFetch("/api/v1/getUser");
  const data = await res.json();

  user.value = data;
  avatarSrc.value = data.avatar || defaultAvatar;
};



/* UPDATE AVATAR */
const updateAvatar = async (e) => {
  const file = e.target.files[0];
  if (!file) return;

  const form = new FormData();
  form.append("avatar", file);

  await authFetch("/api/v1/me/avatar", {
    method: "POST",
    body: form
  });

  await loadUser();
};

const removeAvatar = async () => {
  if (!confirm("Xóa avatar hiện tại?")) return;

  const res = await authFetch("/api/v1/empty-avatar", {
    method: "POST"
  });

  if (!res.ok) {
    const err = await res.json();

    // 🔒 Bị giới hạn theo ngày
    if (res.status === 429) {
      alert(err.message); // "Mỗi ngày chỉ được xóa avatar 20 lần"
      return;
    }

    alert("Xóa avatar thất bại");
    return;
  }

  // ✅ Thành công
   
    await loadUser();
};


const uploadAvatar = async (e) => {
  const file = e.target.files[0];
  if (!file) return;

  avatarUploading.value = true;

  try {
    const form = new FormData();
    form.append("file", file);

    const res = await authFetch("/api/v1/upload", {
      method: "POST",
      body: form
    });

    if (!res.ok) {
      const err = await res.json();

      if (res.status === 409) {
        alert(err.message);
        return;
      }

      alert("Upload thất bại");
      return;
    }

    const text = await res.text();
    if (text === "ok") {
      await loadUser();
    }
  } finally {
    avatarUploading.value = false;
  }
};



/* COMMENT + MODAL (GIỮ NGUYÊN CỦA MÀY) */
const showModal = ref(false);
const comments = ref([]);
const lastId = ref(null);
const loading = ref(false);
const noMore = ref(false);
const LIMIT = 4;
const replyingTo = ref(null);

const onReply = (comment) => {
  replyingTo.value = comment;
  showModal.value = true;
};

const openAnalyze = () => {
  replyingTo.value = null;
  showModal.value = true;
};

const loadComments = async () => {
  if (loading.value || noMore.value) return;
  loading.value = true;

  let url = `/api/v1/loadComment?limit=${LIMIT}&dayAgo=${dayAgo.value}`;
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

const toggleYesterday = async () => {
  // bật / tắt
  dayAgo.value = dayAgo.value === 1 ? 0 : 1;

  // reset list
  comments.value = [];
  lastId.value = null;
  noMore.value = false;

  await loadComments();
};


const onReload = async () => {
  comments.value = [];
  lastId.value = null;
  noMore.value = false;
  await loadComments();
};

onMounted(() => {
  loadUser();
  loadComments();
});
const collapse = () => {
  comments.value.splice(LIMIT); // giữ lại LIMIT comment đầu
  lastId.value = comments.value.at(-1)?.id ?? null;
  noMore.value = false;

  window.scrollTo({ bottom: 0, behavior: "smooth" });
};


</script>

<style scoped>
.remove-avatar {
  margin-left: auto;
  padding: 6px 10px;
  border-radius: 8px;
  border: none;
  background: #f1f1f1;
  color: #333;
  cursor: pointer;
  font-size: 13px;
}

.remove-avatar:hover {
  background: #ffe5e5;
  color: crimson;
}

.user-card {
  max-width: 720px;
  margin: 70px auto 20px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
  background: #fff;
  border-radius: 14px;
}

.avatar-wrapper {
  position: relative;
  cursor: pointer;
}

.avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
}

.edit-avatar {
  position: absolute;
  bottom: 0;
  right: 0;
  background: #000;
  color: #fff;
  font-size: 12px;
  padding: 4px;
  border-radius: 50%;
}

.user-info h4 {
  margin: 0;
  font-size: 16px;
}

.user-info p {
  margin: 4px 0 0;
  font-size: 13px;
  color: #666;
}

.open-btn {
  margin: 20px auto;
  display: block;
  padding: 12px 22px;
  border-radius: 12px;
  border: none;

  background: linear-gradient(135deg, #5f9cff, #7b7dff);
  color: #fff;

  font-size: 15px;
  font-weight: 600;
  letter-spacing: 0.3px;

  cursor: pointer;

  box-shadow:
    0 8px 20px rgba(95,156,255,0.35),
    inset 0 1px 0 rgba(255,255,255,0.35);

  transition: transform .2s ease, box-shadow .2s ease;
}

.open-btn:active {
  transform: translateY(0);
  box-shadow:
    0 6px 14px rgba(95,156,255,0.35),
    inset 0 2px 4px rgba(0,0,0,0.2);
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
  top: 0px;
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
.avatar-limit {
  margin-top: 6px;
  font-size: 12px;
  color: #d97706;
}
.avatar-loading {
  position: absolute;
  inset: 0;
  border-radius: 50%;
  background: rgba(0,0,0,0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
}
.remove-avatar:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.view-avatar {
  padding: 6px 10px;
  border-radius: 8px;
  border: none;
  background: #e9e9e9;
  cursor: pointer;
  font-size: 13px;
}

.view-avatar:hover {
  background: #ddd;
}

.avatar-preview {
  max-width: 90vw;
  max-height: 90vh;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.4);
}
.avatar-actions {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.logout-inline {
  margin-left: auto;
  padding: 6px 12px;
  border-radius: 8px;
  border: none;
  background: #f3f4f6;
  color: #333;
  cursor: pointer;
  font-size: 13px;
}

.logout-inline:hover {
  background: #fee2e2;
  color: crimson;
}
.comment-actions {
  display: flex;
  justify-content: center; /* 👈 kéo cả cụm ra giữa */
  align-items: center;
  gap: 16px;              /* 👈 khoảng cách giữa 2 nút */
  margin-top: 16px;
}
.yesterday-btn {
  padding: 8px 14px;
  border-radius: 10px;
  border: none;
  background: #e5e7eb;
  color: #374151;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.yesterday-btn.active {
  background: #2563eb;
  color: #fff;
}

.yesterday-btn:hover {
  background: #dbeafe;
}
.user-actions {
  margin-left: auto;        /* đẩy cả cụm sang phải */
  display: flex;
  flex-direction: column;   /* xếp dọc */
  gap: 8px;
  align-items: flex-end;
}


</style>