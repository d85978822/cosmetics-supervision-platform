# 化妆品监管模块设计方案

## 1. 业务流程设计

### 1.1 化妆品备案管理

#### 1.1.1 国产普通化妆品备案
- 企业提交备案材料
- 资料形式审查
- 备案信息公示
- 备案后监管

#### 1.1.2 进口普通化妆品备案
- 境内责任人提交备案
- 资料形式审查
- 备案信息公示
- 备案后监管

#### 1.1.3 特殊化妆品注册
- 申请受理
- 技术审评
- 现场核查
- 注册审批
- 证后监管

### 1.2 化妆品生产许可管理

#### 1.2.1 生产许可申请
- 申请受理
- 资料审查
- 现场检查
- 质量管理体系核查
- 许可审批
- 证后监管

#### 1.2.2 许可变更
- 变更申请
- 资料审查
- 现场核查(必要时)
- 变更审批

#### 1.2.3 许可延续
- 延续申请
- 资料审查
- 现场核查
- 延续审批

### 1.3 化妆品监督检查管理

#### 1.3.1 日常监督检查
- 年度计划制定
- 检查任务分派
- 现场检查实施
- 问题整改跟踪
- 检查结果评估

#### 1.3.2 专项监督检查
- 专项任务部署
- 检查方案制定
- 联合检查实施
- 问题整改督查
- 专项总结评估

#### 1.3.3 抽样检验
- 抽样计划制定
- 样品抽取
- 检验实施
- 结果处理
- 问题跟踪

### 1.4 不良反应监测

#### 1.4.1 不良反应报告
- 企业报告
- 医疗机构报告
- 消费者投诉
- 信息初审分析

#### 1.4.2 不良反应调查
- 调查启动
- 现场调查
- 原因分析
- 调查报告
- 处置措施

#### 1.4.3 风险监测
- 信息采集
- 风险评估
- 预警发布
- 处置跟踪

## 2. 数据模型设计

### 2.1 主要数据实体

#### 2.1.1 化妆品基础信息
```
Cosmetic {
    cosmetic_id: string        // 产品ID
    cosmetic_name: string      // 产品名称
    type: string              // 产品类型(普通/特殊)
    category: string          // 产品类别
    manufacturer: string      // 生产企业
    registrant: string       // 注册人/备案人
    reg_certificate: string  // 注册证/备案凭证编号
    status: string          // 状态
}
```

#### 2.1.2 生产许可信息
```
ProductionLicense {
    license_id: string        // 许可证编号
    company_id: string       // 企业ID
    scope: string           // 生产范围
    valid_date: date        // 有效期
    issue_date: date        // 发证日期
    status: string          // 状态
}
```

#### 2.1.3 监督检查记录
```
Inspection {
    inspection_id: string     // 检查ID
    company_id: string       // 企业ID
    check_type: string      // 检查类型
    check_date: date        // 检查日期
    inspector: string       // 检查人员
    result: string         // 检查结果
    issues: array          // 发现问题
    actions: array         // 处置措施
}
```

#### 2.1.4 不良反应信息
```
AdverseReaction {
    reaction_id: string      // 反应ID
    cosmetic_id: string     // 产品ID
    report_type: string     // 报告类型
    report_date: date       // 报告日期
    reporter: string        // 报告人
    description: string     // 反应描述
    investigation: string   // 调查情况
    result: string         // 调查结果
    measures: array        // 处置措施
}
```

#### 2.1.5 风险监测信息
```
RiskMonitor {
    alert_id: string        // 预警ID
    risk_type: string      // 风险类型
    risk_level: string     // 风险等级
    content: object        // 预警内容
    alert_time: date       // 预警时间
    status: string         // 状态
}
```

## 3. 接口设计

### 3.1 对外接口

#### 3.1.1 备案注册接口
```
// 备案/注册申请
POST /api/v1/cosmetic/register
Request: {
    cosmetic_info: object,   // 产品信息
    reg_type: string,       // 备案/注册类型
    materials: array        // 申请材料
}
Response: {
    application_id: string,  // 申请ID
    status: string         // 申请状态
}

// 备案/注册查询
GET /api/v1/cosmetic/register/query
Request: {
    reg_id: string,        // 备案/注册编号
    cosmetic_id: string    // 产品ID
}
Response: {
    reg_info: object      // 备案/注册信息
}
```

#### 3.1.2 生产许可接口
```
// 许可申请
POST /api/v1/cosmetic/license
Request: {
    company_info: object,   // 企业信息
    scope: string,         // 生产范围
    materials: array       // 申请材料
}
Response: {
    application_id: string, // 申请ID
    status: string        // 申请状态
}

// 许可查询
GET /api/v1/cosmetic/license/query
Request: {
    license_id: string,    // 许可证编号
    company_id: string    // 企业ID
}
Response: {
    license_info: object  // 许可信息
}
```

