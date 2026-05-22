const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token')
    
    const baseUrl = 'http://192.168.43.98:8080/api'
    
    const header = {
      'Content-Type': 'application/json'
    }
    
    if (token) {
      header['token'] = token
    }
    
    wx.request({
      url: `${baseUrl}${options.url}`,
      method: options.method || 'GET',
      data: options.data,
      header: header,
      timeout: 30000,
      success: (res) => {
        console.log('响应:', res.statusCode, res.data)
        
        if (res.statusCode === 200) {
          if (res.data.code === 1 || res.data.code === 200) {
            resolve(res.data.data)
          } else if (res.data.code === 401) {
            wx.removeStorageSync('token')
            wx.removeStorageSync('userInfo')
            wx.reLaunch({ url: '/pages/login/login' })
            reject('请先登录')
          } else {
            reject(res.data.msg || '请求失败')
          }
        } else {
          reject(`HTTP错误: ${res.statusCode}`)
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        reject('网络错误')
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