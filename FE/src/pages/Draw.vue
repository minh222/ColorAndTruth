  <template>
    <div class="wrap">
      <canvas
        ref="canvas"
        width="300"
        height="300"
        @mousedown="start"
        @mousemove="move"
        @mouseup="end"
        @mouseleave="end"
      />

      <div class="guess">
        <span v-if="guess" class="guess-text">{{ guess }}</span>
      </div>

      <button class="btn-clear" @click="clear">
        ♻️ Vẽ lại
      </button>

    </div>
  </template>

  <script setup>

  const iconMap = {
    "aircraft carrier": "🛳️",
    "airplane": "✈️",
    "alarm_clock": "⏰",
    "ambulance": "🚑",
    "angel": "😇",
    "animal_migration": "🐾",
    "ant": "🐜",
    "anvil": "⚒️",
    "apple": "🍎",
    "arm": "💪",
    "asparagus": "🥬",
    "axe": "🪓",
    "backpack": "🎒",
    "banana": "🍌",
    "bandage": "🩹",
    "barn": "🏚️",
    "baseball bat": "🏏",
    "baseball": "⚾",
    "basket": "🧺",
    "basketball bat": "🏀",
    "bathtub": "🛁",
    "beach": "🏖️",
    "bear": "🐻",
    "beard": "🧔",
    "bed": "🛏️",
    "bee": "🐝",
    "belt": "🩳",
    "bench": "🪑",
    "bicycle": "🚲",
    "binoculars": "🔭",
    "bird": "🐦",
    "birthday_cake": "🎂",
    "blackberry": "🫐",
    "blueberry": "🫐",
    "book": "📘",
    "boomerang": "🪃",
    "bottlecap": "🥤",
    "bowtie": "🎀",
    "bracelet": "📿",
    "brain": "🧠",
    "bread": "🍞",
    "bridge": "🌉",
    "broccoli": "🥦",
    "broom": "🧹",
    "bucket": "🪣",
    "bulldozer": "🚜",
    "bus": "🚌",
    "bush": "🌳",
    "butterfly": "🦋",
    "cactus": "🌵",
    "cake": "🍰",
    "calculator": "🧮",
    "calendar": "📅",
    "camel": "🐫",
    "camera": "📷",
    "camouflage": "🪖",
    "campfire": "🔥",
    "candle": "🕯️",
    "cannon": "💣",
    "canoe": "🛶",
    "car": "🚗",
    "carrot": "🥕",
    "castle": "🏰",
    "cat": "🐱",
    "ceiling fan": "🌀",
    "cell_phone": "📱",
    "cello": "🎻",
    "chair": "🪑",
    "chandelier": "💡",
    "church": "⛪",
    "circle": "⚪",
    "clarinet": "🎶",
    "clock": "🕰️",
    "cloud": "☁️",
    "coffee_cup": "☕",
    "compass": "🧭",
    "computer": "💻",
    "cookie": "🍪",
    "cooler": "🧊",
    "couch": "🛋️",
    "cow": "🐮",
    "crab": "🦀",
    "crayon": "🖍️",
    "crocodile": "🐊",
    "crown": "👑",
    "cruise_ship": "🚢",
    "cup": "🥛",
    "diamond": "💎",
    "dishwasher": "🍽️",
    "diving board": "🤿",
    "dog": "🐶",
    "dolphin": "🐬",
    "donut": "🍩",
    "door": "🚪",
    "dragon": "🐉",
    "dresser": "🗄️",
    "drill": "🪛",
    "drums": "🥁",
    "duck": "🦆",
    "dumbbell": "🏋️",
    "ear": "👂",
    "elbow": "💪",
    "elephant": "🐘",
    "envelope": "✉️",
    "eraser": "🧽",
    "eye": "👁️",
    "eyeglasses": "👓",
    "face": "🙂",
    "fan": "🪭",
    "feather": "🪶",
    "fence": "🚧",
    "finger": "☝️",
    "fire hydrant": "🚒",
    "fireplace": "🔥",
    "firetruck": "🚒",
    "fish": "🐟",
    "flamingo": "🦩",
    "flashlight": "🔦",
    "flip_flops": "🩴",
    "floor_lamp": "💡",
    "flower": "🌸",
    "flying_saucer": "🛸",
    "foot": "🦶",
    "fork": "🍴",
    "frog": "🐸",
    "frying_pan": "🍳",
    "garden_hose": "🚿",
    "garden": "🌱",
    "giraffe": "🦒",
    "goatee": "🧔",
    "golf_club": "🏌️",
    "grapes": "🍇",
    "grass": "🌿",
    "guitar": "🎸",
    "hamburger": "🍔",
    "hammer": "🔨",
    "hand": "✋",
    "harp": "🎼",
    "hat": "🎩",
    "headphones": "🎧",
    "hedgehog": "🦔",
    "helicopter": "🚁",
    "helmet": "⛑️",
    "hexagon": "⬡",
    "hockey_puck": "🏒",
    "hockey_stick": "🏒",
    "horse": "🐴",
    "hospital": "🏥",
    "hot_air_balloon": "🎈",
    "hot_dog": "🌭",
    "hot_tub": "🛁",
    "hourglass": "⏳",
    "house_plant": "🪴",
    "house": "🏠",
    "hurricane": "🌀",
    "ice_cream": "🍨",
    "jacket": "🧥",
    "jail": "🚓",
    "kangaroo": "🦘",
    "key": "🔑",
    "keyboard": "⌨️",
    "knee": "🦵",
    "knife": "🔪",
    "ladder": "🪜",
    "lantern": "🏮",
    "laptop": "💻",
    "leaf": "🍃",
    "leg": "🦵",
    "light_bulb": "💡",
    "lighter": "🔥",
    "lighthouse": "🗼",
    "lightning": "⚡",
    "line": "➖",
    "lion": "🦁",
    "lipstick": "💄",
    "lobster": "🦞",
    "lollipop": "🍭",
    "mailbox": "📫",
    "map": "🗺️",
    "marker": "🖊️",
    "matches": "🔥",
    "megaphone": "📣",
    "mermaid": "🧜‍♀️",
    "microphone": "🎤",
    "microwave": "📡",
    "monkey": "🐒",
    "moon": "🌙",
    "mosquito": "🦟",
    "motorbike": "🏍️",
    "mountain": "⛰️",
    "mouse": "🐭",
    "moustache": "👨",
    "mouth": "👄",
    "mug": "☕",
    "mushroom": "🍄",
    "nail": "💅",
    "necklace": "📿",
    "nose": "👃",
    "ocean": "🌊",
    "octagon": "🛑",
    "octopus": "🐙",
    "onion": "🧅",
    "oven": "🔥",
    "owl": "🦉",
    "paint_can": "🎨",
    "paintbrush": "🖌️",
    "palm_tree": "🌴",
    "panda": "🐼",
    "pants": "👖",
    "paper_clip": "📎",
    "parachute": "🪂",
    "parrot": "🦜",
    "passport": "📘",
    "peanut": "🥜",
    "pear": "🍐",
    "peas": "🟢",
    "pencil": "✏️",
    "penguin": "🐧",
    "piano": "🎹",
    "pickup_truck": "🛻",
    "picture_frame": "🖼️",
    "pig": "🐷",
    "pillow": "🛌",
    "pineapple": "🍍",
    "pizza": "🍕",
    "pliers": "🛠️",
    "police_car": "🚓",
    "pond": "💧",
    "pool": "🏊",
    "popsicle": "🍡",
    "postcard": "📮",
    "potato": "🥔",
    "power_outlet": "🔌",
    "purse": "👛",
    "rabbit": "🐰",
    "raccoon": "🦝",
    "radio": "📻",
    "rain": "🌧️",
    "rainbow": "🌈",
    "rake": "🪓",
    "remote_control": "📺",
    "rhinoceros": "🦏",
    "rifle": "🔫",
    "river": "🏞️",
    "roller_coaster": "🎢",
    "rollerskates": "🛼",
    "sailboat": "⛵",
    "sandwich": "🥪",
    "saw": "🪚",
    "saxophone": "🎷",
    "school_bus": "🚌",
    "scissors": "✂️",
    "scorpion": "🦂",
    "screwdriver": "🪛",
    "sea_turtle": "🐢",
    "see_saw": "⚖️",
    "shark": "🦈",
    "sheep": "🐑",
    "shoe": "👟",
    "shorts": "🩳",
    "shovel": "🪓",
    "sink": "🚰",
    "skateboard": "🛹",
    "skull": "💀",
    "skyscraper": "🏙️",
    "sleeping bag": "🛌",
    "smiley_face": "😄",
    "snail": "🐌",
    "snake": "🐍",
    "snorkel": "🤿",
    "snowflake": "❄️",
    "snowman": "☃️",
    "soccer_ball": "⚽",
    "sock": "🧦",
    "speedboat": "🚤",
    "spider": "🕷️",
    "spoon": "🥄",
    "spreadsheet": "📊",
    "square": "⬜",
    "squiggle": "〰️",
    "squirrel": "🐿️",
    "stairs": "🪜",
    "star": "⭐",
    "steak": "🥩",
    "stereo": "🔊",
    "stethoscope": "🩺",
    "stitches": "🪡",
    "stop_sign": "🛑",
    "stove": "🔥",
    "strawberry": "🍓",
    "streetlight": "🚦",
    "string_bean": "🫘",
    "submarine": "🚢",
    "suitcase": "🧳",
    "sun": "☀️",
    "swan": "🦢",
    "sweater": "🧥",
    "swing set": "🎠",
    "sword": "⚔️",
    "syringe": "💉",
    "t-shirt": "👕",
    "table": "🪑",
    "teapot": "🫖",
    "teddy-bear": "🧸",
    "telephone": "☎️",
    "television": "📺",
    "tennis_racquet": "🎾",
    "tent": "⛺",
    "The_Eiffel_Tower": "🗼",
    "The_Great_Wall_of_China": "🧱",
    "The_Mona_Lisa": "🖼️",
    "tiger": "🐯",
    "toaster": "🍞",
    "toe": "🦶",
    "toilet": "🚽",
    "tooth": "🦷",
    "toothbrush": "🪥",
    "toothpaste": "🧴",
    "tornado": "🌪️",
    "tractor": "🚜",
    "traffic_light": "🚦",
    "train": "🚆",
    "tree": "🌳",
    "triangle": "🔺",
    "trombone": "🎺",
    "truck": "🚚",
    "trumpet": "🎺",
    "umbrella": "☂️",
    "underwear": "🩲",
    "van": "🚐",
    "vase": "🏺",
    "violin": "🎻",
    "washing_machine": "🧺",
    "watermelon": "🍉",
    "waterslide": "🏄",
    "whale": "🐋",
    "wheel": "🛞",
    "windmill": "🌬️",
    "wine_bottle": "🍾",
    "wine_glass": "🍷",
    "wristwatch": "⌚",
    "yoga": "🧘",
    "zebra": "🦓",
    "zigzag": "⚡"
  }


  import { ref, onMounted } from "vue"
  import * as tf from "@tensorflow/tfjs"

  const canvas = ref(null)
  const guess = ref("")
  let labels = []

  let ctx
  let drawing = false
  let model = null
  let lastX = 0
  let lastY = 0

  /* ---------- LOAD LABELS ---------- */
  async function loadLabels() {
    const text = await fetch("/quickdraw/class_names.txt").then(r => r.text())
    labels = text.split("\n").map(s => s.trim()).filter(Boolean)
  }

  /* ---------- MOUNT ---------- */
  onMounted(async () => {
    const dpr = window.devicePixelRatio || 1

    canvas.value.width = 300 * dpr
    canvas.value.height = 300 * dpr

    canvas.value.style.width = "300px"
    canvas.value.style.height = "300px"

    ctx = canvas.value.getContext("2d")
    ctx.scale(dpr, dpr)


    ctx.lineWidth = 7
    ctx.lineCap = "round"
    ctx.lineJoin = "round"
    ctx.strokeStyle = "#000"

    clear()

    await loadLabels()
    await tf.ready()
    model = await tf.loadLayersModel("/quickdraw/model.json")
  })

  /* ---------- DRAW ---------- */
  function start(e) {
    drawing = true
    lastX = e.offsetX
    lastY = e.offsetY
    ctx.beginPath()
    ctx.moveTo(lastX, lastY)
  }

  function move(e) {
    if (!drawing) return

    const x = e.offsetX
    const y = e.offsetY

    ctx.lineTo(x, y)
    ctx.stroke()

    lastX = x
    lastY = y
  }

  function end() {
    if (!drawing) return
    drawing = false

    const imgData = ctx.getImageData(0, 0, 300, 300)

    if (inkAmount(imgData) < 400) {
      guess.value = "vẽ thêm tí nữa 👀"
      return
    }

    predict(imgData)
  }

  /* ---------- INK CHECK ---------- */
  function inkAmount(imgData) {
    const img = imgData.data
    let c = 0
    for (let i = 0; i < img.length; i += 4) {
      if (img[i] < 240) c++
    }
    return c
  }


  /* ---------- SHAPE JUDGE (MODEL B) ---------- */
  function shapeScore(imgData) {
    const img = imgData.data

    let count = 0
    let rows = new Set()
    let cols = new Set()

    for (let i = 0; i < img.length; i += 4) {
      if (img[i] < 240) {
        count++
        const idx = i / 4
        cols.add(idx % 300)
        rows.add(Math.floor(idx / 300))
      }
    }

    if (count < 300) return 0

    const area = rows.size * cols.size
    return count / area
  }

  /* ---------- PREPROCESS (SMART) ---------- */
  function preprocess() {
    const SIZE = 28
    const MARGIN = 4

    // 1️⃣ tạo canvas tạm
    const tmp = document.createElement("canvas")
    tmp.width = SIZE
    tmp.height = SIZE
    const tctx = tmp.getContext("2d")

    // 2️⃣ nền đen (đúng QuickDraw)
    tctx.fillStyle = "black"
    tctx.fillRect(0, 0, SIZE, SIZE)

    // 3️⃣ tìm bounding box mực
    const src = ctx.getImageData(0, 0, 300, 300)
    let minX = 300, minY = 300, maxX = 0, maxY = 0
    let found = false

    for (let i = 0; i < src.data.length; i += 4) {
      if (src.data[i] < 240) {
        const idx = i / 4
        const x = idx % 300
        const y = Math.floor(idx / 300)
        minX = Math.min(minX, x)
        minY = Math.min(minY, y)
        maxX = Math.max(maxX, x)
        maxY = Math.max(maxY, y)
        found = true
      }
    }

    if (!found) {
      return tf.zeros([1, 28, 28, 1])
    }

    const w = maxX - minX
    const h = maxY - minY
    const scale = Math.max(w, h)

    // 4️⃣ vẽ crop + scale + center
    tctx.drawImage(
      canvas.value,
      minX,
      minY,
      scale,
      scale,
      MARGIN,
      MARGIN,
      SIZE - MARGIN * 2,
      SIZE - MARGIN * 2
    )

    // 5️⃣ extract + invert
    const data = tctx.getImageData(0, 0, SIZE, SIZE).data
    const input = new Float32Array(SIZE * SIZE)

    for (let i = 0, j = 0; i < data.length; i += 4, j++) {
      input[j] = (255 - data[i]) / 255
    }

    return tf.tensor(input).reshape([1, SIZE, SIZE, 1])
  }

  /**
  * Encode probability distribution → 1 integer [0..N-1]
  * Structure-preserving (relative)
  */
  function encodeDistribution(probs, N = 345) {
    const K = 2

    const top = probs
      .map((v, i) => ({ i, v }))
      .sort((a, b) => b.v - a.v)
      .slice(0, K)

    let code = 0

    for (let rank = 0; rank < top.length; rank++) {
      const idx = top[rank].i

      // 🔴 BỎ % HOÀN TOÀN
      // 🔴 CHỈ DÙNG THỨ TỰ + LABEL ID
      code += (idx + 1) * (rank + 1)
    }

    return code % N
  }



  /* ---------- PREDICT (SMART & CALM) ---------- */
  function predict(imgData) {
    if (!model) return

    const heuristic = strokeHeuristicScore(imgData)

    if (heuristic < 0.15) {
      guess.value = "nét chưa rõ 🤔"
      return
    }

    const out = tf.tidy(() => model.predict(preprocess()))
    const probs = Array.from(out.dataSync())

    const encodedIndex = encodeDistribution(probs)
    const objectName = labels[encodedIndex]
    const icon = iconMap[objectName] || iconMap.default

    const top = probs
      .map((v, i) => ({
        i,
        raw: v,
        final: v * heuristic
      }))
      .sort((a, b) => b.final - a.final)
      .slice(0, 2)

    guess.value =
    top.map(t => {
      const name = labels[t.i]
      const ic = iconMap[name] || iconMap.default
      return `${ic} ${name} ${(t.raw * 100).toFixed(1)}%`
    }).join(" + ") +
    `\n\n>>> ${icon} <<<\n${objectName.toUpperCase()}`



  }
  
  function strokeHeuristicScore(imgData) {
    const ink = inkAmount(imgData)
    const shape = shapeScore(imgData)

    let strokeFactor = Math.min(1, ink / 2500)
    let shapeFactor = Math.min(1, shape * 2.2)

    return strokeFactor * shapeFactor
  }

  /* ---------- CLEAR ---------- */
  function clear() {
    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, 300, 300)
    guess.value = ""
  }
  </script>

  <style scoped>
  .wrap {
    position: absolute;
    top: 70px;
    left: calc(50% + 360px + 54px);
    width: 320px;
    z-index: 20;
  }

  canvas {
    display: block;
    margin: 0 auto;
    border: 1px solid #ccc;
    background: #fff;
    cursor: crosshair;
  }

  .guess {
    min-height: 48px;   /* thay height */
    line-height: 24px;
    margin: 12px 0;
    font-size: 16px;
    overflow: visible; /* hoặc xoá dòng này */
  }


  button {
    margin-top: 8px;
    padding: 6px 14px;
    border-radius: 6px;
    border: none;
    cursor: pointer;
  }
  .guess-text {
    white-space: pre-wrap;
    display: block;
    text-align: center;
  }
 
 .btn-clear {
    margin-top: 12px;
    padding: 8px 16px;
    border-radius: 999px;

    background: linear-gradient(135deg, #111827, #374151);
    color: #fff;

    font-size: 13px;
    font-weight: 500;
    letter-spacing: 0.2px;

    border: none;
    cursor: pointer;

    display: inline-flex;
    align-items: center;
    gap: 6px;

    transition: all 0.2s ease;
  }

  .btn-clear:hover {
    transform: translateY(-1px);
    background: linear-gradient(135deg, #1f2937, #4b5563);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.25);
  }

  .btn-clear:active {
    transform: translateY(0);
    box-shadow: 0 3px 8px rgba(0, 0, 0, 0.2);
  }


  </style>
