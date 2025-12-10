<template>
  <div class="login-page">
    <!-- Top Blue Section -->
    <div class="top-section">
      <div class="nav-bar">
        <div class="logo">
          <span class="icon-grid">❖</span> Responsibility-Web
        </div>
        <div class="nav-links">
          <span>智能监管</span>
          <span>数据追踪</span>
          <span>AI赋能</span>
          <span>实时反馈</span>
        </div>
        <div class="nav-icons">
          <img :src="searchIcon" alt="搜索" />
        </div>
      </div>

      <div class="hero-content">
        <div class="tags">
          <span class="tag">原创</span>
          <span class="tag">开发</span>
          <span class="tag">鼓励</span>
        </div>
        <h1 class="main-title">城市配送货运驾驶员适岗状态<br>监管平台</h1>
        <div class="meta-info">
          <span>✎ yanyoyo</span>
          <span>👍 9295</span>
          <span>🕒  3days</span>
          <span>📅 2025/12/10</span>
        </div>
      </div>

      <!-- Animated Waves -->
      <div class="waves-container">
        <svg class="waves" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
        viewBox="0 24 150 28" preserveAspectRatio="none" shape-rendering="auto">
          <defs>
            <path id="gentle-wave" d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z" />
          </defs>
          <g class="parallax">
            <use xlink:href="#gentle-wave" x="48" y="0" fill="rgba(255,255,255,0.7)" />
            <use xlink:href="#gentle-wave" x="48" y="3" fill="rgba(255,255,255,0.5)" />
            <use xlink:href="#gentle-wave" x="48" y="5" fill="rgba(255,255,255,0.3)" />
            <use xlink:href="#gentle-wave" x="48" y="7" fill="#fff" />
          </g>
        </svg>
      </div>
    </div>

    <!-- Bottom White Section -->
    <div class="bottom-section">
      <div class="content-wrapper">
        <div class="left-content">
          <h3>欢迎来到智能监管平台！</h3>
          <div class="link-item">Qwen-plus文本理解大模型已经上线</div>
          <div class="link-item">AI助力驾驶员数据的精准查询</div>
          <div class="link-item">如果你有什么好的想法欢迎加入频道、群组交流</div>
        </div>

        <!-- Login Card (Floating on right) -->
        <div class="login-card-container">
          <div class="login-card">
            <h3 class="card-title">Responsibility-Web</h3>
            <div class>
                <p>
                  <a href="https://github.com/yan-gd/Responsibility-WebNEW" target="_blank" rel="noopener noreferrer" style="display: flex; align-items: center; gap: 8px; color: inherit; text-decoration: none;position: relative;left: 110px;">
                    <img :src="githubIcon" alt="GitHub" style="width: 20px; height: 20px;" />
                    <span>Github</span>
                  </a>
                </p>
            </div>
            <form @submit.prevent="handleLogin" class="login-form">
              <div class="input-row">
                <input type="text" v-model="username" placeholder="用户名" />
              </div>
              <div class="input-row">
                <input type="password" v-model="password" placeholder="密码" />
              </div>
              <button type="submit" class="btn-submit">
                <span>了解更多 / 登录</span>
                <span class="arrow">→</span>
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import searchIcon from '../assets/images/search.png'
import githubIcon from '../assets/images/github.png'

const router = useRouter()
const username = ref('')
const password = ref('')

const handleLogin = () => {
  if (username.value && password.value) {
    router.push('/dashboard')
  }
}
</script>

<style scoped>
.login-page {
  width: 100vw;
  height: 100vh;
  background: #f8f9fa;
  overflow: hidden;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  position: fixed;
}

/* Top Section */
.top-section {
  position: relative;
  height: 55vh;
  background: linear-gradient(135deg, #2546bd 0%, #3b66f5 100%);
  color: #fff;
  padding: 0 5%;
  display: flex;
  flex-direction: column;
}

.nav-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
  font-size: 14px;
  font-weight: 600;
}

