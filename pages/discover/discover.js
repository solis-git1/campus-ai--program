const { get, post } = require('../../utils/request')
const { formatDate } = require('../../utils/utils')

Page({
  data: {
    // Tab状态
    currentTab: 0,
    
    // 空教室筛选相关（匹配接口文档）
    selectedBuildingId: '',
    selectedBuildingName: '全部教学楼',
    selectedDate: '',
    selectedTimeSlot: '',
    selectedTimeText: '全天',
    selectedFloor: '',        // 楼层筛选
    minCapacity: '',          // 最小容量
    hasMedia: false,          // 是否有多媒体设备
    emptyClassrooms: [],
    buildings: [],
    
    // 高级筛选面板
    showAdvanced: false,
    
    // 活动筛选相关（匹配接口文档）
    keyword: '',
    activityType: '',
    activityStatus: '',       // upcoming/ongoing/completed
    activityLocation: '',
    activities: [],
    
    // 状态列表
    statusList: ['upcoming', 'ongoing', 'completed'],
    statusNameMap: {
      'upcoming': '报名中',
      'ongoing': '进行中', 
      'completed': '已结束'
    },
    
    // 活动类型列表（根据实际业务调整）
    activityTypes: [
      { value: 'lecture', label: '讲座' },
      { value: 'competition', label: '竞赛' },
      { value: 'social', label: '社团活动' },
      { value: 'volunteer', label: '志愿服务' }
    ],
    
    // 分页
    page: 1,
    hasMore: true,
    total: 0,
    loading: false
  },

  onLoad() {
    const today = formatDate(new Date())
    this.setData({ 
      selectedDate: today
    })
    this.loadBuildings()
    this.loadEmptyClassrooms()
    this.loadActivities()
  },

  // ==================== 建筑列表 ====================
  loadBuildings() {
    get('/user/classroom/list').then(data => {
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

  // ==================== 时间段映射 ====================
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

  // ==================== 空教室查询 ====================
  loadEmptyClassrooms() {
    this.setData({ loading: true })
    wx.showLoading({ title: '查询中...' })
    
    const { startTime, endTime } = this.getTimeRange(this.data.selectedTimeText)
    
    // 构建符合接口文档的参数
    const params = {
      date: this.data.selectedDate
    }
    
    // 只在有值时添加参数
    if (startTime) params.startTime = startTime
    if (endTime) params.endTime = endTime
    if (this.data.selectedBuildingId) params.buildingId = this.data.selectedBuildingId
    if (this.data.selectedFloor) params.floor = parseInt(this.data.selectedFloor)
    if (this.data.minCapacity) params.minCapacity = parseInt(this.data.minCapacity)
    if (this.data.hasMedia) params.hasMedia = 1
    
    get('/user/classroom/empty/filter', params).then(data => {
      this.setData({ 
        emptyClassrooms: data || [],
        loading: false
      })
      wx.hideLoading()
      
      if ((!data || data.length === 0)) {
        wx.showToast({ title: '暂无空教室', icon: 'none' })
      }
    }).catch(() => {
      wx.hideLoading()
      this.setData({ loading: false })
      wx.showToast({ title: '查询失败', icon: 'none' })
    })
  },

  // 显示/隐藏高级筛选
  showAdvancedFilter() {
    this.setData({ showAdvanced: !this.data.showAdvanced })
  },

  // 选择楼层
  selectFloor(e) {
    const floor = e.currentTarget.dataset.floor
    this.setData({ 
      selectedFloor: this.data.selectedFloor === floor ? '' : floor 
    })
  },

  // 选择最小容量
  selectMinCapacity(e) {
    const capacity = e.currentTarget.dataset.capacity
    this.setData({ 
      minCapacity: this.data.minCapacity === capacity ? '' : capacity 
    })
  },

  // 切换多媒体设备
  toggleHasMedia() {
    this.setData({ hasMedia: !this.data.hasMedia })
  },

  // 重置所有教室筛选条件
  resetClassroomFilters() {
    this.setData({
      selectedBuildingId: '',
      selectedBuildingName: '全部教学楼',
      selectedDate: formatDate(new Date()),
      selectedTimeText: '全天',
      selectedFloor: '',
      minCapacity: '',
      hasMedia: false,
      showAdvanced: false
    })
    this.loadEmptyClassrooms()
  },

  // 应用高级筛选
  applyAdvancedFilter() {
    this.setData({ showAdvanced: false })
    this.loadEmptyClassrooms()
  },

  // 选择教学楼
  showBuildingPicker() {
    const items = ['全部教学楼', ...this.data.buildings.map(b => b.name)]
    wx.showActionSheet({
      itemList: items,
      success: (res) => {
        const selectedName = items[res.tapIndex]
        const selectedBuilding = this.data.buildings.find(b => b.name === selectedName)
        this.setData({
          selectedBuildingName: selectedName,
          selectedBuildingId: selectedName === '全部教学楼' ? '' : (selectedBuilding?.id || '')
        })
        this.loadEmptyClassrooms()
      }
    })
  },

  // 选择日期
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

  // 选择时间段
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

  // ==================== 活动相关 ====================
  
  loadActivities(reset = true) {
    if (this.data.loading) return
    
    this.setData({ loading: true })
    
    if (reset) {
      this.setData({ page: 1, activities: [], hasMore: true })
    }
    
    // 构建符合接口文档的参数
    const params = {
      page: this.data.page,
      pageSize: 10
    }
    
    if (this.data.keyword) params.keyword = this.data.keyword
    if (this.data.activityType) params.type = this.data.activityType
    if (this.data.activityStatus) params.status = this.data.activityStatus
    if (this.data.activityLocation) params.location = this.data.activityLocation
    
    get('/user/activity/list', params).then(res => {
      const data = res.data || res
      const list = data.list || data.records || []
      const total = data.total || 0
      
      const newList = reset ? list : [...this.data.activities, ...list]
      this.setData({
        activities: newList,
        total: total,
        hasMore: newList.length < total,
        loading: false
      })
    }).catch(() => {
      console.error('获取活动列表失败')
      this.setData({ loading: false })
      wx.showToast({ title: '加载失败', icon: 'none' })
    })
  },

  // 搜索活动
  searchActivity(e) {
    if (this.searchTimer) clearTimeout(this.searchTimer)
    this.searchTimer = setTimeout(() => {
      this.setData({ keyword: e.detail.value })
      this.loadActivities(true)
    }, 500)
  },

  // 按类型筛选
  filterByType(e) {
    const type = e.currentTarget.dataset.type
    this.setData({ 
      activityType: this.data.activityType === type ? '' : type 
    })
    this.loadActivities(true)
  },

  // 按状态筛选
  filterByStatus(e) {
    const status = e.currentTarget.dataset.status
    this.setData({ 
      activityStatus: this.data.activityStatus === status ? '' : status 
    })
    this.loadActivities(true)
  },

  // 按地点筛选
  filterByLocation(e) {
    const location = e.detail.value
    this.setData({ activityLocation: location })
    this.loadActivities(true)
  },

  // 重置活动筛选
  resetActivityFilters() {
    this.setData({
      keyword: '',
      activityType: '',
      activityStatus: '',
      activityLocation: ''
    })
    this.loadActivities(true)
  },

  // 活动详情
  // pages/discover/discover.js
goToActivityDetail(e) {
  const id = e.currentTarget.dataset.id || e.currentTarget.dataset.activityid
  console.log('点击的活动ID:', id)
  
  if (!id) {
    wx.showToast({ title: '活动ID不存在', icon: 'none' })
    return
  }
  
  wx.navigateTo({
    url: `/pages/activity-detail/activity-detail?id=${id}`
  })
},

  // 加载更多
  loadMore() {
    if (this.data.hasMore && !this.data.loading) {
      this.setData({ page: this.data.page + 1 })
      this.loadActivities(false)
    }
  },

  // 切换Tab
  switchTab(e) {
    const index = parseInt(e.currentTarget.dataset.index)
    this.setData({ currentTab: index })
    
    if (index === 0 && this.data.emptyClassrooms.length === 0) {
      this.loadEmptyClassrooms()
    } else if (index === 1 && this.data.activities.length === 0) {
      this.loadActivities(true)
    }
  }
})