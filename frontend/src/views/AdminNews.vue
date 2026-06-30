<template>
  <div>
    <div class="page-header">
      <h3>新闻管理</h3>
      <el-button type="primary" @click="openDialog(null)">发布新闻</el-button>
    </div>

    <el-card>
      <el-table :data="newsList" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="source" label="来源" width="100" />
        <el-table-column prop="viewCount" label="阅读量" width="80" />
        <el-table-column prop="publishTime" label="发布时间" width="160">
          <template #default="{ row }">{{ row.publishTime?.slice(0, 16).replace('T', ' ') }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && newsList.length === 0" description="暂无新闻" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑新闻' : '发布新闻'" width="700px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" style="width:100%">
            <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
          </el-select>
        </el-form-item>
        <el-form-item label="来源">
          <el-input v-model="form.source" />
        </el-form-item>
        <el-form-item label="封面图URL">
          <el-input v-model="form.coverImage" placeholder="可选" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="10" placeholder="支持HTML标签" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getNewsList } from '@/api/news.js'
import { createNews, updateNews, deleteNews } from '@/api/admin.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const categories = ['通知公告', '学术动态', '校园生活', '设施建设']
const newsList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const form = ref({})

async function fetchData() {
  loading.value = true
  try {
    const res = await getNewsList({ page: 1, size: 100 })
    newsList.value = res.data.records || []
  } catch {} finally { loading.value = false }
}

function openDialog(row) {
  isEdit.value = !!row
  form.value = row ? { ...row } : { title: '', category: '', source: '校园新闻中心', content: '', coverImage: '' }
  dialogVisible.value = true
}

async function save() {
  saving.value = true
  try {
    if (isEdit.value) {
      await updateNews(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await createNews(form.value)
      ElMessage.success('发布成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch {} finally { saving.value = false }
}

async function del(id) {
  try {
    await ElMessageBox.confirm('确定删除该新闻？', '确认', { type: 'warning' })
    await deleteNews(id)
    ElMessage.success('已删除')
    fetchData()
  } catch {}
}

onMounted(fetchData)
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h3 { font-size: 18px; color: #303133; }
</style>
