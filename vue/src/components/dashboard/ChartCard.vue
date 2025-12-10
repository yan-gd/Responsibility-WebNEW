<template>
  <div class="chart-card">
    <div class="card-header">
      <span class="card-title">{{ title }}</span>
    </div>
    <div class="card-body">
      <slot></slot>
    </div>
    <div class="corner top-left"></div>
    <div class="corner top-right"></div>
    <div class="corner bottom-left"></div>
    <div class="corner bottom-right"></div>
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: ''
  }
})
</script>

<style scoped>
.chart-card {
  position: relative;
  background: #111827; /* Dark background for contrast */
  border: 1px solid rgba(0, 180, 219, 0.3);
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 8px;
  animation: fadeIn 0.5s ease-out;
  min-height: 0;
  border-radius: 8px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.card-header {
  height: 28px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  padding-left: 8px;
  background: linear-gradient(90deg, rgba(0, 180, 219, 0.2), transparent);
  border-left: 4px solid #00b4db;
  margin-bottom: 6px;
}

.card-title {
  font-size: 14px;
  font-weight: bold;
  color: #fff;
}

.card-body {
  flex: 1;
  position: relative;
  width: 100%;
  min-height: 0;
  overflow: hidden;
}

.corner {
  position: absolute;
  width: 10px;
  height: 10px;
  border: 2px solid #00b4db;
  transition: all 0.3s;
}

.top-left {
  top: 0;
  left: 0;
  border-right: none;
  border-bottom: none;
}

.top-right {
  top: 0;
  right: 0;
  border-left: none;
  border-bottom: none;
}

.bottom-left {
  bottom: 0;
  left: 0;
  border-right: none;
  border-top: none;
}

.bottom-right {
  bottom: 0;
  right: 0;
  border-left: none;
  border-top: none;
}
</style>
