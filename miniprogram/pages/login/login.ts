// pages/login/login.ts
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

  /* ========= 其他登录方式 ========= */
  loginByPhone() {
    // 这里演示直接走同一套流程，你也可以拉起微信手机号授权
    this.login();
  },
  loginByPwd() {
    wx.navigateTo({ url: '/pages/login-pwd/login-pwd' });
  }
});
