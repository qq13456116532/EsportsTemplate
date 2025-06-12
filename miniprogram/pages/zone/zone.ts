import { request } from '../../utils/request';


Page({
  data: {
    categories: [] as Category[], // 
    products: [] as Product[],
    activeCategoryId: null, // Start with no category selected
    activeCategoryName: '', // 新增字段
  },

  onLoad() {
    this.loadCategories();
  },

  loadCategories() {
    request({ url: '/shop/categories' }).then(categories => { // 
      this.setData({
        categories: categories,
        activeCategoryId: categories.length > 0 ? categories[0].id : null,
        activeCategoryName: categories.length > 0 ? categories[0].name : '',
      });
      if (this.data.activeCategoryId) {
        this.loadProducts(); // 
      }
    });
  },

  onCategoryTap(e: any) {
    const id = e.currentTarget.dataset.id;
    const name = e.currentTarget.dataset.name;
    if (id === this.data.activeCategoryId) return;
    this.setData({ activeCategoryId: id ,activeCategoryName: name});
    this.loadProducts();
  },
  
  loadProducts() {
    if (!this.data.activeCategoryId) return;
    wx.showLoading({ title: '加载中...' });
    request({ url: `/shop/products?categoryId=${this.data.activeCategoryId}` }).then(products => { // 
      this.setData({ products: products });
      wx.hideLoading();
    }).catch(() => wx.hideLoading());
  }
});