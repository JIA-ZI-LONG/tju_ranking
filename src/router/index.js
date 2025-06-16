import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/main/home'  // 将根路径重定向到主页
    },
    {
      path: '/about',
      name: 'about',
      component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import(/* webpackChunkName: "login" */ '../views/LoginView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import(/* webpackChunkName: "register" */ '../views/RegisterView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/main',
      name: 'main',
      component: () => import(/* webpackChunkName: "main" */ '../views/MainView.vue'),
      meta: { requiresAuth: false },
      children: [
        {
          path: 'home',
          name: 'home',
          component: () => import(/* webpackChunkName: "home" */ '../views/HomeView.vue'),
          meta: { requiresAuth: false }
        },
        {
          path: 'user/profile',
          name: 'profile',
          component: () => import(/* webpackChunkName: "user-profile" */ '../views/user/UserProfile.vue'),
          meta: { requiresAuth: false }
        },
        {
          path: 'user/disheslist',
          name: 'disheslist',
          component: () => import(/* webpackChunkName: "user-dishes" */ '../views/user/DishesList.vue'),
          meta: { requiresAuth: false }
        },
        {
          path: 'user/canteenslist',
          name: 'canteenslist',
          component: () => import(/* webpackChunkName: "user-canteens" */ '../views/user/CanteensList.vue'),
          meta: { requiresAuth: false }
        },
        {
          path: 'user/blogslist',
          name: 'blogslist',
          component: () => import(/* webpackChunkName: "user-blogs" */ '../views/user/BlogsList.vue'),
          meta: { requiresAuth: false }
        },
        {
          path: 'user/add-blog',
          name: 'addblog',
          component: () => import(/* webpackChunkName: "user-add-blog" */ '../views/user/AddBlog.vue'),
          meta: { requiresAuth: false }
        },
        {
          path: 'user/canteen-details',
          name: 'canteendetails',
          component: () => import(/* webpackChunkName: "user-canteen-details" */ '../views/user/CanteenDetails.vue'),
          meta: { requiresAuth: false }
        },
        {
          path: 'user/window-details/:windowId/:canteenId',
          name: 'windowdetails',
          component: () => import(/* webpackChunkName: "user-window-details" */ '../views/user/WindowsList.vue'),
          meta: { requiresAuth: false }
        }
      ]
    }
  ]
})