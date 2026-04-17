const { get, post } = require('../../utils/request')
const { formatDate } = require('../../utils/util')

Page({
  data: {
    currentTab: 0,
    selectedBuildingId: '',      // 改为存储建筑ID
    selectedBuildingName: '全部', // 存储建筑名称用于显示
    selectedDate: '',
    selectedTimeSlot: '',         // 存储时间段标识
    selectedTimeText: '全天',     // 显示用的时间段文本
    emptyClassrooms: [],
    buildings: [],                // 存储建筑列表 {id, name}
    keyword: '',
    activityType: '',
    activities: [],
    page: 1,
    hasMore: true,
    total: 0
  },

  onLoad() {
    this.setData({ selectedDate: formatDate(new Date()) })
    this.loadBuildings()      // 通过 list 接口获取建筑
    this.loadEmptyClassrooms()
    this.loadActivities()
  },

  // 通过 /user/classroom/list 接口获取教学楼列表
  loadBuildings() {
    get('/user/classroom/list').then(data => {
      // 从教室列表中提取不重复的建筑信息
      const buildingMap = new Map()
      if (data && Array.isArray(data)) {
        data.forEach(classroom => {
          if (classroom.buildingId && !buildingMap.has(classroom.buildingId)) {
            buildingMap.set(classroom.buildingId, {
              id: classroom.buildingId,
              name: classroom.buildingName || `教学楼${classroom.buildingId}`
            })
          }
        })
      }
      const buildings = Array.from(buildingMap.values())
      this.setData({ buildings })
    }).catch(() => {
      console.error('获取建筑列表失败')
    })
  },

  // 根据时间段获取起止时间
  getTimeRange(timeText) {
    const timeMap = {
      '全天': { startTime: '', endTime: '' },
      '1-2节': { startTime: '08:00', endTime: '09:40' },
      '3-4节': { startTime: '09:50', endTime: '12:15' },
      '5-6节': { startTime: '13:30', endTime: '15:10' },
      '7-8节': { startTime: '15:20', endTime: '17:00' },
      '9-10节': { startTime: '18:30', endTime: '20:10' }
    }
    return timeMap[timeText] || { startTime: '', endTime: '' }
  },

  // 查询空教室（使用 /user/classroom/empty/filter）
  loadEmptyClassrooms() {
    wx.showLoading({ title: '查询中...' })
    
    const { startTime, endTime } = this.getTimeRange(this.data.selectedTimeText)
    
    const params = {
      date: this.data.selectedDate,
      buildingId: this.data.selectedBuildingId || undefined,
      startTime: startTime || undefined,
      endTime: endTime || undefined
    }
    
    // 移除 undefined 的参数
    Object.keys(params).forEach(key => {
      if (params[key] === undefined || params[key] === '') {
        delete params[key]
      }
    })
    
    get('/user/classroom/empty/filter', params).then(data => {
      this.setData({ emptyClassrooms: data || [] })
      wx.hideLoading()
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({ title: '查询失败', icon: 'none' })
    })
  },

  // 获取活动列表（使用 /user/activity/list）
  loadActivities(reset = true) {
    if (reset) {
      this.setData({ page: 1, activities: [], hasMore: true })
    }
    
    const params = {
      page: this.data.page,
      pageSize: 10
    }
    
    if (this.data.keyword) params.keyword = this.data.keyword
    if (this.data.activityType) params.type = this.data.activityType
    
    get('/user/activity/list', params).then(res => {
      // 根据文档通用响应格式 {code, message, data}
      const data = res.data || res
      const list = data.list || data.records || []
      const total = data.total || 0
      
      const newList = reset ? list : [...this.data.activities, ...list]
      this.setData({
        activities: newList,
        total: total,
        hasMore: newList.length < total
      })
    }).catch(() => {
      console.error('获取活动列表失败')
    })
  },

  switchTab(e) {
    const index = parseInt(e.currentTarget.dataset.index)
    this.setData({ currentTab: index })
    if (index === 0 && this.data.emptyClassrooms.length === 0) {
      this.loadEmptyClassrooms()
    }
  },

  // 建筑选择器（使用 actionSheet）
  showBuildingPicker() {
    const items = ['全部', ...this.data.buildings.map(b => b.name)]
    wx.showActionSheet({
      itemList: items,
      success: (res) => {
        const selectedName = items[res.tapIndex]
        const selectedBuilding = this.data.buildings.find(b => b.name === selectedName)
        this.setData({
          selectedBuildingName: selectedName,
          selectedBuildingId: selectedName === '全部' ? '' : (selectedBuilding?.id || '')
        })
        this.loadEmptyClassrooms()
      }
    })
  },

  showDatePicker() {
    wx.showModal({
      title: '选择日期',
      editable: true,
      placeholderText: '请输入日期 YYYY-MM-DD',
      success: (res) => {
        if (res.confirm && res.content) {
          this.setData({ selectedDate: res.content })
          this.loadEmptyClassrooms()
        }
      }
    })
  },

  showTimePicker() {
    const items = ['全天', '1-2节', '3-4节', '5-6节', '7-8节', '9-10节']
    wx.showActionSheet({
      itemList: items,
      success: (res) => {
        this.setData({ selectedTimeText: items[res.tapIndex] })
        this.loadEmptyClassrooms()
      }
    })
  },

  // 收藏教室
  toggleFavorite(e) {
    const classroomId = e.currentTarget.dataset.id
    wx.showLoading({ title: '收藏中...' })
    post(`/user/classroom/favorite/${classroomId}`).then(() => {
      wx.hideLoading()
      wx.showToast({ title: '已收藏', icon: 'success' })
      this.loadEmptyClassrooms()
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({ title: '操作失败', icon: 'none' })
    })
  },

  // 添加到待办
  addToTodo(e) {
    const room = e.currentTarget.dataset.room
    wx.navigateTo({
      url: `/pages/todo/todo?addRoom=${room.buildingName} ${room.roomNumber}`
    })
  },

  searchActivity(e) {
    this.setData({ keyword: e.detail.value })
    this.loadActivities(true)
  },

  filterByType(e) {
    const type = e.currentTarget.dataset.type
    this.setData({ activityType: type })
    this.loadActivities(true)
  },

  goToActivityDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/activity-detail/activity-detail?id=${id}` })
  },

  loadMore() {
    if (this.data.hasMore && !this.data.loading) {
      this.setData({ page: this.data.page + 1 })
      this.loadActivities(false)
    }
  }
})