const app = getApp()

const request = (options) => {
  return new Promise((resolve, reject) => {
    const header = {
      'Content-Type': 'application/json',
      ...options.header
    }
    
    if (app.globalData.token) {
      header['Authorization'] = `Bearer ${app.globalData.token}`
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
          // token过期，重新登录
          app.logout()
          reject('登录已过期，请重新登录')
        } else {
          reject(res.data.message || '请求失败')
        }
      },
      fail: reject
    })
  })
}

module.exports = {
  get: (url, data) => request({ url, method: 'GET', data }),
  post: (url, data) => request({ url, method: 'POST', data }),
  put: (url, data) => request({ url, method: 'PUT', data }),
  del: (url, data) => request({ url, method: 'DELETE', data })
}