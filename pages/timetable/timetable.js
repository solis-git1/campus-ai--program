const { get, post, del } = require('../../utils/request')
const { formatDate, getWeekday } = require('../../utils/util')

Page({
  data: {
    viewType: 'week',
    currentWeek: 1,
    weekDays: [],
    timeSlots: [
      { time: '08:00-09:40', startTime: '08:00:00' },
      { time: '09:50-12:15', startTime: '09:50:00' },
      { time: '14:00-15:40', startTime: '14:00:00' },
      { time: '15:50-17:30', startTime: '15:50:00' },
      { time: '19:00-20:40', startTime: '19:00:00' }
    ],
    courses: [],
    dayCourses: [],
    currentDate: '',
    currentWeekday: '',
    showModal: false,
    selectedCourse: {}
  },

  onLoad() {
    this.initWeekDays()
    this.loadCourses()
  },

  initWeekDays() {
    const today = new Date()
    const dayOfWeek = today.getDay() || 7
    const monday = new Date(today)
    monday.setDate(today.getDate() - (dayOfWeek - 1))
    
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
    this.updateCurrentDate()
  },

  updateCurrentDate() {
    const now = new Date()
    this.setData({
      currentDate: formatDate(now),
      currentWeekday: getWeekday(now)
    })
  },

  // 获取课表（使用 /user/course/my）
  loadCourses() {
    get('/user/course/my').then(res => {
      // 兼容响应格式 {code, message, data}
      const data = res.data || res || []
      this.setData({ courses: data })
      this.filterDayCourses()
    }).catch(err => {
      console.error('加载课表失败', err)
      wx.showToast({ title: '加载课表失败', icon: 'none' })
    })
  },

  filterDayCourses() {
    const today = new Date().getDay() || 7
    // 根据 weekday 字段筛选（1-7，周一至周日）
    const dayCourses = this.data.courses.filter(c => c.weekday === today)
    this.setData({ dayCourses })
  },

  switchView(e) {
    this.setData({ viewType: e.currentTarget.dataset.type })
  },

  prevWeek() {
    this.setData({ currentWeek: Math.max(1, this.data.currentWeek - 1) })
  },

  nextWeek() {
    this.setData({ currentWeek: this.data.currentWeek + 1 })
  },

  syncTimetable() {
    wx.showLoading({ title: '同步中...' })
    this.loadCourses()
    setTimeout(() => {
      wx.hideLoading()
      wx.showToast({ title: '同步成功', icon: 'success' })
    }, 500)
  },

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

  // 设置课程提醒（使用 /user/course/reminder/{courseId}）
  setReminder(e) {
    const course = e.currentTarget.dataset.course
    wx.showActionSheet({
      itemList: ['课前5分钟', '课前10分钟', '课前15分钟', '课前30分钟'],
      success: (res) => {
        const minutes = [5, 10, 15, 30][res.tapIndex]
        const remindTimes = ['5分钟前', '10分钟前', '15分钟前', '30分钟前']
        
        // 根据文档，参数格式可能需要确认
        post(`/user/course/reminder/${course.courseId}`, {
          remindTime: remindTimes[res.tapIndex],
          remindMinutes: minutes,
          repeatType: 'weekly'  // 改为 weekly 更标准
        }).then(() => {
          wx.showToast({ title: '提醒设置成功', icon: 'success' })
        }).catch((err) => {
          console.error('设置提醒失败', err)
          wx.showToast({ title: '设置失败', icon: 'none' })
        })
      }
    })
  },

  // 删除课程（使用 DELETE /user/course/remove/{courseId}）
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
          }).catch((err) => {
            console.error('删除失败', err)
            wx.showToast({ title: '删除失败', icon: 'none' })
          })
        }
      }
    })
  },

  // 添加课程到课表（如果需要）
  addCourse(courseId) {
    post(`/user/course/add/${courseId}`).then(() => {
      this.loadCourses()
      wx.showToast({ title: '添加成功', icon: 'success' })
    }).catch(() => {
      wx.showToast({ title: '添加失败', icon: 'none' })
    })
  }
})