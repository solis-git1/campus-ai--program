const { get, post, del } = require('../../utils/request')

Page({
  data: {
    activityId: null,
    activity: null,
    isFavorite: false,
    isRegistered: false,
    loading: true
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
    this.loadUserStatusFromStorage()
  },

  // 获取活动详情
  loadActivityDetail() {
    wx.showLoading({ title: '加载中...' })
    
    get(`/user/activity/${this.data.activityId}`).then(data => {
      wx.hideLoading()
      
      let activity = data
      if (data && data.data) {
        activity = data.data
      }
      
      let status = activity.status
      let statusText = ''
      if (status === 'upcoming') {
        statusText = '报名中'
      } else if (status === 'ongoing') {
        statusText = '进行中'
      } else if (status === 'completed') {
        statusText = '已结束'
      }
      activity.statusText = statusText
      
      this.setData({ 
        activity: activity,
        loading: false
      })
    }).catch(err => {
      wx.hideLoading()
      console.error('加载活动详情失败:', err)
      wx.showToast({ title: '加载失败', icon: 'none' })
      this.setData({ loading: false })
    })
  },

  // 从本地存储读取用户状态
  loadUserStatusFromStorage() {
    const token = wx.getStorageSync('token')
    if (!token) {
      this.setData({ isFavorite: false, isRegistered: false })
      return
    }
    
    const favoriteList = wx.getStorageSync('favoriteActivities') || []
    const isFavorite = favoriteList.includes(this.data.activityId)
    
    const registeredList = wx.getStorageSync('registeredActivities') || []
    const isRegistered = registeredList.includes(this.data.activityId)
    
    this.setData({ isFavorite, isRegistered })
  },

  // 保存收藏状态
  saveFavoriteStatus(activityId, isFavorite) {
    let favoriteList = wx.getStorageSync('favoriteActivities') || []
    if (isFavorite) {
      if (!favoriteList.includes(activityId)) {
        favoriteList.push(activityId)
      }
    } else {
      favoriteList = favoriteList.filter(id => id !== activityId)
    }
    wx.setStorageSync('favoriteActivities', favoriteList)
  },

  // 保存报名状态
  saveRegisterStatus(activityId, isRegistered) {
    let registeredList = wx.getStorageSync('registeredActivities') || []
    if (isRegistered) {
      if (!registeredList.includes(activityId)) {
        registeredList.push(activityId)
      }
    } else {
      registeredList = registeredList.filter(id => id !== activityId)
    }
    wx.setStorageSync('registeredActivities', registeredList)
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
    
    const activityId = this.data.activityId
    const newFavoriteStatus = !this.data.isFavorite
    
    this.setData({ isFavorite: newFavoriteStatus })
    this.saveFavoriteStatus(activityId, newFavoriteStatus)
    
    if (newFavoriteStatus) {
      wx.showToast({ title: '收藏成功', icon: 'success' })
      post(`/user/activity/favorite/${activityId}`).catch(err => console.error('后端收藏失败:', err))
    } else {
      wx.showToast({ title: '已取消收藏', icon: 'success' })
      del(`/user/activity/favorite/${activityId}`).catch(err => console.error('后端取消收藏失败:', err))
    }
  },

  // 报名
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
    const activityId = this.data.activityId
    
    this.setData({ 
      isRegistered: true,
      'activity.currentParticipants': (this.data.activity?.currentParticipants || 0) + 1
    })
    this.saveRegisterStatus(activityId, true)
    wx.showToast({ title: '报名成功', icon: 'success' })
    
    post(`/user/activity/register/${activityId}`).catch(err => console.error('后端报名失败:', err))
  },

  // 取消报名（仅前端，后端无接口）
  cancelRegister() {
    wx.showModal({
      title: '确认取消',
      content: '确定要取消报名吗？',
      success: (res) => {
        if (res.confirm) {
          const activityId = this.data.activityId
          
          this.setData({ 
            isRegistered: false,
            'activity.currentParticipants': (this.data.activity?.currentParticipants || 0) - 1
          })
          this.saveRegisterStatus(activityId, false)
          wx.showToast({ title: '已取消报名', icon: 'success' })
          
          // 如果有取消报名接口，调用它
          // del(`/user/activity/register/${activityId}`).catch(err => console.error('后端取消报名失败:', err))
        }
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