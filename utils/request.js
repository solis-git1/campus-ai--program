const app = getApp()

const request = (options) => {
  return new Promise((resolve, reject) => {
    const header = {
      'Content-Type': 'application/json',
      ...options.header
    }
    
    // 从 app.globalData 获取 token
    const token = app.globalData?.token || wx.getStorageSync('token')
    if (token) {
      header['Authorization'] = `Bearer ${token}`
    }
    
    wx.request({
      url: `${app.globalData.baseUrl}${options.url}`,
      method: options.method || 'GET',
      data: options.data,
      header,
      success: (res) => {
        if (res.data.code === 200) {
          resolve(res.data.data)
        } else if (res.data.code === 401) {
          // token过期，清除登录状态
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          wx.reLaunch({ url: '/pages/login/login' })
          reject('登录已过期，请重新登录')
        } else {
          reject(res.data.message || '请求失败')
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        reject('网络错误，请稍后重试')
      }
    })
  })
}

module.exports = {
  get: (url, data) => request({ url, method: 'GET', data }),
  post: (url, data) => request({ url, method: 'POST', data }),
  put: (url, data) => request({ url, method: 'PUT', data }),
  del: (url, data) => request({ url, method: 'DELETE', data })
}