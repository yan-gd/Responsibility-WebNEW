<template>
  <div class="app-container">
    <!-- Sidebar -->
    <div class="sidebar" :class="{ 'sidebar-closed': !isSidebarOpen }">
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
          </g>
        </svg>
      </div>
      
      <div class="logo-area">
        <div class="logo-icon">D</div>
        <span class="logo-text" v-show="isSidebarOpen">Driver Admin</span>
      </div>
      <div class="menu">
        <div class="menu-item" @click="goBack">
          <span class="menu-icon">
            <img :src="dataIcon" alt="数据大屏" />
          </span>
          <span v-show="isSidebarOpen">数据大屏</span>
        </div>
        <div class="menu-item active">
          <span class="menu-icon">
            <img :src="personIcon" alt="驾驶员信息" />
          </span>
          <span v-show="isSidebarOpen">驾驶员信息管理</span>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <!-- Top Header -->
      <div class="top-header">
        <div class="header-left">
          <button class="toggle-btn ripple-btn" @click="toggleSidebar">
            <span class="icon">{{ isSidebarOpen ? '◀' : '▶' }}</span>
          </button>
          <div class="breadcrumb">
            <span class="crumb-item">首页</span>
            <span class="separator">/</span>
            <span class="crumb-item active">驾驶员信息管理</span>
          </div>
        </div>
        <div class="user-profile">
          <div class="notification-badge">
            <span class="icon">🔔</span>
            <span class="badge">3</span>
          </div>
          <div class="avatar-circle">A</div>
          <span class="username">Admin</span>
        </div>
      </div>

      <div class="scrollable-content">
        <div class="card main-card">

          
          <!-- Filter Section -->
          <div class="filter-section">
            <div class="filter-form">
              <div class="form-item">
                <label>驾驶员姓名:</label>
                <input type="text" v-model="searchQuery" placeholder="请输入姓名" @keyup.enter="handleSearch">
              </div>
              <div class="form-item">
                <label>车牌号码:</label>
                <input type="text" v-model="licenseQuery" placeholder="请输入车牌号">
              </div>
              <div class="form-item">
                <label>所属车队:</label>
                <input type="text" placeholder="请输入车队名称">
              </div>
              <div class="form-item">
                <label>状态:</label>
                <select>
                  <option value="">全部</option>
                  <option value="normal">正常</option>
                  <option value="fatigue">疲劳</option>
                </select>
              </div>
            </div>
            <div class="filter-actions">
              <div class="left-actions">
                <button class="btn btn-primary icon-btn" @click="showAddModal">
                  <span>+</span> 新建
                </button>
                <button class="btn btn-danger icon-btn" @click="handleBatchDelete" v-if="selectedIds.length > 0">
                  <span>🗑️</span> 批量删除
                </button>
              </div>
              <div class="right-actions">
                <button class="btn btn-primary" @click="handleSearch">查询</button>
                <button class="btn btn-default" @click="resetSearch">重置</button>
              </div>
            </div>
          </div>

          <!-- Table Section -->
          <div class="table-section">
            <div class="table-container">
              <table class="custom-table">
                <thead>
                  <tr>
                    <th class="checkbox-col"><input type="checkbox" v-model="selectAll" @change="toggleSelectAll"></th>
                    <th>驾驶员姓名</th>
                    <th>工号</th>
                    <th>身份证号</th>
                    <th>性别</th>
                    <th>年龄</th>
                    <th>驾龄</th>
                    <th>车牌号</th>
                    <th>疲劳状态</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="loading">
                    <td colspan="10" class="loading-cell">
                      <div class="spinner"></div> 加载中...
                    </td>
                  </tr>
                  <tr v-else-if="drivers.length === 0">
                    <td colspan="10" class="empty-cell">暂无数据</td>
                  </tr>
                  <tr v-else v-for="driver in paginatedDrivers" :key="driver.id">
                    <td class="checkbox-col">
                      <input type="checkbox" :checked="isSelected(driver.id)" @change="toggleSelect(driver.id)">
                    </td>
                    <td>
                      <div class="user-info">
                        <div class="avatar" :style="{ backgroundColor: getAvatarColor(driver.driverName) }">
                          {{ driver.driverName.charAt(0) }}
                        </div>
                        <span class="name-link">{{ driver.driverName }}</span>
                      </div>
                    </td>
                    <td>{{ 'DR' + String(driver.id).padStart(6, '0') }}</td>
                    <td>{{ generateIdCard(driver) }}</td>
                    <td>{{ driver.gender }}</td>
                    <td>{{ driver.age }}</td>
                    <td>{{ driver.drivingYears }}年</td>
                    <td>{{ driver.licensePlate }}</td>
                    <td>
                      <span :class="['status-badge', getStatusClass(driver.fatigueStatus)]">
                        {{ driver.fatigueStatus }}
                      </span>
                    </td>
                    <td>
                      <button class="text-btn" @click="showEditModal(driver)">编辑</button>
                      <button class="text-btn danger" @click="handleDelete(driver.id)">删除</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            
            <div class="pagination">
              <div class="page-info">共 {{ drivers.length }} 条，每页 {{ pageSize }} 条</div>
              <div class="page-controls">
                <button class="page-btn" @click="prevPage" :disabled="currentPage === 1">&lt;</button>
                <button 
                  v-for="page in pageNumbers" 
                  :key="page"
                  :class="['page-btn', { active: page === currentPage, ellipsis: page === '...' }]"
                  @click="goToPage(page)"
                  :disabled="page === '...'"
                >
                  {{ page }}
                </button>
                <button class="page-btn" @click="nextPage" :disabled="currentPage === totalPages">&gt;</button>
                <span class="jump-text">跳转到</span>
                <input type="text" class="jump-input" v-model="jumpPage" @keyup.enter="handleJumpPage" placeholder="页码">
                <button class="btn btn-default btn-sm" @click="handleJumpPage">确定</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal for Add/Edit Driver -->
    <div class="modal-overlay" v-if="showModal" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ modalMode === 'add' ? '新增驾驶员' : '编辑驾驶员' }}</h3>
          <button class="close-btn" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <div class="form-grid">
            <div class="form-group">
              <label>驾驶员姓名 <span class="required">*</span></label>
              <input type="text" v-model="currentDriver.driverName" placeholder="请输入姓名" required>
            </div>
            <div class="form-group">
              <label>性别 <span class="required">*</span></label>
              <select v-model="currentDriver.gender">
                <option value="男">男</option>
                <option value="女">女</option>
              </select>
            </div>
            <div class="form-group">
              <label>年龄 <span class="required">*</span></label>
              <input type="number" v-model.number="currentDriver.age" placeholder="请输入年龄" required>
            </div>
            <div class="form-group">
              <label>驾龄（年） <span class="required">*</span></label>
              <input type="number" v-model.number="currentDriver.drivingYears" placeholder="请输入驾龄" required>
            </div>
            <div class="form-group">
              <label>车牌号 <span class="required">*</span></label>
              <input type="text" v-model="currentDriver.licensePlate" placeholder="请输入车牌号" required>
            </div>
            <div class="form-group">
              <label>疲劳状态</label>
              <select v-model="currentDriver.fatigueStatus">
                <option value="正常">正常</option>
                <option value="轻度疲劳">轻度疲劳</option>
                <option value="中度疲劳">中度疲劳</option>
                <option value="重度疲劳">重度疲劳</option>
              </select>
            </div>
            <div class="form-group">
              <label>驾驶行为</label>
              <select v-model="currentDriver.behaviorStatus">
                <option value="正常驾驶">正常驾驶</option>
                <option value="超速">超速</option>
                <option value="急刹车">急刹车</option>
                <option value="违规变道">违规变道</option>
                <option value="打电话">打电话</option>
                <option value="抽烟">抽烟</option>
              </select>
            </div>
            <div class="form-group">
              <label>情绪状态</label>
              <select v-model="currentDriver.emotionStatus">
                <option value="平静">平静</option>
                <option value="焦虑">焦虑</option>
                <option value="愤怒">愤怒</option>
                <option value="疲惫">疲惫</option>
              </select>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-default" @click="closeModal">取消</button>
          <button class="btn btn-primary" @click="saveDriver">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getDrivers, addDriver, updateDriver, deleteDriver, batchDeleteDrivers } from '../api'
