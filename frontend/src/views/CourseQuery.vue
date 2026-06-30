<template>
  <div>
    <el-card>
      <div class="search-bar">
        <el-input v-model="keyword" placeholder="搜索课程名称..." clearable style="width:220px" @keyup.enter="search" />
        <el-input v-model="teacher" placeholder="搜索教师..." clearable style="width:180px;margin-left:12px" @keyup.enter="search" />
        <el-select v-model="weekDay" placeholder="选择星期" clearable style="width:140px;margin-left:12px" @change="search">
          <el-option label="星期一" :value="1" />
          <el-option label="星期二" :value="2" />
          <el-option label="星期三" :value="3" />
          <el-option label="星期四" :value="4" />
          <el-option label="星期五" :value="5" />
        </el-select>
        <el-button type="primary" @click="search" style="margin-left:12px">查询</el-button>
      </div>
    </el-card>

    <el-card style="margin-top:20px">
      <el-table :data="courseList" stripe v-loading="loading" empty-text="暂无课程数据">
        <el-table-column prop="name" label="课程名称" min-width="160" />
        <el-table-column prop="teacher" label="授课教师" width="100" />
        <el-table-column label="上课时间" width="180">
          <template #default="{ row }">
            {{ weekDayMap[row.weekDay] }} 第{{ row.startSlot }}-{{ row.endSlot }}节
          </template>
        </el-table-column>
        <el-table-column prop="classroom" label="教室" width="120" />
        <el-table-column prop="credits" label="学分" width="70" />
        <el-table-column prop="capacity" label="容量" width="70" />
        <el-table-column prop="description" label="课程简介" min-width="200" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { searchCourses } from '@/api/course.js'

const weekDayMap = { 1: '周一', 2: '周二', 3: '周三', 4: '周四', 5: '周五', 6: '周六', 7: '周日' }

const courseList = ref([])
const loading = ref(false)
const keyword = ref('')
const teacher = ref('')
const weekDay = ref(null)

async function search() {
  loading.value = true
  try {
    const res = await searchCourses({
      keyword: keyword.value,
      teacher: teacher.value,
      weekDay: weekDay.value
    })
    courseList.value = res.data || []
  } catch {} finally {
    loading.value = false
  }
}

search()
</script>

<style scoped>
.search-bar { display: flex; align-items: center; }
</style>
