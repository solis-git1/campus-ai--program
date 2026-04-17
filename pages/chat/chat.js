const { post, get } = require('../../utils/request')

Page({
  data: {
    messages: [],
    inputText: '',
    isLoading: false,
    sessionId: null,
    scrollTop: 99999
  },

  onLoad() {
    this.loadHistory()
  },

  loadHistory() {
    get('/user/ai/history').then(data => {
      if (data && data.length > 0) {
        const messages = []
        data.forEach(session => {
          session.messages.forEach(msg => {
            messages.push({
              role: msg.role,
              content: msg.content,
              source: msg.source
            })
          })
        })
        this.setData({ messages: messages.slice(-50) })
      }
    }).catch(() => {
      // 接口失败时从本地缓存读取
      const history = wx.getStorageSync('chat_history') || []
      this.setData({ messages: history })
    })
  },

  saveHistory() {
    const history = this.data.messages.slice(-50)
    wx.setStorageSync('chat_history', history)
  },

  sendMessage() {
    const text = this.data.inputText.trim()
    if (!text) return
    
    const userMessage = { role: 'user', content: text }
    this.setData({
      messages: [...this.data.messages, userMessage],
      inputText: '',
      isLoading: true
    })
    this.saveHistory()
    this.scrollToBottom()
    
    // 调用AI对话接口
    post('/user/ai/chat', {
      message: text,
      sessionId: this.data.sessionId
    }).then(res => {
      const assistantMessage = {
        role: 'assistant',
        content: res.response,
        source: res.source
      }
      this.setData({
        messages: [...this.data.messages, assistantMessage],
        isLoading: false,
        sessionId: res.sessionId
      })
      this.saveHistory()
      this.scrollToBottom()
    }).catch(err => {
      console.error('AI对话失败', err)
      this.setData({ isLoading: false })
      const errorMessage = {
        role: 'assistant',
        content: '抱歉,AI服务暂时不可用,请稍后重试。'
      }
      this.setData({ messages: [...this.data.messages, errorMessage] })
    })
  },

  sendSuggestion(e) {
    const question = e.currentTarget.dataset.question
    this.setData({ inputText: question })
    this.sendMessage()
  },

  scrollToBottom() {
    setTimeout(() => {
      this.setData({ scrollTop: 99999 })
    }, 100)
  }
})