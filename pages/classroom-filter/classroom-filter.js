const { get, post } = require('../../utils/request')
const { formatDate } = require('../../utils/utils')

Page({
  data: {
    buildings: [],
    floors: ['1楼', '2楼', '3楼', '4楼', '5楼', '6楼'],
    selectedBuildingId: '',
    selectedBuildingName: '',
    selectedFloor: '',
    selectedDate: '',
    startTime: '',
    endTime: '',
    minCapacity: '',
    hasMedia: false,
    classrooms: []
  },

  onLoad(options) {
    // 设置默认日期为今天
    this.setData({ selectedDate: formatDate(new Date()) })
    this.loadBuildings()
    
    // 如果从高级筛选传入了教室类型
    if (options.classroomType) {
      console.log('传入的教室类型:', options.classroomType)
      if (options.classroomType === 'multimedia') {
        this.setData({ hasMedia: true })
      }
    }
  },

  // 通过 list 接口获取所有教室，提取建筑列表
  loadBuildings() {
    wx.showLoading({ title: '加载中...' })
    get('/user/classroom/list').then(data => {
      const buildingMap = new Map()
      if (data && Array.isArray(data)) {
        data.forEach(classroom => {
          if (classroom.buildingId && !buildingMap.has(classroom.buildingId)) {
            buildingMap.set(classroom.buildingId, {
              id: classroom.buildingId,
              name: classroom.buildingName
            })
          }
        })
      }
      
      const buildings = Array.from(buildingMap.values())
      this.setData({ buildings })
      
      if (buildings.length > 0) {
        this.setData({
          selectedBuildingId: buildings[0].id,
          selectedBuildingName: buildings[0].name
        })
      }
      wx.hideLoading()
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({ title: '加载建筑列表失败', icon: 'none' })
    })
  },

  // 选择建筑
  selectBuilding(e) {
    const index = e.detail.value
    const selectedBuilding = this.data.buildings[index]
    if (selectedBuilding) {
      this.setData({
        selectedBuildingId: selectedBuilding.id,
        selectedBuildingName: selectedBuilding.name
      })
    }
  },

  // 选择楼层
  selectFloor(e) {
    const index = e.detail.value
    this.setData({ selectedFloor: this.data.floors[index] })
  },

  // 选择日期
  selectDate(e) {
    console.log('选择的日期:', e.detail.value)
    this.setData({ selectedDate: e.detail.value })
  },

  // 选择开始时间
  selectStartTime(e) {
    this.setData({ startTime: e.detail.value })
  },

  // 选择结束时间
  selectEndTime(e) {
    this.setData({ endTime: e.detail.value })
  },

  // 最小容量输入
  onMinCapacityInput(e) {
    this.setData({ minCapacity: e.detail.value })
  },

  // 多媒体设备开关
  toggleHasMedia(e) {
    this.setData({ hasMedia: e.detail.value })
  },

  // 查询空教室 - 使用 /user/classroom/empty/filter 接口
  searchClassrooms() {
    if (!this.data.selectedDate) {
      wx.showToast({ title: '请选择日期', icon: 'none' })
      return
    }
    if (!this.data.startTime) {
      wx.showToast({ title: '请选择开始时间', icon: 'none' })
      return
    }
    if (!this.data.endTime) {
      wx.showToast({ title: '请选择结束时间', icon: 'none' })
      return
    }

    wx.showLoading({ title: '查询中...' })
    
    const params = {
      date: this.data.selectedDate,
      startTime: this.data.startTime,
      endTime: this.data.endTime
    }
    
    if (this.data.selectedBuildingId) params.buildingId = this.data.selectedBuildingId
    if (this.data.selectedFloor) params.floor = parseInt(this.data.selectedFloor)
    if (this.data.minCapacity) params.minCapacity = parseInt(this.data.minCapacity)
    if (this.data.hasMedia) params.hasMedia = 1
    
    get('/user/classroom/empty/filter', params).then(data => {
      this.setData({ classrooms: data || [] })
      wx.hideLoading()
      
      if (!data || data.length === 0) {
        wx.showToast({ title: '未找到符合条件的教室', icon: 'none' })
      }
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({ title: '查询失败', icon: 'none' })
    })
  },

  // 收藏教室 - 使用 POST /user/classroom/favorite/{classroomId}
  favoriteClassroom(e) {
    const classroomId = e.currentTarget.dataset.id
    wx.showLoading({ title: '收藏中...' })
    post(`/user/classroom/favorite/${classroomId}`).then(() => {
      wx.hideLoading()
      wx.showToast({ title: '收藏成功', icon: 'success' })
      this.searchClassrooms()
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({ title: '收藏失败', icon: 'none' })
    })
  },

  // 取消收藏 - 使用 DELETE /user/classroom/favorite/{classroomId}
  unfavoriteClassroom(e) {
    const classroomId = e.currentTarget.dataset.id
    wx.showLoading({ title: '取消收藏...' })
    
    const token = wx.getStorageSync('token')
    wx.request({
      url: `http://localhost:8080/api/user/classroom/favorite/${classroomId}`,
      method: 'DELETE',
      header: {
        'token': token,
        'Content-Type': 'application/json'
      },
      success: () => {
        wx.hideLoading()
        wx.showToast({ title: '已取消收藏', icon: 'success' })
        this.searchClassrooms()
      },
      fail: () => {
        wx.hideLoading()
        wx.showToast({ title: '操作失败', icon: 'none' })
      }
    })
  }
})