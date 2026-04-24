const { get, post, del } = require('../../utils/request')
const { formatDate, getWeekday } = require('../../utils/utils')

Page({
  data: {
    viewType: 'week',
    currentWeek: 1,
    weekDays: [],
    timeSlots: [
      { time: '08:00-08:45', startTime: '08:00:00' },
      { time: '08:55-09:40', startTime: '08:55:00' },
      { time: '10:00-10:45', startTime: '10:00:00' },
      { time: '10:55-11:40', startTime: '10:55:00' },
      { time: '14:30-15:15', startTime: '14:30:00' },
      { time: '15:25-16:10', startTime: '15:25:00' },
      { time: '16:30-17:15', startTime: '16:30:00' },
      { time: '17:25-18:10', startTime: '17:25:00' },
      { time: '19:00-19:45', startTime: '19:00:00' },
      { time: '19:50-20:35', startTime: '19:50:00' },
      { time: '20:40-21:25', startTime: '20:40:00' },
      { time: '21:30-22:15', startTime: '21:30:00' }
    ],
    courses: [],
    weekMatrix: [],      // 7天 × N时间段的课程矩阵
    dayCourses: [],
    selectedDate: '',
    weekdayName: '',
    showModal: false,
    selectedCourse: {}
  },

  onLoad() {
    this.checkLoginAndLoad()
  },

  checkLoginAndLoad() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      setTimeout(() => wx.reLaunch({ url: '/pages/login/login' }), 1500)
      return
    }
    
    const today = new Date()
    this.setData({
      selectedDate: formatDate(today),
      weekdayName: getWeekday(today)
    })
    
    this.initWeekDays()
    this.loadCourses()
  },

  initWeekDays() {
    const today = new Date()
    const dayOfWeek = today.getDay() || 7
    const monday = new Date(today)
    monday.setDate(today.getDate() - (dayOfWeek - 1))
    
    // 计算当前周的偏移
    const offset = (this.data.currentWeek - 1) * 7
    monday.setDate(monday.getDate() + offset)
    
    const weekDays = []
    for (let i = 0; i < 7; i++) {
      const date = new Date(monday)
      date.setDate(monday.getDate() + i)
      weekDays.push({
        day: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'][i],
        date: `${date.getMonth() + 1}/${date.getDate()}`
      })
    }
    this.setData({ weekDays })
  },

  loadCourses() {
    wx.showLoading({ title: '加载中...' })
    
    get('/user/course/my').then(res => {
      wx.hideLoading()
      
      const courses = Array.isArray(res) ? res : (res.data || [])
      this.setData({ courses })
      
      // 构建周视图矩阵（7天 × 每个时间段的课程）
      this.buildWeekMatrix(courses)
      this.updateDayCourses()
    }).catch(err => {
      wx.hideLoading()
      console.error('加载课表失败:', err)
      wx.showToast({ title: '加载失败', icon: 'none' })
    })
  },

  // 构建周视图矩阵
  buildWeekMatrix(courses) {
    // weekMatrix[dayIndex][timeSlotIndex] = course 或 null
    const weekMatrix = Array(7).fill().map(() => Array(this.data.timeSlots.length).fill(null))
    
    courses.forEach(course => {
      const dayIndex = course.weekday - 1  // 1=周一 -> index 0
      if (dayIndex >= 0 && dayIndex < 7) {
        const timeSlotIndex = this.data.timeSlots.findIndex(
          slot => slot.startTime === course.startTime
        )
        if (timeSlotIndex !== -1) {
          weekMatrix[dayIndex][timeSlotIndex] = course
        }
      }
    })
    
    this.setData({ weekMatrix })
  },

  // 更新日视图
  updateDayCourses() {
    const date = new Date(this.data.selectedDate)
    const weekday = date.getDay() || 7
    const dayCourses = this.data.courses.filter(c => c.weekday === weekday)
    this.setData({ dayCourses })
  },

  // 判断某个时间段是否有课程（用于日视图）
  hasCourseInSlot(startTime) {
    return this.data.dayCourses.some(c => c.startTime === startTime)
  },

  // 切换视图
  switchView(e) {
    const viewType = e.currentTarget.dataset.type
    this.setData({ viewType })
  },

  // 上一周
  prevWeek() {
    if (this.data.currentWeek > 1) {
      this.setData({ currentWeek: this.data.currentWeek - 1 })
      this.initWeekDays()
      this.loadCourses()
    }
  },

  // 下一周
  nextWeek() {
    this.setData({ currentWeek: this.data.currentWeek + 1 })
    this.initWeekDays()
    this.loadCourses()
  },

  // 同步课表
  syncTimetable() {
    this.loadCourses()
  },

  // 日期选择
  onDateChange(e) {
    const selectedDate = e.detail.value
    const date = new Date(selectedDate)
    const weekdayName = getWeekday(date)
    this.setData({ selectedDate, weekdayName })
    this.updateDayCourses()
  },

  // 显示课程详情
  showCourseDetail(e) {
    this.setData({
      showModal: true,
      selectedCourse: e.currentTarget.dataset.course
    })
  },

  hideModal() {
    this.setData({ showModal: false })
  },

  stopPropagation() {},

  // 设置提醒
  setReminder(e) {
    const course = e.currentTarget.dataset.course
    wx.showActionSheet({
      itemList: ['课前5分钟', '课前10分钟', '课前15分钟', '课前30分钟'],
      success: (res) => {
        const remindTimes = ['5分钟前', '10分钟前', '15分钟前', '30分钟前']
        post(`/user/course/reminder/${course.courseId}`, {
          remindTime: remindTimes[res.tapIndex],
          repeatType: 'weekly'
        }).then(() => {
          wx.showToast({ title: '提醒设置成功', icon: 'success' })
        }).catch(() => {
          wx.showToast({ title: '设置失败', icon: 'none' })
        })
      }
    })
  },

  // 删除课程
  removeCourse() {
    wx.showModal({
      title: '确认删除',
      content: '确定要从课表中删除这门课程吗？',
      success: (res) => {
        if (res.confirm) {
          del(`/user/course/remove/${this.data.selectedCourse.courseId}`).then(() => {
            this.hideModal()
            this.loadCourses()
            wx.showToast({ title: '删除成功', icon: 'success' })
          }).catch(() => {
            wx.showToast({ title: '删除失败', icon: 'none' })
          })
        }
      }
    })
  }
})