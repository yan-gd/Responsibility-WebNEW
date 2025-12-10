<template>
  <div class="panel-container">
    <ChartCard title="预警次数统计图">
      <EChart :options="lineChartOptions" />
    </ChartCard>
    <ChartCard title="发车流量">
      <EChart :options="areaChartOptions" />
    </ChartCard>
    <ChartCard title="货运流量">
      <EChart :options="barChartOptions" />
    </ChartCard>
  </div>
</template>

<script setup>
import { ref } from 'vue'
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

// Line Chart Options
const lineChartOptions = ref({
  color: vibrantPalette,
  grid: { top: '15%', bottom: '10%', left: '10%', right: '5%' },
  xAxis: {
    type: 'category',
    data: ['1', '3', '5', '7', '9', '11', '13', '15', '17', '19', '21', '23'],
    axisLine: { lineStyle: { color: '#fff' } },
    axisLabel: { color: '#fff' }
  },
  yAxis: {
    type: 'value',
    splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
    axisLine: { lineStyle: { color: '#fff' } },
    axisLabel: { color: '#fff' }
  },
  series: [
    {
      data: [150, 230, 224, 218, 135, 147, 260, 200, 240, 210, 250, 280],
      type: 'line',
      smooth: true,
      lineStyle: { color: '#22c55e', width: 3 },
      symbol: 'circle',
      symbolSize: 10,
      itemStyle: { color: '#22c55e', shadowBlur: 20, shadowColor: 'rgba(34,197,94,0.6)' },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(34,197,94,0.35)' },
          { offset: 1, color: 'rgba(34,197,94,0.05)' }
        ])
      },
      animationDuration: 1600,
      animationEasing: 'cubicBezier(.45,.05,.55,.95)',
      animationDelay: (idx) => idx * 120
    },
    {
      data: [120, 180, 150, 160, 110, 130, 210, 180, 200, 190, 220, 240],
      type: 'line',
      smooth: true,
      lineStyle: { color: '#3b82f6', width: 2 },
      symbol: 'circle',
      symbolSize: 0,
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(59,130,246,0.25)' },
          { offset: 1, color: 'rgba(59,130,246,0)' }
        ])
      },
      animationDuration: 1600,
      animationEasing: 'cubicBezier(.45,.05,.55,.95)',
      animationDelay: (idx) => idx * 100 + 180
    }
  ]
})

const areaSeriesData = [
  [120, 132, 101, 134, 90, 230, 210],
  [220, 182, 191, 234, 290, 330, 310],
  [150, 232, 201, 154, 190, 330, 410],
  [320, 332, 301, 334, 390, 330, 320],
  [820, 932, 901, 934, 1290, 1330, 1320]
]

// Area Chart Options
const areaChartOptions = ref({
  color: vibrantPalette,
  grid: { top: '15%', bottom: '10%', left: '10%', right: '5%' },
  legend: {
    data: ['Line 1', 'Line 2', 'Line 3', 'Line 4', 'Line 5'],
    textStyle: { color: '#fff' },
    top: 0
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    axisLine: { lineStyle: { color: '#fff' } },
    axisLabel: { color: '#fff' }
  },
  yAxis: {
    type: 'value',
    splitLine: { show: false },
    axisLine: { lineStyle: { color: '#fff' } },
    axisLabel: { color: '#fff' }
  },
  series: ['Line 1', 'Line 2', 'Line 3', 'Line 4', 'Line 5'].map((name, idx) => ({
    name,
    type: 'line',
    stack: 'Total',
    smooth: true,
    showSymbol: false,
    areaStyle: {
      opacity: 0.5,
      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: hexToRgba(vibrantPalette[idx % vibrantPalette.length], 0.45) },
        { offset: 1, color: hexToRgba(vibrantPalette[idx % vibrantPalette.length], 0.05) }
      ])
    },
    emphasis: { focus: 'series' },
    data: areaSeriesData[idx],
    animationDuration: 1400,
    animationDelay: (dataIndex) => dataIndex * 60 + idx * 100
  }))
})

// Bar Chart Options
const barChartOptions = ref({
  grid: { top: '15%', bottom: '10%', left: '10%', right: '5%' },
  xAxis: {
    type: 'category',
    data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
    axisLine: { lineStyle: { color: '#fff' } },
    axisLabel: { color: '#fff' }
  },
  yAxis: {
    type: 'value',
    splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
    axisLine: { lineStyle: { color: '#fff' } },
    axisLabel: { color: '#fff' }
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130, 180, 220, 190, 150, 100],
      type: 'bar',
      itemStyle: {
        color: (params) => {
          const color = vibrantPalette[params.dataIndex % vibrantPalette.length]
          return new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: hexToRgba(color, 0.95) },
            { offset: 1, color: hexToRgba(color, 0.3) }
          ])
        },
        shadowBlur: 15,
        shadowColor: 'rgba(0,0,0,0.3)'
      },
      animationDuration: 1200,
      animationDelay: (idx) => idx * 80,
      animationEasing: 'elasticOut'
    }
  ]
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
