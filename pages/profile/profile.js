const app = getApp()
const { get, put } = require('../../utils/request')

Page({
  data: {
    userInfo: {}
  },

  onShow() {
    this.loadUserInfo()
  },

  loadUserInfo() {
    const token = wx.getStorageSync('token')
    if (!token) {
      this.setData({ userInfo: { nickname: '点击登录', username: '未登录' } })
      return
    }
    
    get('/user/user/info').then(data => {
      this.setData({ userInfo: data || {} })
      app.globalData.userInfo = data
    }).catch(() => {
      this.setData({ userInfo: app.globalData.userInfo || { nickname: '用户', username: '未绑定' } })
    })
  },

  editProfile() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.navigateTo({ url: '/pages/login/login' })
      return
    }
    
    wx.showModal({
      title: '编辑昵称',
      editable: true,
      placeholderText: '请输入昵称',
      success: (res) => {
        if (res.confirm && res.content) {
          put('/user/user', { nickname: res.content }).then(() => {
            this.loadUserInfo()
            wx.showToast({ title: '保存成功', icon: 'success' })
          }).catch(() => {
            wx.showToast({ title: '保存失败', icon: 'none' })
          })
        }
      }
    })
  },

  // 账号绑定
  goToAccountBind() {
    wx.showModal({
      title: '账号绑定',
      content: '绑定学号后可同步课表信息',
      editable: true,
      placeholderText: '请输入学号',
      success: (res) => {
        if (res.confirm && res.content) {
          put('/user/user', { username: res.content }).then(() => {
            this.loadUserInfo()
            wx.showToast({ title: '绑定成功', icon: 'success' })
          }).catch(() => {
            wx.showToast({ title: '绑定失败', icon: 'none' })
          })
        }
      }
    })
  },

  // 意见反馈
  goToFeedback() {
    wx.showModal({
      title: '意见反馈',
      editable: true,
      placeholderText: '请输入您的反馈内容',
      success: (res) => {
        if (res.confirm && res.content) {
          const { post } = require('../../utils/request')
          post('/user/ai/chat', {
            message: `【用户反馈】${res.content}`,
            sessionId: 'feedback'
          }).then(() => {
            wx.showToast({ title: '反馈已提交', icon: 'success' })
          }).catch(() => {
            wx.showToast({ title: '提交失败', icon: 'none' })
          })
        }
      }
    })
  },

  // 推荐给朋友
  shareToFriend() {
    wx.showShareMenu({
      withShareTicket: true,
      menus: ['shareAppMessage']
    })
  },

  onShareAppMessage() {
    return {
      title: '华农人的校园助手',
      path: '/pages/login/login',
      imageUrl: ''
    }
  },



  // 关于
  showAbout() {
    wx.showModal({
      title: '关于校园智能助手',
      content: '校园智能助手 v1.0.0\n\n为华中农业大学师生打造的校园生活智能管理平台。\n\n功能：\n• 空教室查询\n• 校园活动\n• 课表管理\n• AI智能助手',
      showCancel: false
    })
  },

  // 跳转课表
  goToTimetable() {
    wx.switchTab({ url: '/pages/timetable/timetable' })
  },

  // 跳转发现（导航）
  goToDiscover() {
    wx.switchTab({ url: '/pages/discover/discover' })
  },

  logout() {
    wx.showModal({
      title: '确认退出',
      content: '退出后需要重新登录',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          app.globalData.token = null
          app.globalData.userInfo = null
          this.setData({ userInfo: { nickname: '点击登录', username: '未登录' } })
          wx.reLaunch({ url: '/pages/login/login' })
        }
      }
    })
  }
})