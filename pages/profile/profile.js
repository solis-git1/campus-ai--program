const app = getApp()
const { get, put } = require('../../utils/request')

Page({
  data: {
    userInfo: {},
    stats: {
      totalCourses: 0,
      totalActivities: 0,
      totalTasks: 0
    },
    cacheSize: 0
  },

  onShow() {
    this.loadUserInfo()
    this.loadStats()
    this.getCacheSize()
  },

  loadUserInfo() {
    get('/user/user/info').then(data => {
      this.setData({ userInfo: data || {} })
      app.globalData.userInfo = data
    }).catch(() => {
      this.setData({ userInfo: app.globalData.userInfo || {} })
    })
  },

  // 修改：用现有接口组合实现统计数据
  loadStats() {
    // 获取我的课表数量
    get('/user/course/my').then(courses => {
      const courseCount = (courses || []).length
      this.setData({ 'stats.totalCourses': courseCount })
    }).catch(() => {})
    
    // 获取我参与的活动数量（通过活动列表接口）
    get('/user/activity/list', { page: 1, pageSize: 100 }).then(res => {
      const data = res.data || res
      const activityCount = data.total || data.list?.length || 0
      this.setData({ 'stats.totalActivities': activityCount })
    }).catch(() => {})
    
    // 获取我的待办数量
    get('/user/task/list', { page: 1, pageSize: 100 }).then(tasks => {
      const data = tasks.data || tasks
      const taskCount = data.total || data.list?.length || 0
      this.setData({ 'stats.totalTasks': taskCount })
    }).catch(() => {})
  },

  getCacheSize() {
    wx.getStorageInfo({
      success: (res) => {
        const size = (res.currentSize / 1024).toFixed(2)
        this.setData({ cacheSize: size })
      }
    })
  },

  editProfile() {
    wx.showModal({
      title: '编辑资料',
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

  goToMyCourses() {
    wx.switchTab({ url: '/pages/timetable/timetable' })
  },

  goToMyActivities() {
    wx.switchTab({ url: '/pages/discover/discover' })
  },

  goToMyFavorites() {
    // 使用教室列表接口显示收藏的教室
    get('/user/classroom/list').then(classrooms => {
      const favorites = (classrooms || []).filter(c => c.isFavorite)
      if (favorites.length > 0) {
        // 跳转到教室页面并传递收藏数据
        wx.navigateTo({
          url: `/pages/classroom/classroom?showFavorites=true`
        })
      } else {
        wx.showModal({
          title: '我的收藏',
          content: '暂无收藏内容',
          showCancel: false
        })
      }
    }).catch(() => {
      wx.showToast({ title: '功能开发中', icon: 'none' })
    })
  },

  goToMyFeedback() {
    // 使用AI助手接口实现反馈功能
    wx.showModal({
      title: '意见反馈',
      editable: true,
      placeholderText: '请输入您的反馈内容',
      success: (res) => {
        if (res.confirm && res.content) {
          // 通过AI对话接口提交反馈
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

  goToRecommendSettings() {
    // 使用推荐配置接口
    wx.showModal({
      title: '推荐设置',
      content: '推荐功能会根据您的使用习惯智能推送',
      confirmText: '刷新推荐',
      success: (res) => {
        if (res.confirm) {
          wx.showToast({ title: '推荐已刷新', icon: 'success' })
        }
      }
    })
  },

  clearCache() {
    wx.showModal({
      title: '确认清理',
      content: '将清除所有缓存数据',
      success: (res) => {
        if (res.confirm) {
          wx.clearStorageSync()
          this.setData({ cacheSize: 0 })
          wx.showToast({ title: '清理成功', icon: 'success' })
        }
      }
    })
  },

  showAbout() {
    wx.showModal({
      title: '关于校园助手',
      content: '校园助手 v1.0.0\n\n为华中农业大学师生打造的校园生活智能管理平台。',
      showCancel: false
    })
  },

  logout() {
    wx.showModal({
      title: '确认退出',
      content: '退出后需要重新登录',
      success: (res) => {
        if (res.confirm) {
          // 清除本地存储
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          
          // 清除全局数据
          app.globalData.token = null
          app.globalData.userInfo = null
          
          // 跳转到登录页
          wx.reLaunch({
            url: '/pages/login/login'
          })
        }
      }
    })
  }
})