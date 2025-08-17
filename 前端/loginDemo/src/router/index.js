import { createRouter, createWebHistory } from "vue-router"

const routes = [
    {
        // 路由重定向
        path: "/",
        redirect: "/login"
    },
    {
        path: "/login",
        component: () => import("../views/login.vue"),
    },
    {
        path: "/home",
        component: () => import("../views/home.vue"),
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router