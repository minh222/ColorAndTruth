<template>
  <div class="window" :class="{ plain: mode === 'comment' }">
    <!-- 🔥 DÁN NGUYÊN CÁI <div class="page"> CŨ VÀO ĐÂY -->
    <div class="page" :class="{ plain: mode === 'comment' }">
      <!-- ORIGINAL INPUT -->
      <div class="card">
        <div class="textarea-wrap">
          <textarea
            v-model="originalText"
            class="original-input"
            placeholder="Nhập nội dung cần phân tích…"
          />
          <button class="close-btn" @click="$emit('close')">✖</button>
        </div>
        <div class="actions">
          <!-- ✨ NÚT ẨN/HIỆN PANEL -->
          <button
            v-if="hasResult"
            class="  exact-btn"
            @click="toggleConfirmPanel"
          >
            {{ showConfirmPanel ? "Ẩn ." : "Hiện" }}
          </button>

          <button
            class="exact-btn"
            :disabled="loading || !originalText.trim()"
            @click="onExact"
          >
            {{ loading ? "Đang phân tích..." : "Phân tích" }}
          </button>
        </div>

      </div>

      <div v-if="hasResult" class="confirm-wrapper">
        <!-- CONFIRM (LUÔN TỒN TẠI, CHỈ ẨN) -->
        <div v-show="showConfirmPanel && hasResult" class="card confirm-card">
          <div class="header-row">
            <h4> Tại ô text ý chính, bôi đen phần muốn chuyển thành true emotion -> click giấu cảm xúc</h4>

            <div class="eye-slot">
              <button
                class="eye-toggle"
                @click="toggleAttitude"
                :title="showAttitude ? 'Ẩn attitude' : 'Hiện attitude'"
              >
                {{ showAttitude ? "👁️‍🗨️" : "👁️" }}
              </button>
            </div>
          </div>

          <div class="confirm-area confirm-body">
            <!-- CLAIM -->
            <div class="box claim-box">
              <div class="claim-header">
                <span class="tag">Ý CHÍNH </span>

                <button
                  v-if="!editingClaim"
                  class="edit-btn"
                  @click="
                    () => {
                      editedClaim = claim;
                      editingClaim = true;
                    }
                  "
                >
                  ✏️
                </button>
              </div>

              <!-- VIEW -->
              <p v-if="!editingClaim">
                {{ claim }}
              </p>

              <!-- EDIT -->
              <textarea
                v-else
                ref="claimTextarea"
                v-model="editedClaim"
                class="claim-editor"
                @blur="onClaimBlur"
              />
              <button
                class="switch-btn"
                @mousedown.prevent="analyzeSelectedText"
              >
                Giấu cảm xúc (hoặc những điều bạn muốn giấu kín)
              </button>
            </div>

            <!-- ATTITUDE -->
            <div
              v-if="showAttitude && activePanel === 'attitude'"
              class="box attitude-box"
            >
              <span class="tag">THÁI ĐỘ</span>
              <p>{{ attitude }}</p>

              <button
                v-if="emotion.length"
                class="switch-btn"
                @click="activePanel = 'emotion'"
              >
                Xem True Emotion →
              </button>
            </div>

            <!-- EMOTION -->
            <div
              v-if="showAttitude && activePanel === 'emotion'"
              class="box emotion-box"
            >
              <span class="tag">TRUE EMOTION/BELIVE AND HEART</span>
              <ul>
                <li v-for="(e, i) in emotion" :key="i">{{ e }}</li>
              </ul>
              <p class="original-preview">
                {{ selectedOriginalText || originalText }}
              </p>
              <button class="switch-btn" @click="activePanel = 'attitude'">
                ← Xem Attitude
              </button>
            </div>
          </div>
          <!-- FOOTER ACTION -->
          <div class="confirm-footer">
            <button
              class="submit-btn"
              :disabled="loading || !emotion.length"
              @click="showDebateConfirm = true"
            >
              📤 Gửi bình luận
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  <!-- POPUP CONFIRM -->
  <div v-if="showDebateConfirm" class="debate-popup">
    <div class="popup-card">
      <!-- ❌ NÚT ĐÓNG -->
      <button class="popup-close" @click="cancelDebatePopup"> ✖ </button>
      
      <p>🗣️ Sau khi có người xem true emotion của bạn, bạn có cho phép họ reply không, nếu không họ không thể reply, nếu có họ chỉ có thể bình luận xung quanh ý chính mà không được phản biện true emotion (họ được phép công nhận true emotion của bạn) ? (nhấn x để suy nghĩ thêm)</p>

      <div class="popup-actions">
        <button @click="confirmSubmit(true)">Có</button>
        <button @click="confirmSubmit(false)">Không</button>
      </div>

    </div>
  </div>

