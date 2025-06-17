import http from '../service/Request'

export default {
    /** 查询单个菜品详情 */
    GetDishById(id) {
        return http.get(`/api/dishes/${id}`)
    },
    /** 分页搜索菜品 */
    SearchDishes(params) {
        return http.get(`/api/dishes/search`, { params })
    }
}
