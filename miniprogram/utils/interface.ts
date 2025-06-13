
type Product = { id: number; name: string; price: string; sales: number; imageUrl: string; }; // 

type Category = {id: number; name: string;};


type Comment = {
  id: number;
  username: string;   // 后端返回的 user.nickName
  avatar: string;     // user.avatarUrl
  content: string;
  rating: number;
  timestamp: string;  // ISO 日期或已格式化
};

// ✅ 临时声明，确认字段后再完善
interface PlayerOrder {
  id: string;
  status: 'PENDING_PAYMENT' | 'ONGOING' | 'PENDING_COMMENT' | 'REFUNDED';
  product: {
    name: string;
    price: number;
    imageUrl: string;
  };
  createdAt: string
  // 视情况把后端返回的其他字段也补上
}