</template>

<script setup>
import { ref } from "vue";
import { getCurrentInstance } from "vue";
const emit = defineEmits(["close", "submitted"]);
const showDebateConfirm = ref(false);
const mode = ref("comment");  

/* PROXY */
const { proxy } = getCurrentInstance();
const authFetch = proxy.$authFetch;

/* CLAIM */
const claimTextarea = ref(null);
const selectedOriginalText = ref("");
/* ORIGINAL */
const originalText = ref("");

/* API RESULT */
const claim = ref("");
const emotion = ref([]);
const attitude = ref("");
const editingClaim = ref(false);
const editedClaim = ref("");

/* UI STATE */
const hasResult = ref(false);
const showAttitude = ref(false);
const activePanel = ref("attitude");
const loading = ref(false);
const showConfirmPanel = ref(false);

/* ACTION */
const props = defineProps({
  replyTo: {
    type: [Object, null],
    default: null
  }
});

const cancelDebatePopup = () => {
  showDebateConfirm.value = false;
};

const onExact = async () => {
  if (!originalText.value.trim()) return;
  mode.value = "exact";
  loading.value = true;
  try {
    const query = encodeURIComponent(originalText.value);

    const res = await authFetch(`/api/v1/analyze?original=${query}`, {
      method: "POST",
    });

    const data = await res.json();

    claim.value = data.claim;
    emotion.value = data.emotion || [];
    attitude.value = data.attitude || "";
    editedClaim.value = claim.value;
    editingClaim.value = false;

    hasResult.value = true;
    showConfirmPanel.value = true;

    showAttitude.value = true;
    activePanel.value = "attitude";
  } catch (e) {
    alert("Không gọi được API");
  } finally {
    loading.value = false;
  }
};

const analyzeSelectedText = async () => {
  const el = claimTextarea.value;
  if (!el) return;

  const start = el.selectionStart;
  const end = el.selectionEnd;

  if (start === end) {
    alert("Chưa bôi đen đoạn nào");
    return;
  }

  const selectedText = el.value.substring(start, end);
  selectedOriginalText.value = selectedText;

  // 🔥 CALL API BẰNG ĐOẠN ĐƯỢC CHỌN
  const res = await authFetch(
    `/api/v1/exact?original=${encodeURIComponent(selectedText)}`,
    { method: "POST" },
  );

  const before = el.value.slice(0, start);
  const after = el.value.slice(end);

  editedClaim.value = before + after;

  // 🎯 đưa cursor về đúng chỗ
  requestAnimationFrame(() => {
    el.focus();
    el.setSelectionRange(start, start);
  });

  const data = await res.json();

  // Xử lý kết quả
  emotion.value = data.emotion || [];
  attitude.value = data.attitude || "";
};

const onClose = () => {
  resetState();
  emit("close");
};

const confirmSubmit = async (isDebateClaim) => {
  showDebateConfirm.value = false;

  const params = new URLSearchParams({
    claim: claim.value,
    emotion: selectedOriginalText.value,
  });

  if (isDebateClaim) {
    params.append("isDebateClaim", "true");
  }

  if (props.replyTo) {
    params.append("id", props.replyTo.id);
  }

  try {
    const res = await authFetch(`/api/v1/postComment?${params.toString()}`, {
      method: "POST",
    });

    if (!res.ok) throw new Error("Request failed");

    resetState();
    emit("submitted");
    emit("close");
  } catch (e) {
    console.error(e);
    alert("Không gửi được dữ liệu");
  }
};

const onClaimBlur = () => {
  claim.value = editedClaim.value;
  editingClaim.value = false;
};

