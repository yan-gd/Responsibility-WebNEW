# Responsibility-WebNEW
> 🛰️ 一个简单的springboot+vue3前后端分离项目 + 炫酷可视化 + AI助手。

## 这是什么
- Spring Boot 4 + Java 21 + MySQL 提供驾驶员信息与 AI 能力
- Vue 3 + Vite + ECharts 可视化（全国/重庆地图、热力、线路、流量、风险雷达）
- AI 助手调用阿里云 DashScope Qwen-VL-Plus，读取数据库回答驾驶员问题

## 特色亮点
- ✅ 50 条司机样本数据，摘要自动注入提示词，让 AI 回答更贴地气
- ✅ 交互式仪表盘：预警次数、发车流量、货运流量多图联动
- ✅ 地图与热力：全国线路飞线、重庆司机/车辆热力、动态散点
- ✅ 炫酷登录页：波浪动画 + 渐变背景，轻量登录直达 Dashboard
- ✅ AI 助手：气泡欢迎、打字机效果、复制/下载聊天记录、预设问题一键提问
- ✅ 一键联调：Vite 代理 `/api` → `http://localhost:8080`，CORS 已配好

## 快速上手
### 前置依赖
- JDK 21+
- Node 18+ / npm
- MySQL 8+，并创建 `driverdata` 库
- 阿里云 DashScope API Key（模型 `qwen-vl-plus`）

### 后端（Spring Boot）
```powershell
cd springboot
# 配置 application.properties 中的 dashscope.api.key 与数据库账号
.\mvnw.cmd spring-boot:run
```
- 数据库脚本：`create_drivers_information.txt`（建表 + 50 条示例数据），导入到 `driverdata` 库。
- 主要配置：`springboot/src/main/resources/application.properties`（端口 8080、CORS、DB、DashScope Key）。
- 主要接口：
	- `POST /api/ai/chat` — body 例子：`{"message":"问点什么","history":[{"role":"user","content":"..."}]}`，返回 `{ success, content | error }`。
	- 前端预留了 `/api/ai/chat/stream` 的流式调用。

### 前端（Vue 3 + Vite）
```powershell
cd vue
npm install
npm run dev
# 访问 http://localhost:5173/login
```
- 代理：`/api` 自动代理到 `http://localhost:8080`（见 `vite.config.js`）。
- 构建发布：`npm run build` 生成 `dist/`。

## 主要模块
- 登录页 `views/Login.vue`：渐变背景 + 波浪动画 + 轻量登录表单（本地校验后跳转 Dashboard）。
- 仪表盘 `views/Dashboard.vue`：
	- `AIAssistant.vue`：AI 入口、建议问题、打字机输出、复制/下载/重置。
	- 左侧 `LeftPanel.vue`：预警次数、发车流量、货运流量图。
	- 中间 `CenterPanel.vue`：全国线路图（GeoJSON + 动态箭头）、车辆表格示例。
	- 右侧 `RightPanel.vue`：风险雷达，司机/车辆重庆热力分布。
	- `Header.vue`：标题 + 实时时间。
- 数据层：`DriverService` + `DriverRepository` 使用 `JdbcTemplate` 访问 `drivers_information` 表，`AIService` 汇总司机摘要后请求 Qwen。

## 项目结构
```
springboot/  # 后端：Spring Boot 4, Java 21, MySQL, DashScope
vue/         # 前端：Vue 3, Vite, ECharts, Vue Router
create_drivers_information.txt  # SQL 脚本
```

## 调试小贴士
- 需要公网访问 geojson（阿里云数据源），无网会导致地图为空。
- 若 AI 返回 401/429，请检查 `dashscope.api.key` 是否正确或有频率限制。
- 修改数据库连接后，重启后端以重新加载司机摘要。

## License
MIT

