<template>
  <div class="ai-chat">
    <div class="chat-container">
      <div class="chat-messages" ref="messagesContainer">
        <div v-if="messages.length === 0" class="welcome-msg">
          <el-icon :size="48" color="#409EFF"><Cpu /></el-icon>
          <h3>AI智能校园助手</h3>
          <p>你好！我是校园AI助手，可以帮你解答学习、课程、校园生活等问题。</p>
        </div>
        <ChatMessage v-for="msg in messages" :key="msg.id" :message="msg" />
        <div v-if="thinking" class="thinking-tip">
          <el-icon class="is-loading"><Loading /></el-icon> AI正在思考中...
        </div>
      </div>
      <div class="chat-input">
        <el-input
          v-model="input"
          type="textarea"
          :rows="2"
          placeholder="输入你的问题，如：如何高效准备期末考试？"
          @keydown.enter.exact.prevent="send"
          resize="none"
        />
        <el-button type="primary" @click="send" :disabled="!input.trim() || thinking" style="margin-left:12px;height:52px">
          <el-icon><Promotion /></el-icon> 发送
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { sendMessage, getChatHistory } from '@/api/ai.js'
import ChatMessage from '@/components/ChatMessage.vue'

const messages = ref([])
const input = ref('')
const thinking = ref(false)
const messagesContainer = ref(null)

async function send() {
  const text = input.value.trim()
  if (!text || thinking.value) return

  const userMsg = { id: Date.now(), role: 'user', content: text }
  messages.value.push(userMsg)
  input.value = ''
  thinking.value = true
  scrollBottom()

  try {
    const history = messages.value.slice(0, -1).map(m => ({ role: m.role, content: m.content }))
    const res = await sendMessage({ message: text, history })
    messages.value.push({ id: Date.now() + 1, role: 'assistant', content: res.data })
  } catch {
    messages.value.push({ id: Date.now() + 1, role: 'assistant', content: '抱歉，服务暂时不可用，请稍后再试。' })
  } finally {
    thinking.value = false
    scrollBottom()
  }
}

function scrollBottom() {
  nextTick(() => {
    const el = messagesContainer.value
    if (el) el.scrollTop = el.scrollHeight
  })
}

onMounted(async () => {
  try {
    const res = await getChatHistory()
    if (res.data) {
      messages.value = res.data.map(m => ({ id: m.id, role: m.role, content: m.content }))
    }
    scrollBottom()
  } catch {}
})
</script>

<style scoped>
.ai-chat {
  display: flex; justify-content: center; height: calc(100vh - 140px);
}
.chat-container {
  width: 100%; max-width: 800px; display: flex; flex-direction: column;
  background: #fff; border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}
.chat-messages {
  flex: 1; overflow-y: auto; padding: 0 24px;
}
.welcome-msg {
  text-align: center; padding: 60px 0; color: #909399;
}
.welcome-msg h3 { margin: 12px 0 8px; color: #303133; }
.welcome-msg p { font-size: 14px; }
.thinking-tip {
  padding: 12px 0; font-size: 13px; color: #909399; display: flex; align-items: center; gap: 8px;
}
.chat-input {
  display: flex; padding: 16px; border-top: 1px solid #f0f0f0; background: #fafafa;
}
</style>
