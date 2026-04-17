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

  onTitleInput(e) {
    this.setData({ 'form.title': e.detail.value })
  },

  onDescInput(e) {
    this.setData({ 'form.description': e.detail.value })
  },

  // 获取待办列表（使用 /user/task/list）
  loadTodos() {
    const params = {
      page: 1,
      pageSize: 100
    }
    // 如果接口支持筛选，可以传递，否则前端筛选
    if (this.data.category) params.category = this.data.category
    
    get('/user/task/list', params).then(res => {
      const data = res.data || res || {}
      const list = data.list || data.records || []
      
      // 前端筛选状态
      let filteredList = list
      if (this.data.statusFilter === 'pending') {
        filteredList = list.filter(t => t.status !== 'completed')
      } else if (this.data.statusFilter === 'completed') {
        filteredList = list.filter(t => t.status === 'completed')
      }
      
      const pending = filteredList.filter(t => t.status !== 'completed')
      const completed = filteredList.filter(t => t.status === 'completed')
      
      this.setData({ 
        todos: pending, 
        archivedTodos: completed 
      })
    }).catch(err => {
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

  // 完成待办（使用 POST /user/task/finish/{taskId}）
  finishTodo(e) {
    const { id } = e.currentTarget.dataset
    post(`/user/task/finish/${id}`).then(() => {
      this.loadTodos()
      wx.showToast({ title: '已完成', icon: 'success' })
    }).catch(() => {
      wx.showToast({ title: '操作失败', icon: 'none' })
    })
  },

  // 恢复待办（没有恢复接口，改为重新创建？或使用更新接口）
  restoreTodo(e) {
    const id = e.currentTarget.dataset.id
    // 注意：文档中没有恢复接口，这里使用更新接口将状态改回 pending
    put('/user/task', {
      taskId: id,
      status: 'pending'  // 假设接口支持更新状态
    }).then(() => {
      this.loadTodos()
      wx.showToast({ title: '已恢复', icon: 'success' })
    }).catch(() => {
      wx.showToast({ title: '恢复失败', icon: 'none' })
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
        priority: 'medium',
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

  // 删除待办（注意：文档中没有删除接口，需要后端添加或使用其他方式）
  deleteTodo(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '确认删除',
      content: '删除后无法恢复',
      success: (res) => {
        if (res.confirm) {
          // 方案1：使用更新接口标记为删除状态
          put('/user/task', {
            taskId: id,
            status: 'deleted'
          }).then(() => {
            this.loadTodos()
            wx.showToast({ title: '删除成功', icon: 'success' })
          }).catch(() => {
            wx.showToast({ title: '删除失败', icon: 'none' })
          })
        
        }
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

  // 保存待办（新增用 POST /user/task，更新用 PUT /user/task）
  saveTodo() {
    if (!this.data.form.title || this.data.form.title.trim() === '') {
      wx.showToast({ title: '请输入标题', icon: 'none' })
      return
    }
    
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
        this.hideModal()
        this.loadTodos()
        wx.showToast({ title: '更新成功', icon: 'success' })
      }).catch((err) => {
        console.error('更新失败', err)
        wx.showToast({ title: '更新失败', icon: 'none' })
      })
    } else {
      post('/user/task', data).then(() => {
        this.hideModal()
        this.loadTodos()
        wx.showToast({ title: '添加成功', icon: 'success' })
      }).catch((err) => {
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