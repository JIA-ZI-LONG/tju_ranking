import http from '../service/Request'

export default {
    /** 根据ID查询指定用户的公开信息 */
    GetUserById(id) {
        return http.get(`/api/users/${id}`)
    },
    /** 用户登录 */
    Login(model){
        return http.post(`/api/users/login`,model)
    },
    /** 用户退出登录 */
    Logout() {
        return http.post(`/api/users/logout`)
    },
    /** 获取当前登录用户的详细信息 */
    GetCurrentUser() {
        return http.get(`/api/users/me`)
    },
    /** 编辑个人资料 */
    UpdateProfile(model) {
        return http.put(`/api/users/me`, model)
    },
    /** 重置密码 */
    ResetPassword(model) {
        return http.post(`/api/users/password`, model)
    },
    /** 用户注册 */
    Register(model){
        return http.post(`/api/users/register`,model)
    },
    /** 用户签到 */
    SignIn() {
        return http.post(`/api/users/sign`)
    },
    /** 发送验证码 */
    SendVerificationCode(model) {
        return http.post(`/api/verifications/code`, model)
    },
    /** 获取图形验证码 */
    GetCaptcha() {
        return http.get(`/api/verifications/captcha`)
    }
}