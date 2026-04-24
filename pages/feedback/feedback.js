const { post } = require('../../utils/request')

Page({
  data: {
    type: 'bug',
    content: '',
    contact: '',
    canSubmit: false
  },

  onInputContent(e) {
    const content = e.detail.value
    this.setData({ content, canSubmit: content.trim().length > 0 })
  },

  onInputContact(e) {
    this.setData({ contact: e.detail.value })
  },

  selectType(e) {
    this.setData({ type: e.currentTarget.dataset.type })
  },

  submitFeedback() {
    if (!this.data.canSubmit) {
      wx.showToast({ title: '请填写反馈内容', icon: 'none' })
      return
    }

    wx.showLoading({ title: '提交中...' })

    post('/user/feedback', {
      type: this.data.type,
      content: this.data.content,
      contact: this.data.contact
    }).then(() => {
      wx.hideLoading()
      wx.showModal({
        title: '提交成功',
        content: '感谢您的反馈，我们会认真处理！',
        showCancel: false,
        success: () => {
          wx.navigateBack()
        }
      })
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({ title: '提交失败', icon: 'none' })
    })
  }
})