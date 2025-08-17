import axios from "axios";
import router from "../router";
import qs from "qs";
import { getSessionStorage } from "../utils/common";

const instance = axios.create({
    baseURL: 'http://localhost:8081'
});

// 请求拦截器
instance.interceptors.request.use(function (config) {
    // 检查token
    let token = getSessionStorage("token");
    if (token) {
        config.headers['token'] = token;
    }
    
    // 定义需要使用application/json格式的API路径
    let jsonApiPaths = ['/customernurseitem/addItemToCustomer'];
    
    // 对于POST请求，如果不在JSON API列表中，则使用x-www-form-urlencoded格式
    if (config.method == 'post' && jsonApiPaths.includes(config.url) < 0) {
        config.data = qs.stringify(config.data);
        config.headers['Content-Type'] = 'application/x-www-form-urlencoded';
    }
    
    return config;
}, function (error) {
    // 请求错误处理
    return Promise.reject(error);
});

// 响应拦截器
instance.interceptors.response.use(function (response) {
    // token出现异常，返回登录页面
    if (!response.data.flag && response.data.data == "token_error") {
        sessionStorage.removeItem("token");
        router.push('/login');
    }
    return response.data;
}, function (error) {
    return Promise.reject(error);
});

export default instance;