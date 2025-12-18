<template>
  <div class="header">
    <div class="title">城市配送货运驾驶员适岗状态监管系统</div>
    <div class="time">{{ currentTime }}</div>
    <button class="manage-btn" @click="goToManagement">
      <img :src="driverIcon" alt="驾驶员管理" class="manage-icon" />
      <span>驾驶员管理</span>
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import driverIcon from '../../assets/images/driver.png'

const router = useRouter()
const currentTime = ref('')
let timer = null

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString()
}

const goToManagement = () => {
  router.push('/driver-management')
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
  background: linear-gradient(to bottom, #fff, #09baf0);
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
.manage-btn {
  position: absolute;
  right: 240px; /* Adjust position to be left of time */
  background: transparent;
  border: none;
  color: #09baf0;
  padding: 6px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
  z-index: 20;
  display: flex;
  align-items: center;
  gap: 8px;
}

.manage-btn:hover {
  opacity: 0.8;
  transform: scale(1.05);
}

.manage-icon {
  width: 24px;
  height: 24px;
  object-fit: contain;
}</style>