import dataIcon from '../assets/images/data.png'
import personIcon from '../assets/images/person.png'

const router = useRouter()
const drivers = ref([])
const searchQuery = ref('')
const licenseQuery = ref('')
const loading = ref(false)
const isSidebarOpen = ref(false)
const selectedIds = ref([])
const showModal = ref(false)
const modalMode = ref('add') // 'add' or 'edit'
const currentDriver = ref({
  id: null,
  driverName: '',
  age: null,
  gender: '男',
  drivingYears: null,
  licensePlate: '',
  fatigueStatus: '正常',
  behaviorStatus: '正常驾驶',
  emotionStatus: '平静'
})

// 分页相关
const currentPage = ref(1)
const pageSize = 7 // 每页显示7条
const jumpPage = ref('')

// 计算总页数
const totalPages = computed(() => {
  return Math.ceil(drivers.value.length / pageSize)
})

// 计算当前页显示的数据
const paginatedDrivers = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return drivers.value.slice(start, end)
})

// 页码列表（最多显示7个页码）
const pageNumbers = computed(() => {
  const pages = []
  const total = totalPages.value
  const current = currentPage.value

  if (total <= 7) {
    // 总页数小于等于7，全部显示
    for (let i = 1; i <= total; i++) {
      pages.push(i)
    }
  } else {
    // 总页数大于7，显示部分页码
    if (current <= 4) {
      // 当前页在前面
      for (let i = 1; i <= 5; i++) pages.push(i)
      pages.push('...')
      pages.push(total)
    } else if (current >= total - 3) {
      // 当前页在后面
      pages.push(1)
      pages.push('...')
      for (let i = total - 4; i <= total; i++) pages.push(i)
    } else {
      // 当前页在中间
      pages.push(1)
      pages.push('...')
      for (let i = current - 1; i <= current + 1; i++) pages.push(i)
      pages.push('...')
      pages.push(total)
    }
  }
  return pages
})

