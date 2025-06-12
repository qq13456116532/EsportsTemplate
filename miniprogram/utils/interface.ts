
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
