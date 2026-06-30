<template>
  <div>
    <div class="page-header">
      <h3>课程管理</h3>
      <el-button type="primary" @click="openDialog(null)">添加课程</el-button>
    </div>

    <el-card>
      <el-table :data="courseList" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="课程名称" min-width="140" />
        <el-table-column prop="teacher" label="教师" width="80" />
        <el-table-column label="上课时间" width="140">
          <template #default="{ row }">
            周{{ row.weekDay }} 第{{ row.startSlot }}-{{ row.endSlot }}节
          </template>
        </el-table-column>
        <el-table-column prop="classroom" label="教室" width="100" />
        <el-table-column prop="credits" label="学分" width="60" />
        <el-table-column prop="capacity" label="容量" width="60" />
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑课程' : '添加课程'" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="课程名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="授课教师">
          <el-input v-model="form.teacher" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="星期">
              <el-select v-model="form.weekDay" style="width:100%">
                <el-option v-for="d in 5" :key="d" :label="'周' + d" :value="d" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="开始节次">
              <el-select v-model="form.startSlot" style="width:100%">
                <el-option v-for="s in 10" :key="s" :label="'第' + s + '节'" :value="s" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="结束节次">
              <el-select v-model="form.endSlot" style="width:100%">
                <el-option v-for="s in 10" :key="s" :label="'第' + s + '节'" :value="s" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="教室">
              <el-input v-model="form.classroom" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学分">
              <el-input-number v-model="form.credits" :min="0" :step="0.5" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="容量">
          <el-input-number v-model="form.capacity" :min="1" style="width:100%" />
        </el-form-item>
        <el-form-item label="课程简介">
          <el-input v-model="form.description" type="textarea" :rows="2" />
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
import { searchCourses } from '@/api/course.js'
import { createCourse, updateCourse, deleteCourse } from '@/api/admin.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const courseList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const form = ref({})

async function fetchData() {
  loading.value = true
  try {
    const res = await searchCourses({})
    courseList.value = res.data || []
  } catch {} finally { loading.value = false }
}

function openDialog(row) {
  isEdit.value = !!row
  form.value = row ? { ...row } : { name: '', teacher: '', weekDay: 1, startSlot: 1, endSlot: 2, classroom: '', credits: 3, capacity: 60, description: '', semester: '2024-2025-1' }
  dialogVisible.value = true
}

async function save() {
  saving.value = true
  try {
    if (isEdit.value) {
      await updateCourse(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await createCourse(form.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch {} finally { saving.value = false }
}

async function del(id) {
  try {
    await ElMessageBox.confirm('确定删除该课程？', '确认', { type: 'warning' })
    await deleteCourse(id)
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
