import { ensureLogin, updateUserInfo } from "../../utils/util";
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
    const { avatarUrl } = e.detail;
    if (!avatarUrl || !this.data.userInfo) return;

    wx.showLoading({ title: '上传中...' });
    try {
      // ① 调后端保存
      const user = await updateUserInfo({ avatarUrl });

      // ② 更新本地 & 页面
      wx.setStorageSync('userInfo', user);
      this.setData({ userInfo: user });

      wx.showToast({ title: '头像已更新', icon: 'success' });
    } catch (err) {
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

  goToPendingPay() { if (!ensureLogin()) return; /* 跳转 */ },
  goToOngoing()    { if (!ensureLogin()) return; /* 跳转 */ },
  goToPendingComment() { if (!ensureLogin()) return; /* 跳转 */ },
  goToAllOrders()  { if (!ensureLogin()) return; /* 跳转 */ },
  contactService() { if (!ensureLogin()) return; /* … */ },
});