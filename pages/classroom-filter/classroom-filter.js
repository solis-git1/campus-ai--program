const { get } = require('../../utils/request')
const { formatDate } = require('../../utils/utils')

Page({
  data: {
    buildings: [],           // 存储建筑对象 { id, name }
    floors: ['1楼', '2楼', '3楼', '4楼', '5楼', '6楼'],
    selectedBuildingId: '',  // 改为存储建筑ID
    selectedBuildingName: '', // 存储建筑名称用于显示
    selectedFloor: '',
    selectedDate: '',
    startTime: '',
    endTime: '',
    minCapacity: '',
    hasMedia: false,
    classrooms: []
  },

  onLoad() {
    // 设置默认日期为今天
    this.setData({ selectedDate: formatDate(new Date()) })
    this.loadBuildings()
  },

  // 通过 list 接口获取所有教室，提取建筑列表
  loadBuildings() {
    wx.showLoading({ title: '加载中...' })
    get('/user/classroom/list').then(data => {
      // 从教室列表中提取不重复的建筑信息
      const buildingMap = new Map()
      data.forEach(classroom => {
        if (!buildingMap.has(classroom.buildingId)) {
          buildingMap.set(classroom.buildingId, {
            id: classroom.buildingId,
            name: classroom.buildingName
          })
        }
      })
      
      const buildings = Array.from(buildingMap.values())
      this.setData({ buildings })
      
      // 默认选中第一个建筑
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

  // 选择建筑（picker返回的是索引）
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
    this.setData({ selectedFloor: this.data.floors[e.detail.value] })
  },

  // 选择日期
  selectDate(e) {
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

  // 查询空教室
  searchClassrooms() {
    // 参数校验
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
    
    // 构建请求参数
    const params = {
      date: this.data.selectedDate,
      startTime: this.data.startTime,
      endTime: this.data.endTime,
      buildingId: this.data.selectedBuildingId || undefined,
      floor: this.data.selectedFloor ? parseInt(this.data.selectedFloor) : undefined,
      minCapacity: this.data.minCapacity ? parseInt(this.data.minCapacity) : undefined,
      hasMedia: this.data.hasMedia ? 1 : 0
    }
    
    // 移除 undefined 的参数
    Object.keys(params).forEach(key => {
      if (params[key] === undefined) {
        delete params[key]
      }
    })

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

  // 收藏教室
  favoriteClassroom(classroomId) {
    wx.showLoading({ title: '收藏中...' })
    post(`/user/classroom/favorite/${classroomId}`).then(() => {
      wx.hideLoading()
      wx.showToast({ title: '收藏成功', icon: 'success' })
      // 刷新当前列表，更新收藏状态
      this.searchClassrooms()
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({ title: '收藏失败', icon: 'none' })
    })
  },

  // 取消收藏
  unfavoriteClassroom(classroomId) {
    wx.showLoading({ title: '取消收藏...' })
    // 注意：这里需要用 delete 方法，需要确认 request 工具是否支持
    // 如果不支持，可能需要后端增加 POST 方式的取消接口
    wx.request({
      url: `/user/classroom/favorite/${classroomId}`,
      method: 'DELETE',
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