<template>
  <el-menu mode="horizontal" :router="true" class="main-menu">
    <el-menu-item index="/main/home">
      <img src="../assets/logotju.png" alt="Logo" class="logo">
    </el-menu-item>
    <el-menu-item index="/main/home">主页</el-menu-item>
    <el-menu-item index="/main/user/blogslist">博客</el-menu-item>
    <el-menu-item index="/main/user/canteenslist">食堂</el-menu-item>
    <el-menu-item index="/main/user/disheslist">菜品</el-menu-item>
    <el-menu-item index="/about">关于</el-menu-item>
    <div class="right-menu">
      <el-menu-item index="campus-selection-placeholder" class="campus-dropdown-wrapper">
        <el-dropdown @command="handleCampusChange">
          <span class="el-dropdown-link">
            {{ currentCampus }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="all">所有校区</el-dropdown-item>
            <el-dropdown-item command="weijin">卫津路校区</el-dropdown-item>
            <el-dropdown-item command="beiyang">北洋园校区</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-menu-item>
      <el-menu-item index="/register">注册</el-menu-item>
      <el-menu-item index="/login">登录</el-menu-item>
      <el-menu-item index="/main/user/profile">我的账户</el-menu-item>
    </div>
  </el-menu>
</template>

<script>
export default {
  data() {
    return {
      currentCampus: '所有校区'
    }
  },
  methods: {
    handleCampusChange(command) {
      this.currentCampus = {
        'all': '所有校区',
        'weijin': '卫津路校区',
        'beiyang': '北洋园校区'
      }[command]
      // 这里可以触发一个事件，通知父组件校区已更改
      // 或者直接在这里处理校区相关的逻辑
    }
  }
}
</script>

<style scoped>
.main-menu {
  display: flex;
  align-items: center;
}
.logo {
  height: 40px;
  margin-right: 10px;
}
.right-menu {
  margin-left: auto;
  display: flex;
  align-items: center;
}

/* Style for the el-menu-item that wraps the dropdown */
.right-menu .campus-dropdown-wrapper {
  padding: 0; /* Remove default padding from el-menu-item itself */
  margin-right: 20px; /* Spacing between dropdown and next menu item */
}

/* Style for the actual dropdown link inside the el-menu-item */
.campus-dropdown-wrapper .el-dropdown-link {
  display: flex;
  align-items: center;
  height: 100%; /* Fill the height of the parent el-menu-item */
  padding: 0 20px; /* Mimic el-menu-item's horizontal padding */
  color: #303133; /* Default el-menu-item text color */
  font-size: 14px; /* Default el-menu-item font size */
  cursor: pointer;
  /* Add hover effect for the span to match el-menu-item */
  transition: background-color 0.3s, color 0.3s; 
}

.campus-dropdown-wrapper .el-dropdown-link:hover {
  background-color: #ecf5ff; /* Element UI default hover background */
  color: #409EFF; /* Element UI default hover text color */
}

/* Ensure the el-dropdown component itself doesn't interfere with flex layout */
.campus-dropdown-wrapper .el-dropdown {
  height: 100%;
  display: flex;
  align-items: center;
}
</style>