const goBack = () => {
  router.push('/dashboard')
}

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value
}

const fetchDrivers = async () => {
  loading.value = true
  try {
    drivers.value = await getDrivers(searchQuery.value)
  } catch (error) {
    console.error('获取驾驶员列表失败:', error)
    alert('获取驾驶员列表失败，请检查后端服务是否启动')
    drivers.value = []
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1 // 搜索时重置到第一页
  fetchDrivers()
}

const resetSearch = () => {
  searchQuery.value = ''
  licenseQuery.value = ''
  currentPage.value = 1 // 重置时回到第一页
  fetchDrivers()
}

// 切换页码
const goToPage = (page) => {
  if (page === '...' || page < 1 || page > totalPages.value) return
  currentPage.value = page
  selectedIds.value = [] // 切换页面时清空选择
}

// 上一页
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    selectedIds.value = []
  }
}

// 下一页
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
    selectedIds.value = []
  }
}

// 跳转到指定页
const handleJumpPage = () => {
  const page = parseInt(jumpPage.value)
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    jumpPage.value = ''
    selectedIds.value = []
  } else {
    alert(`请输入1-${totalPages.value}之间的页码`)
  }
}

// 显示新增对话框
const showAddModal = () => {
  modalMode.value = 'add'
  currentDriver.value = {
    id: null,
    driverName: '',
    age: null,
    gender: '男',
    drivingYears: null,
    licensePlate: '',
    fatigueStatus: '正常',
    behaviorStatus: '正常驾驶',
    emotionStatus: '平静'
  }
  showModal.value = true
}

