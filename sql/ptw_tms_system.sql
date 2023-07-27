/*
 Navicat Premium Data Transfer

 Source Server         : aliyuncs-rds
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : penint-al.mysql.rds.aliyuncs.com:3306
 Source Schema         : ptw_tms_system

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 20/07/2023 12:05:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `resource_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `client_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `authorized_grant_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `web_server_redirect_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `authorities` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `access_token_validity` int NOT NULL,
  `refresh_token_validity` int NULL DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `autoapprove` tinyint NULL DEFAULT NULL,
  `origin_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户端配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('app', '', '$2a$10$iqjytcNdDXPZHs1yw0YTP.DSoI90Hk5ajCf4kviPuJxgHdhh8GHMK', 'auth,admin,busi', 'refresh_token,password,authorization_code,client_credentials,implicit', 'http://www.baidu.com', NULL, 86400, 864000, NULL, 1, '123456');

-- ----------------------------
-- Table structure for sys_custom_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_custom_user`;
CREATE TABLE `sys_custom_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位名称',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '移动电话',
  `tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '座机',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_custom_user
-- ----------------------------
INSERT INTO `sys_custom_user` VALUES (3, 'zhangsan', NULL, '张三', '测试科技有限责任公司', '13090901234', '010-1234567', '110909199812120909', '2023-07-17 21:24:16');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮id',
  `parent_id` int NOT NULL COMMENT '上级菜单id',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单/按钮名称',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应路由path',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应路由组件component',
  `perms` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `type` int NOT NULL COMMENT '类型 0菜单 1按钮',
  `order_num` int NULL DEFAULT NULL COMMENT '排序',
  `is_link` tinyint(1) NULL DEFAULT 0 COMMENT '是否是外链',
  `is_visible` tinyint(1) NULL DEFAULT 1 COMMENT '是否可见,1为可见，0为不可见',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 677 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', '/system', '', NULL, 'system', 0, 1, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (3, 1, '用户管理', '/system/user', 'system/user/index', '', 'user', 0, 1, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (4, 1, '角色管理', '/system/role', 'system/role/index', '', 'peoples', 0, 2, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (5, 1, '菜单管理', '/system/menu', 'system/menu/index', '', 'tree-table', 0, 3, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (11, 3, '新增用户', '', '', 'system:user:add', NULL, 1, 3, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (12, 3, '修改用户', '', '', 'system:user:edit', NULL, 1, 5, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (13, 3, '删除用户', '', '', 'system:user:remove', NULL, 1, 6, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (14, 4, '新增角色', '', '', 'system:role:add', NULL, 1, 2, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (15, 4, '修改角色', '', '', 'system:role:edit', NULL, 1, 4, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (16, 4, '删除角色', '', '', 'system:role:remove', NULL, 1, 3, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (17, 5, '新增菜单', '', '', 'system:menu:add', NULL, 1, 5, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (18, 5, '修改菜单', '', '', 'system:menu:edit', NULL, 1, 2, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (19, 5, '删除菜单', '', '', 'system:menu:remove', NULL, 1, 3, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (144, 4, '导出Excel', NULL, NULL, 'system:role:export', NULL, 1, 5, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (148, 3, '密码重置', NULL, NULL, 'system:user:resetPwd', NULL, 1, 4, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (177, 5, '菜单查询详情', NULL, NULL, 'system:menu:query', NULL, 1, 4, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (184, 3, '查询详细信息', NULL, NULL, 'system:user:query', NULL, 1, 2, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (192, 3, '查看列表', NULL, NULL, 'system:user:view', NULL, 1, 1, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (193, 4, '查看列表', NULL, NULL, 'system:role:view', NULL, 1, 1, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (194, 5, '查看列表', NULL, NULL, 'system:menu:view', NULL, 1, 1, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (545, 1, '客户端管理', '/system/client', 'system/client/index', NULL, 'code', 0, 9, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (547, 545, '列表', NULL, NULL, 'client:view', NULL, 1, 1, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (548, 545, '解密', NULL, NULL, 'client:decrypt', NULL, 1, 2, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (549, 545, '删除', NULL, NULL, 'client:delete', NULL, 1, 3, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (550, 545, '修改', NULL, NULL, 'client:update', NULL, 1, 4, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (551, 4, '查看详情', NULL, NULL, 'system:role:query', NULL, 1, 6, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (552, 545, '新增', NULL, NULL, 'client:add', NULL, 1, 6, 0, 1, '2023-07-17 00:00:00', '2023-07-17 00:00:00');
INSERT INTO `sys_menu` VALUES (624, 0, '客户维护', '/customUser', NULL, NULL, 'people', 0, 2, 0, 1, '2023-07-17 16:56:51', '2023-07-17 16:57:22');
INSERT INTO `sys_menu` VALUES (625, 624, '客户维护', '/customUser/index', 'customUser/index', NULL, 'people', 0, 1, 0, 1, '2023-07-17 16:57:51', NULL);
INSERT INTO `sys_menu` VALUES (626, 625, '客户列表查询', NULL, NULL, 'auth:customUser:list', NULL, 1, 1, 0, 1, '2023-07-17 17:20:00', '2023-07-17 17:26:11');
INSERT INTO `sys_menu` VALUES (627, 625, '客户详细信息查询', NULL, NULL, 'auth:customUser:query', NULL, 1, 2, 0, 1, '2023-07-17 17:20:19', NULL);
INSERT INTO `sys_menu` VALUES (628, 625, '新增客户', NULL, NULL, 'auth:customUser:add', NULL, 1, 3, 0, 1, '2023-07-17 17:20:36', NULL);
INSERT INTO `sys_menu` VALUES (629, 625, '修改客户', NULL, NULL, 'auth:customUser:edit', NULL, 1, 4, 0, 1, '2023-07-17 17:20:50', NULL);
INSERT INTO `sys_menu` VALUES (630, 625, '删除客户', NULL, NULL, 'auth:customUser:remove', NULL, 1, 5, 0, 1, '2023-07-17 17:21:09', NULL);
INSERT INTO `sys_menu` VALUES (631, 0, '库房管理', '/goods', NULL, NULL, 'clipboard', 0, 3, 0, 1, '2023-07-18 10:03:33', '2023-07-18 12:33:08');
INSERT INTO `sys_menu` VALUES (632, 631, '类目', '/goods/category', 'goods/category', NULL, 'nested', 0, 1, 0, 1, '2023-07-18 10:04:20', '2023-07-18 11:01:46');
INSERT INTO `sys_menu` VALUES (633, 632, '查询类目表列表', NULL, NULL, 'busi:shopCategory:list', NULL, 1, 1, 0, 1, '2023-07-18 10:04:54', NULL);
INSERT INTO `sys_menu` VALUES (634, 632, '获取类目表详细信息', NULL, NULL, 'busi:shopCategory:query', NULL, 1, 2, 0, 1, '2023-07-18 10:05:05', NULL);
INSERT INTO `sys_menu` VALUES (635, 632, '新增类目表', NULL, NULL, 'busi:shopCategory:add', NULL, 1, 3, 0, 1, '2023-07-18 10:05:18', NULL);
INSERT INTO `sys_menu` VALUES (636, 632, '修改类目表', NULL, NULL, 'busi:shopCategory:edit', NULL, 1, 4, 0, 1, '2023-07-18 10:05:31', NULL);
INSERT INTO `sys_menu` VALUES (637, 632, '删除类目表', NULL, NULL, 'busi:shopCategory:remove', NULL, 1, 5, 0, 1, '2023-07-18 10:05:43', NULL);
INSERT INTO `sys_menu` VALUES (638, 631, '商品', '/goods/index', 'goods/index', NULL, 'guide', 0, 2, 0, 1, '2023-07-18 11:02:11', NULL);
INSERT INTO `sys_menu` VALUES (639, 638, '查询商品表列表', NULL, NULL, 'busi:shopGoods:list', NULL, 1, 1, 0, 1, '2023-07-18 11:02:38', NULL);
INSERT INTO `sys_menu` VALUES (640, 638, '获取商品表详细信息', NULL, NULL, 'busi:shopGoods:query', NULL, 1, 2, 0, 1, '2023-07-18 11:02:56', NULL);
INSERT INTO `sys_menu` VALUES (641, 638, '新增商品表', NULL, NULL, 'busi:shopGoods:add', NULL, 1, 3, 0, 1, '2023-07-18 11:03:09', NULL);
INSERT INTO `sys_menu` VALUES (642, 638, '修改商品表', NULL, NULL, 'busi:shopGoods:edit', NULL, 1, 4, 0, 1, '2023-07-18 11:03:19', NULL);
INSERT INTO `sys_menu` VALUES (643, 638, '删除商品表', NULL, NULL, 'busi:shopGoods:remove', NULL, 1, 5, 0, 1, '2023-07-18 11:03:32', NULL);
INSERT INTO `sys_menu` VALUES (644, 0, '调度车辆管理', '/car', NULL, NULL, 'exit-fullscreen', 0, 4, 0, 1, '2023-07-18 13:34:53', '2023-07-18 13:35:30');
INSERT INTO `sys_menu` VALUES (645, 644, '调度车辆管理', '/car/index', 'car/index', NULL, 'exit-fullscreen', 0, 1, 0, 1, '2023-07-18 13:35:20', NULL);
INSERT INTO `sys_menu` VALUES (646, 645, '查询车辆管理列表', NULL, NULL, 'busi:car:list', NULL, 1, 1, 0, 1, '2023-07-18 13:37:47', NULL);
INSERT INTO `sys_menu` VALUES (647, 645, '获取车辆管理详细信息', NULL, NULL, 'busi:car:query', NULL, 1, 2, 0, 1, '2023-07-18 13:37:59', NULL);
INSERT INTO `sys_menu` VALUES (648, 645, '新增车辆管理', NULL, NULL, 'busi:car:add', NULL, 1, 3, 0, 1, '2023-07-18 13:38:11', NULL);
INSERT INTO `sys_menu` VALUES (649, 645, '修改车辆管理', NULL, NULL, 'busi:car:edit', NULL, 1, 4, 0, 1, '2023-07-18 13:38:23', NULL);
INSERT INTO `sys_menu` VALUES (650, 645, '删除车辆管理', NULL, NULL, 'busi:car:remove', NULL, 1, 5, 0, 1, '2023-07-18 13:38:34', NULL);
INSERT INTO `sys_menu` VALUES (651, 0, '订单管理', '/orders', NULL, NULL, 'code', 0, 5, 0, 1, '2023-07-18 17:01:53', NULL);
INSERT INTO `sys_menu` VALUES (652, 651, '新订', '/orders/new', 'orders/new', NULL, 'edit', 0, 1, 0, 1, '2023-07-18 17:02:32', NULL);
INSERT INTO `sys_menu` VALUES (653, 652, '查询列表', NULL, NULL, 'busi:orders:list', NULL, 1, 1, 0, 1, '2023-07-18 17:03:01', NULL);
INSERT INTO `sys_menu` VALUES (654, 652, '获取详细信息', NULL, NULL, 'busi:orders:query', NULL, 1, 2, 0, 1, '2023-07-18 17:03:12', NULL);
INSERT INTO `sys_menu` VALUES (655, 652, '新增', NULL, NULL, 'busi:orders:add', NULL, 1, 3, 0, 1, '2023-07-18 17:03:25', NULL);
INSERT INTO `sys_menu` VALUES (656, 652, '修改', NULL, NULL, 'busi:orders:edit', NULL, 1, 4, 0, 1, '2023-07-18 17:03:39', NULL);
INSERT INTO `sys_menu` VALUES (657, 652, '删除', NULL, NULL, 'busi:orders:remove', NULL, 1, 5, 0, 1, '2023-07-18 17:03:51', NULL);
INSERT INTO `sys_menu` VALUES (658, 651, '调度', '/orders/dispatch', 'orders/dispatch', NULL, 'list', 0, 2, 0, 1, '2023-07-18 17:20:55', '2023-07-19 12:50:44');
INSERT INTO `sys_menu` VALUES (659, 631, '库房数据看板', 'http://localhost/dataV', NULL, NULL, 'chart', 0, 3, 1, 1, '2023-07-18 22:00:23', '2023-07-19 14:23:27');
INSERT INTO `sys_menu` VALUES (660, 631, '用户地图', '/goods/map', 'goods/map', NULL, 'date-range', 0, 4, 0, 1, '2023-07-18 22:11:32', '2023-07-18 22:11:48');
INSERT INTO `sys_menu` VALUES (661, 658, '查询列表', NULL, NULL, 'busi:orders:list', NULL, 1, 1, 0, 1, '2023-07-19 12:21:35', NULL);
INSERT INTO `sys_menu` VALUES (662, 658, '获取详细信息', NULL, NULL, 'busi:orders:query', NULL, 1, 2, 0, 1, '2023-07-19 12:21:46', NULL);
INSERT INTO `sys_menu` VALUES (663, 658, '修改', NULL, NULL, 'busi:orders:edit', NULL, 1, 3, 0, 1, '2023-07-19 12:22:21', '2023-07-19 12:22:31');
INSERT INTO `sys_menu` VALUES (664, 651, '配送', '/orders/delivery', 'orders/delivery', NULL, 'guide', 0, 3, 0, 1, '2023-07-18 17:20:55', '2023-07-19 14:05:50');
INSERT INTO `sys_menu` VALUES (665, 664, '查询列表', NULL, NULL, 'busi:orders:list', NULL, 1, 1, 0, 1, '2023-07-19 12:21:35', NULL);
INSERT INTO `sys_menu` VALUES (666, 664, '获取详细信息', NULL, NULL, 'busi:orders:query', NULL, 1, 2, 0, 1, '2023-07-19 12:21:46', NULL);
INSERT INTO `sys_menu` VALUES (667, 664, '修改', NULL, NULL, 'busi:orders:edit', NULL, 1, 3, 0, 1, '2023-07-19 12:22:21', '2023-07-19 12:22:31');
INSERT INTO `sys_menu` VALUES (668, 651, '录入', '/orders/list', 'orders/list', NULL, 'input', 0, 4, 0, 1, '2023-07-19 14:06:21', NULL);
INSERT INTO `sys_menu` VALUES (669, 668, '查询列表', NULL, NULL, 'busi:orders:list', NULL, 1, 1, 0, 1, '2023-07-19 12:21:35', NULL);
INSERT INTO `sys_menu` VALUES (670, 668, '获取详细信息', NULL, NULL, 'busi:orders:query', NULL, 1, 2, 0, 1, '2023-07-19 12:21:46', NULL);
INSERT INTO `sys_menu` VALUES (671, 668, '修改', NULL, NULL, 'busi:orders:edit', NULL, 1, 3, 0, 1, '2023-07-19 12:22:21', '2023-07-19 12:22:31');
INSERT INTO `sys_menu` VALUES (672, 668, '删除', NULL, NULL, 'busi:orders:remove', NULL, 1, 4, 0, 1, '2023-07-19 14:07:33', NULL);
INSERT INTO `sys_menu` VALUES (673, 0, '财务管理', '/finance', NULL, NULL, 'money', 0, 6, 0, 1, '2023-07-19 15:34:05', NULL);
INSERT INTO `sys_menu` VALUES (674, 673, '财务管理', '/finance/index', 'finance/index', NULL, 'money', 0, 1, 0, 1, '2023-07-19 15:34:31', NULL);
INSERT INTO `sys_menu` VALUES (675, 674, '查询财务表列表', NULL, NULL, 'busi:finance:list', NULL, 1, 1, 0, 1, '2023-07-19 15:34:54', NULL);
INSERT INTO `sys_menu` VALUES (2052, 0, '问卷', '/ques', NULL, '', 'education', 0, 7, 0, 1, '2023-07-20 10:54:38', '2023-07-20 10:59:50');
INSERT INTO `sys_menu` VALUES (2053, 2052, '题目列表', '/ques/qu-admin', 'ques/qu-admin', NULL, 'list', 0, 1, 0, 1, '2023-07-20 10:57:13', '2023-07-20 10:59:01');
INSERT INTO `sys_menu` VALUES (2054, 2052, '问卷列表', '/ques/exam', 'ques/exam', NULL, 'checkbox', 0, 2, 0, 1, '2023-07-20 10:57:51', NULL);
INSERT INTO `sys_menu` VALUES (2055, 0, '我的问卷', '/my-qu', NULL, NULL, 'documentation', 0, 8, 0, 1, '2023-07-20 10:59:44', NULL);
INSERT INTO `sys_menu` VALUES (2056, 2055, '问卷列表', '/my-ques/ques-list', 'my/ques-list', NULL, 'list', 0, 1, 0, 1, '2023-07-20 11:00:34', NULL);
INSERT INTO `sys_menu` VALUES (2057, 2055, '我的回答', '/my-ques/answer', 'my/answer', NULL, 'question', 0, 2, 0, 1, '2023-07-20 11:07:37', NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `data_scope` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', '超级管理员', '1', '2023-07-17 00:00:00', '2023-07-20 11:08:05');
INSERT INTO `sys_role` VALUES (4, '员工', NULL, NULL, '2023-07-18 12:44:05', '2023-07-20 11:48:47');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `role_id` int NOT NULL COMMENT '角色Id',
  `menu_id` int NOT NULL COMMENT '菜单Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45614 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (45614, 1, 1);
INSERT INTO `sys_role_menu` VALUES (45615, 1, 3);
INSERT INTO `sys_role_menu` VALUES (45616, 1, 192);
INSERT INTO `sys_role_menu` VALUES (45617, 1, 184);
INSERT INTO `sys_role_menu` VALUES (45618, 1, 11);
INSERT INTO `sys_role_menu` VALUES (45619, 1, 148);
INSERT INTO `sys_role_menu` VALUES (45620, 1, 12);
INSERT INTO `sys_role_menu` VALUES (45621, 1, 13);
INSERT INTO `sys_role_menu` VALUES (45622, 1, 4);
INSERT INTO `sys_role_menu` VALUES (45623, 1, 193);
INSERT INTO `sys_role_menu` VALUES (45624, 1, 14);
INSERT INTO `sys_role_menu` VALUES (45625, 1, 16);
INSERT INTO `sys_role_menu` VALUES (45626, 1, 15);
INSERT INTO `sys_role_menu` VALUES (45627, 1, 144);
INSERT INTO `sys_role_menu` VALUES (45628, 1, 551);
INSERT INTO `sys_role_menu` VALUES (45629, 1, 5);
INSERT INTO `sys_role_menu` VALUES (45630, 1, 194);
INSERT INTO `sys_role_menu` VALUES (45631, 1, 18);
INSERT INTO `sys_role_menu` VALUES (45632, 1, 19);
INSERT INTO `sys_role_menu` VALUES (45633, 1, 177);
INSERT INTO `sys_role_menu` VALUES (45634, 1, 17);
INSERT INTO `sys_role_menu` VALUES (45635, 1, 545);
INSERT INTO `sys_role_menu` VALUES (45636, 1, 547);
INSERT INTO `sys_role_menu` VALUES (45637, 1, 548);
INSERT INTO `sys_role_menu` VALUES (45638, 1, 549);
INSERT INTO `sys_role_menu` VALUES (45639, 1, 550);
INSERT INTO `sys_role_menu` VALUES (45640, 1, 552);
INSERT INTO `sys_role_menu` VALUES (45641, 1, 624);
INSERT INTO `sys_role_menu` VALUES (45642, 1, 625);
INSERT INTO `sys_role_menu` VALUES (45643, 1, 626);
INSERT INTO `sys_role_menu` VALUES (45644, 1, 627);
INSERT INTO `sys_role_menu` VALUES (45645, 1, 628);
INSERT INTO `sys_role_menu` VALUES (45646, 1, 629);
INSERT INTO `sys_role_menu` VALUES (45647, 1, 630);
INSERT INTO `sys_role_menu` VALUES (45648, 1, 631);
INSERT INTO `sys_role_menu` VALUES (45649, 1, 632);
INSERT INTO `sys_role_menu` VALUES (45650, 1, 633);
INSERT INTO `sys_role_menu` VALUES (45651, 1, 634);
INSERT INTO `sys_role_menu` VALUES (45652, 1, 635);
INSERT INTO `sys_role_menu` VALUES (45653, 1, 636);
INSERT INTO `sys_role_menu` VALUES (45654, 1, 637);
INSERT INTO `sys_role_menu` VALUES (45655, 1, 638);
INSERT INTO `sys_role_menu` VALUES (45656, 1, 639);
INSERT INTO `sys_role_menu` VALUES (45657, 1, 640);
INSERT INTO `sys_role_menu` VALUES (45658, 1, 641);
INSERT INTO `sys_role_menu` VALUES (45659, 1, 642);
INSERT INTO `sys_role_menu` VALUES (45660, 1, 643);
INSERT INTO `sys_role_menu` VALUES (45661, 1, 659);
INSERT INTO `sys_role_menu` VALUES (45662, 1, 660);
INSERT INTO `sys_role_menu` VALUES (45663, 1, 644);
INSERT INTO `sys_role_menu` VALUES (45664, 1, 645);
INSERT INTO `sys_role_menu` VALUES (45665, 1, 646);
INSERT INTO `sys_role_menu` VALUES (45666, 1, 647);
INSERT INTO `sys_role_menu` VALUES (45667, 1, 648);
INSERT INTO `sys_role_menu` VALUES (45668, 1, 649);
INSERT INTO `sys_role_menu` VALUES (45669, 1, 650);
INSERT INTO `sys_role_menu` VALUES (45670, 1, 651);
INSERT INTO `sys_role_menu` VALUES (45671, 1, 652);
INSERT INTO `sys_role_menu` VALUES (45672, 1, 653);
INSERT INTO `sys_role_menu` VALUES (45673, 1, 654);
INSERT INTO `sys_role_menu` VALUES (45674, 1, 655);
INSERT INTO `sys_role_menu` VALUES (45675, 1, 656);
INSERT INTO `sys_role_menu` VALUES (45676, 1, 657);
INSERT INTO `sys_role_menu` VALUES (45677, 1, 658);
INSERT INTO `sys_role_menu` VALUES (45678, 1, 661);
INSERT INTO `sys_role_menu` VALUES (45679, 1, 662);
INSERT INTO `sys_role_menu` VALUES (45680, 1, 663);
INSERT INTO `sys_role_menu` VALUES (45681, 1, 664);
INSERT INTO `sys_role_menu` VALUES (45682, 1, 665);
INSERT INTO `sys_role_menu` VALUES (45683, 1, 666);
INSERT INTO `sys_role_menu` VALUES (45684, 1, 667);
INSERT INTO `sys_role_menu` VALUES (45685, 1, 668);
INSERT INTO `sys_role_menu` VALUES (45686, 1, 669);
INSERT INTO `sys_role_menu` VALUES (45687, 1, 670);
INSERT INTO `sys_role_menu` VALUES (45688, 1, 671);
INSERT INTO `sys_role_menu` VALUES (45689, 1, 672);
INSERT INTO `sys_role_menu` VALUES (45690, 1, 673);
INSERT INTO `sys_role_menu` VALUES (45691, 1, 674);
INSERT INTO `sys_role_menu` VALUES (45692, 1, 675);
INSERT INTO `sys_role_menu` VALUES (45693, 1, 2052);
INSERT INTO `sys_role_menu` VALUES (45694, 1, 2053);
INSERT INTO `sys_role_menu` VALUES (45695, 1, 2054);
INSERT INTO `sys_role_menu` VALUES (45696, 1, 2055);
INSERT INTO `sys_role_menu` VALUES (45697, 1, 2056);
INSERT INTO `sys_role_menu` VALUES (45698, 1, 2057);
INSERT INTO `sys_role_menu` VALUES (45699, 4, 626);
INSERT INTO `sys_role_menu` VALUES (45700, 4, 627);
INSERT INTO `sys_role_menu` VALUES (45701, 4, 628);
INSERT INTO `sys_role_menu` VALUES (45702, 4, 652);
INSERT INTO `sys_role_menu` VALUES (45703, 4, 653);
INSERT INTO `sys_role_menu` VALUES (45704, 4, 654);
INSERT INTO `sys_role_menu` VALUES (45705, 4, 655);
INSERT INTO `sys_role_menu` VALUES (45706, 4, 656);
INSERT INTO `sys_role_menu` VALUES (45707, 4, 657);
INSERT INTO `sys_role_menu` VALUES (45708, 4, 658);
INSERT INTO `sys_role_menu` VALUES (45709, 4, 661);
INSERT INTO `sys_role_menu` VALUES (45710, 4, 662);
INSERT INTO `sys_role_menu` VALUES (45711, 4, 663);
INSERT INTO `sys_role_menu` VALUES (45712, 4, 664);
INSERT INTO `sys_role_menu` VALUES (45713, 4, 665);
INSERT INTO `sys_role_menu` VALUES (45714, 4, 666);
INSERT INTO `sys_role_menu` VALUES (45715, 4, 667);
INSERT INTO `sys_role_menu` VALUES (45716, 4, 2055);
INSERT INTO `sys_role_menu` VALUES (45717, 4, 2056);
INSERT INTO `sys_role_menu` VALUES (45718, 4, 2057);
INSERT INTO `sys_role_menu` VALUES (45719, 4, 624);
INSERT INTO `sys_role_menu` VALUES (45720, 4, 625);
INSERT INTO `sys_role_menu` VALUES (45721, 4, 651);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `nick_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称/真实姓名',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `last_login_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上次登录Ip',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最近访问时间',
  `ssex` int NULL DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '用户头像',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '状态 0锁定 1有效,默认为1',
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 109 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (44, 'admin', 'admin', '$2a$10$o9dR3uSdli.hzQjLLeK5Zu7CZJQbIqkAhexoD5X84YaHyg3QOeR/6', '123@qq.com', '123123', '127.0.0.1', '2023-07-20 11:52:50', 0, 'www.penint.vip', '', '2023-07-17 00:00:00', '2023-07-17 00:00:00', '1', 0);
INSERT INTO `sys_user` VALUES (107, 'ces1', '测试1', '$2a$10$fmPXljsRW8ShXFJHUrIzxO/qabjmj4L02Yo6ObHTSvC.rfuEEkQ9y', '1230@qq.com', '13090901234', '127.0.0.1', '2023-07-17 00:00:00', NULL, NULL, '', '2023-07-17 00:00:00', '2023-07-17 00:00:00', '1', 1);
INSERT INTO `sys_user` VALUES (108, 'zhangsan', '张三', '$2a$10$yHkcVWtGdCvN0TjpEChtc.J9NyqNKO9R19tEY4fJXJCBqCWhPa/k6', '110@qq.com', '13099991111', NULL, NULL, NULL, NULL, '', '2023-07-18 12:44:42', NULL, '1', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `user_id` int NOT NULL COMMENT '用户id',
  `role_id` int NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (97, 44, 1);
INSERT INTO `sys_user_role` VALUES (105, 108, 4);

SET FOREIGN_KEY_CHECKS = 1;
