// utils/request.ts
const BASE_URL = 'http://127.0.0.1:8080/api';

type RequestOptions = {
  url: string;
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
  data?: any;
  header?: WechatMiniprogram.IAnyObject;   // 新增，方便外部自定义
};

export const request = (options: RequestOptions): Promise<any> => {
  const token = wx.getStorageSync('token');               // ← 取本地 token
  const headers = {
    'content-type': 'application/json',
    ...(options.header || {}),
    ...(token ? { Authorization: `Bearer ${token}` } : {})   // 自动带 token，不用逐个手动塞 header
  };

  return new Promise((resolve, reject) => {
    wx.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: headers,                                      // ← 带上去
      success: (res) => {
        // 后端用 401 / 403 表示未登录或 token 失效
        if (res.statusCode === 401 || res.statusCode === 403) {
          handleAuthFail();
          reject(res);
          return;
        }
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(res);
        }
      },
      fail: (err) => {
        wx.showToast({ title: '请求失败', icon: 'none' });
        reject(err);
      },
    });
  });
};

/** 统一处理登录失效 */
function handleAuthFail() {
  wx.removeStorageSync('token');
  wx.showModal({
    title: '请先登录',
    content: '登录状态已失效，请重新登录',
    confirmText: '去登录',
    success(res) {
      if (res.confirm) {
        wx.redirectTo({ url: '/pages/login/login' });
      }
    }
  });
}
