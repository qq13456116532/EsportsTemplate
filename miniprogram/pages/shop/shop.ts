// pages/shop/shop.ts
Page({
  data: {
    swiperItems: [
      { imageUrl: '/assets/images/mock/banner1.png' },
      { imageUrl: '/assets/images/mock/banner2.png' },
      { imageUrl: '/assets/images/mock/banner3.png' }
    ],
    navItems: [
      { name: '段位提升', iconUrl: '/assets/images/icons/rank-up.svg' },
      { name: '大神陪练', iconUrl: '/assets/images/icons/pro-player.svg' },
      { name: '技能教学', iconUrl: '/assets/images/icons/tutorial.svg' },
      { name: '娱乐开黑', iconUrl: '/assets/images/icons/fun-game.svg' }
    ],
    products: [
      { id: 1, name: '王者荣耀-荣耀王者陪练', price: '50', sales: 120, views: 1500, imageUrl: '/assets/images/mock/banner1.png' },
      { id: 2, name: '英雄联盟-钻石到大师', price: '300', sales: 45, views: 2300, imageUrl: '/assets/images/mock/banner2.png' },
      { id: 3, name: '金铲铲-大师教学局', price: '30', sales: 300, views: 800, imageUrl: '/assets/images/mock/banner3.png' }
    ]
  },
  onLoad() {},
});