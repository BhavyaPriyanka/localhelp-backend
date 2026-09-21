DROP DATABASE IF EXISTS appdb;
CREATE DATABASE appdb;
USE appdb;

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

CREATE TABLE medicines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    price DOUBLE,
    stock INT
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_amount DOUBLE,
    user_id BIGINT,
    status VARCHAR(255) NOT NULL DEFAULT 'PLACED',

    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INT,
    price DOUBLE,
    medicine_id BIGINT,
    order_id BIGINT,

    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id) REFERENCES orders(id),

    CONSTRAINT fk_order_item_medicine
        FOREIGN KEY (medicine_id) REFERENCES medicines(id)
);

INSERT INTO users
(username,password,role,enabled)
VALUES
('admin','$2a$10$0HvMy8XfC1EhI6g6bVd5ieyt97YeN3NpQJdDvPccdlCn5dAgIPsQa','ADMIN',TRUE),
('user','$2a$10$1SgGSXiaBpcRXt6FvYgMBOdd8DMIn5au13zGi80kzEWUKs1iJ63za','USER',TRUE);

INSERT INTO medicines(name,description,price,stock)
VALUES
('Paracetamol','Fever and pain relief',20,100),
('Dolo 650','Pain relief',35,100),
('Crocin','Cold medicine',25,100),
('Azithromycin','Antibiotic',120,100),
('Vitamin C','Supplement',80,100);

CREATE USER 'appuser'@'%' IDENTIFIED BY 'localhelp';

GRANT ALL PRIVILEGES ON appdb.* TO 'appuser'@'%';

FLUSH PRIVILEGES;