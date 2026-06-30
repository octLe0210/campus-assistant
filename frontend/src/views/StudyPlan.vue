<template>
  <div>
    <div class="page-header">
      <h3>我的学习计划</h3>
      <el-button type="primary" @click="showDialog = true">
        <el-icon><MagicStick /></el-icon> AI生成学习计划
      </el-button>
    </div>

    <el-row :gutter="20" v-if="plans.length > 0">
      <el-col :span="8" v-for="plan in plans" :key="plan.id" style="margin-bottom:20px">
        <el-card shadow="hover" class="plan-card">
          <template #header>
            <div class="plan-header">
              <span>{{ plan.title }}</span>
              <el-tag :type="plan.status === 'active' ? 'success' : 'info'" size="small">
                {{ plan.status === 'active' ? '进行中' : plan.status === 'completed' ? '已完成' : '已取消' }}
              </el-tag>
            </div>
          </template>
          <div class="plan-content" v-html="renderContent(plan.content)"></div>
          <div class="plan-footer">
            <span class="plan-date">{{ plan.createdAt?.slice(0, 10) }}</span>
            <div>
              <el-button size="small" type="success" v-if="plan.status === 'active'" @click="markDone(plan)">完成</el-button>
              <el-button size="small" type="danger" @click="delPlan(plan.id)">删除</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-empty v-else description="暂无学习计划，点击上方按钮让AI帮你生成" />

    <el-dialog v-model="showDialog" title="AI生成学习计划" width="500px">
      <el-input
        v-model="goal"
        type="textarea"
        :rows="3"
        placeholder="简单描述你的学习目标，如：准备期末考试，有高等数学、英语、数据结构三门课程需要复习，现在距离考试还有2周时间"
      />
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="generate" :loading="generating">生成计划</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getPlanList, generatePlan, updatePlan, deletePlan } from '@/api/studyPlan.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const plans = ref([])
const showDialog = ref(false)
const generating = ref(false)
const goal = ref('')

function renderContent(text) {
  if (!text) return ''
  return text
    .replace(/```(\w*)\n([\s\S]*?)```/g, '<pre><code>$2</code></pre>')
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\n/g, '<br/>')
}

async function fetchPlans() {
  try {
    const res = await getPlanList()
    plans.value = res.data || []
  } catch {}
}

async function generate() {
  if (!goal.value.trim()) return
  generating.value = true
  try {
    await generatePlan(goal.value)
    ElMessage.success('学习计划已生成')
    showDialog.value = false
    goal.value = ''
    fetchPlans()
  } catch {} finally {
    generating.value = false
  }
}

async function markDone(plan) {
  try {
    await updatePlan(plan.id, { ...plan, status: 'completed' })
    ElMessage.success('已标记为完成')
    fetchPlans()
  } catch {}
}

async function delPlan(id) {
  try {
    await ElMessageBox.confirm('确定删除该学习计划？', '确认', { type: 'warning' })
    await deletePlan(id)
    ElMessage.success('已删除')
    fetchPlans()
  } catch {}
}

onMounted(fetchPlans)
</script>

<style scoped>
.page-header {
  display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;
}
.page-header h3 { font-size: 18px; color: #303133; }
.plan-header { display: flex; justify-content: space-between; align-items: center; }
.plan-content {
  font-size: 14px; color: #606266; line-height: 1.8;
  max-height: 300px; overflow-y: auto;
}
.plan-content :deep(pre) { background: #f5f5f5; padding: 8px; border-radius: 4px; overflow-x: auto; font-size: 13px; }
.plan-footer {
  display: flex; justify-content: space-between; align-items: center; margin-top: 12px;
  padding-top: 12px; border-top: 1px solid #f0f0f0;
}
.plan-date { font-size: 12px; color: #909399; }
</style>
