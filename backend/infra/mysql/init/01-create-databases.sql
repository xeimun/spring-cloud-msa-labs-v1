DROP DATABASE IF EXISTS `user_service_db`;
DROP DATABASE IF EXISTS `product_service_db`;
DROP DATABASE IF EXISTS `order_service_db`;

-- 각 서비스별 데이터베이스 생성
CREATE DATABASE `user_service_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE `product_service_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE `order_service_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
