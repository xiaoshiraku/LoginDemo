import http from '../request/request.js'

export const login= (data) => {
  return http.post(`/auth/login`, data)
}


export const sendCode = (data) => {
    return http.post(`/auth/send-code`, data)
}