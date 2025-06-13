import { request } from '../../utils/request';

Page({
  data: {
    swiperItems: [],   // 轮播
    navItems:   [],    // 分类导航
    products:   [],    // 列表
    searchQuery: '',   
    listTitle: '精选推荐',
    loading: false,     // ← 可配合 skeleton / loading 动画
  },

  /* 页面加载 */
  onLoad() {
    this.loadShopData();        // 首屏还是取精选
  },

  /* ========= 搜索 ========= */
  onSearchInput(e: WechatMiniprogram.Input) {
    const val = e.detail.value;
    this.setData({ searchQuery: val });
    if (!val.trim()) this.loadFeatured();   // 清空即恢复精选
  },

  onSearchConfirm() {
    const q = this.data.searchQuery.trim();
    if (!q) return;

    this.setData({ loading: true, listTitle: '搜索结果' });
    request({ url: '/shop/products', data: { searchQuery: q } })
      .then(products => this.setData({ products }))
      .catch(() => wx.showToast({ title: '搜索失败', icon: 'none' }))
      .finally(() => this.setData({ loading: false }));
  },

  onCancelSearch() {
    this.setData({ searchQuery: '' });
    this.loadFeatured();
  },
  loadFeatured() {
    this.setData({ loading: true, listTitle: '精选推荐' });
    request({ url: '/shop/products/featured' })
      .then(products => this.setData({ products }))
      .finally(() => this.setData({ loading: false }));
  },

  loadShopData() {
    wx.showLoading({ title: '加载中...' });

    // Fetch all data in parallel
    Promise.all([
      request({ url: '/shop/banners' }), // 
      request({ url: '/shop/categories' }), // 
      request({ url: '/shop/products/featured' }) // 
    ]).then(([banners, categories, products]) => {
      // The backend doesn't provide icons for categories, so we add them here
      const navIcons = [
          '/assets/images/icons/rank-up.svg', 
          '/assets/images/icons/pro-player.svg', 
          '/assets/images/icons/tutorial.svg'
      ];
      const navItems = categories.map((category: {name: string}, index: number) => ({
        name: category.name,
        iconUrl: navIcons[index] || '/assets/images/icons/fun-game.svg' // Provide a default icon
      }));

      this.setData({
        swiperItems: banners,
        navItems: navItems,
        products: products
      });
      wx.hideLoading();
    }).catch(err => {
      console.error("Failed to load shop data", err);
      wx.hideLoading();
      wx.showToast({ title: '数据加载失败', icon: 'none' });
    });
  },
});