<template>
  <div class="bg-wrapper">
  <div class="snow"></div>
</div>

<div class="sparkle"></div>

</template>

<style>
.bg-wrapper {
  position: fixed;
  inset: 0;
  z-index: -1;
  overflow: hidden;
  pointer-events: none;

  background: radial-gradient(
    circle at top left,
    #fff5f8 0%,
    #f7dbe6 30%,
    #efc1d3 65%,
    #e3a9c1 100%
  );
}

/* ===== SNOW ===== */
.snow {
  position: absolute;
  inset: -200%;
  opacity: 0.22;

  background-image: url("data:image/svg+xml;utf8,\
  <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'>\
  <g transform='scale(0.25) translate(36 36)'>\
  <path fill='%23ffffff' d='M11 2h2v20h-2z M2 11h20v2H2z M4.2 4.2l1.4-1.4L19.8 17.8l-1.4 1.4z M4.2 19.8l-1.4-1.4L17.8 4.2l1.4 1.4z'/>\
  </g></svg>");

  background-size: 140px 140px;
  background-repeat: repeat;

  will-change: transform;
  transform: translate3d(0,0,0);

  animation: snowMove 120s linear infinite;
}

@keyframes snowMove {
  from { transform: translate3d(0, 0, 0); }
  to   { transform: translate3d(120px, 1200px, 0); }
}

/* ===== SPARKLE ===== */
.sparkle {
  pointer-events: none;
  position: absolute;
  inset: -200%;
  opacity: 0.45;
  z-index: 0;   /* cao hơn snow */
  background:
    radial-gradient(circle, rgba(100, 248, 164, 0.9) 2px, transparent 4px),
    radial-gradient(circle, rgba(249, 70, 228, 0.7) 1.5px, transparent 3px),
    radial-gradient(circle, rgba(61, 214, 222, 0.45) 1px, transparent 2.5px);

  background-size:
    80px 80px,
    120px 120px,
    160px 160px;

  background-repeat: repeat;

  will-change: transform;
  transform: translate3d(0,0,0);

  animation: sparkleMove 25s linear infinite;
}

@keyframes sparkleMove {
  from { transform: translate3d(0,0,0); }
  to   { transform: translate3d(1000px,0,0); }
}

/* ===== MOBILE ===== */
@media (max-width: 768px),
       (prefers-reduced-motion: reduce) {
  .snow,
  .sparkle {
    animation: none;
  }
}
</style>