#### 3.1.3 监督检查接口
```
// 检查任务下达
POST /api/v1/cosmetic/inspection/assign
Request: {
    company_id: string,   // 企业ID
    check_type: string,   // 检查类型
    check_items: array   // 检查项目
}
Response: {
    task_id: string,     // 任务ID
    status: string      // 任务状态
}

// 检查结果上报
POST /api/v1/cosmetic/inspection/report
Request: {
    task_id: string,    // 任务ID
    result: object,     // 检查结果
    issues: array,      // 发现问题
    actions: array      // 处置措施
}
Response: {
    status: string     // 处理状态
}
```

### 3.2 内部接口

#### 3.2.1 数据同步接口
```
// 基础数据同步
POST /api/v1/cosmetic/sync/basic
Request: {
    data_type: string,  // 数据类型
    data: array        // 同步数据
}
Response: {
    status: string,    // 同步状态
    message: string   // 处理消息
}

// 业务数据同步
POST /api/v1/cosmetic/sync/business
Request: {
    business_type: string, // 业务类型
    data: array          // 业务数据
}
Response: {
    status: string,      // 同步状态
    message: string     // 处理消息
}
```

#### 3.2.2 风险监测接口
```
// 风险预警
POST /api/v1/cosmetic/risk/alert
Request: {
    risk_type: string,   // 风险类型
    risk_level: string,  // 风险等级
    content: object     // 预警内容
}
Response: {
    alert_id: string,   // 预警ID
    status: string     // 处理状态
}

// 风险处置
POST /api/v1/cosmetic/risk/handle
Request: {
    alert_id: string,   // 预警ID
    actions: array     // 处置措施
}
Response: {
    status: string,    // 处理状态
    message: string   // 处理消息
}
```

## 4. 数据共享机制

### 4.1 共享范围

#### 4.1.1 基础信息共享
- 企业基本信息
- 产品基本信息
- 许可证信息
- 备案注册信息

#### 4.1.2 监管信息共享
- 检查记录信息
- 抽检结果信息
- 不良反应信息
- 风险预警信息

#### 4.1.3 统计分析信息
- 监管指标信息
- 风险评级信息
- 信用评价信息

### 4.2 共享方式

#### 4.2.1 实时同步
- 关键业务数据
- 重要监管信息
- 风险预警信息

#### 4.2.2 定期同步
- 基础信息更新
- 统计分析数据
- 历史档案数据

#### 4.2.3 按需同步
- 专项检查数据
- 统计报表数据
- 查询分析数据

### 4.3 共享机制

#### 4.3.1 数据推送
- 实时消息推送
- 批量数据推送
- 定时任务推送

#### 4.3.2 数据获取
- 接口调用获取
- 数据库直连查询
- 文件传输获取

#### 4.3.3 数据订阅
- 业务数据订阅
- 预警信息订阅
- 统计报表订阅

## 5. 业务规则

### 5.1 备案注册规则
- 普通化妆品实行备案管理
- 特殊化妆品实行注册管理
- 进口产品需设境内责任人
- 新原料需单独注册备案

### 5.2 生产许可规则
- 许可证有效期为5年
- 重要事项变更需现场核查
- 委托生产需备案审批
- 质量安全事故停产整改

### 5.3 检查监管规则
- 年度例行检查不少于1次
- 投诉问题48小时内核查
- 风险预警及时处置
- 严重违法立即查处

## 6. 系统集成

### 6.1 系统对接
- 行政审批系统
- 日常监管系统
- 检验检测系统
- 投诉举报系统

### 6.2 数据对接
- 数据共享交换平台
- 信用信息平台
- 综合执法平台
- 投诉举报平台

### 6.3 业务协同
- 联合检查协同
- 案件查处协同
- 风险防控协同
- 应急处置协同

## 7. 性能要求

### 7.1 响应时间
- 页面操作响应 < 2秒
- 数据查询响应 < 3秒
- 报表生成响应 < 5秒

### 7.2 并发处理
- 系统并发用户 >= 300
- 数据处理能力 >= 10万条/天
- 文件处理能力 >= 1GB/天

### 7.3 数据处理
- 实时数据延迟 < 1分钟
- 批量数据延迟 < 30分钟
- 统计数据延迟 < 60分钟

## 8. 安全要求

### 8.1 安全控制
- 统一身份认证
- 细粒度权限控制
- 数据脱敏加密
- 操作日志记录

### 8.2 数据安全
- 数据传输加密
- 数据存储加密
- 数据备份恢复
- 数据完整性校验

### 8.3 安全审计
- 用户操作审计
- 数据访问审计
- 系统运行审计
- 安全事件审计