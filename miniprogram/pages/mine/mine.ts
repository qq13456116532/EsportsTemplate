import { ensureLogin, updateUserInfo, uploadFile } from "../../utils/util";
// pages/mine/mine.ts
Page({
  data: {
    userInfo: null as { avatarUrl: string, nickName: string } | null,
  },
  /** 页面每次可见都刷新一下用户信息 */
  onShow() {
    const userInfo = wx.getStorageSync('userInfo');
    this.setData({
      userInfo: userInfo || null,  // 为空时下方 WXML 会自动给出“立即登录”
    });
  },
  onLoad() {},
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/login',
    });
  },

  /**
   * 响应用户选择头像
   */
  async onChooseAvatar(e: any) {
    const temp = e.detail.avatarUrl;
    console.log(temp)
    if (!temp) return;

    wx.showLoading({ title: '上传中...' });
    try {
      // ① 先传文件
      const onlineUrl = await uploadFile(temp);

      // ② 再写用户资料
      const user = await updateUserInfo({ avatarUrl: onlineUrl });

      wx.setStorageSync('userInfo', user);
      this.setData({ userInfo: user });
      wx.showToast({ title: '头像已更新', icon: 'success' });
    } catch {
      wx.showToast({ title: '上传失败', icon: 'none' });
    } finally {
      wx.hideLoading();
    }
  },

  /**
   * 响应用户修改昵称
   */
  async onNicknameChange(e: any) {
    const nickName = (e.detail.value || '').trim();
    console.log('从输入框获取到的新昵称是：', nickName); 
    if (!nickName) {
      wx.showToast({ title: '昵称不能为空', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '保存中...' });
    try {
      const user = await updateUserInfo({ nickName });

      wx.setStorageSync('userInfo', user);
      this.setData({ userInfo: user });

      wx.showToast({ title: '昵称已更新', icon: 'success' });
    } catch {
      wx.showToast({ title: '保存失败', icon: 'none' });
    } finally {
      wx.hideLoading();
    }
  },

  goToPendingPay() {
    if (!ensureLogin()) return;
    wx.navigateTo({ url: '/pages/order/order?status=PENDING_PAYMENT' });
  },
  goToOngoing() {
    if (!ensureLogin()) return;
    wx.navigateTo({ url: '/pages/order/order?status=ONGOING' });
  },
  goToPendingComment() {
    if (!ensureLogin()) return;
    wx.navigateTo({ url: '/pages/order/order?status=PENDING_COMMENT' });
  },
  goToAllOrders() {
    if (!ensureLogin()) return;
    wx.navigateTo({ url: '/pages/order/order' });   // 全部
  },

});