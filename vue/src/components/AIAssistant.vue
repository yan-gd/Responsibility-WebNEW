<template>
  <div class="ai-assistant">
    <!-- Backdrop -->
    <transition name="fade">
      <div class="ai-backdrop" v-if="isOpen" @click="togglePanel"></div>
    </transition>

    <button class="ai-toggle" @click="togglePanel" aria-label="打开 AI 助手">
      <img :src="aiIcon" alt="AI助手" style="width:48px;height:48px;" />
    </button>

    <!-- Welcome Message Tooltip -->
    <transition name="tooltip-fade">
      <div class="welcome-tooltip" v-if="showWelcome">
        <p>我是您的智能助手！</p>
        <div class="tooltip-arrow"></div>
      </div>
    </transition>

    <!-- Main Panel -->
    <transition name="panel-scale">
      <div class="ai-panel" v-if="isOpen">
        <!-- Header -->
        <div class="panel-header">
          <div class="header-left">
            <img :src="aiIcon" alt="Logo" class="header-logo" />
            <span class="header-title">智驾助手</span>
          </div>
          <div class="header-actions">
            <button class="action-btn" title="复制" @click="copyChat"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path></svg></button>
            <button class="action-btn" title="下载" @click="downloadChat"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="7 10 12 15 17 10"></polyline><line x1="12" y1="15" x2="12" y2="3"></line></svg></button>
            <button class="action-btn" title="刷新" @click="resetChat"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="23 4 23 10 17 10"></polyline><polyline points="1 20 1 14 7 14"></polyline><path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15"></path></svg></button>
            <button class="action-btn close" @click="togglePanel" title="关闭"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg></button>
          </div>
        </div>

        <!-- Content Area -->
        <div class="panel-content">
          <div v-if="messages.length === 0" class="welcome-section">
            <h2 class="welcome-title">Welcome👋</h2>
            <p class="welcome-desc">我是您的网站助手。您可以询问关于驾驶员监控、YOLO接入、定价与授权、集成或支持的任何问题。</p>
          </div>

          <div v-if="messages.length === 0" class="suggestions-grid">
            <button v-for="(item, idx) in suggestions" :key="idx" class="suggestion-chip" @click="handleSuggestionClick(item)">
              {{ item }}
            </button>
          </div>

          <div v-else class="chat-messages" ref="chatContainer">
            <div v-for="(msg, index) in messages" :key="index" :class="['message-row', msg.role]">
              <div v-if="msg.role === 'ai'" class="ai-avatar">
                <img :src="aiIcon" alt="AI" />
              </div>
              <div class="message-container">
                <div class="message-content">{{ msg.content }}</div>
              </div>
            </div>
            <div v-if="isLoading" class="message-row ai">
              <div class="ai-avatar">
                <img :src="aiIcon" alt="AI" />
              </div>
              <div class="message-container">
                <div class="message-content loading">
                  <span class="dot"></span>
                  <span class="dot"></span>
                  <span class="dot"></span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Input Area -->
        <div class="input-wrapper">
          <div class="input-container">
            <input 
              type="text" 
              placeholder="Ask anything..." 
              class="chat-input" 
              v-model="inputMessage"
              @keydown="handleKeydown"
              :disabled="isLoading"
            />
            <button class="send-btn" @click="sendMessage(inputMessage)" :disabled="isLoading || !inputMessage.trim()">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="22" y1="2" x2="11" y2="13"></line><polygon points="22 2 15 22 11 13 2 9 22 2"></polygon></svg>
            </button>
          </div>
          <div class="footer-text">
            Powered by Qwen3-VL-Plus
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import aiIcon from '../assets/images/ai.png'
import { chatWithAI } from '../api/index.js'

const isOpen = ref(false)
const showWelcome = ref(false)
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const chatContainer = ref(null)

const suggestions = [
  'What\'s new with 平台近期更新？',
  '如何接入驾驶员适岗监控数据？',
  '企业版授权与部署流程？'
]

const togglePanel = () => {
  isOpen.value = !isOpen.value
}

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick()
  const container = document.querySelector('.chat-messages')
  if (container) {
    container.scrollTop = container.scrollHeight
  }
}

const isTyping = ref(false)

