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
    selectedCourse: {},
    // 提醒相关
    showReminderModal: false,
    reminderOptions: ['课前5分钟', '课前10分钟', '课前15分钟', '课前30分钟'],
    selectedReminderTime: '课前10分钟'
  },

  onLoad() {
    this.checkLoginAndLoad()
    this.initReminderTimer()
  },

  onShow() {
    this.loadCourses()
  },

  onUnload() {
    if (this.reminderTimer) {
      clearInterval(this.reminderTimer)
    }
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
      
      // 为每个课程添加提醒状态
      const coursesWithReminder = courses.map(course => {
        const reminderSetting = wx.getStorageSync(`reminder_${course.courseId}`)
        return {
          ...course,
          reminderEnabled: reminderSetting ? reminderSetting.enabled : false,
          reminderTime: reminderSetting ? reminderSetting.time : null
        }
      })
      
      this.setData({ courses: coursesWithReminder })
      this.buildWeekMatrix(coursesWithReminder)
      this.updateDayCourses()
    }).catch(err => {
      wx.hideLoading()
      console.error('加载课表失败:', err)
      wx.showToast({ title: '加载失败', icon: 'none' })
    })
  },

  buildWeekMatrix(courses) {
    const weekMatrix = Array(7).fill().map(() => Array(this.data.timeSlots.length).fill(null))
    
    courses.forEach(course => {
      const dayIndex = course.weekday - 1
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

  updateDayCourses() {
    const date = new Date(this.data.selectedDate)
    const weekday = date.getDay() || 7
    const dayCourses = this.data.courses.filter(c => c.weekday === weekday)
    this.setData({ dayCourses })
  },

  hasCourseInSlot(startTime) {
    return this.data.dayCourses.some(c => c.startTime === startTime)
  },

  switchView(e) {
    const viewType = e.currentTarget.dataset.type
    this.setData({ viewType })
  },

  prevWeek() {
    if (this.data.currentWeek > 1) {
      this.setData({ currentWeek: this.data.currentWeek - 1 })
      this.initWeekDays()
      this.loadCourses()
    }
  },

  nextWeek() {
    this.setData({ currentWeek: this.data.currentWeek + 1 })
    this.initWeekDays()
    this.loadCourses()
  },

  syncTimetable() {
    this.loadCourses()
  },

  onDateChange(e) {
    const selectedDate = e.detail.value
    const date = new Date(selectedDate)
    const weekdayName = getWeekday(date)
    this.setData({ selectedDate, weekdayName })
    this.updateDayCourses()
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

  // ========== 提醒功能 ==========

  // 初始化提醒定时器
  initReminderTimer() {
    // 立即检查一次
    this.checkReminders()
    // 每分钟检查一次
    this.reminderTimer = setInterval(() => {
      this.checkReminders()
    }, 60 * 1000)
  },

  // 检查是否需要提醒
  checkReminders() {
    const courses = this.data.courses
    const now = new Date()
    const today = now.getDay() || 7
    const currentHour = now.getHours()
    const currentMinute = now.getMinutes()
    
    courses.forEach(course => {
      if (course.weekday !== today) return
      
      const [startHour, startMinute] = course.startTime.split(':').map(Number)
      let diffMinutes = (startHour - currentHour) * 60 + (startMinute - currentMinute)
      
      const reminderSetting = wx.getStorageSync(`reminder_${course.courseId}`)
      if (!reminderSetting || !reminderSetting.enabled) return
      
      let reminderMinutes = 10
      switch (reminderSetting.time) {
        case '课前5分钟': reminderMinutes = 5; break
        case '课前10分钟': reminderMinutes = 10; break
        case '课前15分钟': reminderMinutes = 15; break
        case '课前30分钟': reminderMinutes = 30; break
        default: reminderMinutes = 10
      }
      
      // 时间差等于提醒时间，且未提醒过
      if (diffMinutes === reminderMinutes && !reminderSetting.reminded) {
        this.sendReminder(course, reminderMinutes)
        reminderSetting.reminded = true
        wx.setStorageSync(`reminder_${course.courseId}`, reminderSetting)
      }
      
      // 课程已开始或已过，重置提醒标记
      if (diffMinutes < 0 && reminderSetting.reminded) {
        reminderSetting.reminded = false
        wx.setStorageSync(`reminder_${course.courseId}`, reminderSetting)
      }
    })
  },

  // 发送提醒
  sendReminder(course, minutes) {
    // 震动
    wx.vibrateShort()
    
    // 显示弹窗提醒
    wx.showModal({
      title: '课程提醒',
      content: `「${course.courseName}」将在${minutes}分钟后开始\n地点：${course.classroomName}\n教师：${course.teacher}`,
      confirmText: '知道了',
      showCancel: false
    })
  },

  // 打开提醒设置弹窗
  openReminderSetting(e) {
    const course = e.currentTarget.dataset.course
    const setting = wx.getStorageSync(`reminder_${course.courseId}`) || {
      enabled: false,
      time: '课前10分钟',
      reminded: false
    }
    
    this.setData({
      showReminderModal: true,
      selectedCourse: course,
      selectedReminderTime: setting.time
    })
  },

  // 关闭提醒弹窗
  closeReminderModal() {
    this.setData({ showReminderModal: false })
  },

  // 选择提醒时间
  onReminderTimeChange(e) {
    const index = e.detail.value
    this.setData({ selectedReminderTime: this.data.reminderOptions[index] })
  },

  // 保存提醒设置
  saveReminderSetting() {
    const { selectedCourse, selectedReminderTime } = this.data
    const setting = {
      enabled: true,
      time: selectedReminderTime,
      reminded: false
    }
    
    wx.setStorageSync(`reminder_${selectedCourse.courseId}`, setting)
    
    // 调用后端接口
    post(`/user/course/reminder/${selectedCourse.courseId}`, {
      remindTime: selectedReminderTime,
      repeatType: 'week'
    }).catch(() => {
      console.log('后端提醒设置失败，已保存到本地')
    })
    
    // 更新课程列表中的提醒状态
    const updatedCourses = this.data.courses.map(c => {
      if (c.courseId === selectedCourse.courseId) {
        return { ...c, reminderEnabled: true, reminderTime: selectedReminderTime }
      }
      return c
    })
    
    this.setData({ 
      courses: updatedCourses,
      showReminderModal: false
    })
    
    wx.showToast({ title: '提醒已开启', icon: 'success' })
  },

  // 关闭课程提醒
  disableReminder(e) {
    const course = e.currentTarget.dataset.course
    wx.showModal({
      title: '关闭提醒',
      content: '确定要关闭该课程的提醒吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync(`reminder_${course.courseId}`)
          
          const updatedCourses = this.data.courses.map(c => {
            if (c.courseId === course.courseId) {
              return { ...c, reminderEnabled: false, reminderTime: null }
            }
            return c
          })
          
          this.setData({ courses: updatedCourses })
          wx.showToast({ title: '已关闭提醒', icon: 'success' })
        }
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
            // 同时删除提醒设置
            wx.removeStorageSync(`reminder_${this.data.selectedCourse.courseId}`)
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