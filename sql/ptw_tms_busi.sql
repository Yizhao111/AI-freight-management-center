/*
 Navicat Premium Data Transfer

 Source Server         : aliyuncs-rds
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : penint-al.mysql.rds.aliyuncs.com:3306
 Source Schema         : ptw_tms_busi

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 20/07/2023 13:03:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for busi_car
-- ----------------------------
DROP TABLE IF EXISTS `busi_car`;
CREATE TABLE `busi_car`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `car_num` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车牌号',
  `user_id` int NULL DEFAULT NULL COMMENT '员工id',
  `status` int NULL DEFAULT 2 COMMENT '状态，1使用中，2未使用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车辆管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of busi_car
-- ----------------------------
INSERT INTO `busi_car` VALUES (2, '京A12345', 108, 2, '2023-07-18 15:43:23');

-- ----------------------------
-- Table structure for busi_finance
-- ----------------------------
DROP TABLE IF EXISTS `busi_finance`;
CREATE TABLE `busi_finance`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `type` int NOT NULL COMMENT '类型，例如：1-收入，2-支出',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '财务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of busi_finance
-- ----------------------------
INSERT INTO `busi_finance` VALUES (1, 'SN667487138773864448', 1, 5.00, '2023-07-19 15:32:55');
INSERT INTO `busi_finance` VALUES (2, 'SN667487138773864448', 2, -5.00, '2023-07-19 15:33:14');

-- ----------------------------
-- Table structure for busi_orders
-- ----------------------------
DROP TABLE IF EXISTS `busi_orders`;
CREATE TABLE `busi_orders`  (
  `order_id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单编号',
  `total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '总额',
  `product_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品说明',
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注信息',
  `delivery_date` date NULL DEFAULT NULL COMMENT '送货日期',
  `order_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单类型',
  `delivery_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '送货地址',
  `recipient_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接收人',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  `postal_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮编',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '货物状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `lat` decimal(20, 6) NULL DEFAULT NULL COMMENT '纬度',
  `lng` decimal(20, 6) NULL DEFAULT NULL COMMENT '经度',
  `custom_user_id` int NULL DEFAULT NULL,
  `car_id` int NULL DEFAULT NULL COMMENT '车辆id',
  `car_num` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车牌号',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of busi_orders
-- ----------------------------
INSERT INTO `busi_orders` VALUES (2, 'SN667435973105291264', 6.00, NULL, '测试备注', '2023-07-21', NULL, '北京市海淀区甘家口街道甘家口8号院', '张三', '13090901234', '111001', '0', '2023-07-19 11:21:37', 39.913292, 116.405532, 3, NULL, NULL);
INSERT INTO `busi_orders` VALUES (3, 'SN667445699436220416', 3.00, NULL, NULL, '2023-07-20', NULL, '内蒙古自治区阿拉善盟阿拉善左旗温都尔勒图镇', '张三', '13090901234', '131111', '0', '2023-07-19 12:00:19', 37.378433, 106.973270, 3, NULL, NULL);
INSERT INTO `busi_orders` VALUES (4, 'SN667487138773864448', 5.00, NULL, NULL, '2023-07-21', NULL, '山西省吕梁市离石区交口街道商业街', '张三', '13090901234', '111111', '4', '2023-07-19 14:44:58', 37.520921, 111.347737, 3, 2, '京A12345');

-- ----------------------------
-- Table structure for busi_orders_goods
-- ----------------------------
DROP TABLE IF EXISTS `busi_orders_goods`;
CREATE TABLE `busi_orders_goods`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `goods_id` int NULL DEFAULT NULL COMMENT '商品id',
  `goods_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名',
  `number` int NULL DEFAULT NULL COMMENT '订货数量',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `order_id` int NULL DEFAULT NULL COMMENT '订单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of busi_orders_goods
-- ----------------------------
INSERT INTO `busi_orders_goods` VALUES (1, 34, '2022中闽峰州正宗安溪铁观音兰花香特级浓香型新茶叶乌龙茶5', 1, 1.00, 2);
INSERT INTO `busi_orders_goods` VALUES (2, 35, '功夫茶具套装家用办公室泡茶喝茶全自动一体茶桌茶几茶台茶道茶盘', 2, 1.00, 2);
INSERT INTO `busi_orders_goods` VALUES (3, 39, '测试', 3, 1.00, 2);
INSERT INTO `busi_orders_goods` VALUES (32, 34, '2022中闽峰州正宗安溪铁观音兰花香特级浓香型新茶叶乌龙茶5', 1, 1.00, 3);
INSERT INTO `busi_orders_goods` VALUES (33, 35, '功夫茶具套装家用办公室泡茶喝茶全自动一体茶桌茶几茶台茶道茶盘', 1, 1.00, 3);
INSERT INTO `busi_orders_goods` VALUES (34, 39, '测试', 1, 1.00, 3);
INSERT INTO `busi_orders_goods` VALUES (35, 34, '2022中闽峰州正宗安溪铁观音兰花香特级浓香型新茶叶乌龙茶5', 1, 1.00, 4);
INSERT INTO `busi_orders_goods` VALUES (36, 35, '功夫茶具套装家用办公室泡茶喝茶全自动一体茶桌茶几茶台茶道茶盘', 1, 1.00, 4);
INSERT INTO `busi_orders_goods` VALUES (37, 39, '测试', 3, 1.00, 4);

-- ----------------------------
-- Table structure for busi_shop_category
-- ----------------------------
DROP TABLE IF EXISTS `busi_shop_category`;
CREATE TABLE `busi_shop_category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类目名称',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '类目广告语介绍',
  `pid` int NOT NULL DEFAULT 0 COMMENT '父类目ID',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '类目图标',
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '类目图片',
  `level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'L1' COMMENT '等级',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `sort` int NOT NULL DEFAULT 9999 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '类目表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of busi_shop_category
-- ----------------------------
INSERT INTO `busi_shop_category` VALUES (1, '分类', '123', 0, '', '', 'l1', '2023-07-18 09:10:09', 0);
INSERT INTO `busi_shop_category` VALUES (7, '测试1', '1', 1, '', '', 'l2', '2023-07-18 09:10:09', 9999);
INSERT INTO `busi_shop_category` VALUES (12, 'ces', 'ces', 0, '', '', 'l1', '2023-07-18 10:52:48', 9999);
INSERT INTO `busi_shop_category` VALUES (14, '测试2', '', 1, '', '', 'l2', '2023-07-19 16:46:57', 9999);
INSERT INTO `busi_shop_category` VALUES (15, '测试3', '测试', 1, '', '', 'l2', '2023-07-19 16:47:03', 9999);
INSERT INTO `busi_shop_category` VALUES (16, '测试4', '', 12, '', '', 'l2', '2023-07-19 16:47:11', 9999);

-- ----------------------------
-- Table structure for busi_shop_goods
-- ----------------------------
DROP TABLE IF EXISTS `busi_shop_goods`;
CREATE TABLE `busi_shop_goods`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键自增ID',
  `goods_sn` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品编号',
  `name` varchar(125) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `category_id` int NULL DEFAULT 0 COMMENT '商品所属类目ID',
  `brief` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品简介',
  `is_on_sale` tinyint(1) NULL DEFAULT 1 COMMENT '是否上架，1为上架，0为不上架',
  `unit` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '件' COMMENT '商品单位，例如件、盒，默认为件',
  `sku` int NULL DEFAULT NULL COMMENT '库存',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `category_source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类目原数组',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of busi_shop_goods
-- ----------------------------
INSERT INTO `busi_shop_goods` VALUES (34, 'SP500405053287301120', '2022中闽峰州正宗安溪铁观音兰花香特级浓香型新茶叶乌龙茶5', 7, '商品具有生产许可证编号，符合食品质量安全准入标准。\n', 0, '盒', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (35, 'SP500409009438720000', '功夫茶具套装家用办公室泡茶喝茶全自动一体茶桌茶几茶台茶道茶盘', 14, '简约奢华', 1, 'ff', 111, '2022-06-23 15:10:09', '[1,14]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (39, 'SP667084123487076352', '测试', 7, '123', 1, '213', 213, '2023-07-18 12:03:29', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (64, 'SP500405053287301120', '2022中闽峰州正宗安溪铁观音兰花香特级浓香型新茶叶乌龙茶5', 7, '商品具有生产许可证编号，符合食品质量安全准入标准。\n', 0, '盒', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (65, 'SP500409009438720001', '功夫茶具套装家用办公室泡茶喝茶全自动一体茶桌茶几茶台茶道茶盘', 7, '简约奢华', 1, 'ff', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (66, 'SP500405053287301122', '2022中闽峰州正宗安溪铁观音兰花香特级浓香型新茶叶乌龙茶5', 15, '商品具有生产许可证编号，符合食品质量安全准入标准。\n', 0, '盒', 111, '2022-06-23 15:10:09', '[1,15]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (67, 'SP500409009438720001', '功夫茶具套装家用办公室泡茶喝茶全自动一体茶桌茶几茶台茶道茶盘', 7, '简约奢华', 1, 'ff', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (68, 'SP500405053287301110', '2022中闽峰州正宗安溪铁观音兰花香特级浓香型新茶叶乌龙茶5', 7, '商品具有生产许可证编号，符合食品质量安全准入标准。\n', 0, '盒', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (69, 'SP500409009438720100', '功夫茶具套装家用办公室泡茶喝茶全自动一体茶桌茶几茶台茶道茶盘', 7, '简约奢华', 1, 'ff', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (70, 'SP500405053287302120', '2022中闽峰州正宗安溪铁观音兰花香特级浓香型新茶叶乌龙茶5', 7, '商品具有生产许可证编号，符合食品质量安全准入标准。\n', 0, '盒', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (71, 'SP500409009438720500', '功夫茶具套装家用办公室泡茶喝茶全自动一体茶桌茶几茶台茶道茶盘', 7, '简约奢华', 1, 'ff', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (72, 'SP500405053287301420', '2022中闽峰州正宗安溪铁观音兰花香特级浓香型新茶叶乌龙茶5', 7, '商品具有生产许可证编号，符合食品质量安全准入标准。\n', 0, '盒', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (73, 'SP500409009438720300', '功夫茶具套装家用办公室泡茶喝茶全自动一体茶桌茶几茶台茶道茶盘', 7, '简约奢华', 1, 'ff', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (74, 'SP500405053281301120', '2022中闽峰州正宗安溪铁观音兰花香特级浓香型新茶叶乌龙茶5', 7, '商品具有生产许可证编号，符合食品质量安全准入标准。\n', 0, '盒', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (75, 'SP500409009439720000', '功夫茶具套装家用办公室泡茶喝茶全自动一体茶桌茶几茶台茶道茶盘', 7, '简约奢华', 1, 'ff', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (76, 'SP500405153287301120', '2022中闽峰州正宗安溪铁观音兰花香特级浓香型新茶叶乌龙茶5', 7, '商品具有生产许可证编号，符合食品质量安全准入标准。\n', 0, '盒', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);
INSERT INTO `busi_shop_goods` VALUES (77, 'SP500409209438720000', '功夫茶具套装家用办公室泡茶喝茶全自动一体茶桌茶几茶台茶道茶盘', 7, '简约奢华', 1, 'ff', 111, '2022-06-23 15:10:09', '[1,7]', 1.00);

SET FOREIGN_KEY_CHECKS = 1;
