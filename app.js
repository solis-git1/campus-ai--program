App({
  globalData: {
    userInfo: null,
    token: '',
    baseUrl: 'http://localhost:8080/api' // 改成你的后端地址
  },

  onLaunch() {
    const token = wx.getStorageSync('token')
    const userInfo = wx.getStorageSync('userInfo')
    if (token) {
      this.globalData.token = token
      this.globalData.userInfo = userInfo
    }
  },

  // 微信登录（调用真实后端）
  wxLogin(code) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: `${this.globalData.baseUrl}/user/user/login`,
        method: 'POST',
        data: { code },
        success: (res) => {
          if (res.data.code === 200) {
            const data = res.data.data
            this.globalData.token = data.token
            this.globalData.userInfo = {
              userId: data.userId,
              nickname: data.nickname,
              avatar: data.avatar
            }
            wx.setStorageSync('token', data.token)
            wx.setStorageSync('userInfo', this.globalData.userInfo)
            resolve(data)
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
    wx.removeStorageSync('userInfo')
    wx.reLaunch({ url: '/pages/login/login' })
  }
})