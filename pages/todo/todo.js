const { get, post, put, del } = require('../../utils/request')

Page({
  data: {
    todos: [],
    archivedTodos: [],
    showArchived: false,
    statusFilter: '',
    category: '',
    showModal: false,
    editMode: false,
    form: {
      taskId: null,
      title: '',
      description: '',
      deadlineDate: '',
      deadlineTime: '',
      priority: 'medium',
      category: '学习'
    }
  },

  onLoad(options) {
    this.loadTodos()
    if (options.addRoom) {
      this.setData({ showModal: true })
      this.setData({ 'form.title': `去 ${options.addRoom} 自习` })
    }
  },

  onShow() {
    this.loadTodos()
  },

  onTitleInput(e) {
    this.setData({ 'form.title': e.detail.value })
  },

  onDescInput(e) {
    this.setData({ 'form.description': e.detail.value })
  },

  // 获取待办列表
  loadTodos() {
    wx.showLoading({ title: '加载中...' })
    
    const params = { page: 1, pageSize: 100 }
    if (this.data.category) params.category = this.data.category
    
    get('/user/task/list', params).then(res => {
      wx.hideLoading()
      
      // 解析数据
      let list = []
      if (Array.isArray(res)) {
        list = res
      } else if (res && res.data && Array.isArray(res.data)) {
        list = res.data
      } else if (res && res.records && Array.isArray(res.records)) {
        list = res.records
      } else if (res && res.list && Array.isArray(res.list)) {
        list = res.list
      } else {
        list = res || []
      }
      
      // 前端筛选状态
      let filteredList = list
      if (this.data.statusFilter === 'pending') {
        filteredList = list.filter(t => t.status !== '已完成')
      } else if (this.data.statusFilter === 'completed') {
        filteredList = list.filter(t => t.status === '已完成')
      }
      
      const pending = filteredList.filter(t => t.status !== '已完成')
      const completed = filteredList.filter(t => t.status === '已完成')
      
      this.setData({ 
        todos: pending, 
        archivedTodos: completed 
      })
    }).catch(err => {
      wx.hideLoading()
      console.error('加载待办失败', err)
      wx.showToast({ title: '加载失败', icon: 'none' })
    })
  },

  filterByStatus(e) {
    const index = parseInt(e.detail.value)
    const statusMap = ['', 'pending', 'completed']
    this.setData({ statusFilter: statusMap[index] })
    this.loadTodos()
  },

  filterByCategory(e) {
    const category = e.currentTarget.dataset.cate
    this.setData({ category: category === 'all' ? '' : category })
    this.loadTodos()
  },

  // ✅ 完成待办（修复）
  finishTodo(e) {
    const { id } = e.currentTarget.dataset
    console.log('完成待办，taskId:', id)
    
    wx.showLoading({ title: '更新中...' })
    
    post(`/user/task/finish/${id}`).then(() => {
      wx.hideLoading()
      this.loadTodos()
      wx.showToast({ title: '已完成', icon: 'success' })
    }).catch(err => {
      wx.hideLoading()
      console.error('完成失败:', err)
      wx.showToast({ title: '操作失败', icon: 'none' })
    })
  },

  // ✅ 恢复待办（标记为未完成）
  restoreTodo(e) {
    const { id } = e.currentTarget.dataset
    console.log('恢复待办，taskId:', id)
    
    wx.showLoading({ title: '恢复中...' })
    
    // 使用更新接口将状态改回未完成
    put('/user/task', {
      taskId: id,
      status: '未完成'
    }).then(() => {
      wx.hideLoading()
      this.loadTodos()
      wx.showToast({ title: '已恢复', icon: 'success' })
    }).catch(err => {
      wx.hideLoading()
      console.error('恢复失败:', err)
      // 如果后端不支持，尝试用完成接口的反向？或者提示
      wx.showToast({ title: '恢复失败', icon: 'none' })
    })
  },

  // ✅ 删除待办（修复 - 使用 DELETE 请求）
  deleteTodo(e) {
    const { id } = e.currentTarget.dataset
    console.log('删除待办，taskId:', id)
    
    wx.showModal({
      title: '确认删除',
      content: '删除后无法恢复',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '删除中...' })
          
          // 使用 DELETE 请求
          del(`/user/task/${id}`).then(() => {
            wx.hideLoading()
            this.loadTodos()
            wx.showToast({ title: '删除成功', icon: 'success' })
          }).catch(err => {
            wx.hideLoading()
            console.error('删除失败:', err)
            
            // 如果 DELETE 不支持，尝试用更新接口标记删除
            put('/user/task', {
              taskId: id,
              status: '已删除'
            }).then(() => {
              this.loadTodos()
              wx.showToast({ title: '删除成功', icon: 'success' })
            }).catch(() => {
              wx.showToast({ title: '删除失败', icon: 'none' })
            })
          })
        }
      }
    })
  },

  showAddModal() {
    this.setData({
      showModal: true,
      editMode: false,
      form: {
        taskId: null,
        title: '',
        description: '',
        deadlineDate: '',
        deadlineTime: '',
        priority: '中',
        category: '学习'
      }
    })
  },

  editTodo(e) {
    const todo = e.currentTarget.dataset.todo
    const deadlineParts = todo.deadline ? todo.deadline.split(' ') : ['', '']
    this.setData({
      showModal: true,
      editMode: true,
      form: {
        taskId: todo.taskId,
        title: todo.title,
        description: todo.description || '',
        deadlineDate: deadlineParts[0] || '',
        deadlineTime: deadlineParts[1] || '',
        priority: todo.priority,
        category: todo.category
      }
    })
  },

  toggleArchived() {
    this.setData({ showArchived: !this.data.showArchived })
  },

  setDeadlineDate(e) {
    this.setData({ 'form.deadlineDate': e.detail.value })
  },

  setDeadlineTime(e) {
    this.setData({ 'form.deadlineTime': e.detail.value })
  },

  setPriority(e) {
    this.setData({ 'form.priority': e.currentTarget.dataset.prio })
  },

  setCategory(e) {
    const categories = ['学习', '生活', '工作']
    this.setData({ 'form.category': categories[e.detail.value] })
  },

  saveTodo() {
    if (!this.data.form.title || this.data.form.title.trim() === '') {
      wx.showToast({ title: '请输入标题', icon: 'none' })
      return
    }
    
    wx.showLoading({ title: '保存中...' })
    
    const deadline = `${this.data.form.deadlineDate} ${this.data.form.deadlineTime}`.trim()
    const data = {
      title: this.data.form.title,
      description: this.data.form.description,
      deadline: deadline === ' ' ? null : deadline,
      priority: this.data.form.priority,
      category: this.data.form.category
    }
    
    if (this.data.editMode) {
      data.taskId = this.data.form.taskId
      put('/user/task', data).then(() => {
        wx.hideLoading()
        this.hideModal()
        this.loadTodos()
        wx.showToast({ title: '更新成功', icon: 'success' })
      }).catch((err) => {
        wx.hideLoading()
        console.error('更新失败', err)
        wx.showToast({ title: '更新失败', icon: 'none' })
      })
    } else {
      post('/user/task', data).then(() => {
        wx.hideLoading()
        this.hideModal()
        this.loadTodos()
        wx.showToast({ title: '添加成功', icon: 'success' })
      }).catch((err) => {
        wx.hideLoading()
        console.error('添加失败', err)
        wx.showToast({ title: '添加失败', icon: 'none' })
      })
    }
  },

  hideModal() {
    this.setData({ showModal: false })
  },

  stopPropagation() {}
})