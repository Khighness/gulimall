SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of pms_attr
-- ----------------------------
INSERT INTO `pms_attr` VALUES (1, '入网参数', 0, 1, 'xxx', '4G;5G', 1, 1, 225, 1);
INSERT INTO `pms_attr` VALUES (2, '上市年份', 0, 1, 'xxx', '2019;2020;2021', 1, 1, 225, 1);
INSERT INTO `pms_attr` VALUES (3, '颜色', 1, 0, 'https://mall-fire.oss-cn-shenzhen.aliyuncs.com/1.jpg', '极光蓝;钛空银;霓影紫;流光幻镜', 1, 1, 225, 1);
INSERT INTO `pms_attr` VALUES (4, '颜色', 0, 0, 'https://mall-fire.oss-cn-shenzhen.aliyuncs.com/1.jpg', '极光蓝;钛空银;霓影紫;流光幻镜', 0, 1, 225, 0);
INSERT INTO `pms_attr` VALUES (5, '内存', 0, 0, 'xxx', '4G;6G;8G;12G', 0, 1, 225, 0);
INSERT INTO `pms_attr` VALUES (6, '套餐', 0, 0, 'xxx', '套餐一;套餐二;套餐三', 0, 1, 225, 0);
INSERT INTO `pms_attr` VALUES (7, 'CPU型号', 1, 0, 'xxx', '麒麟990;麒麟820', 1, 1, 225, 1);
INSERT INTO `pms_attr` VALUES (8, 'CPU工艺', 1, 0, 'xxx', '7nm;5nm;3nm', 1, 1, 225, 1);
INSERT INTO `pms_attr` VALUES (9, '电池容量', 1, 0, 'xxx', '3500mAh;4000mAh;5000mAh', 1, 1, 225, 1);
INSERT INTO `pms_attr` VALUES (10, '机身长度(mm)', 1, 0, 'xxx', '168;155;172', 1, 1, 225, 1);
INSERT INTO `pms_attr` VALUES (11, '版本', 0, 0, 'https://mall-fire.oss-cn-shenzhen.aliyuncs.com/2020-06-05/e5ba78ec-9cf5-4bbd-86a7-ad2f611e2512_%E5%9B%BE%E9%9B%861.jpg', '8GB+128GB;8GB+256GB;12GB+256GB', 0, 1, 225, 0);
INSERT INTO `pms_attr` VALUES (12, 'CPU品牌', 1, 0, 'xxx', '海思(Hisilicon);高通(Qualcomm)', 1, 1, 225, 1);

-- ----------------------------
-- Records of pms_attr_attrgroup_relation
-- ----------------------------
INSERT INTO `pms_attr_attrgroup_relation` VALUES (6, 3, 1, 0);
INSERT INTO `pms_attr_attrgroup_relation` VALUES (7, 2, 1, 0);
INSERT INTO `pms_attr_attrgroup_relation` VALUES (8, 1, 2, 0);
INSERT INTO `pms_attr_attrgroup_relation` VALUES (9, 7, 4, 0);
INSERT INTO `pms_attr_attrgroup_relation` VALUES (10, 8, 4, 0);
INSERT INTO `pms_attr_attrgroup_relation` VALUES (11, 9, 2, 0);
INSERT INTO `pms_attr_attrgroup_relation` VALUES (12, 10, 2, 0);
INSERT INTO `pms_attr_attrgroup_relation` VALUES (13, 12, 4, 0);

-- ----------------------------
-- Records of pms_attr_group
-- ----------------------------
INSERT INTO `pms_attr_group` VALUES (1, '信息', 0, '荣耀', 'https://mall-fire.oss-cn-shenzhen.aliyuncs.com/1.jpg', 225);
INSERT INTO `pms_attr_group` VALUES (2, '基本信息', 0, '华为', 'https://mall-fire.oss-cn-shenzhen.aliyuncs.com/1.jpg', 225);
INSERT INTO `pms_attr_group` VALUES (3, '香奈儿', 0, 'fire', 'https://mall-fire.oss-cn-shenzhen.aliyuncs.com/1.jpg', 637);
INSERT INTO `pms_attr_group` VALUES (4, '芯片', 0, 'CPU型号', 'https://mall-fire.oss-cn-shenzhen.aliyuncs.com/1.jpg', 225);

