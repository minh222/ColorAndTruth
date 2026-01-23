<template>
  <div class="page">
    <!-- ORIGINAL INPUT -->
    <div class="card">
      <h2>📝 Original Text</h2>

      <div class="input-wrapper">
        <textarea
          v-model="originalText"
          class="original-input"
          placeholder="Nhập nội dung cần phân tích..."
        />
      </div>

      <div class="actions">
        <button
          class="exact-btn"
          :disabled="loading || !originalText.trim()"
          @click="onExact"
        >
          {{ loading ? 'Đang phân tích...' : 'Exact' }}
        </button>
      </div>
    </div>

    <!-- CONFIRM (LUÔN TỒN TẠI, CHỈ ẨN) -->
    <div
      class="card confirm-card"
      :class="{ hidden: !hasResult }"
    >
      <div class="header-row">
        <h2>✅ Confirm Extracted Content</h2>

        <div class="eye-slot">
          <button
            class="eye-toggle"
            @click="toggleAttitude"
            :title="showAttitude ? 'Ẩn attitude' : 'Hiện attitude'"
          >
            {{ showAttitude ? '👁️‍🗨️' : '👁️' }}
          </button>
        </div>
      </div>

      <div class="confirm-area confirm-body">
        <!-- CLAIM -->
        <div class="box claim-box">
          <span class="tag">CLAIM</span>
          <p>{{ claim }}</p>
        </div>

        <!-- ATTITUDE -->
        <div
          v-if="showAttitude && activePanel === 'attitude'"
          class="box attitude-box"
        >
          <span class="tag">ATTITUDE</span>
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
          <span class="tag">EMOTION</span>
          <ul>
            <li v-for="(e, i) in emotion" :key="i">{{ e }}</li>
          </ul>

          <button
            class="switch-btn"
            @click="activePanel = 'attitude'"
          >
            ← Quay lại Attitude
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

/* ORIGINAL */
const originalText = ref('')

/* API RESULT */
const claim = ref('')
const emotion = ref([])
const attitude = ref('')

/* UI STATE */
const hasResult = ref(false)
const showAttitude = ref(false)
const activePanel = ref('attitude')
const loading = ref(false)

/* ACTION */
const onExact = async () => {
  if (!originalText.value.trim()) return

  loading.value = true
  try {
    const query = encodeURIComponent(originalText.value)

    const res = await fetch(
      `/api/v1/analyze?original=${query}`,
      { method: 'POST' }
    )

    const data = await res.json()

    claim.value = data.claim
    emotion.value = data.emotion || []
    attitude.value = data.attitude || ''

    hasResult.value = true
    showAttitude.value = true
    activePanel.value = 'attitude'
  } catch (e) {
    alert('Không gọi được API')
  } finally {
    loading.value = false
  }
}

/* TOGGLE */
const toggleAttitude = () => {
  showAttitude.value = !showAttitude.value
  activePanel.value = 'attitude'
}
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
  box-shadow: 0 6px 18px rgba(0,0,0,0.06);
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
  min-height: 320px;
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
</style>