// 显示编辑对话框
const showEditModal = (driver) => {
  modalMode.value = 'edit'
  currentDriver.value = { ...driver }
  showModal.value = true
}

// 关闭对话框
const closeModal = () => {
  showModal.value = false
}

// 保存驾驶员（新增或编辑）
const saveDriver = async () => {
  try {
    if (modalMode.value === 'add') {
      await addDriver(currentDriver.value)
      alert('添加成功！')
    } else {
      await updateDriver(currentDriver.value)
      alert('更新成功！')
    }
    closeModal()
    fetchDrivers()
  } catch (error) {
    console.error('保存失败:', error)
    alert('保存失败: ' + error.message)
  }
}

// 删除单个驾驶员
const handleDelete = async (id) => {
  if (!confirm('确定要删除这个驾驶员吗？')) {
    return
  }
  try {
    await deleteDriver(id)
    alert('删除成功！')
    fetchDrivers()
  } catch (error) {
    console.error('删除失败:', error)
    alert('删除失败: ' + error.message)
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    alert('请选择要删除的驾驶员')
    return
  }
  if (!confirm(`确定要删除选中的 ${selectedIds.value.length} 个驾驶员吗？`)) {
    return
  }
  try {
    await batchDeleteDrivers(selectedIds.value)
    alert('批量删除成功！')
    selectedIds.value = []
    fetchDrivers()
  } catch (error) {
    console.error('批量删除失败:', error)
    alert('批量删除失败: ' + error.message)
  }
}

// 全选/取消全选
const selectAll = ref(false)
const toggleSelectAll = () => {
  if (selectAll.value) {
    selectedIds.value = drivers.value.map(d => d.id)
  } else {
    selectedIds.value = []
  }
}

const toggleSelect = (id) => {
  const index = selectedIds.value.indexOf(id)
  if (index > -1) {
    selectedIds.value.splice(index, 1)
  } else {
    selectedIds.value.push(id)
  }
}

const isSelected = (id) => {
  return selectedIds.value.includes(id)
}

const getStatusClass = (status) => {
  if (['正常', '平静'].includes(status)) return 'status-success'
  if (['疲劳', '焦虑', '轻度疲劳'].includes(status)) return 'status-warning'
  if (['抽烟', '打电话', '闭眼', '中度疲劳', '重度疲劳'].includes(status)) return 'status-danger'
  return 'status-default'
}

const getAvatarColor = (name) => {
  const colors = ['#1890ff', '#52c41a', '#faad14', '#f56a00', '#722ed1', '#eb2f96']
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  return colors[Math.abs(hash) % colors.length]
}

const generateIdCard = (driver) => {
  // Mock ID card generation based on ID
  const prefix = '11010119'
  const year = 2024 - driver.age
  const suffix = String(driver.id).padStart(4, '0')
  return `${prefix}${year}0101${suffix}`
}

onMounted(() => {
  setTimeout(() => {
    isSidebarOpen.value = true
  }, 100)
  fetchDrivers()
})
</script>

<style scoped>
.app-container {
  display: flex;
  height: 100vh;
  width: 100vw;
  background-color: #f0f2f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  overflow: hidden;
  position: fixed;
}

