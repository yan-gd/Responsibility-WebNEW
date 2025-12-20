<div align="center">

# Responsibility-WebNEW

一个简单的 Spring Boot + Vue 3 前后端分离项目，包含驾驶员信息管理、可视化仪表盘与 AI 助手。

<p>
	<img alt="Java" src="https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white" />
	<img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-4.0.0-6DB33F?logo=springboot&logoColor=white" />
	<img alt="Vue" src="https://img.shields.io/badge/Vue-3-4FC08D?logo=vue.js&logoColor=white" />
	<img alt="Vite" src="https://img.shields.io/badge/Vite-7-646CFF?logo=vite&logoColor=white" />
	<img alt="ECharts" src="https://img.shields.io/badge/ECharts-6-AA344D?logo=apacheecharts&logoColor=white" />
	<img alt="MySQL" src="https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white" />
	<img alt="License" src="https://img.shields.io/badge/License-MIT-black" />
</p>

<p>
	<img alt="Backend" src="https://img.shields.io/badge/Backend-http://localhost:8080-2ea44f" />
	<img alt="Frontend" src="https://img.shields.io/badge/Frontend-http://localhost:5173-2ea44f" />
	<img alt="API" src="https://img.shields.io/badge/API-/api-2ea44f" />
</p>

**中文** | [English](README.en.md)

</div>

## 🔥 亮点
- 50 条示例司机数据 + 自动摘要注入提示词，让 AI 回答更贴近业务
- 仪表盘可视化：多图表联动、全国/重庆地图（在线 GeoJSON）、热力/线路展示
- AI 助手：欢迎气泡、打字机效果、复制/下载/重置聊天记录、预设问题一键提问

## 🧩 技术栈
- 后端：Spring Boot 4.0.0、Java 21、MySQL 8、JdbcTemplate（Spring JDBC）
- 前端：Vue 3、Vite、Vue Router、ECharts
- AI：阿里云 DashScope（OpenAI Compatible Mode）`qwen-vl-plus`

## ✨ 功能概览
- 驾驶员管理：列表/搜索/详情/新增/更新/删除/批量删除
- 可视化仪表盘：多图表联动、全国/重庆地图（在线 GeoJSON）、热力/线路等展示
- AI 助手：欢迎气泡、打字机输出、复制/下载/重置聊天记录、预设问题一键提问

## 🚀 快速上手
### 1) 前置依赖
- JDK 21+
- Node.js 18+ / npm
- MySQL 8+（创建数据库 `driverdata`）
- DashScope API Key（配置项 `dashscope.api.key`）

### 2) 🗄️ 初始化数据库
将根目录的 `create_drivers_information.txt` 导入到 `driverdata` 库：
- 表：`drivers_information`
- 数据：50 条示例驾驶员记录

### 3) 🧠 启动后端（Spring Boot）
配置文件：`springboot/src/main/resources/application.properties`
- MySQL：`spring.datasource.*`
- DashScope：`dashscope.api.key`

启动：
```powershell
cd springboot
.\mvnw.cmd spring-boot:run
```
默认端口：`http://localhost:8080`

### 4) 🖥️ 启动前端（Vue 3 + Vite）
```powershell
cd vue
npm install
npm run dev
```
访问：
- 登录页：`http://localhost:5173/login`
- 仪表盘：`http://localhost:5173/dashboard`

开发联调：Vite 已代理 `/api` → `http://localhost:8080`（见 `vue/vite.config.js`）。

## 📡 接口说明
### 🤖 AI
- `POST /api/ai/chat`
	- body 示例：
		```json
		{
			"message": "张伟的疲劳状态是什么？",
			"history": [{"role":"user","content":"..."}]
		}
		```
	- 返回：`{ "success": true, "content": "..." }` 或 `{ "success": false, "error": "..." }`

说明：后端会将数据库里的驾驶员摘要注入 system prompt，再请求 DashScope。

### 🧑‍✈️ 驾驶员
所有接口前缀：`/api/driver`
- `GET /list` 获取全部驾驶员
- `GET /search?name=xxx` 按姓名模糊搜索
- `GET /{id}` 按 ID 获取详情
- `POST /add` 新增驾驶员（JSON body）
- `PUT /update` 更新驾驶员（JSON body，需包含 `id`）
- `DELETE /{id}` 删除驾驶员
- `DELETE /batch` 批量删除（body 为 `ids` 数组）

返回结构：`{ code, message, data }`

## 🧯 常见问题
- 地图空白：全国/重庆地图 GeoJSON 来自 `https://geo.datav.aliyun.com/`，无公网或被拦截时会加载失败。
- AI 401/429：检查 `dashscope.api.key` 是否正确、是否触发限流。
- 修改数据库连接后：重启后端以重新加载驾驶员摘要。

## 📄 License
MIT
