<template>
  <div>
    <el-card>
      <div class="search-bar">
        <el-select v-model="building" placeholder="选择教学楼" clearable style="width:160px">
          <el-option v-for="b in buildings" :key="b" :label="b" :value="b" />
        </el-select>
        <el-select v-model="weekDay" placeholder="选择星期" style="width:140px;margin-left:12px">
          <el-option v-for="(label, val) in weekDayMap" :key="val" :label="label" :value="Number(val)" />
        </el-select>
        <el-select v-model="startSlot" placeholder="开始节次" style="width:140px;margin-left:12px">
          <el-option v-for="i in 10" :key="i" :label="`第${i}节`" :value="i" />
        </el-select>
        <el-select v-model="endSlot" placeholder="结束节次" style="width:140px;margin-left:12px">
          <el-option v-for="i in 10" :key="i" :label="`第${i}节`" :value="i" />
        </el-select>
        <el-button type="primary" @click="search" style="margin-left:12px" :disabled="!weekDay || !startSlot || !endSlot">查询空教室</el-button>
      </div>
    </el-card>

    <el-card style="margin-top:20px">
      <template #header>
        <span>当前条件：{{ weekDayMap[weekDay] }} 第{{ startSlot }}-{{ endSlot }}节</span>
        <span style="margin-left:12px;color:#409EFF">共 {{ classroomList.length }} 间可用</span>
      </template>
      <el-row :gutter="16" v-if="classroomList.length > 0">
        <el-col :span="6" v-for="room in classroomList" :key="room.id" style="margin-bottom:16px">
          <el-card shadow="hover" class="classroom-card">
            <div class="room-building">{{ room.building }}</div>
            <div class="room-name">{{ room.name }}室</div>
            <div class="room-info">
              <span>容量：{{ room.capacity }}人</span>
              <span v-if="room.hasProjector" style="color:#67C23A">有投影仪</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-else description="请选择查询条件，或当前时间段暂无可用教室" />
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getAvailableClassrooms } from '@/api/classroom.js'

const weekDayMap = { 1: '周一', 2: '周二', 3: '周三', 4: '周四', 5: '周五' }
const buildings = ['教一楼', '教二楼', '教三楼']

const building = ref('')
const weekDay = ref(null)
const startSlot = ref(null)
const endSlot = ref(null)
const classroomList = ref([])

async function search() {
  try {
    const res = await getAvailableClassrooms({
      building: building.value,
      weekDay: weekDay.value,
      startSlot: startSlot.value,
      endSlot: endSlot.value
    })
    classroomList.value = res.data || []
  } catch {}
}
</script>

<style scoped>
.search-bar { display: flex; align-items: center; flex-wrap: wrap; gap: 0; }
.classroom-card { text-align: center; cursor: pointer; }
.classroom-card:hover { border-color: #409EFF; }
.room-building { font-size: 13px; color: #909399; }
.room-name { font-size: 22px; font-weight: bold; color: #303133; margin: 8px 0; }
.room-info { display: flex; justify-content: center; gap: 12px; font-size: 12px; color: #909399; }
</style>