/* Sidebar */
.sidebar {
  width: 256px;
  background: linear-gradient(135deg, #0091ff 0%, #005bea 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  z-index: 10;
  transition: width 0.3s ease;
  overflow: hidden;
  white-space: nowrap;
  position: relative;
}

/* Waves */
.waves-container {
  position: absolute;
  top: 0;
  right: 0;
  width: 100px;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
  z-index: 1;
}

.waves {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 200vh;
  height: 100px;
  transform: translate(-50%, -50%) rotate(-90deg);
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

.sidebar-closed {
  width: 0;
}

.logo-area {
  height: 64px;
  display: flex;
  align-items: center;
  padding-left: 24px;
  background: rgba(0, 0, 0, 0.1);
  font-size: 20px;
  font-weight: bold;
  color: #fff;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background: #fff;
  color: #0091ff;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  font-weight: bold;
}

.menu {
  flex: 1;
  padding-top: 10px;
}

.menu-group-title {
  padding: 12px 24px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
}

.menu-item {
  height: 40px;
  display: flex;
  align-items: center;
  padding: 0 24px;
  cursor: pointer;
  transition: all 0.3s;
  color: rgba(255, 255, 255, 0.85);
  margin: 4px 0;
}

.menu-item:hover {

  background: rgba(255, 255, 255, 0.1);
}

.menu-item.active {
  background: rgba(255, 255, 255, 0.2);
  font-weight: 500;
}

.menu-icon {
  margin-right: 10px;
  font-size: 16px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.menu-icon img {
  width: 20px;
  height: 20px;
  object-fit: contain;
}

/* Main Content */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.top-header {
  height: 50px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 25px;
  z-index: 9;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.toggle-btn {
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 18px;
  color: rgba(0, 0, 0, 0.65);
  padding: 8px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.3s;
}

.toggle-btn:hover {
  background: rgba(0, 0, 0, 0.025);
}

.ripple-btn {
  position: relative;
  overflow: hidden;
}

.ripple-btn::after {
  content: "";
  display: block;
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  pointer-events: none;
  background-image: radial-gradient(circle, #000 10%, transparent 10.01%);
  background-repeat: no-repeat;
  background-position: 50%;
  transform: scale(10, 10);
  opacity: 0;
  transition: transform .5s, opacity 1s;
}

.ripple-btn:active::after {
  transform: scale(0, 0);
  opacity: .2;
  transition: 0s;
}

.breadcrumb {
  color: rgba(0, 0, 0, 0.45);
  font-size: 14px;
}

.crumb-item.active {
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
}

.separator {
  margin: 0 8px;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 16px;
}

.notification-badge {
  position: relative;
  cursor: pointer;
  margin-right: 8px;
}

.badge {
  position: absolute;
  top: -6px;
  right: -6px;
  background: #ff4d4f;
  color: #fff;
  font-size: 12px;
  height: 16px;
  min-width: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
}

.avatar-circle {
  width: 32px;
  height: 32px;
  background: #7265e6;
  border-radius: 50%;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.username {
  color: rgba(0, 0, 0, 0.85);
  font-size: 14px;
}

.scrollable-content {
  flex: 1;
  overflow: hidden;
  padding: 10px;
  display: flex;
  flex-direction: column;
}

/* Cards */
.card {
  background: #fff;
  padding: 5px 20px;
}

.search-icon {
  margin-right: 8px;
  font-size: 18px;
}

/* Filter Form */
.filter-form {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
  margin-bottom: 12px;
}

.form-item {
  display: flex;
  flex-direction: column;
}

.form-item label {
  margin-bottom: 4px;
  color: rgba(0, 0, 0, 0.85);
  font-size: 14px;
}

.form-item input, .form-item select {
  height: 32px;
  padding: 4px 11px;
  border: 1px solid #d9d9d9;
  border-radius: 2px;
  transition: all 0.3s;
  outline: none;
}

.form-item input:focus, .form-item select:focus {
  border-color: #40a9ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.filter-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.left-actions, .right-actions {
  display: flex;
  gap: 8px;
}

/* Buttons */
.btn {
  height: 32px;
  padding: 4px 15px;
  font-size: 14px;
  border-radius: 2px;
  cursor: pointer;
  border: 1px solid transparent;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-primary {
  background: #1890ff;
  color: #fff;
  border-color: #1890ff;
}

.btn-primary:hover {
  background: #40a9ff;
  border-color: #40a9ff;
}

.btn-default {
  background: #fff;
  border-color: #d9d9d9;
  color: rgba(0, 0, 0, 0.65);
}

.btn-default:hover {
  color: #40a9ff;
  border-color: #40a9ff;
}

.icon-btn span {
  margin-right: 4px;
  font-size: 16px;
  line-height: 1;
}

.btn-danger {
  background: #ff4d4f;
  color: #fff;
  border-color: #ff4d4f;
}

.btn-danger:hover {
  background: #ff7875;
  border-color: #ff7875;
}

/* Table */
.main-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  margin-bottom: 0;
}

.filter-section {
  margin-bottom: 16px;
}

.table-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.table-container {
  flex: 1;
  overflow: auto;
}

.custom-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}

.custom-table th {
  background: #fafafa;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.custom-table td {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  color: rgba(0, 0, 0, 0.65);
}

.custom-table tr:hover td {
  background: #fafafa;
}

.checkbox-col {
  width: 40px;
  text-align: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
}

.name-link {
  color: #1890ff;
  cursor: pointer;
}

.name-link:hover {
  text-decoration: underline;
}

.status-badge {
  display: inline-block;
  padding: 0 8px;
  font-size: 12px;
  line-height: 20px;
  border-radius: 4px;
  border: 1px solid;
}

.status-success {
  background: #f6ffed;
  border-color: #b7eb8f;
  color: #52c41a;
}

.status-warning {
  background: #fffbe6;
  border-color: #ffe58f;
  color: #faad14;
}

.status-danger {
  background: #fff1f0;
  border-color: #ffa39e;
  color: #f5222d;
}

.text-btn {
  background: none;
  border: none;
  color: #1890ff;
  cursor: pointer;
  padding: 0 4px;
}

.text-btn:hover {
  color: #40a9ff;
}

.text-btn.danger {
  color: #ff4d4f;
}

.text-btn.danger:hover {
  color: #ff7875;
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 24px;
  padding-top: 16px;
}

.page-info {
  color: rgba(0, 0, 0, 0.45);
}

.page-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.page-btn {
  min-width: 32px;
  height: 32px;
  border: 1px solid #d9d9d9;
  background: #fff;
  border-radius: 2px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.page-btn:hover:not(:disabled) {
  border-color: #1890ff;
  color: #1890ff;
}

.page-btn.active {
  background: #1890ff;
  border-color: #1890ff;
  color: #fff;
}

.page-btn:disabled {
  color: rgba(0, 0, 0, 0.25);
  background: #f5f5f5;
  border-color: #d9d9d9;
  cursor: not-allowed;
}

.page-btn.ellipsis {
  border: none;
  background: transparent;
  cursor: default;
}

.page-btn.ellipsis:hover {
  border: none;
  color: inherit;
}

.jump-text {
  margin-left: 8px;
  color: rgba(0, 0, 0, 0.65);
}

.jump-input {
  width: 50px;
  height: 32px;
  margin: 0 8px;
  padding: 4px 11px;
  border: 1px solid #d9d9d9;
  border-radius: 2px;
  text-align: center;
}

.btn-sm {
  height: 32px;
  padding: 0 12px;
}

.spinner {
  width: 20px;
  height: 20px;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #1890ff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  display: inline-block;
  vertical-align: middle;
  margin-right: 8px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.modal-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 800px;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
  animation: slideUp 0.3s;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

@keyframes slideUp {
  from {
    transform: translateY(50px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.modal-header {
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.85);
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: rgba(0, 0, 0, 0.45);
  cursor: pointer;
  line-height: 1;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 2px;
  transition: all 0.3s;
}

.close-btn:hover {
  background: rgba(0, 0, 0, 0.06);
  color: rgba(0, 0, 0, 0.75);
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
  flex: 1;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 8px;
  color: rgba(0, 0, 0, 0.85);
  font-size: 14px;
  font-weight: 500;
}

.required {
  color: #ff4d4f;
  margin-left: 2px;
}

.form-group input,
.form-group select {
  height: 32px;
  padding: 4px 11px;
  border: 1px solid #d9d9d9;
  border-radius: 2px;
  font-size: 14px;
  transition: all 0.3s;
  outline: none;
}

.form-group input:focus,
.form-group select:focus {
  border-color: #40a9ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.modal-footer {
  padding: 16px 24px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
</style>
