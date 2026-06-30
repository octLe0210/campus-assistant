# AI智能校园助手平台

基于 Vue3 + Spring Boot + MySQL + DeepSeek AI 的全栈校园服务平台。

## 功能模块

- 校园新闻 — 新闻列表、分类筛选、详情查看
- 课程查询 — 按课程名/教师/星期搜索课程
- 空教室查询 — 按教学楼+时间段查找可用教室
- AI问答助手 — 接入 DeepSeek API，智能对话
- AI学习计划 — 输入目标，自动生成学习计划

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue3 + Vite + Element Plus + Pinia + Vue Router |
| 后端 | Spring Boot 3.2 + MyBatis-Plus 3.5 |
| 数据库 | MySQL 8.0 |
| AI | DeepSeek Chat API |
| 认证 | JWT |

## 项目结构

```
campus-assistant/
├── frontend/          # Vue3 前端
│   └── src/
│       ├── api/       # API 请求封装
│       ├── views/     # 页面组件
│       ├── router/    # 路由配置
│       └── stores/    # Pinia 状态管理
├── backend/           # Spring Boot 后端
│   └── src/main/java/com/campus/
│       ├── controller/  # REST 接口
│       ├── service/     # 业务逻辑
│       ├── mapper/      # 数据访问
│       └── entity/      # 实体类
└── README.md
```

## 快速启动

### 环境要求
- JDK 17+
- Maven 3.9+（或使用项目自带的 mvnw）
- MySQL 8.0+
- Node.js 20+

### 1. 初始化数据库

执行 `backend/src/main/resources/campus.sql` 脚本。

### 2. 配置

编辑 `backend/src/main/resources/application.yml`：
- 修改数据库连接信息（用户名/密码）
- 配置 DeepSeek API Key

### 3. 启动后端

```bash
cd backend
./mvnw spring-boot:run
```

后端运行在 http://localhost:8080

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端运行在 http://localhost:5173

### 5. 测试账号

- 用户名: `admin`
- 密码: `123456`

## API 接口

| 方法 | 端点 | 说明 | 认证 |
|------|------|------|------|
| POST | /api/user/login | 登录 | 否 |
| POST | /api/user/register | 注册 | 否 |
| GET | /api/news | 新闻列表（分页） | 否 |
| GET | /api/news/{id} | 新闻详情 | 否 |
| GET | /api/courses | 课程搜索 | 否 |
| GET | /api/classrooms/available | 空教室查询 | 否 |
| POST | /api/ai/chat | AI对话 | 是 |
| GET | /api/ai/history | 对话历史 | 是 |
| POST | /api/study-plans/generate | 生成学习计划 | 是 |
| GET | /api/study-plans | 学习计划列表 | 是 |
