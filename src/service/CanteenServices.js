import http from '../service/Request'

export default {
    /** 查询所有食堂列表 */
    GetAllCanteens() {
        return http.get(`/api/canteens`)
    },
    /** 查询单个食堂详情 */
    GetCanteenById(id) {
        return http.get(`/api/canteens/${id}`)
    },
    /** 查询食堂下的所有窗口 */
    GetStallsByCanteenId(canteenId) {
        return http.get(`/api/stalls/canteen/${canteenId}`)
    }
}
