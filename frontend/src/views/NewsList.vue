<template>
  <div>
    <el-card>
      <div class="search-bar">
        <el-input v-model="keyword" placeholder="搜索新闻标题..." clearable style="width:300px" @keyup.enter="search" />
        <el-select v-model="category" placeholder="新闻分类" clearable style="width:160px;margin-left:12px" @change="search">
          <el-option label="全部" value="" />
          <el-option label="通知公告" value="通知公告" />
          <el-option label="学术动态" value="学术动态" />
          <el-option label="校园生活" value="校园生活" />
          <el-option label="设施建设" value="设施建设" />
        </el-select>
        <el-button type="primary" @click="search" style="margin-left:12px">搜索</el-button>
      </div>
    </el-card>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="8" v-for="item in newsList" :key="item.id" style="margin-bottom:20px">
        <el-card shadow="hover" class="news-card" @click="$router.push(`/news/${item.id}`)">
          <img :src="item.coverImage || 'https://picsum.photos/400/200?random=' + item.id" class="news-cover" />
          <div class="news-info">
            <div class="news-tag" v-if="item.category">
              <el-tag size="small">{{ item.category }}</el-tag>
            </div>
            <h3 class="news-title">{{ item.title }}</h3>
            <div class="news-meta">
              <span>{{ item.source }}</span>
              <span>{{ item.publishTime?.slice(0, 10) }}</span>
              <span><el-icon><View /></el-icon> {{ item.viewCount }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div class="pagination-wrap" v-if="total > 0">
      <el-pagination
        v-model:current-page="page"
        :page-size="size"
        :total="total"
        layout="prev, pager, next, total"
        @current-change="fetchNews"
      />
    </div>
    <el-empty v-if="newsList.length === 0" description="暂无新闻" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getNewsList } from '@/api/news.js'

const newsList = ref([])
const page = ref(1)
const size = ref(9)
const total = ref(0)
const keyword = ref('')
const category = ref('')

function search() {
  page.value = 1
  fetchNews()
}

async function fetchNews() {
  try {
    const res = await getNewsList({ page: page.value, size: size.value, keyword: keyword.value, category: category.value })
    newsList.value = res.data.records || []
    total.value = res.data.total || 0
  } catch {}
}

onMounted(fetchNews)
</script>

<style scoped>
.search-bar { display: flex; align-items: center; }
.news-card { cursor: pointer; overflow: hidden; }
.news-card:hover { transform: translateY(-2px); transition: transform 0.2s; }
.news-cover { width: 100%; height: 160px; object-fit: cover; display: block; }
.news-info { padding: 12px 0 0; }
.news-tag { margin-bottom: 8px; }
.news-title {
  font-size: 15px; color: #303133; line-height: 1.6;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
}
.news-meta {
  display: flex; gap: 16px; margin-top: 10px; font-size: 12px; color: #909399;
}
.news-meta span { display: flex; align-items: center; gap: 4px; }
.pagination-wrap { margin-top: 20px; display: flex; justify-content: center; }
</style>
