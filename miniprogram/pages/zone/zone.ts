type Product = {
  id: number;
  name: string;
  price: string;
  sales: number;
  imageUrl: string;
};

const allProducts: Record<number, Product[]> = {
   1: [ // 英雄联盟
    { id: 101, name: '峡谷之巅-大师陪练', price: '80', sales: 50, imageUrl: '/assets/images/mock/banner2.png' },
    { id: 102, name: '全英雄教学', price: '40', sales: 120, imageUrl: '/assets/images/mock/banner2.png' }
  ],
  2: [ // 王者荣耀
    { id: 201, name: '荣耀王者-陪玩带飞', price: '50', sales: 250, imageUrl: '/assets/images/mock/banner1.png' },
    { id: 202, name: '国服打野教学', price: '100', sales: 80, imageUrl: '/assets/images/mock/banner1.png' }
  ],
  3: [ // 金铲铲
    { id: 301, name: '大师冲分', price: '30', sales: 500, imageUrl: '/assets/images/mock/banner3.png' }
  ],
};

Page({
  data: {
    categories: [
      { id: 1, name: '英雄联盟' },
      { id: 2, name: '王者荣耀' },
      { id: 3, name: '金铲铲' }
    ],
    products: [] as Product[], // ✅ 显式声明类型,
    activeCategoryId: 1,
  },

  onLoad() {
    this.loadProducts();
  },

  onCategoryTap(e: any) {
    const id = e.currentTarget.dataset.id;
    this.setData({
      activeCategoryId: id
    });
    this.loadProducts();
  },
  
  loadProducts() {
    this.setData({
      products: allProducts[this.data.activeCategoryId]
    });
  }
});