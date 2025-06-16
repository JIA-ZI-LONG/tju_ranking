<template>
  <div class="home-container">
    <!-- 顶部欢迎区域 -->
    <div class="welcome-section">
      <h1 class="welcome-title">欢迎来到天津大学美食点评社区！</h1>
      <p class="welcome-subtitle">发现舌尖上的天大，分享你的美食故事。</p>
      <div class="search-bar">
        <el-input placeholder="搜索食堂、菜品或博客" v-model="searchKeyword" class="input-with-select">
          <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
        </el-input>
      </div>
    </div>

    <!-- 核心功能导航 -->
    <el-card class="nav-card">
      <el-row :gutter="20" class="nav-links">
        <el-col :span="8">
          <router-link to="/main/user/canteenslist" class="nav-item">
            <i class="el-icon-food"></i>
            <span>食堂大全</span>
          </router-link>
        </el-col>
        <!--
        <el-col :span="6">
          <router-link to="/main/user/disheslist" class="nav-item">
            <i class="el-icon-dish"></i>
            <span>招牌菜品</span>
          </router-link>
        </el-col>
        -->
        <el-col :span="8">
          <router-link to="/main/user/blogslist" class="nav-item">
            <i class="el-icon-notebook-2"></i>
            <span>美食博客</span>
          </router-link>
        </el-col>
        <el-col :span="8">
          <router-link to="/main/user/profile" class="nav-item">
            <i class="el-icon-user"></i>
            <span>个人中心</span>
          </router-link>
        </el-col>
      </el-row>
    </el-card>

    <!-- 推荐内容区域 -->
    <el-row :gutter="20" class="content-sections">
      <el-col :span="12">
        <el-card class="content-card">
          <div slot="header" class="clearfix">
            <span>热门食堂</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="$router.push('/main/user/canteenslist')">查看更多</el-button>
          </div>
          <div v-for="canteen in hotCanteens" :key="canteen.id" class="list-item">
            <img :src="canteen.image" alt="食堂图片" class="item-image">
            <div class="item-info">
              <span class="item-title">{{ canteen.name }}</span>
              <el-rate v-model="canteen.rating" disabled show-score text-color="#ff9900" score-template="{value}分"></el-rate>
              <p class="item-desc">{{ canteen.desc }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="content-card">
          <div slot="header" class="clearfix">
            <span>最新博客</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="$router.push('/main/user/blogslist')">查看更多</el-button>
          </div>
          <div v-for="blog in latestBlogs" :key="blog.id" class="list-item">
            <img :src="blog.image" alt="博客图片" class="item-image">
            <div class="item-info">
              <span class="item-title">{{ blog.title }}</span>
              <p class="item-author">作者: {{ blog.author }}</p>
              <p class="item-desc">{{ blog.summary }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'HomeView',
  data() {
    return {
      searchKeyword: '',
      hotCanteens: [
        { id: 1, name: '学一食堂', rating: 4.8, desc: '北洋园校区人气最旺，菜品种类丰富，味道地道。', image: 'https://via.placeholder.com/100x100?text=Canteen1' },
        { id: 2, name: '学三食堂', rating: 4.5, desc: '卫津路校区经典老牌食堂，特色面食值得一试。', image: 'https://via.placeholder.com/100x100?text=Canteen2' },
        // 更多热门食堂数据...
      ],
      latestBlogs: [
        { id: 1, title: '天大特色美食攻略', author: '美食探索家', summary: '带你探秘天大校园内的隐藏美食！', image: 'https://via.placeholder.com/100x100?text=Blog1' },
        { id: 2, title: '食堂一周不重样挑战', author: '吃货小分队', summary: '挑战在天大食堂吃一周不重样，附详细菜单。', image: 'https://via.placeholder.com/100x100?text=Blog2' },
        // 更多最新博客数据...
      ]
    }
  },
  methods: {
    handleSearch() {
      this.$message.info(`您搜索了: ${this.searchKeyword}`)
      // TODO: 实现搜索功能跳转
    }
  }
}
</script>

<style scoped>
.home-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px); /* 减去MainMenu的高度 */
}

.welcome-section {
  background-image: url('@/assets/building9.jpg'); /* 替换为天津大学相关背景图 */
  background-size: cover;
  background-position: center;
  color: #fff;
  text-align: center;
  padding: 80px 20px;
  border-radius: 8px;
  margin-bottom: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.welcome-title {
  font-size: 38px;
  margin-bottom: 15px;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.welcome-subtitle {
  font-size: 18px;
  margin-bottom: 30px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
}

.search-bar {
  max-width: 600px;
  margin: 0 auto;
}

.search-bar .el-input {
  border-radius: 50px;
  overflow: hidden;
}

.search-bar .el-input /deep/ .el-input__inner {
  border-radius: 50px 0 0 50px;
  height: 45px;
  padding-left: 20px;
}

.search-bar .el-input /deep/ .el-input-group__append {
  background-color: #409EFF;
  color: #fff;
  border-radius: 0 50px 50px 0;
  border: none;
  height: 45px;
  line-height: 45px;
  padding: 0 20px;
}

.nav-card {
  margin-bottom: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.nav-links {
  text-align: center;
  padding: 10px 0;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-decoration: none;
  color: #333;
  padding: 15px 0;
  transition: all 0.3s ease;
  border-radius: 8px;
}

.nav-item:hover {
  background-color: #ecf5ff;
  color: #409EFF;
  transform: translateY(-3px);
}

.nav-item i {
  font-size: 36px;
  margin-bottom: 10px;
  color: #409EFF; /* 图标颜色 */
}

.nav-item span {
  font-size: 16px;
  font-weight: bold;
}

.content-sections {
  margin-bottom: 30px;
}

.content-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  min-height: 350px; /* 统一卡片高度 */
}

.content-card .clearfix:before,
.content-card .clearfix:after {
  display: table;
  content: "";
}
.content-card .clearfix:after {
  clear: both
}

.content-card .el-card__header {
  padding: 15px 20px;
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.list-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #ebeef5;
}

.list-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  object-fit: cover;
  margin-right: 15px;
  flex-shrink: 0;
}

.item-info {
  flex-grow: 1;
}

.item-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
  display: block;
}

.item-desc {
  font-size: 14px;
  color: #909399;
  line-height: 1.4;
  margin-top: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2; /* 限制2行 */
  -webkit-box-orient: vertical;
}

.el-rate {
  line-height: 1; /* 解决评分组件垂直对齐问题 */
}
</style>
