<template>
  <div class="window" >
    <button class="close-btn" @click="$emit('close')">✖</button>

    <!-- 🔥 DÁN NGUYÊN CÁI <div class="page"> CŨ VÀO ĐÂY -->
    <div class="page">
      <!-- ORIGINAL INPUT -->
      <div class="card">
        <h2>📝 Nhập nội dung</h2>

        <div class="input-wrapper">
          <textarea
            v-model="originalText"
            class="original-input"
            placeholder="Nhập nội dung cần phân tích..."
          />
        </div>

        <div class="actions">
          <!-- ✨ NÚT ẨN/HIỆN PANEL -->
          <button
            v-if="hasResult"
            class="toggle-confirm-btn"
            @click="toggleConfirmPanel"
          >
            {{ showConfirmPanel ? "Ẩn ." : "Hiện" }}
          </button>

          <button
            class="exact-btn"
            :disabled="loading || !originalText.trim()"
            @click="onExact"
          >
            {{ loading ? "Đang phân tích..." : "Exact" }}
          </button>
        </div>
      </div>

      <div v-if="hasResult" class="confirm-wrapper">
        <!-- CONFIRM (LUÔN TỒN TẠI, CHỈ ẨN) -->
        <div v-show="showConfirmPanel && hasResult" class="card confirm-card">
          <div class="header-row">
            <h2>✅ Chỉnh sửa lại luận điểm</h2>

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
                <span class="tag">Ý CHÍNH</span>

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
                Hiện Emotion →
              </button>
            </div>

            <!-- EMOTION -->
            <div
              v-if="showAttitude && activePanel === 'emotion'"
              class="box emotion-box"
            >
              <span class="tag">CẢM SÚC</span>
              <ul>
                <li v-for="(e, i) in emotion" :key="i">{{ e }}</li>
              </ul>
              <p class="original-preview">
                {{ selectedOriginalText || originalText }}
              </p>
              <button class="switch-btn" @click="activePanel = 'attitude'">
                ← Quay lại Attitude
              </button>
            </div>
          </div>
          <!-- FOOTER ACTION -->
          <div class="confirm-footer">
            <button
              class="submit-btn"
              :disabled="loading || !emotion.length"
              @click="submitClaimEmotion"
            >
              📤 Gửi Claim + Emotion
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
const emit = defineEmits(["close", "submitted"]);

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
const onExact = async () => {
  if (!originalText.value.trim()) return;

  loading.value = true;
  try {
    const query = encodeURIComponent(originalText.value);

    const res = await fetch(`/api/v1/analyze?original=${query}`, {
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
  const res = await fetch(
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

const submitClaimEmotion = async () => {
  const params = new URLSearchParams({
    claim: claim.value,
    emotion: selectedOriginalText.value,
  });

  try {
    const res = await fetch(`/api/v1/postComment?${params.toString()}`, {
      method: "POST",
    });

    if (!res.ok) throw new Error("Request failed");

    // 🔥 reset cửa sổ
    resetState();

    // 🔥 báo cho CHA reload
    emit("submitted");

    // 🔥 đóng modal
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
/* PAGE */
.page {
  width: min(960px, 100vw);
  margin: 30px auto;
  font-family: Arial, sans-serif;
}

/* CARD */
.card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
}

/* INPUT */
.input-wrapper {
  display: flex;
  justify-content: center;
}

.original-input {
  width: 90%;
  max-width: 820px;
  min-height: 220px;
  padding: 16px 18px;
  font-size: 16px;
  line-height: 1.7;
  border-radius: 12px;
  border: 2px solid #dee2e6;
  background: #f8f9fa;
  resize: vertical;
}

.original-input:focus {
  outline: none;
  border-color: #212529;
  background: #fff;
}

/* ACTION */
.actions {
  margin-top: 12px;
  text-align: right;
}

.exact-btn {
  padding: 8px 18px;
  border-radius: 8px;
  border: none;
  background: #212529;
  color: white;
  cursor: pointer;
}

.exact-btn:disabled {
  opacity: 0.5;
}

/* CONFIRM */
.confirm-card {
  position: absolute;
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
  margin-top: 15px;
  min-height: 240px;
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
  background: #f8f9fa;
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
  top: 10px;
  right: 10px;
  z-index: 10;      /* 🔥 quan trọng */
  background: transparent;
  border: none;
  cursor: pointer;
  font-size: 18px;
}


textarea {
  scroll-margin-top: 0;
}
</style>
