DROP DATABASE IF EXISTS appdb;
CREATE DATABASE appdb;
USE appdb;
UPDATE users
SET password='ADMIN_HASH_HERE'
WHERE username='admin';

UPDATE users
SET password='USER_HASH_HERE'
WHERE username='user';

-- =========================
-- USERS
-- =========================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN','USER') NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- =========================
-- MEDICINES
-- =========================
CREATE TABLE medicines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    price DOUBLE,
    stock INT
);

-- =========================
-- ORDERS
-- =========================
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_amount DOUBLE
);

-- =========================
-- ORDER ITEMS
-- =========================
CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INT,
    price DOUBLE,
    medicine_id BIGINT,
    order_id BIGINT,

    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id),

    CONSTRAINT fk_order_item_medicine
        FOREIGN KEY (medicine_id)
        REFERENCES medicines(id)
);

-- =========================
-- DEFAULT USERS
-- =========================
INSERT INTO users
(username,password,role,enabled)
VALUES
('admin','admin123','ADMIN',TRUE),
('user','user123','USER',TRUE);

-- =========================
-- SAMPLE MEDICINES
-- =========================
INSERT INTO medicines(name,description,price,stock)
VALUES
('Paracetamol','Fever and pain relief',20,100),
('Dolo 650','Pain relief',35,100),
('Crocin','Cold medicine',25,100),
('Azithromycin','Antibiotic',120,100),
('Vitamin C','Supplement',80,100);