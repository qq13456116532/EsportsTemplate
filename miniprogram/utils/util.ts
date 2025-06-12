import { request } from "./request"

export const formatTime = (date: Date) => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return (
    [year, month, day].map(formatNumber).join('/') +
    ' ' +
    [hour, minute, second].map(formatNumber).join(':')
  )
}

const formatNumber = (n: number) => {
  const s = n.toString()
  return s[1] ? s : '0' + s
}

export function ensureLogin(): boolean {
  const token = wx.getStorageSync('token');
  if (token) return true;
  wx.showToast({ title: '请先登录', icon: 'none' });
  setTimeout(() => {
    wx.navigateTo({ url: '/pages/login/login' });
  }, 600);
  return false;
}

/** 只把需要改的字段丢进来即可（avatarUrl / nickName） */
export const updateUserInfo = (payload: Partial<{ nickName: string; avatarUrl: string; }>) =>
  request({
    url: '/users/me',
    method: 'PUT',
    data: payload,   // { nickName?, avatarUrl? }
  });