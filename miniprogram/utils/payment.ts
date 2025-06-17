import { request } from './request';

/** 模拟付款，返回更新后的订单 */
export const payOrder = (orderId: number) =>
  request({ url: `/pay/${orderId}`, method: 'POST' });

/** 模拟退款 */
export const refundOrder = (orderId: number) =>
  request({ url: `/pay/${orderId}/refund`, method: 'POST' });
