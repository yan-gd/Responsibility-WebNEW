<template>
  <div class="header">
    <div class="title">城市配送货运驾驶员适岗状态监管系统</div>
    <div class="time">{{ currentTime }}</div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const currentTime = ref('')
let timer = null

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString()
}

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
})

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<style scoped>
.header {
  height: 60px;
  flex-shrink: 0;
  background: transparent;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  color: #fff;
  border-bottom: none;
  z-index: 10;
}

.title {
  font-size: 28px;
  font-weight: bold;
  letter-spacing: 4px;
  background: linear-gradient(to bottom, #fff, #8ec5fc);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.time {
  position: absolute;
  right: 20px;
  font-size: 16px;
  color: #00b4db;
}
</style>
