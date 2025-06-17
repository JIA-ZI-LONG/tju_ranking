import http from '../service/Request'

export default {
    /**检查指定名称是否使用 */
    IsNameUsed(name) {
        return http.get(`/api/users/isNameUsed/${name}`)
    },
    /** 注册*/
    Register(model){
        return http.post(`/api/users/register`,model)
    },
    /** 登录 */
    Login(model){
        return http.post(`/api/users/login`,model)
    },
    /** 重置密码 */
    ResetPassword(model) {
        return http.post(`/api/users/password`, model)
    },
    /** 发送验证码 */
    SendVerificationCode(model) {
        return http.post(`/api/verifications/code`, model)
    }
}