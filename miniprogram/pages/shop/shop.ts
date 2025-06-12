import { request } from '../../utils/request';

Page({
  data: {
    swiperItems: [], // 
    navItems: [],
    products: [] // 
  },

  onLoad() {
    this.loadShopData();
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