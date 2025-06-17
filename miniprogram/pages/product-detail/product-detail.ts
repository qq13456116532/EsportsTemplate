import { request } from '../../utils/request';
import { ensureLogin,formatDate } from '../../utils/util';

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
        this.checkFavoriteStatus(product.id);
        this.loadComments(product.id);
      })
      .catch()
      .finally(()=>wx.hideLoading());
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
          timestamp: formatDate(c.timestamp),
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
    if (!ensureLogin()) return;
    wx.navigateTo({
      url: `/pages/order-confirm/order-confirm?id=${this.data.product?.id}`,
    });
  },

  onToggleFavorite() {
    if (!ensureLogin()) return;           // 未登录先跳登录
    const { isFavorited, product } = this.data;
    this.syncFavorite(product!.id, !isFavorited)
        .then(() => {
          this.setData({ isFavorited: !isFavorited });
          wx.showToast({ title: isFavorited ? '已取消收藏' : '已收藏', icon: 'success' });
        })
        .catch(() => wx.showToast({ title: '操作失败', icon: 'none' }));
  },
  /** 查询是否已收藏（有 token 时才发请求） */
  checkFavoriteStatus(productId: number) {
    const token = wx.getStorageSync('token');
    if (!token) return;           // 未登录直接跳过
    request({ url: `/favorites/${productId}` })
      .then((fav: boolean) => this.setData({ isFavorited: fav }));
  },

  /** 后端同步收藏状态 */
  syncFavorite(productId: number, add: boolean) {
    const method = add ? 'POST' : 'DELETE';
    const url    = add ? '/favorites' : `/favorites/${productId}`;
    const data   = add ? { productId } : undefined;
    return request({ url, method, data });
  },

  onShareAppMessage() {
    return {
      title: this.data.product ? this.data.product.name : '查看商单详情',
      path: `/pages/product-detail/product-detail?id=${this.data.product?.id}`,
    };
  }
});
