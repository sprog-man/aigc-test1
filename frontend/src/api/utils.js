import axios from 'axios'
import ElementPlus from 'element-plus'
import 'nprogress/nprogress.css'
import router from '@/router'

const service = axios.create({
  baseURL: process.env.VUE_APP_API_URL || '',
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data

    // 如果返回的数据是Response格式
    if (res.code !== undefined) {
      if (res.code === 200) {
        return res
      } else {
        ElementPlus.ElMessage.error(res.message || '请求失败')
        return Promise.reject(res)
      }
    }

    return res
  },
  error => {
    console.log('err' + error)

    if (error.response) {
      switch (error.response.status) {
        case 401:
          ElementPlus.ElMessage.error('登录已过期，请重新登录')
          localStorage.removeItem('token')
          localStorage.removeItem('user')
          router.push('/login')
          break
        case 403:
          ElementPlus.ElMessage.error('没有权限访问')
          break
        case 404:
          ElementPlus.ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElementPlus.ElMessage.error('服务器内部错误')
          break
        default:
          ElementPlus.ElMessage.error(error.response.data.message || '请求失败')
      }
    } else if (error.request) {
      ElementPlus.ElMessage.error('网络错误，请检查网络连接')
    } else {
      ElementPlus.ElMessage.error('请求失败')
    }

    return Promise.reject(error)
  }
)

export default service