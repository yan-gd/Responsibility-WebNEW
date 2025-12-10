<template>
  <div class="dashboard-container">
    <AIAssistant />
    <div class="dashboard">
      <DashboardHeader />
      <div class="main-content">
        <div class="left-panel">
          <LeftPanel />
        </div>
        <div class="center-panel">
          <CenterPanel />
        </div>
        <div class="right-panel">
          <RightPanel />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import DashboardHeader from '../components/dashboard/Header.vue'
import LeftPanel from '../components/dashboard/LeftPanel.vue'
import CenterPanel from '../components/dashboard/CenterPanel.vue'
import RightPanel from '../components/dashboard/RightPanel.vue'
import AIAssistant from '../components/AIAssistant.vue'
</script>

<style scoped>
.dashboard-container {
  width: 100vw;
  height: 100vh;
  background-color: #0b1124;
  overflow: hidden;
  position: fixed;
}

.dashboard {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.main-content {
  flex: 1;
  display: flex;
  padding: 8px;
  gap: 8px;
  overflow: hidden;
  min-height: 0;
}

.left-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 0;
  min-height: 0;
}

.center-panel {
  flex: 2;
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 0;
  min-height: 0;
}

.right-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 0;
  min-height: 0;
}
</style>
