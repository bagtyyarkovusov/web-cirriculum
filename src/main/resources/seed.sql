-- Online Bookstore — seed data. Run AFTER schema.sql:
--   mysql -uroot < schema.sql && mysql -uroot < seed.sql
SET NAMES utf8mb4;
USE bookstore;

-- Categories (flat) ---------------------------------------------------------
INSERT INTO category (id, name, parent_id) VALUES
 (1, '计算机',   NULL),
 (2, '文学艺术', NULL),
 (3, '科技',     NULL),
 (4, '外语',     NULL),
 (5, '考试辅导', NULL);

-- Books (status ON = on sale; one OFF row proves the user-facing filter) -----
INSERT INTO book (id, title, author, publisher, isbn, price, stock, category_id, cover_path, intro, status) VALUES
 (1, 'Java核心技术 卷I（原书第12版）', 'Cay S. Horstmann', '机械工业出版社', '9787111709473', 149.00, 50, 1, NULL, 'Java入门与进阶的经典教程。', 'ON'),
 (2, '深入理解Java虚拟机（第3版）',     '周志明',           '机械工业出版社', '9787111641247', 129.00, 30, 1, NULL, 'JVM 原理与实战。', 'ON'),
 (3, '算法导论（原书第3版）',           'Thomas H. Cormen', '机械工业出版社', '9787111407010', 128.00, 20, 1, NULL, '算法领域的权威著作。', 'ON'),
 (4, '活着',                           '余华',             '作家出版社',     '9787506365437',  45.00, 80, 2, NULL, '一部关于生命与苦难的小说。', 'ON'),
 (5, '三体（全三册）',                 '刘慈欣',           '重庆出版社',     '9787229137939', 168.00, 60, 2, NULL, '中国科幻里程碑之作。', 'ON'),
 (6, '时间简史',                       '史蒂芬·霍金',      '湖南科学技术出版社', '9787535732309', 45.00, 40, 3, NULL, '探索宇宙与时间的本质。', 'ON'),
 (7, '新概念英语（2）',                '亚历山大、何其莘',  '外语教学与研究出版社', '9787560022086', 38.00, 100, 4, NULL, '经典英语学习教材。', 'ON'),
 (8, '考研数学复习全书',               '李永乐',           '中国农业出版社', '9787109267701',  89.00, 25, 5, NULL, '考研数学系统复习用书。', 'ON'),
 (9, '已下架示例图书',                 '示例',             '示例出版社',     '0000000000000',   1.00,  0, 1, NULL, '该书已下架，用户端不可见。', 'OFF');

-- Accounts: one per role. Dev login password for all four = "Bookstore@123".
-- password_sm3 = SM3(salt + password); phone_enc = SM4(phone) as Base64(IV ‖ ciphertext),
-- generated with the project's own Sm3Util / Sm4Util against the configured sm4.key.
INSERT INTO `user`
 (id, username, password_sm3, salt, real_name, gender, phone_enc, address_enc, role, status, pwd_changed_at) VALUES
 (1, 'customer', 'da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93', '74ae25abc1b9080dcc90715881962b30', '张三',       'M', 'CMrkDR2GIUqkqPDuFHU02RkvAMXLthXv3AA0gPzcP2c=', NULL, 'CUSTOMER',       'ACTIVE', NOW()),
 (2, 'operator', '71128fa23502a7d52ecad197b7bfad87312b292632f9870bd111cae62d49afcb', '3c8d7cfeface30b4bd4d4b99a2975f4d', '运营小李',   'F', 'VsxRrmLzs69DA+f70F2/8MpaIhgPZsNItagkx+858g0=', NULL, 'OPERATOR_ADMIN', 'ACTIVE', NOW()),
 (3, 'sysadmin', '17d736a9a3c8088994a46b40bab10d376e4e5a0420e36c0af1e0f7beb974f224', '9e495e1d9d34d27642d0f1db8e9862e7', '系统管理员', 'M', 'gt42I0mTS2nJaJa1x8syntYwLBguetCbMwVQ82kmjnA=', NULL, 'SYSTEM_ADMIN',   'ACTIVE', NOW()),
 (4, 'auditor',  '57bea300cff3f3f5c048c8ea1d3c72c66c9ad114cffadf985488d8a72e2f2712', 'f0bea610b392a9be65b6657e87f88557', '审计员',     'F', '42BHvTuMi4xNcKZgNrVruLtMV2oOWmSzcRuk6s3wqi8=', NULL, 'AUDIT_ADMIN',    'ACTIVE', NOW());
