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
          path: 'user/blog-details',
          name: 'blogdetails',
          component: () => import(/* webpackChunkName: "user-blog-details" */ '../views/user/BlogDetails.vue'),
          meta: { requiresAuth: false }
        }
      ]
    },
    // boss 子路由
    {
      path: '/boss/statistics',
      name: 'statisticsA',
      component: () => import(/* webpackChunkName: "boss-statistics" */ '../views/boss/StatisticsAnalytics.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/boss/role',
      name: 'roleM',
      component: () => import(/* webpackChunkName: "boss-role" */ '../views/boss/RoleManage.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/boss/program',
      name: 'programM',
      component: () => import(/* webpackChunkName: "boss-program" */ '../views/boss/ProgrameManage.vue'),
      meta: { requiresAuth: false }
    },
    // admin 子路由
    {
      path: '/admin/bulletins',
      name: 'bulletinsM',
      component: () => import(/* webpackChunkName: "admin-bulletins" */ '../views/admin/BulletinsManage.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/admin/homepage',
      name: 'homepageM',
      component: () => import(/* webpackChunkName: "admin-homepage" */ '../views/admin/HomepageManage.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/admin/data',
      name: 'dataM',
      component: () => import(/* webpackChunkName: "admin-data" */ '../views/admin/DataManage.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/admin/blogs',
      name: 'blogsM',
      component: () => import(/* webpackChunkName: "admin-blogs" */ '../views/admin/BlogsManage.vue'),
      meta: { requiresAuth: false }
    }
  ]
})