// 打字机效果
const typeWriter = async (text, messageIndex) => {
  const speed = 20 // 打字速度 ms
  let i = 0
  messages.value[messageIndex].content = ''
  isTyping.value = true

  return new Promise((resolve) => {
    const timer = setInterval(() => {
      if (i < text.length) {
        messages.value[messageIndex].content += text.charAt(i)
        i++
        scrollToBottom()
      } else {
        clearInterval(timer)
        isTyping.value = false
        resolve()
      }
    }, speed)
  })
}

// 发送消息
const sendMessage = async (text) => {
  if (!text || !text.trim() || isLoading.value || isTyping.value) return

  const userMessage = text.trim()
  inputMessage.value = ''

  // 添加用户消息
  messages.value.push({ role: 'user', content: userMessage })
  await scrollToBottom()

  // 准备历史消息格式
  const history = messages.value.slice(0, -1).map(msg => ({
    role: msg.role === 'user' ? 'user' : 'assistant',
    content: msg.content
  }))

  isLoading.value = true

  try {
    const response = await chatWithAI(userMessage, history)
    isLoading.value = false
    
    const fullText = response.content || response.message || '抱歉，我没有收到有效的回复。'
    
    // 添加 AI 消息占位
    messages.value.push({ role: 'ai', content: '' })
    const aiMsgIndex = messages.value.length - 1
    
    // 开始打字
    await typeWriter(fullText, aiMsgIndex)
    
  } catch (error) {
    console.error('AI 请求失败:', error)
    isLoading.value = false
    messages.value.push({ role: 'ai', content: '抱歉，请求出现错误，请稍后重试。' })
  } finally {
    await scrollToBottom()
  }
}

const handleSuggestionClick = (text) => {
  sendMessage(text)
}

const handleKeydown = (event) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    sendMessage(inputMessage.value)
  }
}

const copyChat = async () => {
  if (messages.value.length === 0) return
  const text = messages.value.map(m => `${m.role === 'user' ? 'You' : 'AI'}: ${m.content}`).join('\n')
  try {
    await navigator.clipboard.writeText(text)
    alert('聊天记录已复制')
  } catch (err) {
    console.error('复制失败', err)
  }
}

const downloadChat = () => {
  if (messages.value.length === 0) return
  const text = messages.value.map(m => `${m.role === 'user' ? 'You' : 'AI'}: ${m.content}`).join('\n')
  const blob = new Blob([text], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = 'chat-history.txt'
  a.click()
  URL.revokeObjectURL(url)
}

const resetChat = () => {
  messages.value = []
}

onMounted(() => {
  setTimeout(() => {
    showWelcome.value = true
  }, 800)
  
  setTimeout(() => {
    showWelcome.value = false
  }, 4000)
})
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
  outline: none;
}

.ai-toggle:hover {
  transform: translateY(-2px) scale(1.02);
}

/* Welcome Tooltip */
.welcome-tooltip {
  position: absolute;
  left: 65px;
  top: 8px;
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.95), rgba(99, 102, 241, 0.95));
  color: #fff;
  padding: 12px 18px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
  box-shadow: 0 8px 24px rgba(59, 130, 246, 0.4);
  z-index: 25;
}

.welcome-tooltip p {
  margin: 0;
}

.tooltip-arrow {
  position: absolute;
  left: -6px;
  top: 50%;
  transform: translateY(-50%);
  width: 0;
  height: 0;
  border-top: 6px solid transparent;
  border-bottom: 6px solid transparent;
  border-right: 6px solid rgba(59, 130, 246, 0.95);
}

.tooltip-fade-enter-active {
  animation: tooltipBounce 0.6s ease-out;
}

.tooltip-fade-leave-active {
  animation: tooltipFadeOut 0.3s ease-in;
}

@keyframes tooltipBounce {
  0% {
    opacity: 0;
    transform: translateX(-20px) scale(0.8);
  }
  60% {
    opacity: 1;
    transform: translateX(5px) scale(1.05);
  }
  100% {
    opacity: 1;
    transform: translateX(0) scale(1);
  }
}

@keyframes tooltipFadeOut {
  from {
    opacity: 1;
    transform: translateX(0);
  }
  to {
    opacity: 0;
    transform: translateX(-10px);
  }
}

