# 数据库设计文档

## 1. 数据库架构

### 1.1 数据库选型
- 主数据库：MySQL 8.0
- 缓存数据库：Redis 6.x 
- 搜索引擎：Elasticsearch 8.x
- 消息队列：Kafka

### 1.2 数据库集群
- 主从复制
- 读写分离
- 分库分表
- 数据备份

## 2. 数据模型设计

### 2.1 企业模块
```sql
-- 企业基本信息表
CREATE TABLE enterprise (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL COMMENT '企业名称',
    credit_code VARCHAR(50) NOT NULL COMMENT '统一社会信用代码',
    legal_person VARCHAR(50) NOT NULL COMMENT '法定代表人',
    address VARCHAR(500) NOT NULL COMMENT '企业地址',
    contact VARCHAR(50) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '电子邮箱',
    status TINYINT NOT NULL COMMENT '状态',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    UNIQUE KEY uk_credit_code(credit_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业基本信息表';

-- 企业许可证表
CREATE TABLE enterprise_license (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    enterprise_id BIGINT NOT NULL COMMENT '企业ID',
    license_type TINYINT NOT NULL COMMENT '许可类型',
    license_no VARCHAR(50) NOT NULL COMMENT '许可证号',
    valid_from DATE NOT NULL COMMENT '生效日期',
    valid_to DATE NOT NULL COMMENT '失效日期',
    status TINYINT NOT NULL COMMENT '状态',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_enterprise_id(enterprise_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业许可证表';
```

### 2.2 产品模块
```sql
-- 产品信息表
CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL COMMENT '产品名称',
    code VARCHAR(50) NOT NULL COMMENT '产品编码',
    category_id INT NOT NULL COMMENT '产品类别',
    spec VARCHAR(200) COMMENT '规格型号',
    enterprise_id BIGINT NOT NULL COMMENT '生产企业ID',
    status TINYINT NOT NULL COMMENT '状态',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_enterprise_id(enterprise_id),
    KEY idx_category_id(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品信息表';

-- 产品批次表
CREATE TABLE product_batch (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL COMMENT '产品ID',
    batch_no VARCHAR(50) NOT NULL COMMENT '批次号',
    production_date DATE NOT NULL COMMENT '生产日期',
    valid_date DATE NOT NULL COMMENT '有效期',
    quantity INT NOT NULL COMMENT '生产数量',
    status TINYINT NOT NULL COMMENT '状态',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_product_id(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品批次表';
```

### 2.3 监管模块
```sql
-- 检查记录表
CREATE TABLE inspection_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    enterprise_id BIGINT NOT NULL COMMENT '企业ID',
    inspector_id BIGINT NOT NULL COMMENT '检查人ID',
    inspection_type TINYINT NOT NULL COMMENT '检查类型',
    inspection_date DATE NOT NULL COMMENT '检查日期',
    result TINYINT NOT NULL COMMENT '检查结果',
    remark TEXT COMMENT '备注',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_enterprise_id(enterprise_id),
    KEY idx_inspector_id(inspector_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检查记录表';

-- 风险预警表
CREATE TABLE risk_warning (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    enterprise_id BIGINT NOT NULL COMMENT '企业ID',
    product_id BIGINT COMMENT '产品ID',
    risk_type TINYINT NOT NULL COMMENT '风险类型',
    risk_level TINYINT NOT NULL COMMENT '风险等级',
    warning_content TEXT NOT NULL COMMENT '预警内容',
    status TINYINT NOT NULL COMMENT '状态',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_enterprise_id(enterprise_id),
    KEY idx_product_id(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险预警表';
```

## 3. 数据库优化

### 3.1 索引优化
- 主键索引设计
- 普通索引设计
- 联合索引设计
- 索引维护策略

### 3.2 SQL优化
- 查询优化
- 连接优化
- 子查询优化
- 分页优化

### 3.3 性能优化
- 数据库参数优化
- 查询缓存优化
- 连接池优化
- 服务器优化

## 4. 数据安全

### 4.1 安全策略
- 访问控制
- 数据加密
- 审计日志
- 备份恢复

### 4.2 数据权限
- 用户权限
- 角色权限
- 数据权限
- 操作权限

## 5. 运维管理

### 5.1 监控告警
- 性能监控
- 容量监控
- 可用性监控
- 告警策略

### 5.2 运维规范
- 发布规范
- 变更规范
- 备份规范
- 应急预案