-- ----------------------------
-- Records of pms_category_brand_relation
-- ----------------------------
INSERT INTO `pms_category_brand_relation` VALUES (1, 2, 225, '华为', '手机');
INSERT INTO `pms_category_brand_relation` VALUES (2, 1, 225, '荣耀', '手机');

-- ----------------------------
-- Records of pms_product_attr_value
-- ----------------------------
INSERT INTO `pms_product_attr_value` VALUES (29, 3, 2, '上市年份', '2020', 0, 1);
INSERT INTO `pms_product_attr_value` VALUES (30, 3, 3, '颜色', '流光幻镜', 0, 1);
INSERT INTO `pms_product_attr_value` VALUES (31, 3, 1, '入网参数', '5G', 0, 1);
INSERT INTO `pms_product_attr_value` VALUES (32, 3, 9, '电池容量', '5000mAh', 0, 1);
INSERT INTO `pms_product_attr_value` VALUES (33, 3, 10, '机身长度(mm)', '168', 0, 1);
INSERT INTO `pms_product_attr_value` VALUES (34, 3, 7, 'CPU型号', '麒麟990', 0, 1);
INSERT INTO `pms_product_attr_value` VALUES (35, 3, 8, 'CPU工艺', '5nm', 0, 1);
INSERT INTO `pms_product_attr_value` VALUES (36, 3, 12, 'CPU品牌', '海思(Hisilicon)', 0, 1);

