<template>
  <div ref="chartRef" class="echart-container"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  options: {
    type: Object,
    required: true
  }
})

const chartRef = ref(null)
let chartInstance = null

const initChart = () => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value)
    chartInstance.setOption(props.options)
  }
}

const resizeChart = () => {
  if (chartInstance) {
    chartInstance.resize()
  }
}

watch(() => props.options, (newOptions) => {
  if (chartInstance) {
    chartInstance.setOption(newOptions)
  }
}, { deep: true })

onMounted(async () => {
  await nextTick()
  initChart()
  window.addEventListener('resize', resizeChart)
  // 延迟触发一次resize确保图表正确渲染
  setTimeout(() => {
    resizeChart()
  }, 100)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeChart)
  if (chartInstance) {
    chartInstance.dispose()
  }
})
</script>

<style scoped>
.echart-container {
  width: 100%;
  height: 100%;
  min-height: 150px;
  min-width: 0;
}
</style>
