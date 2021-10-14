


在数据库的 pms_attr 表加上value_type字段，类型为tinyint就行；
在代码中，AttyEntity.java、AttrVo.java中各添加：private Integer valueType，
在AttrDao.xml中添加：《result property="valueType" column="value_type"/》  （把尖括号换成英文的）