-- ----------------------------
-- Records of pms_sku_images
-- ----------------------------
INSERT INTO `pms_sku_images` VALUES (1, 1, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', 0, 1);
INSERT INTO `pms_sku_images` VALUES (2, 1, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (3, 1, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%863.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (4, 1, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%864.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (5, 1, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%865.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (6, 1, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%866.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (7, 1, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (8, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (9, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (10, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%863.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (11, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%864.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (12, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%866.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (13, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (14, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (15, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (16, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%863.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (17, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%864.jpg', 0, 1);
INSERT INTO `pms_sku_images` VALUES (18, 3, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%865.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (19, 3, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%866.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (20, 3, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (21, 3, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%868.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (22, 3, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (23, 3, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (24, 3, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%863.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (25, 3, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%864.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (26, 4, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%866.jpg', 0, 1);
INSERT INTO `pms_sku_images` VALUES (27, 4, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (28, 4, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%868.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (29, 4, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (30, 4, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (31, 4, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%863.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (32, 4, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%864.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (33, 4, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%865.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (34, 5, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%866.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (35, 5, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', 0, 1);
INSERT INTO `pms_sku_images` VALUES (36, 5, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%868.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (37, 5, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (38, 5, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (39, 5, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%863.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (40, 5, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%864.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (41, 5, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%865.jpg', 0, 1);
INSERT INTO `pms_sku_images` VALUES (42, 5, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%866.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (43, 5, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (44, 6, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%868.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (45, 6, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (46, 6, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', 0, 1);
INSERT INTO `pms_sku_images` VALUES (47, 6, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%863.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (48, 6, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%864.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (49, 6, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%866.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (50, 6, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (51, 7, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%868.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (52, 7, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (53, 7, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (54, 7, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%863.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (55, 7, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%864.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (56, 7, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%865.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (57, 7, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%866.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (58, 8, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (59, 8, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%868.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (60, 8, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', 0, 1);
INSERT INTO `pms_sku_images` VALUES (61, 8, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (62, 8, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%863.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (63, 9, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%864.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (64, 9, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%865.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (65, 9, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%866.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (66, 9, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', 0, 0);
INSERT INTO `pms_sku_images` VALUES (67, 9, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%868.jpg', 0, 0);

-- ----------------------------
-- Records of pms_sku_info
-- ----------------------------
INSERT INTO `pms_sku_info` VALUES (1, 3, '华为 HUAWEI P40 Pro+ 麒麟990 5G  流光幻镜 套餐二', NULL, 225, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', '华为 HUAWEI P40 Pro+ 麒麟990 5G  流光幻镜 套餐二 麒麟990 5G SoC芯片 5000万超感知徕卡五摄 100倍双目变焦 全网通5G', '【6月6日10:08抢购】抢券享24期免息；5G手机至高享24期免息，低至5.1元/天》', 8888.0000, 2654);
INSERT INTO `pms_sku_info` VALUES (2, 3, '华为 HUAWEI P40 Pro+ 麒麟990 5G  流光幻镜 套餐三', NULL, 225, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', '华为 HUAWEI P40 Pro+ 麒麟990 5G  流光幻镜 套餐二 麒麟990 5G SoC芯片 5000万超感知徕卡五摄 100倍双目变焦 全网通5G', '【6月6日10:08抢购】抢券享24期免息；5G手机至高享24期免息，低至5.1元/天》', 8888.0000, 1454);
INSERT INTO `pms_sku_info` VALUES (3, 3, '华为 HUAWEI P40 Pro+ 麒麟990 5G  流光幻镜 套餐一', NULL, 225, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%864.jpg', '华为 HUAWEI P40 Pro+ 麒麟990 5G  流光幻镜 套餐二 麒麟990 5G SoC芯片 5000万超感知徕卡五摄 100倍双目变焦 全网通5G', '【品牌日限时享24期免息】5000万超感知徕卡四摄；50倍数字变焦；5G手机至高享24期免息，低至5.1元/天》', 5988.0000, 1673);
INSERT INTO `pms_sku_info` VALUES (4, 3, '华为 HUAWEI P40 Pro+ 麒麟990 5G  霓影紫 套餐二', NULL, 225, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%866.jpg', '华为 HUAWEI P40 Pro+ 麒麟990 5G  霓影紫 套餐二 麒麟990 5G SoC芯片 5000万超感知徕卡四摄 50倍数字变焦  全网通5G手机', '【品牌日限时享24期免息】5000万超感知徕卡四摄；50倍数字变焦；5G手机至高享24期免息，低至5.1元/天》', 5988.0000, 3587);
INSERT INTO `pms_sku_info` VALUES (5, 3, '华为 HUAWEI P40 Pro+ 麒麟990 5G  霓影紫 套餐三', NULL, 225, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', '华为 HUAWEI P40 Pro+ 麒麟990 5G  霓影紫 套餐二 麒麟990 5G SoC芯片 5000万超感知徕卡四摄 50倍数字变焦  全网通5G手机', '【品牌日限时享24期免息】5000万超感知徕卡四摄；50倍数字变焦；5G手机至高享24期免息，低至5.1元/天》', 5988.0000, 1235);
INSERT INTO `pms_sku_info` VALUES (6, 3, '华为 HUAWEI P40 Pro+ 麒麟990 5G  霓影紫 套餐一', NULL, 225, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%865.jpg', '华为 HUAWEI P40 Pro+ 麒麟990 5G  霓影紫 套餐二 麒麟990 5G SoC芯片 5000万超感知徕卡四摄 50倍数字变焦  全网通5G手机', '【下单立减100/12期免息/咨询可省更多钱】赠原装碎屏险+华为原装无线充+华为智能手环+AI智能蓝牙音箱+硅胶保护壳+保护膜+晒单礼购买P40咨询客服享优惠', 5988.0000, 1456);
INSERT INTO `pms_sku_info` VALUES (7, 3, '华为 HUAWEI P40 Pro+ 麒麟990 5G  钛空银 套餐二', NULL, 225, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', '华为 HUAWEI P40 Pro+ 麒麟990 5G  钛空银 套餐二 【白条12期免息送碎屏险】华为 P40 Pro 5G手机 分期', '【下单立减100/12期免息/咨询可省更多钱】赠原装碎屏险+华为原装无线充+华为智能手环+AI智能蓝牙音箱+硅胶保护壳+保护膜+晒单礼购买P40咨询客服享优惠', 5988.0000, 2354);
INSERT INTO `pms_sku_info` VALUES (8, 3, '华为 HUAWEI P40 Pro+ 麒麟990 5G  钛空银 套餐三', NULL, 225, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', '华为 HUAWEI P40 Pro+ 麒麟990 5G  钛空银 套餐三 【白条12期免息送碎屏险】华为 P40 Pro 5G手机 分期', '【下单立减100/12期免息/咨询可省更多钱】赠原装碎屏险+华为原装无线充+华为智能手环+AI智能蓝牙音箱+硅胶保护壳+保护膜+晒单礼购买P40咨询客服享优惠', 5988.0000, 2546);
INSERT INTO `pms_sku_info` VALUES (9, 3, '华为 HUAWEI P40 Pro+ 麒麟990 5G  钛空银 套餐一', NULL, 225, 2, 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', '华为 HUAWEI P40 Pro+ 麒麟990 5G  钛空银 套餐一 【白条12期免息送碎屏险】华为 P40 Pro 5G手机 分期', '【保价618，下单立减200，未减咨询，至高可减400】赠原装碎屏险+华为原装智能手环+网红榨汁机+AI蓝牙音箱+硅胶保护壳+保护膜+晒单礼', 4188.0000, 957);

-- ----------------------------
-- Records of pms_spu_images
-- ----------------------------
INSERT INTO `pms_spu_images` VALUES (17, 3, '图集1.jpg', 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%861.jpg', 0, NULL);
INSERT INTO `pms_spu_images` VALUES (18, 3, '图集2.jpg', 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%862.jpg', 0, NULL);
INSERT INTO `pms_spu_images` VALUES (19, 3, '图集3.jpg', 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%863.jpg', 0, NULL);
INSERT INTO `pms_spu_images` VALUES (20, 3, '图集4.jpg', 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%864.jpg', 0, NULL);
INSERT INTO `pms_spu_images` VALUES (21, 3, '图集5.jpg', 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%865.jpg', 0, NULL);
INSERT INTO `pms_spu_images` VALUES (22, 3, '图集6.jpg', 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%866.jpg', 0, NULL);
INSERT INTO `pms_spu_images` VALUES (23, 3, '图集7.jpg', 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%867.jpg', 0, NULL);
INSERT INTO `pms_spu_images` VALUES (24, 3, '图集8.jpg', 'https://khighness-gulimall.oss-cn-shanghai.aliyuncs.com/2021-12-19/%E5%9B%BE%E9%9B%868.jpg', 0, NULL);

-- ----------------------------
-- Records of pms_spu_info
-- ----------------------------
INSERT INTO `pms_spu_info` VALUES (3, '华为 HUAWEI P40 Pro+ 麒麟990 5G ', '华为 HUAWEI P40 Pro+ 麒麟990 5G ', 225, 2, 0.5700, 0, '2020-06-06 10:40:25', '2020-06-15 02:39:01');

-- ----------------------------
-- Records of pms_spu_info_desc
-- ----------------------------
INSERT INTO `pms_spu_info_desc` VALUES (3, 'https://mall-fire.oss-cn-shenzhen.aliyuncs.com/2020-06-05/5fcc807c-1a0c-431f-8635-ac75fe2b2a72_华为2.jpg,https://mall-fire.oss-cn-shenzhen.aliyuncs.com/2020-06-05/5952be7a-bca4-4794-8a3f-8612b45bffb0_华为1.jpg,https://mall-fire.oss-cn-shenzhen.aliyuncs.com/2020-06-05/fad57e61-8650-4d5c-87d1-064bfff90553_华为3.jpg,https://mall-fire.oss-cn-shenzhen.aliyuncs.com/2020-06-05/344445d8-346b-4d04-8de1-4e31f6db4336_华为4.jpg,https://mall-fire.oss-cn-shenzhen.aliyuncs.com/2020-06-05/7d36cce9-94dc-49a4-8069-0ae66f57cdc3_华为5.jpg');

SET FOREIGN_KEY_CHECKS = 1;
