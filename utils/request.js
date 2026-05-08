const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token')
    console.log('=== request.js 读取 token ===')
    console.log('token 是否存在:', !!token)
    console.log('token 值:', token)

    const baseUrl = 'http://localhost:8080/api'
    
    
    const header = {
      'Content-Type': 'application/json'
    }
    
    // 后端使用 Constant.TOKEN_HEADER = "token"
    if (token) {
      header['token'] = token  // 注意：是小写的 token
      console.log('设置 token header:', token.substring(0, 30) + '...')
    }
    
    console.log('完整 header:', header)
    
    wx.request({
      url: `${baseUrl}${options.url}`,
      method: options.method || 'GET',
      data: options.data,
      header: header,
      timeout: 30000,
      success: (res) => {
        console.log('响应:', res.statusCode, res.data)
        
        if (res.statusCode === 200) {
          if (res.data.code === 1) {
            resolve(res.data.data)
          } else if (res.data.code === 401) {
            console.log('token 无效')
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