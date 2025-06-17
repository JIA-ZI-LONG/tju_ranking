import http from '../service/Request'

export default {
    /** 发布新博客 */
    CreateBlog(blogDTO) {
        return http.post(`/api/blogs`, blogDTO)
    },
    /** 根据ID查询博客详情 */
    GetBlogById(id) {
        return http.get(`/api/blogs/${id}`)
    },
    /** 分页查询热门博客 */
    GetHotBlogs(current = 1) {
        return http.get(`/api/blogs/hot`, { params: { current } })
    },
    /** 点赞或取消点赞博客 */
    LikeBlog(id) {
        return http.put(`/api/blogs/like/${id}`)
    },
    /** 查询为博客点赞的用户列表 */
    GetBlogLikes(id) {
        return http.get(`/api/blogs/likes/${id}`)
    }
}
