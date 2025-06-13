import { request } from '../../utils/request';
import { ensureLogin } from '../../utils/util';

Page({
  data: {
    product: null as Product | null,
    qty: 1,
    remark: '',
    submitting: false,
    total: '0.00'
  },

  onLoad(options: { id?: string }) {
    if (options.id) this.loadProduct(options.id);
  },

  /* ---------- 数据加载 ---------- */
  loadProduct(id: string) {
    wx.showLoading({ title: '加载中...' });
    request({ url: `/shop/products/${id}` })
      .then(product => {
        this.setData({ product });
        this.recalc();
      })
      .finally(() => wx.hideLoading());
  },

  /* ---------- 数量加减 ---------- */
  inc() { this.setQty(this.data.qty + 1); },
  dec() { if (this.data.qty > 1) this.setQty(this.data.qty - 1); },
  setQty(qty: number) { this.setData({ qty }); this.recalc(); },

  /* ---------- 备注 ---------- */
  onRemark(e: WechatMiniprogram.Input) {
    this.setData({ remark: e.detail.value });
  },

  /* ---------- 计算总价 ---------- */
  recalc() {
    if (!this.data.product) return;
    const totalNum = (Number(this.data.product.price) * this.data.qty).toFixed(2);
    this.setData({ total: totalNum });
  },

  /* ---------- 提交订单 ---------- */
  async submitOrder() {
    if (!ensureLogin()) return;
    if (this.data.submitting) return;
    this.setData({ submitting: true });

    try {
      await request({
        url: '/orders',          // 后端接口，见下面新增说明
        method: 'POST',
        data: {
          productId: this.data.product!.id,
          quantity: this.data.qty,
          remark: this.data.remark
        }
      });

      wx.showToast({ title: '下单成功', icon: 'success' });
      // 简易跳转：支付流程接入前先回到订单列表/个人中心
      setTimeout(() => wx.navigateBack({ delta: 1 }), 1500);
    } catch {
      wx.showToast({ title: '下单失败', icon: 'none' });
    } finally {
      this.setData({ submitting: false });
    }
  }
});