/* Main Panel */
.ai-panel {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 800px;
  height: 700px;
  max-width: 90vw;
  max-height: 90vh;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.12);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border: 1px solid rgba(0,0,0,0.05);
  z-index: 2000;
}

/* Header */
.panel-header {
  padding: 16px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-logo {
  width: 24px;
  height: 24px;
}

.header-title {
  font-weight: 600;
  font-size: 16px;
  color: #1a1a1a;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  background: transparent;
  border: none;
  color: #666;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
  outline: none;
}

.action-btn:hover {
  background: #f5f5f5;
  color: #333;
}

/* Content */
.panel-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.welcome-section {
  margin-bottom: 24px;
}

.welcome-title {
  font-size: 20px;
  font-weight: 600;
  color: #111;
  margin: 0 0 8px 0;
}

.welcome-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin: 0;
}

.suggestions-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.suggestion-chip {
  background: #f3f4f6;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 13px;
  color: #374151;
  cursor: pointer;
  transition: all 0.2s;
  text-align: left;
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  outline: none;
}

.suggestion-chip:hover {
  background: #e5e7eb;
}

/* Input Area */
.input-wrapper {
  padding: 16px 24px;
  background: #fff;
  border-top: 1px solid #f3f4f6;
}

.input-container {
  display: flex;
  align-items: center;
  background: #f3f4f6;
  border-radius: 24px;
  padding: 8px 16px;
  gap: 12px;
  border: 1px solid transparent;
  transition: all 0.2s;
}

.input-container:focus-within {
  background: #fff;
  border-color: #e5e7eb;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.plus-btn:hover {
  background: #d1d5db;
  color: #374151;
}

.chat-input {
  flex: 1;
  border: none;
  background: transparent;
  font-size: 15px;
  color: #1f2937;
  outline: none;
  padding: 8px 0;
}

.chat-input::placeholder {
  color: #9ca3af;
}

.footer-text {
  text-align: center;
  font-size: 11px;
  color: #9ca3af;
  margin-top: 8px;
}

/* Transitions */
.tooltip-fade-enter-active,
.tooltip-fade-leave-active {
  transition: opacity 0.3s, transform 0.3s;
}

.tooltip-fade-enter-from,
.tooltip-fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

.panel-scale-enter-active,
.panel-scale-leave-active {
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  transform-origin: center center;
}

.panel-scale-enter-from,
.panel-scale-leave-to {
  opacity: 0;
  transform: translate(-50%, -50%) scale(0.9);
}

/* Chat Messages */
.chat-messages {
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding-bottom: 16px;
}

.message-row {
  display: flex;
  gap: 12px;
  max-width: 100%;
}

.message-row.user {
  justify-content: flex-end;
}

.message-row.ai {
  justify-content: flex-start;
  align-items: flex-start;
}

.ai-avatar {
  width: 32px;
  height: 32px;
  flex-shrink: 0;
  border-radius: 50%;
  overflow: hidden;
  background: #f3f4f6;
}

.ai-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.message-container {
  display: flex;
  flex-direction: column;
  gap: 4px;
  max-width: 85%;
}

.message-content {
  font-size: 15px;
  line-height: 1.6;
  color: #1f2937;
  white-space: pre-wrap;
  word-break: break-word;
}

.message-row.user .message-content {
  background: #000;
  color: #fff;
  padding: 10px 16px;
  border-radius: 12px;
  border-bottom-right-radius: 2px;
}

.message-row.ai .message-content {
  background: transparent;
  padding: 0;
  color: #111;
  text-align: left;
}

/* Loading Animation */
.message-content.loading {
  display: flex;
  gap: 4px;
  padding: 8px 0;
}

.message-content.loading .dot {
  width: 8px;
  height: 8px;
  background: #9ca3af;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out both;
}

.message-content.loading .dot:nth-child(1) {
  animation-delay: -0.32s;
}

.message-content.loading .dot:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes bounce {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

/* Send Button */
.send-btn {
  background: #3b82f6;
  border: none;
  color: #fff;
  cursor: pointer;
  padding: 8px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  outline: none;
}

.send-btn:hover:not(:disabled) {
  background: #2563eb;
}

.send-btn:disabled {
  background: #d1d5db;
  cursor: not-allowed;
}

/* Backdrop */
.ai-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(4px);
  z-index: 1990;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
