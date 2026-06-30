<template>
  <div class="cockpit">
    <!-- 顶部智能驾驶舱横幅 -->
    <div class="cockpit-header">
      <div class="header-left">
        <div class="campus-brand">
          <el-icon :size="28"><School /></el-icon>
          <div>
            <div class="brand-name">AI智能校园</div>
            <div class="brand-sub">Smart Campus Cockpit</div>
          </div>
        </div>
        <el-divider direction="vertical" style="border-color:rgba(255,255,255,0.2);height:40px" />
        <div class="weather-widget">
          <el-icon :size="28" color="#FFD700"><Sunny /></el-icon>
          <div>
            <div class="weather-temp">{{ weather.temp }}°</div>
            <div class="weather-desc">{{ weather.desc }}</div>
          </div>
        </div>
      </div>

      <div class="header-center">
        <div class="datetime">
          <div class="time">{{ currentTime }}</div>
          <div class="date">{{ currentDate }}</div>
        </div>
      </div>

      <div class="header-right">
        <el-badge :value="notifications.length" :hidden="!notifications.length">
          <el-popover placement="bottom" :width="280" trigger="click">
            <template #reference>
              <div class="notify-btn">
                <el-icon :size="22"><Bell /></el-icon>
              </div>
            </template>
            <div class="notify-list">
              <div v-if="!notifications.length" class="notify-empty">暂无新消息</div>
              <div v-for="n in notifications" :key="n.id" class="notify-item">
                <el-icon :size="16" :color="n.color"><component :is="n.icon" /></el-icon>
                <div>
                  <div class="notify-title">{{ n.title }}</div>
                  <div class="notify-time">{{ n.time }}</div>
                </div>
              </div>
            </div>
          </el-popover>
        </el-badge>

        <el-dropdown @command="handleCommand">
          <div class="user-widget">
            <el-avatar :size="36" :src="userStore.user?.avatar">
              <el-icon :size="20"><UserFilled /></el-icon>
            </el-avatar>
            <div class="user-info">
              <div class="user-name">{{ userStore.user?.nickname || userStore.user?.username }}</div>
              <div class="user-role">{{ userStore.user?.role === 'admin' ? '管理员' : '学生' }}</div>
            </div>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 数据卡片行 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="card in statCards" :key="card.key">
        <div class="stat-panel" :style="{ background: card.bg }" @click="$router.push(card.link)">
          <div class="stat-icon-box">
            <el-icon :size="28" color="#fff"><component :is="card.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-num">{{ card.num }}</div>
            <div class="stat-label">{{ card.label }}</div>
          </div>
          <div class="stat-trend">
            <el-icon :size="14"><Top /></el-icon> {{ card.trend }}
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 中间两栏内容 -->
    <el-row :gutter="16" class="main-row">
      <!-- 左侧：快捷入口 -->
      <el-col :span="14">
        <el-card class="section-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>快捷功能</span>
              <span class="card-sub">Quick Actions</span>
            </div>
          </template>
          <el-row :gutter="12">
            <el-col :span="8" v-for="item in quickActions" :key="item.key">
              <div class="action-card" @click="$router.push(item.link)">
                <div class="action-icon" :style="{ background: item.bg }">
                  <el-icon :size="24" color="#fff"><component :is="item.icon" /></el-icon>
                </div>
                <div class="action-name">{{ item.name }}</div>
                <div class="action-desc">{{ item.desc }}</div>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <!-- AI助手入口 -->
        <el-card class="section-card ai-entry" shadow="never" @click="$router.push('/ai-chat')">
          <div class="ai-entry-content">
            <div class="ai-entry-left">
              <el-icon :size="36" color="#fff"><Cpu /></el-icon>
              <div>
                <div class="ai-entry-title">AI智能助手</div>
                <div class="ai-entry-sub">有什么我可以帮你的？点击开始对话</div>
              </div>
            </div>
            <el-icon :size="20" color="rgba(255,255,255,0.7)"><ArrowRight /></el-icon>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：新闻 + 课程提醒 -->
      <el-col :span="10">
        <el-card class="section-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>校园动态</span>
              <el-button text size="small" @click="$router.push('/news')">查看全部</el-button>
            </div>
          </template>
          <div v-if="!latestNews.length" class="empty-block">暂无新闻</div>
          <div v-for="item in latestNews" :key="item.id" class="news-row" @click="$router.push(`/news/${item.id}`)">
            <div class="news-dot" :style="{ background: item._color }"></div>
            <div class="news-text">
              <div class="news-row-title">{{ item.title }}</div>
              <div class="news-row-meta">
                <el-tag size="small" v-if="item.category">{{ item.category }}</el-tag>
                <span>{{ formatDate(item.publishTime) }}</span>
              </div>
            </div>
          </div>
        </el-card>

        <el-card class="section-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>今日课程</span>
              <span class="card-sub">周{{ weekDayMap[todayWeekDay] || '一' }}</span>
            </div>
          </template>
          <div v-if="!todayCourses.length" class="empty-block">今日暂无课程安排</div>
          <div v-for="c in todayCourses" :key="c.id" class="course-row">
            <div class="course-time">第{{ c.startSlot }}-{{ c.endSlot }}节</div>
            <div class="course-info">
              <div class="course-name">{{ c.name }}</div>
              <div class="course-room">{{ c.teacher }} · {{ c.classroom }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user.js'
import { getNewsList } from '@/api/news.js'
import { searchCourses } from '@/api/course.js'
import { getStatistics } from '@/api/admin.js'

const router = useRouter()
const userStore = useUserStore()
const latestNews = ref([])
const todayCourses = ref([])
let timer = null

const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
const weekDayMap = { 1: '一', 2: '二', 3: '三', 4: '四', 5: '五' }
const todayWeekDay = new Date().getDay() || 7

const weather = ref({ temp: '26', desc: '晴' })

// 模拟通知
const notifications = ref([
  { id: 1, title: '期末考试安排已发布', time: '10分钟前', icon: 'Bell', color: '#E6A23C' },
  { id: 2, title: '图书馆开放时间调整通知', time: '1小时前', icon: 'Reading', color: '#409EFF' }
])

const statCards = ref([
  { key: 'course', num: 0, label: '课程总数', icon: 'Reading', trend: '+2', bg: 'linear-gradient(135deg, #409EFF, #337ECC)', link: '/courses' },
  { key: 'classroom', num: 0, label: '可用教室', icon: 'OfficeBuilding', trend: '实时', bg: 'linear-gradient(135deg, #67C23A, #529B2E)', link: '/classrooms' },
  { key: 'news', num: 0, label: '校园新闻', icon: 'Notebook', trend: '新', bg: 'linear-gradient(135deg, #E6A23C, #C78E2E)', link: '/news' },
  { key: 'user', num: 0, label: '平台用户', icon: 'User', trend: '+5', bg: 'linear-gradient(135deg, #F56C6C, #D94F4F)', link: '/admin/users' }
])

const quickActions = [
  { key: 'news', name: '校园新闻', desc: '最新校园动态', icon: 'Notebook', bg: '#409EFF', link: '/news' },
  { key: 'courses', name: '课程查询', desc: '快速查找课程', icon: 'Reading', bg: '#67C23A', link: '/courses' },
  { key: 'classrooms', name: '空教室', desc: '找到自习空间', icon: 'OfficeBuilding', bg: '#E6A23C', link: '/classrooms' },
  { key: 'ai', name: 'AI助手', desc: '智能问答对话', icon: 'ChatDotRound', bg: '#9B59B6', link: '/ai-chat' },
  { key: 'plan', name: '学习计划', desc: 'AI定制方案', icon: 'Document', bg: '#F56C6C', link: '/study-plan' },
  { key: 'admin', name: '系统管理', desc: '后台管理', icon: 'Setting', bg: '#606266', link: '/admin/news', admin: true }
]

const currentTime = ref('')
const currentDate = ref('')

function updateTime() {
  const now = new Date()
  currentTime.value = now.toLocaleTimeString('zh-CN', { hour12: false })
  currentDate.value = now.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' })
}
timer = setInterval(updateTime, 1000)
updateTime()

function formatDate(d) {
  if (!d) return ''
  return d.slice(0, 10)
}

function handleCommand(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}

onMounted(async () => {
  try {
    const [ newsRes, courseRes ] = await Promise.all([
      getNewsList({ page: 1, size: 5 }),
      searchCourses({ weekDay: todayWeekDay > 5 ? 1 : todayWeekDay })
    ])
    const newsData = newsRes.data.records || newsRes.data || []
    latestNews.value = (Array.isArray(newsData) ? newsData : []).slice(0, 5).map((n, i) => ({
      ...n,
      _color: colors[i % colors.length]
    }))
    statCards.value[2].num = newsRes.data.total || latestNews.value.length

    todayCourses.value = (Array.isArray(courseRes.data) ? courseRes.data : []).slice(0, 6)
    statCards.value[0].num = todayCourses.value.length

    // 仅管理员获取全局统计数据
    if (userStore.user?.role === 'admin') {
      try {
        const stats = await getStatistics()
        if (stats.data) {
          statCards.value[0].num = stats.data.courseCount || statCards.value[0].num
          statCards.value[1].num = stats.data.classroomCount || statCards.value[1].num
          statCards.value[2].num = stats.data.newsCount || statCards.value[2].num
          statCards.value[3].num = stats.data.userCount || statCards.value[3].num
        }
      } catch {}
    }
  } catch {}
})

onBeforeUnmount(() => clearInterval(timer))
</script>

<style scoped>
/* ===== 顶部驾驶舱横幅 ===== */
.cockpit-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
  padding: 16px 28px;
  border-radius: 12px;
  margin-bottom: 16px;
  color: #fff;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}
.campus-brand {
  display: flex;
  align-items: center;
  gap: 10px;
}
.brand-name { font-size: 18px; font-weight: bold; }
.brand-sub { font-size: 11px; opacity: 0.6; letter-spacing: 1px; }
.weather-widget {
  display: flex;
  align-items: center;
  gap: 8px;
}
.weather-temp { font-size: 20px; font-weight: bold; }
.weather-desc { font-size: 12px; opacity: 0.7; }

.header-center { text-align: center; }
.time { font-size: 28px; font-weight: bold; letter-spacing: 2px; font-family: 'Courier New', monospace; }
.date { font-size: 13px; opacity: 0.7; margin-top: 2px; }

.header-right {
  display: flex;
  align-items: center;
  gap: 18px;
}
.notify-btn {
  cursor: pointer;
  width: 38px;
  height: 38px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  background: rgba(255,255,255,0.1);
  transition: 0.2s;
}
.notify-btn:hover { background: rgba(255,255,255,0.2); }
.notify-list { max-height: 240px; overflow-y: auto; }
.notify-empty { text-align: center; color: #909399; padding: 20px 0; font-size: 14px; }
.notify-item {
  display: flex; align-items: flex-start; gap: 10px; padding: 10px 0; border-bottom: 1px solid #f0f0f0;
}
.notify-item:last-child { border: none; }
.notify-title { font-size: 13px; color: #303133; }
.notify-time { font-size: 11px; color: #909399; margin-top: 2px; }

.user-widget {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 4px 12px 4px 4px;
  border-radius: 24px;
  background: rgba(255,255,255,0.08);
}
.user-name { font-size: 14px; font-weight: 500; }
.user-role { font-size: 11px; opacity: 0.6; }

/* ===== 数据卡片 ===== */
.stat-row { margin-bottom: 16px; }
.stat-panel {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 20px;
  border-radius: 12px;
  color: #fff;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  position: relative;
  overflow: hidden;
}
.stat-panel:hover { transform: translateY(-3px); box-shadow: 0 8px 25px rgba(0,0,0,0.15); }
.stat-panel::after {
  content: '';
  position: absolute;
  right: -15px;
  top: -15px;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: rgba(255,255,255,0.08);
}
.stat-icon-box {
  width: 52px;
  height: 52px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255,255,255,0.15);
  border-radius: 12px;
}
.stat-num { font-size: 26px; font-weight: bold; }
.stat-label { font-size: 13px; opacity: 0.85; margin-top: 2px; }
.stat-trend {
  position: absolute;
  right: 16px;
  bottom: 12px;
  font-size: 12px;
  opacity: 0.7;
  display: flex;
  align-items: center;
  gap: 2px;
}

/* ===== 主内容区 ===== */
.main-row { margin-bottom: 16px; }
.section-card {
  margin-bottom: 16px;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}
.card-sub { font-size: 12px; color: #909399; font-weight: normal; }

/* 快捷入口卡片 */
.action-card {
  text-align: center;
  padding: 18px 10px;
  border-radius: 10px;
  cursor: pointer;
  transition: 0.2s;
  border: 1px solid transparent;
}
.action-card:hover {
  background: #f8fafc;
  border-color: #e0e0e0;
  transform: translateY(-2px);
}
.action-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 10px;
}
.action-name { font-size: 14px; color: #303133; font-weight: 500; }
.action-desc { font-size: 12px; color: #909399; margin-top: 4px; }

/* AI 入口横幅 */
.ai-entry {
  background: linear-gradient(135deg, #667eea, #764ba2) !important;
  border: none !important;
  cursor: pointer;
  color: #fff !important;
}
.ai-entry :deep(.el-card__header) { border-bottom-color: rgba(255,255,255,0.15); }
.ai-entry-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.ai-entry-left {
  display: flex;
  align-items: center;
  gap: 14px;
}
.ai-entry-title { font-size: 16px; font-weight: 600; }
.ai-entry-sub { font-size: 13px; opacity: 0.8; margin-top: 4px; }

/* 新闻行 */
.news-row {
  display: flex; align-items: flex-start; gap: 10px; padding: 10px 0; cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
}
.news-row:last-child { border: none; }
.news-dot {
  width: 8px; height: 8px; border-radius: 50%; margin-top: 6px; flex-shrink: 0;
}
.news-row-title {
  font-size: 14px; color: #303133; line-height: 1.4;
  display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden;
}
.news-row-title:hover { color: #409EFF; }
.news-row-meta { display: flex; gap: 8px; align-items: center; margin-top: 4px; font-size: 11px; color: #909399; }

/* 课程行 */
.course-row {
  display: flex; gap: 14px; padding: 10px 0; border-bottom: 1px solid #f5f5f5; align-items: center;
}
.course-row:last-child { border: none; }
.course-time {
  font-size: 12px; color: #fff; background: #409EFF; padding: 4px 10px; border-radius: 4px;
  white-space: nowrap;
}
.course-name { font-size: 14px; color: #303133; font-weight: 500; }
.course-room { font-size: 12px; color: #909399; margin-top: 2px; }

.empty-block { text-align: center; color: #909399; padding: 28px 0; font-size: 14px; }
</style>
