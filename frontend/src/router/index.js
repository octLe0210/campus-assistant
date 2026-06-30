import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'news',
        name: 'NewsList',
        component: () => import('@/views/NewsList.vue'),
        meta: { title: '校园新闻' }
      },
      {
        path: 'news/:id',
        name: 'NewsDetail',
        component: () => import('@/views/NewsDetail.vue'),
        meta: { title: '新闻详情' }
      },
      {
        path: 'courses',
        name: 'CourseQuery',
        component: () => import('@/views/CourseQuery.vue'),
        meta: { title: '课程查询' }
      },
      {
        path: 'classrooms',
        name: 'ClassroomQuery',
        component: () => import('@/views/ClassroomQuery.vue'),
        meta: { title: '空教室查询' }
      },
      {
        path: 'ai-chat',
        name: 'AiChat',
        component: () => import('@/views/AiChat.vue'),
        meta: { title: 'AI问答助手' }
      },
      {
        path: 'study-plan',
        name: 'StudyPlan',
        component: () => import('@/views/StudyPlan.vue'),
        meta: { title: '学习计划' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/home')
  } else {
    next()
  }
})

export default router
