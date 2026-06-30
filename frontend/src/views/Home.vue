<template>
  <div class="home">
    <h2 class="page-title">AI智能校园助手平台</h2>
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6" v-for="card in statCards" :key="card.title">
        <el-card shadow="hover" class="stat-card" :style="{ borderTop: `3px solid ${card.color}` }">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-num">{{ card.num }}</div>
              <div class="stat-title">{{ card.title }}</div>
            </div>
            <el-icon :size="40" :color="card.color">
              <component :is="card.icon" />
            </el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card class="welcome-card">
          <template #header>
            <span>欢迎使用智能校园助手</span>
          </template>
          <div class="welcome-content">
            <p>AI智能校园助手是专为大学生打造的一站式校园服务平台，提供以下功能：</p>
            <el-row :gutter="16" style="margin-top: 16px">
              <el-col :span="8" v-for="feature in features" :key="feature.title">
                <div class="feature-item">
                  <el-icon :size="24" color="#409EFF"><component :is="feature.icon" /></el-icon>
                  <div>
                    <h4>{{ feature.title }}</h4>
                    <p>{{ feature.desc }}</p>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="news-card">
          <template #header>
            <span>最新校园新闻</span>
          </template>
          <div v-if="latestNews.length === 0" class="empty-hint">暂无新闻</div>
          <div v-for="item in latestNews" :key="item.id" class="news-item" @click="$router.push(`/news/${item.id}`)">
            <div class="news-item-title">{{ item.title }}</div>
            <div class="news-item-time">{{ item.publishTime?.slice(0, 10) }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getNewsList } from '@/api/news.js'

const statCards = ref([
  { title: '课程总数', num: 0, color: '#409EFF', icon: 'Reading' },
  { title: '可用教室', num: 0, color: '#67C23A', icon: 'OfficeBuilding' },
  { title: '校园新闻', num: 0, color: '#E6A23C', icon: 'Notebook' },
  { title: '学习计划', num: 0, color: '#F56C6C', icon: 'Document' }
])

const features = [
  { title: '校园新闻', desc: '获取最新校园动态', icon: 'Notebook' },
  { title: '课程查询', desc: '快速查找课程信息', icon: 'Reading' },
  { title: '空教室查询', desc: '随时找到空闲教室', icon: 'OfficeBuilding' },
  { title: 'AI问答助手', desc: '智能对话解决问题', icon: 'ChatDotRound' },
  { title: '学习计划', desc: 'AI定制学习方案', icon: 'Document' },
  { title: '数据看板', desc: '可视化数据展示', icon: 'DataAnalysis' }
]

const latestNews = ref([])

onMounted(async () => {
  try {
    const res = await getNewsList({ page: 1, size: 5 })
    latestNews.value = res.data.records || []
    statCards.value[2].num = res.data.total || 0
  } catch {}
})
</script>

<style scoped>
.home { max-width: 1400px; margin: 0 auto; }
.page-title {
  margin-bottom: 20px;
  font-size: 24px;
  color: #303133;
}
.stat-card { cursor: pointer; transition: transform 0.2s; }
.stat-card:hover { transform: translateY(-2px); }
.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.stat-num { font-size: 28px; font-weight: bold; color: #303133; }
.stat-title { font-size: 14px; color: #909399; margin-top: 4px; }
.welcome-content p { color: #606266; line-height: 1.8; }
.feature-item {
  display: flex; gap: 12px; padding: 12px 0;
}
.feature-item h4 { font-size: 15px; color: #303133; margin-bottom: 4px; }
.feature-item p { font-size: 13px; color: #909399; }
.news-item {
  padding: 10px 0; border-bottom: 1px solid #f0f0f0; cursor: pointer;
}
.news-item:last-child { border-bottom: none; }
.news-item-title { font-size: 14px; color: #303133; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.news-item-title:hover { color: #409EFF; }
.news-item-time { font-size: 12px; color: #909399; margin-top: 4px; }
.empty-hint { color: #909399; text-align: center; padding: 20px 0; }
</style>
