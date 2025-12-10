<template>
  <div class="panel-container">
    <ChartCard title="司机风险接管评估">
      <EChart :options="radarChartOptions" />
    </ChartCard>
    <ChartCard title="司机分布">
      <EChart v-if="driverHeatmapOptions" :options="driverHeatmapOptions" />
    </ChartCard>
    <ChartCard title="车辆分布情况">
      <EChart v-if="vehicleHeatmapOptions" :options="vehicleHeatmapOptions" />
    </ChartCard>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import ChartCard from './ChartCard.vue'
import EChart from './EChart.vue'

const vibrantPalette = ['#facc15', '#ef4444', '#22c55e', '#3b82f6', '#a855f7']

const hexToRgba = (hex, alpha) => {
  const normalized = hex.replace('#', '')
  const bigint = parseInt(normalized, 16)
  const r = (bigint >> 16) & 255
  const g = (bigint >> 8) & 255
  const b = bigint & 255
  return `rgba(${r}, ${g}, ${b}, ${alpha})`
}

// Radar Chart Options
const radarChartOptions = ref({
  color: vibrantPalette,
  backgroundColor: 'transparent',
  radar: {
    center: ['50%', '55%'],
    radius: '65%',
    name: {
      textStyle: {
        color: '#fff',
        fontSize: 12
      }
    },
    indicator: [
      { name: '视觉可达性', max: 100 },
      { name: '急加速(刹车)', max: 100 },
      { name: '驾驶员警觉', max: 100 },
      { name: '驾驶员疲劳', max: 100 },
      { name: '干扰驾驶', max: 100 }
    ],
    splitArea: {
      areaStyle: {
        color: vibrantPalette.map((color, idx) => hexToRgba(color, 0.1 + idx * 0.08)),
        shadowColor: 'rgba(0, 0, 0, 0.3)',
        shadowBlur: 10
      }
    },
    axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.5)' } },
    splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } }
  },
  series: [
    {
      type: 'radar',
      data: [
        {
          value: [80, 90, 70, 85, 60],
          name: '风险评估',
          areaStyle: { color: hexToRgba('#3b82f6', 0.4) },
          lineStyle: { color: '#3b82f6', width: 3 },
          itemStyle: { color: '#facc15', shadowBlur: 12, shadowColor: 'rgba(250,204,21,0.6)' }
        }
      ],
      animationDuration: 1500,
      animationDelay: 200,
      animationEasing: 'cubicOut'
    }
  ]
})

const driverHeatmapOptions = ref(null)
const vehicleHeatmapOptions = ref(null)

const driverHeatPoints = [
  { name: '渝北区', value: [106.63, 29.72, 92] },
  { name: '江北区', value: [106.57, 29.60, 78] },
  { name: '南岸区', value: [106.64, 29.50, 70] },
  { name: '九龙坡区', value: [106.48, 29.52, 66] },
  { name: '巴南区', value: [106.52, 29.38, 58] },
  { name: '沙坪坝区', value: [106.45, 29.55, 64] },
  { name: '北碚区', value: [106.39, 29.80, 55] },
  { name: '涪陵区', value: [107.39, 29.71, 60] }
]

const vehicleHeatPoints = [
  { name: '两江新区', value: [106.51, 29.63, 88] },
  { name: '江津区', value: [106.26, 29.29, 65] },
  { name: '永川区', value: [105.89, 29.35, 62] },
  { name: '綦江区', value: [106.64, 29.03, 54] },
  { name: '合川区', value: [106.28, 29.97, 68] },
  { name: '大足区', value: [105.77, 29.70, 57] },
  { name: '南川区', value: [107.10, 29.15, 50] },
  { name: '黔江区', value: [108.77, 29.54, 72] }
]

const createHeatmapOptions = (data, colorRange) => {
  const maxValue = Math.max(...data.map((item) => item.value[2]))
  return {
    backgroundColor: 'transparent',
    geo: {
      map: 'chongqing',
      roam: true,
      zoom: 3,
      itemStyle: {
        areaColor: '#0f172a',
        borderColor: '#1d4ed8',
        shadowColor: 'rgba(15, 23, 42, 0.7)',
        shadowBlur: 15
      },
      emphasis: {
        itemStyle: {
          areaColor: '#1e293b'
        }
      }
    },
    visualMap: {
      show: false,
      min: 0,
      max: maxValue,
      inRange: { color: colorRange }
    },
    series: [
      {
        type: 'heatmap',
        coordinateSystem: 'geo',
        data,
        pointSize: 28,
        blurSize: 40,
        animationDuration: 1500,
        animationDelay: (idx) => idx * 80,
        animationEasing: 'cubicOut'
      }
    ]
  }
}

const loadChongqingMap = async () => {
  try {
    if (!echarts.getMap('chongqing')) {
      const response = await fetch('https://geo.datav.aliyun.com/areas_v3/bound/geojson?code=500000_full')
      if (!response.ok) throw new Error('Failed to fetch Chongqing geojson')
      const geoJson = await response.json()
      echarts.registerMap('chongqing', geoJson)
    }
    driverHeatmapOptions.value = createHeatmapOptions(driverHeatPoints, ['#22c55e', '#facc15', '#ef4444'])
    vehicleHeatmapOptions.value = createHeatmapOptions(vehicleHeatPoints, ['#3b82f6', '#a855f7', '#f97316'])
  } catch (error) {
    console.error('Chongqing map loading failed:', error)
  }
}

onMounted(() => {
  loadChongqingMap()
})
</script>

<style scoped>
.panel-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  gap: 6px;
  min-height: 0;
}
</style>
