<template>
  <div class="chat-message" :class="message.role">
    <div class="message-avatar">
      <el-avatar :size="36" v-if="message.role === 'assistant'" src="/ai-avatar.png">
        <el-icon :size="20"><Cpu /></el-icon>
      </el-avatar>
      <el-avatar :size="36" v-else>
        <el-icon :size="20"><UserFilled /></el-icon>
      </el-avatar>
    </div>
    <div class="message-body">
      <div class="message-name">{{ message.role === 'assistant' ? 'AI助手' : '我' }}</div>
      <div class="message-content" v-html="renderContent(message.content)"></div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  message: { type: Object, required: true }
})

function renderContent(text) {
  return text
    .replace(/```(\w*)\n([\s\S]*?)```/g, '<pre><code>$2</code></pre>')
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\n/g, '<br/>')
}
</script>

<style scoped>
.chat-message {
  display: flex; gap: 12px; padding: 16px 0; max-width: 85%;
}
.chat-message.user { margin-left: auto; flex-direction: row-reverse; }
.chat-message.assistant .message-content { background: #f5f5f5; }
.chat-message.user .message-content { background: #409EFF; color: #fff; }
.chat-message.user .message-name { text-align: right; }
.message-body { flex: 1; }
.message-name { font-size: 13px; color: #909399; margin-bottom: 6px; }
.message-content {
  padding: 12px 16px; border-radius: 12px; font-size: 14px; line-height: 1.7; word-break: break-word;
}
.message-content :deep(pre) {
  background: #2d2d2d; color: #f8f8f2; padding: 12px; border-radius: 8px; overflow-x: auto;
  margin: 8px 0; font-size: 13px;
}
.message-content :deep(code) { font-family: 'Courier New', monospace; }
.message-content :deep(strong) { font-weight: 600; }
</style>
