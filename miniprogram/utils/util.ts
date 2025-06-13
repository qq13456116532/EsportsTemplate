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



export const uploadFile = (tempPath: string): Promise<string> =>
  new Promise((resolve, reject) => {
    wx.uploadFile({
      url: 'http://127.0.0.1:8080/api/upload/avatar', // 你的后端地址
      filePath: tempPath,
      name: 'file',
      success(res) {
        const data = JSON.parse(res.data || '{}');
        if (data.url) resolve(data.url);
        else reject('no url');
      },
      fail: reject,
    });
  });
