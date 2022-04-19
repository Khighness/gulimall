

### ⚠ 注意 

使用原始脚本，需要：

- `gulimall_pms`数据库执行

  ```sql
  ALTER TABLE `pms_attr` ADD COLUMN `value_type` tinyint(1) NULL COMMENT '值类型[0-单选，1-多选]' AFTER `search_type`;
  ```

- `AttyEntity`、`AttrVo`中各添加：
  
  ```java
  private Integer valueType;
  ```

- `AttrDao.xml中`添加：

  ```xml
  <result property="valueType" column="value_type"/>
  ```

