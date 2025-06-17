import http from '../service/Request'

export default {
    /** 提交窗口评价 */
    CreateStallReview(stallReview) {
        return http.post(`/api/reviews/stall`, stallReview)
    },
    /** 分页查询指定窗口的评价列表 */
    GetStallReviews(stallId, params) {
        return http.get(`/api/reviews/stall/${stallId}`, { params })
    }
}
