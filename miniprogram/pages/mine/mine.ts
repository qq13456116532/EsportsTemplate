// pages/mine/mine.ts
Page({
  data: {
    userInfo: {
      avatarUrl: '/assets/images/icons/defaultUser.svg',
      nickName: '电竞大神',
    }
  },
  onLoad() {},
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/login',
    });
  },
});