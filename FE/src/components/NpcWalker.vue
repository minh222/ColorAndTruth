<template>
  <div class="npc-world">
    <svg class="path-layer" :width="vw" :height="vh">
      <path
        ref="pathEl"
        :d="pathD"
        fill="none"
        stroke="rgba(255,255,255,0.4)"
        stroke-width="3"
        stroke-dasharray="6 6"
      />
    </svg>

    <div
      class="npc chibi"
      :data-emotion="emotion"
      :class="[{ walking: moving }, direction]"
      :style="{ left: pos.x + 'px', top: pos.y + 'px' }"
    >
      <div v-if="speech" class="speech">{{ speech }}</div>

      <div class="head">
        <span class="eye left"></span>
        <span class="eye right"></span>
        <span class="blush left"></span>
        <span class="blush right"></span>
        <span class="mouth"></span>
      </div>

      <div class="neck"></div>
      <div class="body"></div>
      <div class="skirt"></div>
      <div class="leg back"></div>
      <div class="leg front"></div>
    </div>
  </div>
</template>

<script setup>
/* ⚠️ GIỮ NGUYÊN LOGIC */
import { ref, onMounted, onBeforeUnmount } from "vue"

const vw = ref(window.innerWidth)
const vh = ref(window.innerHeight)

const pathEl = ref(null)
const pathD = ref("")
let pathLength = 0
let progress = 0

const pos = ref({ x: 0, y: 0 })
const direction = ref("right")
const moving = ref(false)

const emotion = ref("idle")
const speech = ref("")
let speechTimer = null

const EMOTIONS = {
  idle: ["Hmm...", "Đứng nghỉ tí 😐"],
  happy: ["Đi dạo vui ghê~", "Biết bí mật rùi nha 😄", "Cậu đang xem gì zọ"],
  angry: ["Tới mép rồi 😠"],
  sad: ["Mỏi chân quá 😢"]
}

let lastEmotionTime = 0
const EMOTION_COOLDOWN = 3000
const EMOTION_DELAY = 500

function say(type) {
  const now = Date.now()
  if (now - lastEmotionTime < EMOTION_COOLDOWN) return
  lastEmotionTime = now

  setTimeout(() => {
    emotion.value = type
    speech.value =
      EMOTIONS[type][Math.floor(Math.random() * EMOTIONS[type].length)]

    clearTimeout(speechTimer)
    speechTimer = setTimeout(() => {
      speech.value = ""
      emotion.value = "idle"
    }, 2500)
  }, EMOTION_DELAY)
}

const SPEED = 2
let moveDir = 0
let rafId = null

function buildPath() {
  const y = vh.value / 2   // 👈 kéo đường ra giữa màn hình
  pathD.value = `M 0 ${y} L ${vw.value} ${y}`

  requestAnimationFrame(() => {
    if (pathEl.value) {
      pathLength = pathEl.value.getTotalLength()
      updateNpcPosition()
    }
  })
}


function updateNpcPosition() {
  if (!pathEl.value) return

  progress += moveDir * SPEED
  progress = Math.max(0, Math.min(pathLength, progress))

  if (progress === 0 || progress === pathLength) say("angry")

  const p = pathEl.value.getPointAtLength(progress)
  pos.value = { x: p.x - 18, y: p.y - 90 }
}

function loop() {
  if (moveDir !== 0) updateNpcPosition()
  rafId = requestAnimationFrame(loop)
}

function onKeyDown(e) {
  if (e.key === "ArrowRight" && moveDir !== 1) {
    moveDir = 1
    direction.value = "right"
    moving.value = true
    say("happy")
  }
  if (e.key === "ArrowLeft" && moveDir !== -1) {
    moveDir = -1
    direction.value = "left"
    moving.value = true
    say("happy")
  }
}

function onKeyUp(e) {
  if (
    (e.key === "ArrowRight" && moveDir === 1) ||
    (e.key === "ArrowLeft" && moveDir === -1)
  ) {
    moveDir = 0
    moving.value = false
  }
}

let idleTimer = null
function startIdleTalk() {
  idleTimer = setInterval(() => {
    if (!moving.value) say("idle")
  }, 6000)
}

