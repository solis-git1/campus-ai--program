const { get, post, del } = require('../../utils/request')
const { formatDate } = require('../../utils/utils')

Page({
  data: {
    // Tab状态
    currentTab: 0,
    
    // ========== 空教室筛选相关 ==========
    // 教学楼（固定4个）
    buildings: [
      { id: 1, name: '一教' },
      { id: 2, name: '二教' },
      { id: 3, name: '三教' },
      { id: 4, name: '四教' }
    ],
    selectedBuildingId: '',
    selectedBuildingName: '全部教学楼',
    
    // 楼层（最高5层）
    floors: ['全部楼层', '1楼', '2楼', '3楼', '4楼', '5楼'],
    selectedFloor: '',
    selectedFloorText: '全部楼层',
    
    // 日期
    selectedDate: '',
    dateList: [],
    
    // 时间段
    selectedTimeText: '全天',
    
    // 教室数据
    emptyClassrooms: [],
    allClassrooms: [],
    
    // 高级筛选
    showAdvanced: false,
    seatCapacity: '',
    classroomType: '',
    
    // 加载状态
    loading: false,
    
    // ========== 校园活动相关 ==========
    keyword: '',
    activityType: '',
    activityStatus: '',
    activityLocation: '',
    activities: [],
    selectedActivityDate: '',
    
    statusList: ['upcoming', 'ongoing', 'completed'],
    statusNameMap: {
      'upcoming': '报名中',
      'ongoing': '进行中', 
      'completed': '已结束'
    },
    
    activityTypes: [
      { value: 'club', label: '社团活动' },
      { value: 'volunteer', label: '志愿活动' },
      { value: 'lecture', label: '讲座' },
      { value: 'sports', label: '体育赛事' }
    ],
    
    page: 1,
    hasMore: true,
    total: 0
  },

  onLoad() {
    const today = formatDate(new Date())
    this.setData({ selectedDate: today })
    this.initDateList()
    this.loadClassrooms()
    this.loadEmptyClassrooms()
    this.loadActivities()
  },

  // ==================== 日期相关 ====================
  initDateList() {
    const dateList = []
    const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
    const today = new Date()
    
    for (let i = -3; i <= 3; i++) {
      const date = new Date(today)
      date.setDate(today.getDate() + i)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      
      dateList.push({
        date: `${year}-${month}-${day}`,
        weekday: weekdays[date.getDay()],
        day: String(date.getDate()),
        isSelected: i === 0
      })
    }
    this.setData({ dateList })
  },

  selectDate(e) {
    const { date, index } = e.currentTarget.dataset
    const dateList = this.data.dateList.map((item, i) => {
      item.isSelected = i === index
      return item
    })
    this.setData({ selectedDate: date, dateList })
    this.loadEmptyClassrooms()
  },

  // ==================== 获取教室数据 ====================
  loadClassrooms() {
    get('/user/classroom/list').then(data => {
      this.setData({ allClassrooms: data || [] })
    }).catch(err => {
      console.error('获取教室列表失败', err)
    })
  },

  // ==================== 教学楼选择 ====================
  showBuildingPicker() {
    const buildings = this.data.buildings
    const items = ['全部教学楼', ...buildings.map(b => b.name)]
    
    wx.showActionSheet({
      itemList: items,
      success: (res) => {
        if (res.tapIndex === 0) {
          this.setData({
            selectedBuildingId: '',
            selectedBuildingName: '全部教学楼'
          })
        } else {
          const selected = buildings[res.tapIndex - 1]
          this.setData({
            selectedBuildingId: selected.id,
            selectedBuildingName: selected.name
          })
        }
        this.loadEmptyClassrooms()
      }
    })
  },

  // ==================== 楼层选择 ====================
  showFloorPicker() {
    const floors = this.data.floors
    wx.showActionSheet({
      itemList: floors,
      success: (res) => {
        const floorText = floors[res.tapIndex]
        const floor = floorText === '全部楼层' ? '' : floorText.replace('楼', '')
        this.setData({
          selectedFloorText: floorText,
          selectedFloor: floor
        })
        this.loadEmptyClassrooms()
      }
    })
  },

  // ==================== 时间段 ====================
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

  showTimePicker(e) {
    const time = e.currentTarget.dataset.time
    this.setData({ selectedTimeText: time })
    this.loadEmptyClassrooms()
  },

  // ==================== 高级筛选 ====================
  showAdvancedFilter() {
    this.setData({ showAdvanced: !this.data.showAdvanced })
  },

  selectClassroomType(e) {
    const type = e.currentTarget.dataset.type
    const newType = this.data.classroomType === type ? '' : type
    this.setData({ classroomType: newType })
  },

  selectSeatCapacity(e) {
    const capacity = e.currentTarget.dataset.capacity
    const newCapacity = this.data.seatCapacity === capacity ? '' : capacity
    this.setData({ seatCapacity: newCapacity })
  },

  resetClassroomFilters() {
    this.setData({
      selectedBuildingId: '',
      selectedBuildingName: '全部教学楼',
      selectedFloor: '',
      selectedFloorText: '全部楼层',
      selectedDate: formatDate(new Date()),
      selectedTimeText: '全天',
      classroomType: '',
      seatCapacity: '',
      showAdvanced: false
    })
    this.initDateList()
    this.loadEmptyClassrooms()
  },

  applyAdvancedFilter() {
    this.setData({ showAdvanced: false })
    this.loadEmptyClassrooms()
  },

  // ==================== 空教室查询 ====================
  loadEmptyClassrooms() {
    this.setData({ loading: true })
    wx.showLoading({ title: '查询中...' })
    
    const { startTime, endTime } = this.getTimeRange(this.data.selectedTimeText)
    
    const params = { date: this.data.selectedDate }
    if (startTime) params.startTime = startTime
    if (endTime) params.endTime = endTime
    if (this.data.selectedBuildingId) params.buildingId = this.data.selectedBuildingId
    if (this.data.selectedFloor) params.floor = parseInt(this.data.selectedFloor)
    
    console.log('查询参数:', params)
    
    get('/user/classroom/empty/filter', params).then(data => {
      let filteredData = data || []
      
      // 教室类型筛选
      if (this.data.classroomType === 'seminar') {
        filteredData = filteredData.filter(room => (room.capacity || 0) <= 15)
      } else if (this.data.classroomType === 'normal') {
        filteredData = filteredData.filter(room => (room.capacity || 0) > 15)
      }
      
      // 座位数筛选
      if (this.data.seatCapacity === 'small') {
        filteredData = filteredData.filter(room => (room.capacity || 0) <= 50)
      } else if (this.data.seatCapacity === 'medium') {
        filteredData = filteredData.filter(room => (room.capacity || 0) >= 50 && (room.capacity || 0) <= 100)
      } else if (this.data.seatCapacity === 'large') {
        filteredData = filteredData.filter(room => (room.capacity || 0) >= 100)
      }
      
      console.log('筛选后教室数量:', filteredData.length)
      
      this.setData({ emptyClassrooms: filteredData, loading: false })
      wx.hideLoading()
      
      if (!filteredData || filteredData.length === 0) {
        wx.showToast({ title: '暂无空教室', icon: 'none' })
      }
    }).catch(() => {
      wx.hideLoading()
      this.setData({ loading: false })
      wx.showToast({ title: '查询失败', icon: 'none' })
    })
  },

  // ==================== 校园活动 ====================
  loadActivities(reset = true) {
    if (this.data.loading) return
    
    this.setData({ loading: true })
    
    if (reset) {
      this.setData({ page: 1, activities: [], hasMore: true })
    }
    
    const params = {
      page: this.data.page,
      pageSize: 10
    }
    
    if (this.data.keyword) params.keyword = this.data.keyword
    if (this.data.activityType) params.type = this.data.activityType
    if (this.data.activityStatus) params.status = this.data.activityStatus
    if (this.data.activityLocation) params.location = this.data.activityLocation
    
    get('/user/activity/list', params).then(res => {
      const list = res.list || res.records || []
      const total = res.total || 0

      const favoriteList = wx.getStorageSync('favoriteActivities') || []
      const registeredList = wx.getStorageSync('registeredActivities') || []
      
      // 处理列表数据，添加显示状态
      const processedList = list.map(item => {
        let statusText = ''
        let statusClass = ''
        if (item.status === 'upcoming') {
          statusText = '报名中'
          statusClass = 'upcoming'
        } else if (item.status === 'ongoing') {
          statusText = '进行中'
          statusClass = 'ongoing'
        } else if (item.status === 'completed') {
          statusText = '已结束'
          statusClass = 'completed'
        }
        const activityId = item.activityId || item.id
        return {
          ...item,
          activityId: item.activityId || item.id,
          statusText: statusText,
          statusClass: statusClass
        }
      })
      
      const newList = reset ? processedList : [...this.data.activities, ...processedList]
      this.setData({
        activities: newList,
        total: total,
        hasMore: newList.length < total,
        loading: false
      })
    }).catch(() => {
      console.error('获取活动列表失败')
      this.setData({ loading: false })
    })
  },

  searchActivity(e) {
    if (this.searchTimer) clearTimeout(this.searchTimer)
    this.searchTimer = setTimeout(() => {
      this.setData({ keyword: e.detail.value })
      this.loadActivities(true)
    }, 500)
  },

  filterByType(e) {
    const type = e.currentTarget.dataset.type
    this.setData({ activityType: this.data.activityType === type ? '' : type })
    this.loadActivities(true)
  },

  filterByStatus(e) {
    const index = e.detail.value
    const status = this.data.statusList[index]
    this.setData({ activityStatus: this.data.activityStatus === status ? '' : status })
    this.loadActivities(true)
  },

  filterByDate(e) {
    this.setData({ selectedActivityDate: e.detail.value })
    this.loadActivities(true)
  },

  // 活动详情跳转
  goToActivityDetail(e) {
    const id = e.currentTarget.dataset.id
    console.log('点击的活动ID:', id)
    
    if (!id) {
      wx.showToast({ title: '活动ID不存在', icon: 'none' })
      return
    }
    
    wx.navigateTo({
      url: `/pages/activity-detail/activity-detail?id=${id}`,
      fail: (err) => {
        console.error('跳转失败:', err)
        wx.showToast({ title: '页面不存在', icon: 'none' })
      }
    })
  },

  loadMore() {
    if (this.data.hasMore && !this.data.loading) {
      this.setData({ page: this.data.page + 1 })
      this.loadActivities(false)
    }
  },

  // 在 discover.js 中添加
  onShow() {
       // 从详情页返回时刷新活动列表
    if (this.data.currentTab === 1) {
      this.loadActivities(true)
    }
  },

  // ==================== Tab切换 ====================
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