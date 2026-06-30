<template>
  <div class="news-detail" v-loading="loading">
    <el-button @click="$router.back()" style="margin-bottom:20px">
      <el-icon><ArrowLeft /></el-icon> 返回
    </el-button>
    <el-card>
      <div class="detail-header">
        <h1>{{ news.title }}</h1>
        <div class="detail-meta">
          <span>来源：{{ news.source }}</span>
          <span>发布时间：{{ news.publishTime?.slice(0, 16).replace('T', ' ') }}</span>
          <span>阅读量：{{ news.viewCount }}</span>
        </div>
      </div>
      <el-divider />
      <div class="detail-content" v-html="news.content"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getNewsDetail } from '@/api/news.js'

const route = useRoute()
const news = ref({})
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getNewsDetail(route.params.id)
    news.value = res.data
  } catch {} finally {
    loading.value = false
  }
})
</script>

<style scoped>
.news-detail { max-width: 900px; margin: 0 auto; }
.detail-header h1 { font-size: 24px; color: #303133; line-height: 1.6; }
.detail-meta {
  display: flex; gap: 24px; margin-top: 12px; font-size: 13px; color: #909399;
}
.detail-content { line-height: 1.8; color: #303133; font-size: 15px; }
.detail-content :deep(p) { margin-bottom: 12px; }
.detail-content :deep(img) { max-width: 100%; }
</style>
