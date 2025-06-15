<template>
  <div class="blogs-list-container">
    <!--栅格系统是整个bloglist的容器-->
    <el-row :gutter="20" class="blog-cards">
      <el-col :span="6" v-for="blog in blogs" :key="blog.id">
        <el-card :body-style="{ padding: '0px' }" class="single-blog">
          <div class="blog-header">
            <el-avatar :size="40" :src="blog.author.avatar"></el-avatar>
            <div class="blog-info">
              <div class="author-name">{{ blog.author.name }}</div>
              <div class="blog-date">{{ blog.date }}</div>
            </div>
          </div>
          <img :src="blog.imageUrl" class="image">
          <div class="blog-content">
            <h3 class="blog-title">{{ blog.title }}</h3>
            <div class="blog-rating">
              <el-rate
                v-model="blog.rating"
                disabled
                :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                show-score>
              </el-rate>
              <span class="price">¥{{ blog.price }}/人</span>
            </div>
            <div class="blog-tags">
              <el-tag v-for="tag in blog.tags" :key="tag" size="small" type="info">{{ tag }}</el-tag>
            </div>
            <div class="blog-footer">
              <el-button type="text" class="button" @click="showBlogDetail(blog)">查看详情</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 悬浮添加按钮 -->
    <router-link to="/main/user/add-blog">
      <el-button
        type="primary"
        class="floating-button"
        circle
        size="large">
        <i class="el-icon-plus"></i>
      </el-button>
    </router-link>

    <!-- 博客详情弹窗 -->
    <el-dialog
      :title="currentBlog.title"
      :visible.sync="dialogVisible"
      width="60%"
      class="blog-dialog">
      <div class="blog-detail-content">
        <div class="blog-detail-header">
          <el-avatar :size="50" :src="currentBlog.author?.avatar"></el-avatar>
          <div class="blog-detail-info">
            <div class="blog-detail-author">{{ currentBlog.author?.name }}</div>
            <div class="blog-detail-date">{{ currentBlog.date }}</div>
          </div>
        </div>
        <div class="blog-detail-image">
          <img :src="currentBlog.imageUrl" alt="博客图片">
        </div>
        <div class="blog-detail-rating">
          <el-rate
            v-model="currentBlog.rating"
            disabled
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
            show-score>
          </el-rate>
          <span class="blog-detail-price">¥{{ currentBlog.price }}/人</span>
        </div>
        <div class="blog-detail-tags">
          <el-tag v-for="tag in currentBlog.tags" :key="tag" size="small" type="info">{{ tag }}</el-tag>
        </div>
        <div class="blog-detail-text">
          {{ currentBlog.content || '暂无详细内容' }}
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false,
      currentBlog: {},
      blogs: [
        {
          id: 1,
          title: '天津大学美食探店：学一食堂隐藏菜单大揭秘！',
          imageUrl: 'https://via.placeholder.com/300x200?text=Blog+Image+1',
          date: '2023-03-15',
          rating: 4.5,
          price: 15,
          content: '今天给大家带来学一食堂的隐藏菜单！这里的红烧肉真的是一绝，肉质鲜嫩，入口即化。还有他们家的特色小炒，价格实惠，分量十足。强烈推荐大家来尝试！',
          tags: ['美食', '实惠', '特色菜'],
          author: {
            name: '美食达人',
            avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
          }
        },
        {
          id: 2,
          title: '北洋园校区夜跑攻略：发现不一样的校园美景',
          imageUrl: 'https://via.placeholder.com/300x200?text=Blog+Image+2',
          date: '2023-03-10',
          rating: 4.0,
          price: 0,
          tags: ['运动', '校园', '美景'],
          author: {
            name: '运动健将',
            avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
          }
        },
        {
          id: 3,
          title: '卫津路校区图书馆学习心得：如何高效利用资源',
          imageUrl: 'https://via.placeholder.com/300x200?text=Blog+Image+3',
          date: '2023-03-08',
          rating: 4.8,
          price: 0,
          tags: ['学习', '图书馆', '攻略'],
          author: {
            name: '学霸笔记',
            avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
          }
        },
        {
          id: 4,
          title: '天大周边咖啡馆推荐：期末复习好去处',
          imageUrl: 'https://via.placeholder.com/300x200?text=Blog+Image+4',
          date: '2023-03-05',
          rating: 4.8,
          price: 0,
          tags: ['学习', '图书馆', '攻略'],
          author: {
            name: '咖啡控',
            avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
          }
        }
      ]
    }
  },
  methods: {
    showBlogDetail(blog) {
      this.currentBlog = blog
      this.dialogVisible = true
    }
  }
}
</script>

<style scoped>
.blogs-list-container {
  padding: 20px;
}
.blog-cards {
  margin-top: 20px;
}
.single-blog {
  margin-bottom: 20px;
  transition: all 0.3s;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.single-blog:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
.blog-header {
  padding: 15px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
  flex-shrink: 0;
}
.blog-info {
  margin-left: 10px;
}
.author-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}
.blog-date {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
.image {
  width: 100%;
  display: block;
  height: 200px;
  object-fit: cover;
  flex-shrink: 0;
}
.blog-content {
  padding: 15px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}
.blog-title {
  margin: 0 0 10px 0;
  font-size: 18px;
  font-weight: 500;
  color: #303133;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  overflow: hidden;
}
.blog-rating {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}
.price {
  color: #f56c6c;
  font-size: 16px;
  font-weight: 500;
}
.blog-tags {
  margin: 10px 0;
  flex-grow: 1;
}
.blog-tags .el-tag {
  margin-right: 8px;
  margin-bottom: 8px;
}
.blog-footer {
  margin-top: 15px;
  text-align: right;
  flex-shrink: 0;
}
.button {
  padding: 0;
  font-size: 14px;
}

.floating-button {
  position: fixed;
  right: 40px;
  bottom: 40px;
  width: 60px;
  height: 60px;
  font-size: 24px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  z-index: 1000;
}

/* 博客详情弹窗样式 */
.blog-dialog :deep(.el-dialog__body) {
  padding: 20px;
}

.blog-detail-content {
  padding: 20px;
}

.blog-detail-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.blog-detail-info {
  margin-left: 15px;
}

.blog-detail-author {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.blog-detail-date {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.blog-detail-image {
  margin-bottom: 20px;
}

.blog-detail-image img {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 8px;
}

.blog-detail-rating {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
}

.blog-detail-price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: 500;
}

.blog-detail-tags {
  margin-bottom: 20px;
}

.blog-detail-tags .el-tag {
  margin-right: 8px;
  margin-bottom: 8px;
}

.blog-detail-text {
  font-size: 16px;
  line-height: 1.6;
  color: #606266;
  white-space: pre-line;
}
</style>
