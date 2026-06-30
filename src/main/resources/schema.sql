-- Online Bookstore (网上书店管理系统) — schema
-- MySQL 8/9, InnoDB, utf8mb4. Run: mysql -uroot < schema.sql
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS bookstore DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE bookstore;

DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS audit_log;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS `user`;

-- Accounts (passwords hashed with salted SM3; phone/address encrypted with SM4).
CREATE TABLE `user` (
  id             BIGINT       NOT NULL AUTO_INCREMENT,
  username       VARCHAR(50)  NOT NULL,
  password_sm3   CHAR(64)     NOT NULL,
  salt           VARCHAR(32)  NOT NULL,
  real_name      VARCHAR(50)      NULL,
  gender         VARCHAR(10)      NULL,
  phone_enc      VARCHAR(255)     NULL,
  address_enc    VARCHAR(512)     NULL,
  role           VARCHAR(20)  NOT NULL DEFAULT 'CUSTOMER',
  status         VARCHAR(10)  NOT NULL DEFAULT 'ACTIVE',
  fail_count     INT          NOT NULL DEFAULT 0,
  lock_until     DATETIME         NULL,
  pwd_changed_at DATETIME         NULL,
  created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Categories (flat; parent_id supports an optional tree).
CREATE TABLE category (
  id        BIGINT      NOT NULL AUTO_INCREMENT,
  name      VARCHAR(50) NOT NULL,
  parent_id BIGINT          NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE book (
  id          BIGINT        NOT NULL AUTO_INCREMENT,
  title       VARCHAR(200)  NOT NULL,
  author      VARCHAR(100)      NULL,
  publisher   VARCHAR(100)      NULL,
  isbn        VARCHAR(20)       NULL,
  price       DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  stock       INT           NOT NULL DEFAULT 0,
  category_id BIGINT            NULL,
  cover_path  VARCHAR(255)      NULL,
  intro       TEXT              NULL,
  status      VARCHAR(10)   NOT NULL DEFAULT 'ON',   -- ON / OFF
  created_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_book_category FOREIGN KEY (category_id) REFERENCES category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE cart_item (
  id      BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  book_id BIGINT NOT NULL,
  qty     INT    NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  UNIQUE KEY uk_cart_user_book (user_id, book_id),
  CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES `user`(id),
  CONSTRAINT fk_cart_book FOREIGN KEY (book_id) REFERENCES book(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE orders (
  id                BIGINT        NOT NULL AUTO_INCREMENT,
  order_no          VARCHAR(32)   NOT NULL,
  user_id           BIGINT        NOT NULL,
  total             DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  status            VARCHAR(20)   NOT NULL DEFAULT 'PENDING_PAY', -- PENDING_PAY/PENDING_SHIP/SHIPPED/COMPLETED/CANCELLED
  receiver_snapshot VARCHAR(512)      NULL,
  tracking_no       VARCHAR(50)       NULL,
  created_at        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  paid_at           DATETIME          NULL,
  shipped_at        DATETIME          NULL,
  completed_at      DATETIME          NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_orders_no (order_no),
  CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES `user`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE order_item (
  id             BIGINT        NOT NULL AUTO_INCREMENT,
  order_id       BIGINT        NOT NULL,
  book_id        BIGINT        NOT NULL,
  title_snapshot VARCHAR(200)      NULL,
  price_snapshot DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  qty            INT           NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  CONSTRAINT fk_oi_order FOREIGN KEY (order_id) REFERENCES orders(id),
  CONSTRAINT fk_oi_book  FOREIGN KEY (book_id)  REFERENCES book(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Security audit log (retained per 等保三级; optional per-row HMAC-SM3 integrity).
CREATE TABLE audit_log (
  id         BIGINT       NOT NULL AUTO_INCREMENT,
  user_id    BIGINT           NULL,
  username   VARCHAR(50)      NULL,
  action     VARCHAR(50)  NOT NULL,
  detail     VARCHAR(1000)    NULL,
  ip         VARCHAR(45)      NULL,
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  hmac       VARCHAR(64)      NULL,
  PRIMARY KEY (id),
  KEY idx_audit_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
