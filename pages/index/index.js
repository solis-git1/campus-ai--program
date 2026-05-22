const { get, post } = require('../../utils/request')  // ✅ 添加 post 导入

Page({
  data: {
    recommendations: [],
    hotActivities: []
  },

  onShow() {
    const token = wx.getStorageSync('token')
    console.log('index.js - token:', token)
    
    if (!token) {
      console.error('没有 token，跳转登录')
      wx.reLaunch({ url: '/pages/login/login' })
      return
    }
    this.loadRecommendations()
    this.loadHotActivities()
  },

  // 获取推荐列表
  loadRecommendations() {
    get('/user/recommendation/my').then(data => {
      // 兼容响应格式 {code, message, data}
      const list = data.data || data || []
      this.setData({ recommendations: list })
    }).catch(err => {
      console.error('加载推荐失败', err)
    })
  },

  // 获取热门活动（去掉 limit 参数）
  loadHotActivities() {
    get('/user/activity/hot').then(data => {
      // 兼容响应格式
      const list = data.data || data || []
      // 如果需要限制数量，在前端处理
      this.setData({ hotActivities: list.slice(0, 5) })
    }).catch(err => {
      console.error('加载热门活动失败', err)
    })
  },

  refreshRecommend() {
    this.loadRecommendations()
    wx.showToast({ title: '已刷新', icon: 'success', duration: 1000 })
  },

  goToChat() {
    wx.navigateTo({ url: '/pages/chat/chat' })
  },

  goToTimetable() {
    wx.switchTab({ url: '/pages/timetable/timetable' })
  },

  goToClassroom() {
    wx.navigateTo({ url: '/pages/classroom-filter/classroom-filter' })
  },

  goToActivity() {
    wx.switchTab({ url: '/pages/discover/discover' })
  },

  goToTodo() {
    wx.switchTab({ url: '/pages/todo/todo' })
  },

  goToActivityDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/activity-detail/activity-detail?id=${id}` })
  },

  // 点击推荐
  handleRecommend(e) {
    const item = e.currentTarget.dataset.item
    if (item.type === 'activity') {
      wx.navigateTo({ url: `/pages/activity-detail/activity-detail?id=${item.contentId}` })
    } else if (item.type === 'course') {
      wx.switchTab({ url: '/pages/timetable/timetable' })
    } else if (item.type === 'classroom') {
      wx.navigateTo({ url: '/pages/classroom-filter/classroom-filter' })
    }
    
    // 标记推荐已点击（需要确保 recId 存在且 post 已导入）
    if (item.recId) {
      post(`/user/recommendation/click/${item.recId}`).catch(() => {
        console.error('标记推荐点击失败')
      })
    }
  }
})