.logo {
  font-size: 18px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-links {
  display: flex;
  gap: 30px;
  position: relative;
  left: -5vw;
}

.nav-links span {
  cursor: pointer;
  opacity: 0.9;
  transition: opacity 0.3s;
}

.nav-links span:hover {
  opacity: 1;
}

.nav-icons img {
  width: 20px;
  height: 20px;
  cursor: pointer;
}
.hero-content {
  margin-top: 60px;
  max-width: 800px;
  animation: fadeInDown 0.8s ease-out;
}

.tags {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.tag {
  background: rgba(255, 255, 255, 0.2);
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
  backdrop-filter: blur(4px);
}

.main-title {
  font-size: 48px;
  font-weight: 700;
  line-height: 1.2;
  margin-bottom: 20px;
  letter-spacing: 1px;
  position: relative;
  left: 40%;
  top: -30%;
}

.meta-info {
  gap: 20px;
  font-size: 13px;
  opacity: 0.8;
  display: flex;
  position: relative;
  left: -2vw;
  top: -0.5vh;
}

.meta-info span {
  display: flex;
  align-items: center;
  gap: 5px;
}

/* Waves */
.waves-container {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100px;
  overflow: hidden;
  line-height: 0;
}

.waves {
  position: relative;
  width: 100%;
  height: 100%;
  margin-bottom: -7px;
}

.parallax > use {
  animation: move-forever 25s cubic-bezier(.55,.5,.45,.5) infinite;
}
.parallax > use:nth-child(1) { animation-delay: -2s; animation-duration: 7s; }
.parallax > use:nth-child(2) { animation-delay: -3s; animation-duration: 10s; }
.parallax > use:nth-child(3) { animation-delay: -4s; animation-duration: 13s; }
.parallax > use:nth-child(4) { animation-delay: -5s; animation-duration: 20s; }

@keyframes move-forever {
  0% { transform: translate3d(-90px,0,0); }
  100% { transform: translate3d(85px,0,0); }
}

/* Bottom Section */
.bottom-section {
  flex: 1;
  background: #fff;
  position: relative;
  height: 50vh;
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  display: flex;
  justify-content: space-between;
  position: relative;
}

.left-content {
  flex: 1;
  padding-right: 50px;
  padding-top: 0px;
  
}

.left-content h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 20px;
  font-weight: bold;
  border-bottom: 2px solid #eee;
  padding-bottom: 10px;
  display: inline-block;
}

.link-item {
  color: #2563eb;
  margin-bottom: 15px;
  font-size: 15px;
  cursor: pointer;
  font-weight: 500;
  transition: color 0.2s;
}

.link-item:hover {
  color: #1d4ed8;
  text-decoration: underline;
}


/* Login Card */
.login-card-container {
  width: 320px;
  position: relative;
  margin-top: -100px; /* Pull up to overlap wave/blue area slightly or just sit nicely */
  z-index: 10;
}

.login-card {
  background: linear-gradient(145deg, #2a2a72 0%, #009ffd 100%); /* Dark Blue Gradient */
  border-radius: 16px;
  padding: 10px;
  color: #fff;
  box-shadow: 0 20px 40px rgba(0,0,0,0.2);
  overflow: hidden;
  animation: slideUpCard 0.8s ease-out;
  position: relative;
  left: 50px;
  top: 70px;
}

.card-header-bg {
  background: rgba(255,255,255,0.1);
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-badge {
  background: rgba(0,0,0,0.2);
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
}


.card-body {
  padding: 25px;
}

.card-title {
  font-size: 24px;
  margin-bottom: 10px;
  font-weight: bold;
}

.card-desc {
  font-size: 13px;
  opacity: 0.8;
  margin-bottom: 25px;
  line-height: 1.6;
}

.input-row {
  margin-bottom: 15px;
}

.input-row input {
  width: 100%;
  background: rgba(255,255,255,0.1);
  border: 1px solid rgba(255,255,255,0.2);
  padding: 10px 15px;
  border-radius: 8px;
  color: #fff;
  font-size: 14px;
  outline: none;
  transition: all 0.3s;
}

.input-row input:focus {
  background: rgba(255,255,255,0.2);
  border-color: rgba(255,255,255,0.5);
}

.input-row input::placeholder {
  color: rgba(255,255,255,0.5);
}

.btn-submit {
  width: 100%;
  background: rgba(255,255,255,0.2);
  border: 1px solid rgba(255,255,255,0.3);
  padding: 10px;
  border-radius: 20px;
  color: #fff;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  font-size: 14px;
  transition: all 0.3s;
  margin-top: 10px;
}

.btn-submit:hover {
  background: rgba(255,255,255,0.3);
}

@keyframes fadeInDown {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes slideUpCard {
  from { opacity: 0; transform: translateY(50px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
