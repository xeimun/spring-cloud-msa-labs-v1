-- User Service 샘플 데이터
USE `user_service_db`;

-- 사용자 데이터 (패스워드는 BCrypt로 암호화됨 - 원본: "1234")
INSERT INTO user_service_db.users (email, password, name) VALUES
  ('user1@example.com', '$2a$10$TR449R5BNAKor7G8C9QyjuwONmlkrQiCybwkEOUAVVN2rzFiAqQC6', '박민수'),
  ('user2@example.com', '$2a$10$TR449R5BNAKor7G8C9QyjuwONmlkrQiCybwkEOUAVVN2rzFiAqQC6', '최지은'),
  ('admin@example.com', '$2a$10$TR449R5BNAKor7G8C9QyjuwONmlkrQiCybwkEOUAVVN2rzFiAqQC6', '관리자');

-- Product Service 샘플 데이터
USE `product_service_db`;

INSERT INTO products (name, description, price, stock_quantity, category, image_url) VALUES
('MacBook Air M2', 'Apple MacBook Air with M2 chip, 13-inch display', 1290000.00, 15, 'Electronics', 'https://example.com/images/macbook-air.jpg'),
('iPhone 15 Pro', 'Latest iPhone with A17 Pro chip', 1550000.00, 25, 'Electronics', 'https://example.com/images/iphone15.jpg'),
('Nike Air Max', 'Comfortable running shoes for daily wear', 120000.00, 50, 'Fashion', 'https://example.com/images/nike-airmax.jpg'),
('Samsung 4K Monitor', '32-inch 4K UHD monitor with USB-C', 450000.00, 8, 'Electronics', 'https://example.com/images/samsung-monitor.jpg'),
('Starbucks Tumbler', 'Reusable coffee tumbler 473ml', 35000.00, 100, 'Lifestyle', 'https://example.com/images/starbucks-tumbler.jpg'),
('Wireless Keyboard', 'Bluetooth mechanical keyboard', 89000.00, 30, 'Electronics', 'https://example.com/images/keyboard.jpg'),
('Cotton T-Shirt', 'Basic cotton t-shirt, multiple colors', 25000.00, 200, 'Fashion', 'https://example.com/images/tshirt.jpg'),
('Coffee Beans 1kg', 'Premium Arabica coffee beans', 45000.00, 60, 'Food', 'https://example.com/images/coffee-beans.jpg'),
('Yoga Mat', 'Non-slip yoga mat for exercise', 65000.00, 40, 'Sports', 'https://example.com/images/yoga-mat.jpg'),
('Bluetooth Speaker', 'Portable wireless speaker with bass', 95000.00, 35, 'Electronics', 'https://example.com/images/speaker.jpg');
