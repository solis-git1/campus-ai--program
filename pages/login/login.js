const { post } = require('../../utils/request')

Page({
  data: {
    agreed: false,
    loading: false
  },

  onLoad() {
    this.checkLoginStatus()
  },

  checkLoginStatus() {
    const token = wx.getStorageSync('token')
    if (token) {
      const { get } = require('../../utils/request')
      get('/user/user/info').then(() => {
        wx.switchTab({
          url: '/pages/index/index'
        })
      }).catch(() => {
        wx.removeStorageSync('token')
        wx.removeStorageSync('userInfo')
      })
    }
  },

  toggleAgreement() {
    this.setData({ agreed: !this.data.agreed })
  },

  handleWechatLogin() {
    if (!this.data.agreed) {
      wx.showToast({
        title: '请先阅读并同意协议',
        icon: 'none'
      })
      return
    }

    if (this.data.loading) return

    this.setData({ loading: true })
    wx.showLoading({ title: '登录中...' })

    wx.login({
      success: (res) => {
        if (res.code) {
          // 使用 post 方法（会通过 request.js 处理）
          post('/user/user/login', { code: res.code })
            .then(response => {
              console.log('登录响应:', response)
              wx.hideLoading()
              this.setData({ loading: false })
              
              if (response && response.token) {
                // 保存 token
                wx.setStorageSync('token', response.token)
                
                const userInfo = {
                  userId: response.userId,
                  nickname: response.nickname,
                  avatar: response.avatar
                }
                wx.setStorageSync('userInfo', userInfo)
                
                const app = getApp()
                if (app.globalData) {
                  app.globalData.token = response.token
                  app.globalData.userInfo = userInfo
                }
                
                wx.showToast({
                  title: '登录成功',
                  icon: 'success'
                })
                
                setTimeout(() => {
                  wx.switchTab({
                    url: '/pages/index/index'
                  })
                }, 1000)
              } else {
                wx.showToast({
                  title: response?.msg || '登录失败',
                  icon: 'none'
                })
              }
            })
            .catch(err => {
              wx.hideLoading()
              this.setData({ loading: false })
              console.error('登录失败:', err)
              wx.showToast({
                title: err || '登录失败，请重试',
                icon: 'none'
              })
            })
        } else {
          this.setData({ loading: false })
          wx.hideLoading()
          wx.showToast({
            title: '获取登录凭证失败',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        this.setData({ loading: false })
        wx.hideLoading()
        console.error('wx.login失败:', err)
        wx.showToast({
          title: '网络异常，请重试',
          icon: 'none'
        })
      }
    })
  },

  viewUserAgreement() {
    wx.showModal({
      title: '用户协议',
      content: '这是用户协议内容，请仔细阅读...',
      showCancel: false
    })
  },

  viewPrivacyPolicy() {
    wx.showModal({
      title: '隐私政策',
      content: '这是隐私政策内容，请仔细阅读...',
      showCancel: false
    })
  }
})