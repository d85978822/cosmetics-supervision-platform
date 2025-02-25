# 数据中台API接口设计

## 1. API接口规范

### 1.1 接口设计原则
- 遵循RESTful架构设计
- 统一使用HTTPS协议
- 统一使用JSON数据格式
- 统一的错误码和返回格式
- 完善的接口文档和版本控制

### 1.2 接口认证
- 采用JWT令牌认证
- Token在Header中传递
- Token过期时间可配置
- 支持Token刷新机制

### 1.3 通用返回格式
```json
{
    "code": 0,           // 响应码,0表示成功
    "message": "成功",  // 响应消息
    "data": {}          // 响应数据
}
```

## 2. 数据采集API

### 2.1 数据源管理
#### 2.1.1 注册数据源
```
POST /api/v1/datasource/register
请求:
{
    "source_name": "string",     // 数据源名称
    "source_type": "string",     // 数据源类型
    "connection_info": {},         // 连接信息
    "description": "string"      // 描述信息
}
```

#### 2.1.2 更新数据源
```
PUT /api/v1/datasource/{id}
请求:
{
    "source_name": "string",     // 数据源名称
    "connection_info": {},         // 连接信息
    "description": "string"      // 描述信息
}
```

### 2.2 数据采集任务
#### 2.2.1 创建采集任务
```
POST /api/v1/collect/task
请求:
{
    "task_name": "string",      // 任务名称
    "source_id": "string",      // 数据源ID
    "schedule": "string",       // 调度策略
    "collect_config": {}          // 采集配置
}
```

#### 2.2.2 查询任务状态
```
GET /api/v1/collect/task/{id}/status
响应:
{
    "task_id": "string",        // 任务ID
    "task_status": "string",    // 任务状态
    "last_run_time": "string",  // 上次运行时间
    "next_run_time": "string"   // 下次运行时间
}
```

## 3. 数据服务API

### 3.1 数据查询服务
#### 3.1.1 通用查询接口
```
POST /api/v1/data/query
请求:
{
    "table_name": "string",     // 表名
    "conditions": [],             // 查询条件
    "fields": [],                 // 查询字段
    "page_size": 10,             // 分页大小
    "page_num": 1                // 页码
}
```

#### 3.1.2 高级查询接口
```
POST /api/v1/data/advanced-query
请求:
{
    "query_type": "string",     // 查询类型
    "query_params": {},           // 查询参数
    "aggregations": [],           // 聚合条件
    "sort_fields": []             // 排序字段
}
```

### 3.2 数据分析服务
#### 3.2.1 统计分析接口
```
POST /api/v1/data/statistics
请求:
{
    "stat_type": "string",      // 统计类型
    "dimensions": [],             // 统计维度
    "metrics": [],                // 统计指标
    "filters": []                 // 过滤条件
}
```

#### 3.2.2 趋势分析接口
```
POST /api/v1/data/trend
请求:
{
    "time_range": "string",     // 时间范围
    "time_unit": "string",      // 时间单位
    "metrics": [],                // 分析指标
    "group_by": []                // 分组字段
}
```

## 4. 数据治理API

### 4.1 数据质量管理
#### 4.1.1 质量检查接口
```
POST /api/v1/quality/check
请求:
{
    "check_type": "string",     // 检查类型
    "check_rules": [],            // 检查规则
    "data_scope": {}              // 检查范围
}
```

#### 4.1.2 质量报告接口
```
GET /api/v1/quality/report
请求参数:
{
    "report_type": "string",    // 报告类型
    "time_range": "string",     // 时间范围
    "data_scope": {}              // 数据范围
}
```

## 5. 错误码定义

### 5.1 系统错误码
- 10000: 系统内部错误
- 10001: 参数验证失败
- 10002: 权限验证失败
- 10003: 资源不存在
- 10004: 操作超时

### 5.2 业务错误码
- 20001: 数据源连接失败
- 20002: 数据采集失败
- 20003: 数据处理失败
- 20004: 数据服务调用失败
- 20005: 数据质量检查失败