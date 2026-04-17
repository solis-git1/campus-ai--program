App({
  globalData: {
    userInfo: null,
    token: '',
    baseUrl: 'https://api.campus.example.com' // 替换为实际后端地址
  },

  onLaunch() {
    // 检查登录状态
    const token = wx.getStorageSync('token')
    if (token) {
      this.globalData.token = token
      this.getUserInfo()
    }
  },

  getUserInfo() {
    wx.request({
      url: `${this.globalData.baseUrl}/user/user/info`,
      method: 'GET',
      header: {
        'Authorization': `Bearer ${this.globalData.token}`
      },
      success: (res) => {
        if (res.data.code === 200) {
          this.globalData.userInfo = res.data.data
        }
      }
    })
  },

  login(code) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: `${this.globalData.baseUrl}/user/user/login`,
        method: 'POST',
        data: { code },
        success: (res) => {
          if (res.data.code === 200) {
            this.globalData.token = res.data.data.token
            this.globalData.userInfo = res.data.data
            wx.setStorageSync('token', res.data.data.token)
            resolve(res.data.data)
          } else {
            reject(res.data.message)
          }
        },
        fail: reject
      })
    })
  },

  logout() {
    this.globalData.token = ''
    this.globalData.userInfo = null
    wx.removeStorageSync('token')
    wx.reLaunch({ url: '/pages/login/login' })
  }
})