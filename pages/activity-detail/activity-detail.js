const { get, post } = require('../../utils/request')

Page({
  data: {
    activityId: null,
    activity: null,
    isFavorite: false,
    isRegistered: false
  },

  onLoad(options) {
    this.setData({ activityId: options.id })
    this.loadActivityDetail()
    this.checkFavorite()
    this.checkRegistration()
  },

  loadActivityDetail() {
    get(`/user/activity/${this.data.activityId}`).then(data => {
      this.setData({ activity: data })
    }).catch(() => {
      wx.showToast({ title: '加载失败', icon: 'none' })
    })
  },

  checkFavorite() {
    // 如果需要检查收藏状态，调用对应接口
    get('/user/activity/favorite/{activityId}').then(data => {
      this.setData({ isFavorite: data.isFavorite })
    })
  },

  checkRegistration() {
    // 如果需要检查报名状态，调用对应接口
     get('/user/activity/register/{activityId}').then(data => {
      this.setData({ isRegistered: data.isRegistered })
     })
  },

  toggleFavorite() {
    if (this.data.isFavorite) {
      this.setData({ isFavorite: false })
      wx.showToast({ title: '已取消收藏', icon: 'none' })
    } else {
      post(`/user/activity/favorite/{activityId}/${this.data.activityId}`).then(() => {
        this.setData({ isFavorite: true })
        wx.showToast({ title: '收藏成功', icon: 'success' })
      }).catch(() => {
        wx.showToast({ title: '操作失败', icon: 'none' })
      })
    }
  },

  registerActivity() {
    if (this.data.isRegistered) {
      wx.showToast({ title: '您已报名', icon: 'none' })
      return
    }
    
    wx.showModal({
      title: '确认报名',
      content: `确定要报名参加「${this.data.activity.title}」吗？`,
      success: (res) => {
        if (res.confirm) {
          post(`/user/activity/register{activityId}/${this.data.activityId}`).then(() => {
            this.setData({ isRegistered: true })
            this.setData({ 'activity.currentParticipants': this.data.activity.currentParticipants + 1 })
            wx.showToast({ title: '报名成功', icon: 'success' })
          }).catch(() => {
            wx.showToast({ title: '报名失败', icon: 'none' })
          })
        }
      }
    })
  }
})