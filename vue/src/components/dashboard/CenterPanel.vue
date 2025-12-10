<template>
  <div class="center-panel-container">
    <div class="map-container">
      <ChartCard title="实时地图">
        <div class="map-content">
          <!-- Placeholder for map, in real app use ECharts with GeoJSON or Baidu/Gaode Map SDK -->
          <div class="map-bg"></div>
          <EChart v-if="mapOptions" :options="mapOptions" class="map-overlay" />
        </div>
      </ChartCard>
    </div>
    <div class="table-container">
      <ChartCard title="车辆信息">
        <div class="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>车牌号</th>
                <th>所属企业</th>
                <th>运输内容</th>
                <th>行驶速度</th>
                <th>车辆载重</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in tableData" :key="index">
                <td>{{ item.plate }}</td>
                <td>{{ item.company }}</td>
                <td>{{ item.content }}</td>
                <td>{{ item.speed }}</td>
                <td>{{ item.load }}</td>
                <td>{{ item.capacity }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </ChartCard>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import ChartCard from './ChartCard.vue'
import EChart from './EChart.vue'

const vibrantPalette = ['#facc15', '#ef4444', '#22c55e', '#3b82f6', '#a855f7']

const tableData = ref([
  { plate: '渝B-A3456', company: '华茂物流', content: '危化品运输', speed: 58, load: '40000KG', capacity: '40000KG' },
  { plate: '渝C-B7890', company: '安达危化品运输', content: '疲劳驾驶', speed: 62, load: '38000KG', capacity: '38000KG' },
  { plate: '渝D-C4567', company: '瑞通物流', content: '未按规定路线行驶', speed: 78, load: '42000KG', capacity: '42000KG' },
  { plate: '渝E-D8910', company: '宏盛危化品物流', content: '危化品车罐体检验过期', speed: '停放', load: '50000KG', capacity: '50000KG' },
  { plate: '渝F-E2468', company: '丰源物流', content: '与其他车辆剐蹭', speed: 68, load: '36000KG', capacity: '36000KG' }
])

const cityData = [
  { name: '重庆', value: [106.55, 29.56, 95], color: vibrantPalette[0] },
  { name: '成都', value: [104.06, 30.67, 80], color: vibrantPalette[1] },
  { name: '武汉', value: [114.31, 30.52, 60], color: vibrantPalette[2] },
  { name: '西安', value: [108.95, 34.27, 72], color: vibrantPalette[3] },
  { name: '贵阳', value: [106.71, 26.57, 55], color: vibrantPalette[4] }
]

const routeData = [
  { coords: [cityData[0].value, cityData[1].value] },
  { coords: [cityData[0].value, cityData[2].value] },
  { coords: [cityData[0].value, cityData[3].value] },
  { coords: [cityData[0].value, cityData[4].value] }
]

const mapOptions = ref(null)

const createMapOptions = () => ({
  backgroundColor: 'transparent',
  geo: {
    map: 'china',
    roam: true,
    zoom: 1.1,
    itemStyle: {
      areaColor: '#111e3a',
      borderColor: '#1e40af',
      shadowColor: 'rgba(15, 23, 42, 0.8)',
      shadowBlur: 20
    },
    emphasis: {
      itemStyle: {
        areaColor: '#0f172a'
      }
    }
  },
  series: [
    {
      name: '线路',
      type: 'lines',
      coordinateSystem: 'geo',
      zlevel: 1,
      effect: {
        show: true,
        period: 5,
        trailLength: 0.2,
        symbol: 'arrow',
        symbolSize: 8,
        color: '#facc15'
      },
      lineStyle: {
        color: '#a855f7',
        width: 2,
        opacity: 0.4,
        curveness: 0.25
      },
      data: routeData,
      animationDuration: 1800,
      animationDelay: 300
    },
    {
      type: 'effectScatter',
      coordinateSystem: 'geo',
      data: cityData.map(({ name, value, color }) => ({
        name,
        value,
        itemStyle: { color }
      })),
      symbolSize: (val) => 10 + val[2] / 8,
      rippleEffect: {
        brushType: 'stroke',
        scale: 4
      },
      label: {
        show: true,
        formatter: '{b}',
        position: 'right',
        color: '#fff'
      },
      animationDuration: 1600,
      animationDelay: (idx) => idx * 150,
      zlevel: 2
    }
  ]
})

const loadChinaMap = async () => {
  try {
    if (!echarts.getMap('china')) {
      const response = await fetch('https://geo.datav.aliyun.com/areas_v3/bound/geojson?code=100000_full')
      if (!response.ok) throw new Error('Failed to fetch China geojson')
      const geoJson = await response.json()
      echarts.registerMap('china', geoJson)
    }
    mapOptions.value = createMapOptions()
  } catch (error) {
    console.error('China map loading failed:', error)
  }
}

onMounted(() => {
  loadChinaMap()
})
</script>

<style scoped>
.center-panel-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  gap: 6px;
  min-height: 0;
}

.map-container {
  flex: 2;
  display: flex;
}

.map-content {
  width: 100%;
  height: 100%;
  position: relative;
  background: #0b1124 url('https://img.alicdn.com/tfs/TB1.7.2a.T1gK0jSZFhXXaAtVXa-1920-100.png') no-repeat center center; /* Placeholder */
  background-size: cover;
}

.map-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('https://api.map.baidu.com/customimage/tile?&x=100&y=100&z=10&udt=20200101&scale=1&ak=E4805d16520de693a3fe707cdc962045&styles=t%3Awater%7Ce%3Aall%7Cc%3A%23044161%2C%23044161%7Ct%3Aland%7Ce%3Aall%7Cc%3A%23091934%2C%23091934%7Ct%3Aboundary%7Ce%3Ag%7Cc%3A%23064f85%2C%23064f85%7Ct%3Arailway%7Ce%3Aall%7Cv%3Aoff%7Ct%3Ahighway%7Ce%3Ag%7Cc%3A%23004981%2C%23004981%7Ct%3Ahighway%7Ce%3Ag.f%7Cc%3A%23005b96%2C%23005b96%7Ct%3Ahighway%7Ce%3Al%7Cv%3Aon%7Ct%3Aarterial%7Ce%3Ag%7Cc%3A%23004981%2C%23004981%7Ct%3Aarterial%7Ce%3Ag.f%7Cc%3A%2300508b%2C%2300508b%7Ct%3Apoi%7Ce%3Aall%7Cv%3Aoff%7Ct%3Agreen%7Ce%3Aall%7Cv%3Aoff%7Ct%3Asubway%7Ce%3Aall%7Cv%3Aoff%7Ct%3Amanmade%7Ce%3Aall%7Cv%3Aoff%7Ct%3Alocal%7Ce%3Aall%7Cv%3Aoff%7Ct%3Aarterial%7Ce%3Al%7Cv%3Aoff%7Ct%3Aboundary%7Ce%3Ag.f%7Cc%3A%23029fd4%2C%23029fd4%7Ct%3Abuilding%7Ce%3Aall%7Cc%3A%231a5787%2C%231a5787%7Ct%3Alabel%7Ce%3Aall%7Cv%3Aoff') repeat;
  opacity: 0.5;
}

.map-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.table-container {
  flex: 1;
  display: flex;
}

.table-wrapper {
  width: 100%;
  height: 100%;
  overflow: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  color: #fff;
  font-size: 14px;
}

th, td {
  padding: 10px;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

th {
  background: rgba(0, 180, 219, 0.2);
  color: #00b4db;
}

tr:hover {
  background: rgba(255, 255, 255, 0.05);
}
</style>
