const { post } = require('../../utils/request')

Page({
  data: {
    agreed: false,
    loading: false
  },

  onLoad() {
    // 检查是否已登录
    this.checkLoginStatus()
  },

  // 检查登录状态
  checkLoginStatus() {
    const token = wx.getStorageSync('token')
    if (token) {
      const { get } = require('../../utils/request')
      get('/user/user/info').then(() => {
        // token有效，跳转到首页
        wx.switchTab({
          url: '/pages/index/index'
        })
      }).catch(() => {
        // token无效，清除缓存
        wx.removeStorageSync('token')
        wx.removeStorageSync('userInfo')
      })
    }
  },

  // 同意协议
  toggleAgreement() {
    this.setData({ agreed: !this.data.agreed })
  },

  // 微信一键登录
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

    // 获取微信登录code
    wx.login({
      success: (res) => {
        if (res.code) {
          // 调用后端登录接口 POST /user/user/login
          post('/user/user/login', {
            code: res.code
          }).then(response => {
            wx.hideLoading()
            this.setData({ loading: false })
            
            // 兼容响应格式 {code, message, data}
            const data = response.data || response
            
            if (data.token) {
              // 保存token
              wx.setStorageSync('token', data.token)
              
              // 保存用户信息
              if (data.userInfo) {
                wx.setStorageSync('userInfo', data.userInfo)
              }
              
              // 更新全局数据
              const app = getApp()
              if (app.globalData) {
                app.globalData.token = data.token
                app.globalData.userInfo = data.userInfo
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
                title: data.message || '登录失败',
                icon: 'none'
              })
            }
          }).catch(err => {
            wx.hideLoading()
            this.setData({ loading: false })
            console.error('登录失败:', err)
            
            const message = err.message || err.errMsg || '登录失败，请重试'
            wx.showToast({
              title: message,
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

  // 查看用户协议
  viewUserAgreement() {
    wx.showModal({
      title: '用户协议',
      content: '这是用户协议内容，请仔细阅读...',
      showCancel: false
    })
  },

  // 查看隐私政策
  viewPrivacyPolicy() {
    wx.showModal({
      title: '隐私政策',
      content: '这是隐私政策内容，请仔细阅读...',
      showCancel: false
    })
  }
})