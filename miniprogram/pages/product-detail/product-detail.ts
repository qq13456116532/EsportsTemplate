import { request } from '../../utils/request';

Page({
  data: {
    product: null as Product | null,
    isFavorited: false, // 默认未收藏
    comments: [] as Comment[], 
  },

  onLoad(options: { id?: string }) {
    if (options.id) {
      this.loadProductDetail(options.id);
    }
  },

  loadProductDetail(productId: string) {
    wx.showLoading({ title: '加载中...' });
    request({ url: `/shop/products/${productId}` })
      .then(product => {
        // 假设接口返回 product.isFavorited 字段用于收藏状态
        this.setData({ 
          product, 
          isFavorited: product.isFavorited || false 
        });
        this.loadComments(product.id);
        wx.hideLoading();
      })
      .catch(() => wx.hideLoading());
  },
  loadComments(productId: number) {
    request({ url: `/comments`, data: { productId } })
      .then((comments: Comment[]) => {
        // 为了复用现有 WXML，先做一次映射
        const formatted = comments.map(c => ({
          id: c.id,
          username: c.username,
          avatar: c.avatar,
          content: c.content,
          rating: c.rating ?? 5,
          timestamp: c.timestamp,
        }));
        this.setData({ comments: formatted });
      })
      .catch(() => {
        wx.showToast({ title: '评论加载失败', icon: 'none' });
      });
  },


  onBackToShop() {
    wx.navigateBack();
  },
  onBuyNow() {
    // 检查登录状态
    const token = wx.getStorageSync('token');

    if (!token) {
      // 如果没有token，则跳转到登录页面
      wx.navigateTo({
        url: '/pages/login/login',
      });
    } else {
      // 如果有token，则正常跳转到购买页面
      wx.navigateTo({
        url: `/pages/order-confirm/order-confirm?id=${this.data.product?.id}`,
      });
    }
  },

  onToggleFavorite() {
    const current = this.data.isFavorited;
    // 模拟收藏/取消收藏动作，可替换为请求接口
    this.setData({ isFavorited: !current });

    wx.showToast({
      title: current ? '已取消收藏' : '已收藏',
      icon: 'success',
    });

    // 也可以在此发送请求更新后端状态：
    // request({ 
    //   url: `/user/favorites/${this.data.product.id}`, 
    //   method: current ? 'DELETE' : 'POST' 
    // });
  },

  onShareAppMessage() {
    return {
      title: this.data.product ? this.data.product.name : '查看商单详情',
      path: `/pages/product-detail/product-detail?id=${this.data.product?.id}`,
    };
  }
});