onMounted(() => {
  buildPath()
  window.addEventListener("keydown", onKeyDown)
  window.addEventListener("keyup", onKeyUp)
  startIdleTalk()
  loop()
})

onBeforeUnmount(() => {
  cancelAnimationFrame(rafId)
  clearInterval(idleTimer)
})
</script>

<style scoped>
/* WORLD */
.npc-world {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 9999;
}

/* NPC */
.npc.chibi {
  position: absolute;
  width: 34px;
  height: 90px;
  transform-origin: center bottom;
}

/* QUAY NGƯỜI */
.npc.left  { transform: scaleX(-1); }
.npc.right { transform: scaleX(1); }

/* SPEECH */
.speech {
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: white;
  padding: 4px 8px;
  font-size: 11px;
  border-radius: 14px;
  box-shadow: 0 2px 4px rgba(0,0,0,.2);
}

/* HEAD – TO HƠN, CHIBI */
.head {
  overflow: visible;
  width: 44px;
  height: 44px;
  background: #ffe0cc;
  border-radius: 50%;
  margin: 0 auto;
  position: relative;
}

/* BOUNCE */
.walking .head {
  animation: head-bounce 0.5s infinite ease-in-out;
}

/* EYES – ANIME */
.eye {
  position: absolute;
  top: 14px;
  width: 10px;
  height: 13px;
  background: #222;
  border-radius: 50%;
  transition: transform 0.15s;
  animation: blink 5s infinite;
}
.eye.left  { left: 11px; }
.eye.right { left: 25px; }

/* HIGHLIGHT */
.eye::after {
  content: "";
  position: absolute;
  top: 2px;
  left: 2px;
  width: 4px;
  height: 4px;
  background: white;
  border-radius: 50%;
}
.eye::before {
  content: "";
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 2px;
  height: 2px;
  background: rgba(255,255,255,.8);
  border-radius: 50%;
}

/* BLINK (KHÔNG PHÁ HƯỚNG) */
@keyframes blink {
  0%, 95%, 100% { transform: scaleY(1); }
  96% { transform: scaleY(0.1); }
}

/* BLUSH */
.blush {
  position: absolute;
  top: 28px;
  width: 9px;
  height: 5px;
  background: rgba(255,120,150,0.5);
  border-radius: 50%;
}
.blush.left  { left: 6px; }
.blush.right { right: 6px; }

/* MOUTH */
.mouth {
  position: absolute;
  bottom: 9px;
  left: 50%;
  width: 11px;
  height: 6px;
  transform: translateX(-50%);
  border-bottom: 2px solid #333;
  border-radius: 0 0 14px 14px;
  transition: transform 0.15s;
}

/* 👀 LOOK DIRECTION – GIỮ NGUYÊN GỐC */
.npc.right .eye,
.npc.right .eye::after,
.npc.right .eye::before,
.npc.right .mouth {
  transform: translateX(1.5px);
}

.npc.left .eye,
.npc.left .eye::after,
.npc.left .eye::before,
.npc.left .mouth {
  transform: translateX(-1.5px);
}

/* EMOTION */
.npc[data-emotion="sad"] .mouth {
  transform: translateX(-50%) rotate(180deg);
}

/* BODY – NGẮN CHIBI */
.neck {
  width: 4px;
  height: 4px;
  background: #ffddb8;
  margin: -2px auto 0;
}
.body {
  width: 16px;
  height: 14px;
  background: #ff9acb;
  margin: 0 auto;
  border-radius: 8px;
}
.skirt {
  width: 28px;
  height: 12px;
  background: #ff77aa;
  margin: -1px auto 0;
  border-radius: 0 0 16px 16px;
}

/* LEGS */
.leg {
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 4px;
  height: 18px;
  background: #444;
  border-radius: 4px;
  transform: translateX(-50%);
}

.walking .leg.front {
  animation: leg-front 0.8s infinite ease-in-out;
}
.walking .leg.back {
  animation: leg-back 0.8s infinite ease-in-out;
}

@keyframes leg-front {
  50% { transform: translateX(-50%) translateX(4px); }
}
@keyframes leg-back {
  50% { transform: translateX(-50%) translateX(-4px); }
}

@keyframes head-bounce {
  50% { transform: translateY(2px); }
}
</style>
