<template>
  <div class="ai-assistant">
    <button class="ai-toggle" @click="togglePanel" aria-label="打开 AI 助手">
      <svg viewBox="0 0 200 200" aria-hidden="true">
        <circle cx="100" cy="100" r="100" fill="#3b82f6" />
        <path d="M100 20c25 0 45 15 45 34v6h6a29 29 0 0 1 29 29v6c0 16-13 29-29 29h-6v6c0 19-20 34-45 34s-45-15-45-34v-6h-6a29 29 0 0 1-29-29v-6c0-16 13-29 29-29h6v-6c0-19 20-34 45-34z" fill="#fff" />
        <circle cx="80" cy="100" r="16" fill="#0f172a" />
        <circle cx="120" cy="100" r="16" fill="#0f172a" />
      </svg>
    </button>

    <transition name="ai-panel">
      <div class="ai-panel" v-if="isOpen">
        <div class="panel-header">
          <div class="assistant-info">
            <span class="status-dot"></span>
            <p class="title">智驾 AI</p>
          </div>
          <button class="close-btn" @click="togglePanel" aria-label="关闭">
            <img :src="closeIcon" alt="关闭图标" />
          </button>
        </div>

        <div class="chat-body">
          <div v-for="(msg, index) in messages" :key="index" :class="['chat-bubble', msg.role]">
            <p>{{ msg.text }}</p>
            <span class="timestamp">{{ msg.time }}</span>
          </div>
        </div>

        <div class="chat-input">
          <input type="text" placeholder="待接入对话功能" disabled>
          <button type="button" disabled>
            <img :src="sendIcon" alt="发送图标" />
          </button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import sendIcon from '../assets/images/send.png'
import closeIcon from '../assets/images/close.png'

const isOpen = ref(false)
const messages = [
  { role: 'ai', text: '嗨～我是你的智驾助手。', time: '09:12' },
  { role: 'user', text: '可以先看看今日运行概况吗？', time: '09:13' },
  { role: 'ai', text: '当然，示例数据稍后呈现。', time: '09:13' }
]

const togglePanel = () => {
  isOpen.value = !isOpen.value
}
</script>

<style scoped>
.ai-assistant {
  position: absolute;
  top: 8px;
  left: 24px;
  z-index: 30;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.ai-toggle {
  width: 48px;
  height: 48px;
  border: none;
  background: transparent;
  cursor: pointer;
  padding: 0;
  transition: transform 0.25s ease;
}

.ai-toggle:hover {
  transform: translateY(-2px) scale(1.02);
}

.ai-toggle svg {
  width: 40px;
  height: 40px;
  filter: drop-shadow(0 6px 12px rgba(37, 99, 235, 0.35));
}

.ai-panel-enter-active,
ai-panel-leave-active {
  transition: all 0.35s ease;
}

.ai-panel-enter-from,
ai-panel-leave-to {
  opacity: 0;
  transform: translateY(-8px) scale(0.98);
}

.ai-panel {
  width: 320px;
  background: rgba(8, 16, 31, 0.95);
  border-radius: 20px;
  border: 1px solid rgba(59, 130, 246, 0.2);
  box-shadow: 0 20px 40px rgba(15, 23, 42, 0.45), inset 0 0 40px rgba(59, 130, 246, 0.08);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  backdrop-filter: blur(15px);
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(15, 23, 42, 0.85);
}

.assistant-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #22c55e;
  box-shadow: 0 0 10px rgba(34, 197, 94, 0.8);
}

.title {
  font-size: 15px;
  font-weight: 600;
  color: #f8fafc;
}

.close-btn {
  width: 30px;
  height: 30px;
  border-radius: 10px;
  border: 1px solid rgba(148, 163, 184, 0.3);
  background: rgba(31, 41, 55, 0.8);
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: border-color 0.2s ease, background 0.2s ease;
}

.close-btn img {
  width: 16px;
  height: 16px;
}

.close-btn:hover {
  border-color: rgba(248, 113, 113, 0.7);
  background: rgba(239, 68, 68, 0.3);
}

.chat-body {
  padding: 16px 20px 8px;
  max-height: 280px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.chat-bubble {
  padding: 0px 14px;
  border-radius: 16px;
  max-width: 90%;
  color: #e2e8f0;
  font-size: 13px;
  box-shadow: 0 8px 20px rgba(15, 23, 42, 0.25);
  position: relative;
}

.chat-bubble.ai {
  align-self: flex-start;
  background: rgba(59, 130, 246, 0.18);
  border: 1px solid rgba(59, 130, 246, 0.4);
}

.chat-bubble.user {
  align-self: flex-end;
  background: rgba(248, 250, 252, 0.1);
  border: 1px solid rgba(248, 250, 252, 0.25);
}

.timestamp {
  display: block;
  margin-top: 6px;
  font-size: 11px;
  color: rgba(148, 163, 184, 0.8);
}

.chat-input {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 5px 12px 5px;
  border-top: 1px solid rgba(59, 130, 246, 0.2);
  background: rgba(2, 6, 23, 0.6);
}

.chat-input input {
  flex: 1;
  padding: 10px 14px;
  border-radius: 12px;
  border: 1px solid rgba(148, 163, 184, 0.4);
  background: rgba(15, 23, 42, 0.8);
  color: rgba(226, 232, 240, 0.8);
  font-size: 13px;
}

.chat-input button {
  width: 44px;
  height: 44px;
  border-radius: 18px;
  border: none;
  background: linear-gradient(135deg, #2563eb, #a855f7);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 1;
}

.chat-input button img {
  width: 22px;
  height: 22px;
}
</style>
