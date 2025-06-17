// pages/order/order.ts
import { request } from '../../utils/request';
import { ensureLogin } from '../../utils/util';
import { formatDate } from '../../utils/util';
import { payOrder, refundOrder } from '../../utils/payment';


Page({
  data: {
    tabs: [
      { key: '',                  label: '全部'     },
      { key: 'PENDING_PAYMENT',   label: '待付款'   },
      { key: 'ONGOING',           label: '进行中'   },
      { key: 'PENDING_COMMENT',   label: '待评价'   }
    ],
    activeTab: '',       // '' = 全部
    orders: [] as PlayerOrder[],
    loading: false,
    commentModalVisible: false,
    commentContent: '',
    commentRating: 5,
    commentOrderId: null,
    commentProductId: null,
    ratingOptions: [1,2,3,4,5]
  },

  /** 接收 mine 页带过来的 ?status=... */
  onLoad(options: { status?: string }) {
    this.setData({ activeTab: options.status || '' });
    this.fetchOrders();
  },
  /** 根据订单状态返回对应 badge class */
  statusClass(status: string) {
    const map: Record<string,string> = {
      PENDING_PAYMENT: 'badge-pay',
      ONGOING:         'badge-ing',
      PENDING_COMMENT: 'badge-comment',
      REFUNDED:        'badge-refund'
    };
    return map[status] || 'badge';
  },

  /** 去逛逛（空状态按钮） */
  goShop() {
    wx.switchTab({ url: '/pages/shop/shop' });
  },
  /** 顶部标签点击 */
  onTabTap(e: WechatMiniprogram.BaseEvent) {
    const key = (e.currentTarget.dataset.key || '') as string;
    if (key === this.data.activeTab) return;
    this.setData({ activeTab: key });
    this.fetchOrders();
  },

  /** 下拉刷新 */
  onPullDownRefresh() {
    this.fetchOrders().finally(() => wx.stopPullDownRefresh());
  },

    /** 点击“去付款” */
    onPay(e: WechatMiniprogram.BaseEvent) {
      const id = e.currentTarget.dataset.id;
      wx.showLoading({ title: '支付中...' });
      payOrder(id)
        .then(() => {
          wx.showToast({ title: '支付成功' });
          this.fetchOrders();       // 刷新列表
        })
        .catch(() => wx.showToast({ title: '支付失败', icon: 'none' }))
        .finally(() => wx.hideLoading());
    },

    /** 点击“申请退款” */
    onRefund(e: WechatMiniprogram.BaseEvent) {
      const id = e.currentTarget.dataset.id;
      wx.showModal({
        title: '确认退款？',
        success: (res) => {
          if (!res.confirm) return;
          wx.showLoading({ title: '退款中...' });
          refundOrder(id)
            .then(() => {
              wx.showToast({ title: '已退款' });
              this.fetchOrders();
            })
            .catch(() => wx.showToast({ title: '退款失败', icon: 'none' }))
            .finally(() => wx.hideLoading());
        }
      });
    },

  /** 请求订单列表 */
  async fetchOrders() {
    if (!ensureLogin()) return;
    this.setData({ loading: true, orders: [] });

    const params = this.data.activeTab ? { status: this.data.activeTab } : {};
    try {
      const orders = await request({ url: '/orders', data: params });
      this.setData({
        orders: orders.map((o: PlayerOrder) => ({
          ...o,
          createdAt: formatDate(o.createdAt)   // 新增字段
        }))
      });
    } catch (err) { // (1) 给捕获的错误命名为 err
      console.error('获取订单列表失败, 后端返回的原始错误:', err); // (2) 在控制台打印详细错误
      wx.showToast({ title: '加载失败,请查看控制台', icon: 'none' }); // (3) 更新提示信息
    }  finally {
      this.setData({ loading: false });
    }
  },

  /** 辅助：把后端枚举翻成人类可读 */
  translateStatus(status: string) {
    const map: Record<string,string> = {
      PENDING_PAYMENT: '待付款',
      ONGOING:         '进行中',
      PENDING_COMMENT: '待评价',
      REFUNDED:        '已退款',
      COMPLETED:       '已完成'
    };
    return map[status] || status;
  },
  onComment(e: WechatMiniprogram.BaseEvent) {
    this.setData({
      commentModalVisible: true,
      commentOrderId: e.currentTarget.dataset.id,
      commentProductId: e.currentTarget.dataset.productId,
      commentContent: '',
      commentRating: 5,
    });
  },
  onCommentInput(e: WechatMiniprogram.Input) {
    this.setData({ commentContent: e.detail.value });
  },
  onRatingChange(e: WechatMiniprogram.PickerChange) {
    const idx = Number(e.detail.value);
    this.setData({ commentRating: this.data.ratingOptions[idx] });
  },
  onCancelComment() {
    this.setData({ commentModalVisible: false });
  },
  onSelectStar(e: WechatMiniprogram.TouchEvent) {
    const index = Number(e.currentTarget.dataset.index);
    this.setData({ commentRating: index + 1 });
  },
  stopPropagation() {
    // 阻止事件冒泡的空函数
  },
  submitComment() {
    const { commentOrderId, commentProductId, commentContent, commentRating } = this.data;
    if (!commentContent.trim()) {
      wx.showToast({ title: '请输入评价内容', icon: 'none' });
      return;
    }
    request({
      url: '/comments',
      method: 'POST',
      data: {
        orderId: commentOrderId,
        productId: commentProductId,
        content: commentContent,
        rating: commentRating,
      }
    }).then(() => {
      wx.showToast({ title: '评价成功' });
      this.setData({ commentModalVisible: false });
      this.fetchOrders(); //刷新订单
    });
  }
});

