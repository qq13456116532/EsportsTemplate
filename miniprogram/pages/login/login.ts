// pages/login/login.ts
import { request } from '../../utils/request';
Page({
  data: {
    phone: '',
    code: '',
    agree: true,
    codeBtnDisabled: false,
    codeBtnText: '获取验证码',
    countdown: 0,       // 秒
    canSubmit: true,
  },

  /* ========= 输入处理 ========= */
  onPhoneInput(e: WechatMiniprogram.CustomEvent) {
    this.setData({ phone: e.detail.value.replace(/\D/g, '') });
  },
  onCodeInput(e: WechatMiniprogram.CustomEvent) {
    this.setData({ code: e.detail.value.replace(/\D/g, '') });
  },
  toggleAgree() {
    this.setData({ agree: !this.data.agree });
  },

  /* ========= 验证码 ========= */
  getCode() {
    if (this.data.codeBtnDisabled) return;
    if (!/^1\d{10}$/.test(this.data.phone)) {
      wx.showToast({ title: '请输入正确的手机号', icon: 'none' });
      return;
    }

    // TODO: 请求后端发送验证码
    wx.showLoading({ title: '发送中...' });
    setTimeout(() => {
      wx.hideLoading();
      wx.showToast({ title: '已发送', icon: 'success' });
      this.startCountdown(60);   // 60 秒倒计时
    }, 500);
  },

  startCountdown(sec: number) {
    this.setData({ codeBtnDisabled: true, countdown: sec });
    this.tick();
  },
  tick() {
    if (this.data.countdown <= 0) {
      this.setData({ codeBtnDisabled: false, codeBtnText: '获取验证码' });
      return;
    }
    this.setData({
      codeBtnText: `${this.data.countdown}s`,
      countdown: this.data.countdown - 1
    });
    setTimeout(() => this.tick(), 1000);
  },

  /* ========= 表单提交 ========= */
  login() {
    if (!this.data.canSubmit) return;
    wx.showLoading({ title: '登录中...' });
    // TODO: 调用后端登录接口
    setTimeout(() => {
      wx.hideLoading();
      wx.showToast({ title: '登录成功', icon: 'success' });
      wx.switchTab({ url: '/pages/index/index' });
    }, 800);
  },

  /* ========= 微信登录方式 ========= */
  loginByWechat() {
    if (!this.data.canSubmit) return;
    this.setData({ canSubmit: false });
    wx.showLoading({ title: '登录中...' });

    // ① 静默换取 code
    wx.login({
      success: ({ code }) => {
        // ② 发送 code 到后端，不再需要 userInfo
        request({
          url: '/wxlogin',          // ← 后端对应接口
          method: 'POST',
          data: { code },           // ← 只发送 code
        })
          .then(({ token, user }) => {    // user = 后端最终保存后的对象
            console.log(token)
            console.log(user)
            wx.setStorageSync('token', token);
            wx.setStorageSync('userInfo', user);
            wx.showToast({ title: '登录成功', icon: 'success' });
            this.leaveAfterLogin();       // 返回原页面 / 首页
          })
          .catch(() => wx.showToast({ title: '登录失败', icon: 'none' }))
          .finally(() => {
            this.setData({ canSubmit: true });
          });
      },
      fail: () => {
        wx.showToast({ title: '微信登录失败', icon: 'none' });
        this.setData({ canSubmit: true });
      },
    });
    wx.hideLoading();
  },


  /** 登录完成后返回上一页；若没有历史就去首页 */
  leaveAfterLogin() {
    const pages = getCurrentPages();
    if (pages.length > 1) {
      wx.navigateBack(); // 来源页在栈里，直接返回
    } else {
      wx.switchTab({ url: '/pages/index/index' }); // 栈被覆盖，回首页
    }
  },

  loginByPwd() {
    wx.navigateTo({ url: '/pages/login-pwd/login-pwd' });
  }
});