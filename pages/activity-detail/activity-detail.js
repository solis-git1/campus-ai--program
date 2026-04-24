const { get, post } = require('../../utils/request')

Page({
  data: {
    activityId: null,
    activity: null,
    isFavorite: false,
    isRegistered: false
  },

  onLoad(options) {
    let activityId = options.id || options.activityId
    const id = parseInt(activityId)
    
    if (isNaN(id) || id <= 0) {
      wx.showToast({ title: '活动ID无效', icon: 'none' })
      setTimeout(() => wx.navigateBack(), 1500)
      return
    }
    
    this.setData({ activityId: id })
    this.loadActivityDetail()
    this.checkFavoriteStatus()  // ✅ 单独检查收藏状态
    this.checkRegistrationStatus()  // ✅ 单独检查报名状态
  },

  loadActivityDetail() {
    wx.showLoading({ title: '加载中...' })
    
    get(`/user/activity/${this.data.activityId}`).then(data => {
      wx.hideLoading()
      
      let activity = data
      if (data && data.data) {
        activity = data.data
      }
      
      // 状态转换
      let status = activity.status
      if (status === '报名中') status = 'upcoming'
      if (status === '进行中') status = 'ongoing'
      if (status === '已结束') status = 'completed'
      activity.status = status
      
      this.setData({ activity: activity })
    }).catch(err => {
      wx.hideLoading()
      console.error('加载活动详情失败:', err)
      wx.showToast({ title: '加载失败', icon: 'none' })
    })
  },

  // ✅ 单独检查收藏状态
  checkFavoriteStatus() {
    const app = getApp()
    wx.request({
      url: `${app.globalData.baseUrl}/user/activity/check-favorite/${this.data.activityId}`,
      method: 'GET',
      header: { 'token': wx.getStorageSync('token') },
      success: (res) => {
        if (res.data.code === 1) {
          this.setData({ isFavorite: res.data.data })
        }
      }
    })
  },

  // ✅ 单独检查报名状态
  checkRegistrationStatus() {
    // 注意：后端可能需要一个检查是否报名的接口
    // 如果没有，可以从活动列表数据中获取，或者调用报名状态的接口
    const app = getApp()
    wx.request({
      url: `${app.globalData.baseUrl}/user/activity/check-register/${this.data.activityId}`,
      method: 'GET',
      header: { 'token': wx.getStorageSync('token') },
      success: (res) => {
        if (res.data.code === 1) {
          this.setData({ isRegistered: res.data.data })
        }
      },
      fail: () => {
        // 如果没有检查接口，可以从活动详情数据中获取
        this.setData({ isRegistered: false })
      }
    })
  },

  // 切换收藏
  toggleFavorite() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showModal({
        title: '提示',
        content: '请先登录',
        success: (res) => {
          if (res.confirm) wx.reLaunch({ url: '/pages/login/login' })
        }
      })
      return
    }
    
    const app = getApp()
    const activityId = this.data.activityId
    
    if (this.data.isFavorite) {
      // 取消收藏
      wx.request({
        url: `${app.globalData.baseUrl}/user/activity/favorite/${activityId}`,
        method: 'DELETE',
        header: { 'token': token },
        success: () => {
          this.setData({ isFavorite: false })
          wx.showToast({ title: '已取消收藏', icon: 'none' })
        },
        fail: (err) => {
          console.error('取消收藏失败:', err)
          wx.showToast({ title: '操作失败', icon: 'none' })
        }
      })
    } else {
      // 添加收藏
      wx.request({
        url: `${app.globalData.baseUrl}/user/activity/favorite/${activityId}`,
        method: 'POST',
        header: { 'token': token },
        success: () => {
          this.setData({ isFavorite: true })
          wx.showToast({ title: '收藏成功', icon: 'success' })
        },
        fail: (err) => {
          console.error('收藏失败:', err)
          wx.showToast({ title: '操作失败', icon: 'none' })
        }
      })
    }
  },

  // 活动报名
  registerActivity() {
    if (this.data.isRegistered) {
      wx.showToast({ title: '您已报名', icon: 'none' })
      return
    }
    
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showModal({
        title: '提示',
        content: '请先登录',
        success: (res) => {
          if (res.confirm) wx.reLaunch({ url: '/pages/login/login' })
        }
      })
      return
    }
    
    const activity = this.data.activity
    if (activity && activity.status !== 'upcoming') {
      wx.showToast({ title: '当前状态无法报名', icon: 'none' })
      return
    }
    
    wx.showModal({
      title: '确认报名',
      content: `确定要报名参加「${activity?.title}」吗？`,
      success: (res) => {
        if (res.confirm) {
          this.doRegister()
        }
      }
    })
  },

  // 执行报名
  doRegister() {
    wx.showLoading({ title: '报名中...' })
    
    const app = getApp()
    wx.request({
      url: `${app.globalData.baseUrl}/user/activity/register/${this.data.activityId}`,
      method: 'POST',
      header: { 'token': wx.getStorageSync('token') },
      success: (res) => {
        wx.hideLoading()
        if (res.data.code === 1) {
          this.setData({ 
            isRegistered: true,
            'activity.currentParticipants': (this.data.activity?.currentParticipants || 0) + 1
          })
          wx.showToast({ title: '报名成功', icon: 'success' })
        } else {
          wx.showToast({ title: res.data.msg || '报名失败', icon: 'none' })
        }
      },
      fail: (err) => {
        wx.hideLoading()
        console.error('报名失败:', err)
        wx.showToast({ title: '报名失败', icon: 'none' })
      }
    })
  },

  onShareAppMessage() {
    return {
      title: this.data.activity?.title || '校园活动',
      path: `/pages/activity-detail/activity-detail?id=${this.data.activityId}`
    }
  }
})