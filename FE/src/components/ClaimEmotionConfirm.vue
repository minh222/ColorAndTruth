<template>
  <div class="page">
    <!-- ORIGINAL INPUT -->
    <div class="card">
    <h2>📝 Original Text</h2>

    <textarea
        v-model="originalText"
        class="original-input"
        placeholder="Nhập nội dung cần phân tích..."
        rows="6"
    ></textarea>

    <button
        class="exact-btn"
        :disabled="loading || !originalText.trim()"
        @click="onExact"
    >
        {{ loading ? 'Đang phân tích...' : 'Exact' }}
    </button>
    </div>


    <!-- CONFIRM -->
<!-- CONFIRM AREA -->
    <div v-if="hasResult" class="card">
    <div class="header-row">
        <h2>✅ Confirm Extracted Content</h2>

        <button class="toggle-btn" @click="toggleAttitude">
        {{ showAttitude ? 'Ẩn Attitude' : 'Hiện Attitude' }}
        </button>
    </div>

      <div class="confirm-area">
        <!-- CLAIM -->
        <div class="box claim-box">
          <span class="tag claim-tag">CLAIM</span>
          <p>{{ claim }}</p>
        </div>

        <!-- ATTITUDE -->
        <div
          v-if="showAttitude && activePanel === 'attitude'"
          class="box attitude-box"
        >
          <span class="tag attitude-tag">ATTITUDE</span>
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
          <span class="tag emotion-tag">EMOTION</span>
          <ul>
            <li v-for="(e, i) in emotion" :key="i">{{ e }}</li>
          </ul>

          <button class="switch-btn" @click="activePanel = 'attitude'">
            ← Quay lại Attitude
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const hasResult = ref(false)

import { ref } from 'vue'

/* ORIGINAL */
const originalText = ref(
  '.'
)

/* API RESULT */
const claim = ref('')
const emotion = ref([])
const attitude = ref('')

/* UI STATE */
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
    emotion.value = data.emotion
    attitude.value = data.attitude

    hasResult.value = true          // 👈 DÒNG QUAN TRỌNG
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
.page {
  max-width: 960px;
  margin: 30px auto;
  font-family: Arial, sans-serif;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
}

.original-text {
  line-height: 1.6;
  white-space: pre-line;
  margin-bottom: 10px;
}

.exact-btn {
  margin-top: 12px;
  padding: 8px 18px;
  border-radius: 6px;
  border: none;
  background: #212529;
  color: white;
  cursor: pointer;
}
.header-row {
  display: flex;
  justify-content: space-between;
}

.confirm-area {
  display: flex;
  gap: 20px;
  margin-top: 15px;
}

.box {
  flex: 1;
  padding: 16px;
  border-radius: 10px;
}

.switch-btn {
  margin-top: 12px;
  padding: 6px 12px;
  border-radius: 6px;
  border: 1px dashed #666;
  background: #f8f9fa;
  cursor: pointer;
}
.original-input {
  width: 100%;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #ced4da;
  font-size: 14px;
  line-height: 1.6;
  resize: vertical;
}

.exact-btn:disabled {
  background: #adb5bd;
  cursor: not-allowed;
}


/* COLORS */
.claim-box {
  background: #fff8e1;
}
.attitude-box {
  background: #f3e5f5;
}
.emotion-box {
  background: #e3f2fd;
}
</style>