const toggleConfirmPanel = () => {
  const scrollY = window.scrollY;
  document.activeElement?.blur();
  showConfirmPanel.value = !showConfirmPanel.value;

  requestAnimationFrame(() => {
    window.scrollTo({
      top: scrollY,
      behavior: "auto",
    });
  });
};

/* TOGGLE */
const toggleAttitude = () => {
  showAttitude.value = !showAttitude.value;
  activePanel.value = "attitude";
};

/* RESET STATE */
const resetState = () => {
  originalText.value = "";
  claim.value = "";
  emotion.value = [];
  attitude.value = "";

  editedClaim.value = "";
  editingClaim.value = false;

  hasResult.value = false;
  showConfirmPanel.value = false;
  showAttitude.value = false;
  activePanel.value = "attitude";

  selectedOriginalText.value = "";
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


/* PAGE */
.page::before {
  content: "";
  position: absolute;
  inset: 0;
  border-radius: inherit;
  pointer-events: none;

  background:
    radial-gradient(2px 2px at 12% 22%, rgba(255,255,255,.7), transparent),
    radial-gradient(1.5px 1.5px at 25% 70%, rgba(255,255,255,.5), transparent),
    radial-gradient(2px 2px at 40% 35%, rgba(255,255,255,.6), transparent),
    radial-gradient(1px 1px at 55% 55%, rgba(255,255,255,.4), transparent),
    radial-gradient(2px 2px at 70% 20%, rgba(255,255,255,.6), transparent),
    radial-gradient(1.5px 1.5px at 82% 65%, rgba(255,255,255,.5), transparent);

  animation: sparkleFloat 6s ease-in-out infinite;
}

.page {
  position: relative;
}

@keyframes sparkleFloat {
  0% {
    opacity: 0.35;
    transform: translateY(0);
  }
  50% {
    opacity: 0.75;
    transform: translateY(-6px);
  }
  100% {
    opacity: 0.35;
    transform: translateY(0);
  }
}



/* CARD */
.card {
  background: #fff;
  border-radius: 12px;
 
  margin-bottom: 20px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
  max-width: 520px;
  padding: 6px 8px;
  margin: 12px auto;
}

 

.original-input {
  width: 100%;
  max-width: 480px;
  min-height: 100px;

  padding: 18px 20px;
  font-size: 16px;
  line-height: 1.7;

  border-radius: 12px;
  border: 1px solid #ddd;
  background: #fff;

  resize: vertical;
}

.original-input:focus {
  outline: none;
  border-color: #212529;
}

/* ACTION */
.actions {
  margin-top: 12px;
  text-align: right;
}

.exact-btn {
  padding: 10px 22px;
  border-radius: 999px;              /* bo tròn đẹp */
  border: none;
  background: linear-gradient(135deg, #1375d6, #c01515);
  color: #fff;

  font-size: 15px;
  font-weight: 700;
  letter-spacing: 0.3px;

  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 6px 18px rgba(0,0,0,0.25);
}

.exact-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 10px 26px rgba(0,0,0,0.35);
}

.exact-btn:active:not(:disabled) {
  transform: translateY(0);
  box-shadow: 0 4px 12px rgba(0,0,0,0.25);
}


.exact-btn:disabled {
  opacity: 0.5;
}

/* CONFIRM */
.confirm-card {
  max-width: 100%;
  overflow: hidden; /* 🔥 quan trọng */
  position: relative;
  inset: 0;
  pointer-events: auto;
}


.hidden {
  visibility: hidden;
  opacity: 0;
}

/* HEADER */
.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.eye-slot {
  width: 32px;
  display: flex;
  justify-content: center;
  flex-shrink: 0;
}

.eye-toggle {
  width: 24px;
  height: 24px;
  border: none;
  background: transparent;
  cursor: pointer;
}

.eye-toggle:hover {
  background: #f1f3f5;
  border-radius: 50%;
}

/* BODY */
.confirm-area {
  display: flex;
  gap: 20px;
  width: 100%;
  box-sizing: border-box;
}


.confirm-area .box {
  max-width: 100%;
  box-sizing: border-box;
}
.box {
  flex: 1;
  padding: 16px;
  border-radius: 10px;
}

.claim-box {
  background: #fff8e1;
}

.attitude-box {
  background: #f3e5f5;
}

.emotion-box {
  background: #e3f2fd;
}

.tag {
  font-size: 12px;
  font-weight: bold;
}

/* SWITCH */
.switch-btn {
  margin-top: 12px;
  min-width: 160px;
  white-space: nowrap;
  padding: 6px 12px;
  border-radius: 6px;
  border: 1px dashed #666;
  background: #f8f9fa;
  cursor: pointer;
}

.claim-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.edit-btn {
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 14px;
}

.edit-btn:hover {
  opacity: 0.7;
}

.claim-editor {
  width: 100%;
  min-height: 80px;
  padding: 10px 12px;
  border-radius: 8px;
  border: 1px solid #ccc;
  resize: vertical;
  font-size: 15px;
  line-height: 1.6;
  max-width: 420px;   /* ← giảm chiều ngang */
}
.actions {
  display: flex;
  justify-content: center; /* ← CĂN GIỮA */
  gap: 12px; /* khoảng cách giữa 2 nút */
  align-items: center;
  margin-top: 12px;
}

.toggle-confirm-btn {
  padding: 8px 16px;
  border-radius: 8px;
  border: 1px solid #444;
  background: #f5f5f5;
  cursor: pointer;
  font-weight: bold;
}
.toggle-confirm-btn:hover {
  background: #e9ecef;
}
.confirm-wrapper {
  position: relative;
  min-height: 340px;
}

.confirm-card.hidden {
  visibility: hidden;
  opacity: 0;
  pointer-events: none;
}
.confirm-footer {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.submit-btn {
  padding: 10px 24px;
  font-size: 15px;
  border-radius: 10px;
  border: none;
  background: #212529;
  color: #fff;
  cursor: pointer;
}

.submit-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.submit-btn:hover:not(:disabled) {
  opacity: 0.9;
}
.window {
  background: transparent;
  width: min(1000px, 95vw);
  max-height: 90vh;
  overflow-y: auto;
  border-radius: 16px;
  padding: 16px;
  position: relative;
    pointer-events: auto;
}
.close-btn {
  position: absolute;
  top: 8px;
  right: 12px;

  width: 34px;
  height: 34px;
  border-radius: 50%;

  display: flex;
  align-items: center;
  justify-content: center;

  font-size: 18px;
  font-weight: bold;

  border: none;
  cursor: pointer;

  color: #fff;
  background: linear-gradient(135deg, #ff4d4f, #ff7a7a);

  box-shadow:
    0 6px 16px rgba(255, 77, 79, 0.45),
    inset 0 1px 0 rgba(255,255,255,0.35);

  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.close-btn:hover {
  transform: scale(1.08);
}

.close-btn:active {
  transform: scale(0.95);
}




textarea {
  scroll-margin-top: 0;
}

.input-card {
  width: 100%;
  max-width: 820px;
  background: #ffffff;
  border-radius: 16px;
  padding: 20px 22px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.06);
  border: 1px solid #e9ecef;
}

.input-label {
  display: block;
  font-weight: 600;
  font-size: 14px;
  margin-bottom: 8px;
  color: #343a40;
}

 

.original-input::placeholder {
  color: #adb5bd;
}

 

.input-hint {
  margin-top: 8px;
  font-size: 13px;
  color: #868e96;
  font-style: italic;
}
.page.plain {
  background: transparent;
  box-shadow: none;
  margin: 0;
}
.window.plain {
  background: transparent;
}
.window.plain .card {
  background: transparent;
  box-shadow: none;
}

.submit-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;

  margin: 0;              /* ❌ bỏ auto */
  padding: 10px 22px;
  border-radius: 10px;
  border: none;

  background: linear-gradient(135deg, #5f9cff, #7b7dff);
  color: #fff;

  font-size: 14px;
  font-weight: 600;

  cursor: pointer;

  box-shadow: 0 6px 16px rgba(95,156,255,0.35);
  transition: all .2s ease;
}


.submit-btn:active {
  transform: translateY(0);
  box-shadow:
    0 6px 14px rgba(95,156,255,0.35),
    inset 0 2px 4px rgba(0,0,0,0.2);
}
.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow:
    0 12px 26px rgba(95,156,255,0.45),
    inset 0 1px 0 rgba(255,255,255,0.45);
}
.textarea-wrap {
  position: relative;
  display: flex;
  justify-content: center;
}


</style>
