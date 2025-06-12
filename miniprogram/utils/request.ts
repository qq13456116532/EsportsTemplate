const BASE_URL = 'http://127.0.0.1:8080/api'; // Your backend API base URL 

type RequestOptions = {
  url: string;
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
  data?: any;
};

export const request = (options: RequestOptions): Promise<any> => {
  return new Promise((resolve, reject) => {
    wx.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(res);
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '请求失败',
          icon: 'none'
        });
        reject(err);
      },
    });
  });
};