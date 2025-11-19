# CreateFrontEndApplicationTicketEntryProvider 帮助文档

## 概述

`CreateFrontEndApplicationTicketEntryProvider` 是一个工单条目提供者，专门用于处理前端应用创建的工单流程。它继承自 `BaseTicketEntryProvider`，实现了从工单参数验证到应用创建的完整流程。

## 核心功能

### 1. 前端应用创建流程
- **应用创建**: 通过 `ApplicationFacade` 创建应用实例
- **标签管理**: 自动添加应用标签和业务标签
- **Kubernetes资源**: 使用模板创建K8s资源
- **资源扫描**: 自动扫描应用相关资源

### 2. 参数验证机制
- **应用名称**: 验证命名规范和唯一性
- **标签验证**: 确保必需标签存在
- **仓库验证**: 校验GitLab项目资产
- **路径验证**: 验证映射路径格式

## 类结构分析

```mermaid
graph TB
    A[CreateFrontEndApplicationTicketEntryProvider] --> B[BaseTicketEntryProvider]
    A --> C[@Component]
    A --> D[@BusinessType]
    A --> E[@WorkOrderKey]
    
    A --> F[processEntry方法]
    A --> G[verifyEntryParam方法]
    A --> H[paramToEntry方法]
    A --> I[getTableTitle方法]
    A --> J[getEntryTableRow方法]
    
    F --> K[创建应用]
    F --> L[添加标签]
    F --> M[创建K8s资源]
    F --> N[扫描资源]
    
    style A fill:#e1f5fe
    style F fill:#e8f5e8
    style G fill:#fff3e0
    style H fill:#f3e5f5
```

## 关键方法详解

### 1. processEntry() - 核心处理逻辑

```java
protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                          ApplicationModel.CreateFrontEndApplication createFrontEndApplication)
```

**执行流程**:
1. **创建应用**: 调用 `applicationFacade.createApplication()`
2. **添加标签**: 处理应用标签和业务标签绑定
3. **创建资源**: 使用 `kubernetesResourceFacade` 创建K8s资源
4. **扫描资源**: 自动扫描应用相关资源

### 2. verifyEntryParam() - 参数验证

```java
protected void verifyEntryParam(WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry param,
                              WorkOrderTicketEntry entry)
```

**验证项目**:
- **应用名称**: 格式验证 + 唯一性检查
- **必需标签**: 检查 `LEVEL` 标签是否存在
- **仓库资产**: 验证GitLab项目资产的有效性
- **映射路径**: 验证URL路径格式

### 3. paramToEntry() - 参数转换

```java
protected WorkOrderTicketEntry paramToEntry(
    WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry addCreateFrontEndApplicationTicketEntry)
```

**转换过程**:
1. 提取GitLab项目资产信息
2. 获取SSH URL
3. 构建默认标签 (`CREATED_BY`, `FRONT_END`)
4. 使用Builder模式构建工单条目

### 4. 表格渲染方法

#### getTableTitle() - 表格标题
```java
public String getTableTitle(WorkOrderTicketEntry entry)
```
返回Markdown格式的表格标题

#### getEntryTableRow() - 表格行
```java
public String getEntryTableRow(WorkOrderTicketEntry entry)
```
生成包含以下信息的表格行：
- 应用名称
- 应用类型 (FRONT_END)
- 级别标签
- SSH URL
- 网站地址

## 依赖服务

### 核心服务
- **ApplicationService**: 应用基础服务
- **ApplicationFacade**: 应用门面服务
- **EdsAssetService**: 资产服务
- **TagService**: 标签服务
- **BusinessTagFacade**: 业务标签门面

### 专用服务
- **WorkOrderKubernetesResourceFacade**: K8s资源管理
- **EdsAssetIndexService**: 资产索引服务

## 配置注解

### @BusinessType
```java
@BusinessType(type = BusinessTypeEnum.APPLICATION)
```
指定业务类型为应用

### @WorkOrderKey
```java
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_FRONTEND_CREATE)
```
指定工单类型键值

## 数据模型

### 输入模型
```java
ApplicationModel.CreateFrontEndApplication
├── applicationName: String        // 应用名称
├── domain: String                // 域名
├── mappingsPath: String          // 映射路径
├── repository: GitLabRepository  // 仓库信息
└── tags: Map<String, String>     // 标签集合
```

### 仓库模型
```java
ApplicationModel.GitLabRepository
├── assetId: Integer              // 资产ID
└── sshUrl: String               // SSH URL
```

## 验证规则

### 1. 应用名称规则
- 必须以字母开头
- 只能包含小写字母、数字和连字符
- 不能为空
- 必须唯一

### 2. 标签规则
- 必须包含 `LEVEL` 标签
- 标签不能为空

### 3. 仓库规则
- 资产ID不能为空
- 资产必须存在
- 资产类型必须是 `GITLAB_PROJECT`

### 4. 映射路径规则
- 必须以斜杠开头
- 只能包含小写字母、数字、连字符、下划线和点
- 根路径 "/" 有效

## 异常处理

### 常见异常
- `WorkOrderTicketException`: 工单处理异常
  - 应用名称为空
  - 应用名称格式不正确
  - 应用名称已存在
  - 标签为空或缺少必需标签
  - 仓库信息无效
  - 映射路径格式错误

## 使用示例

### 创建前端应用工单
```java
WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry param = 
    WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry.builder()
        .ticketId(123)
        .detail(ApplicationModel.CreateFrontEndApplication.builder()
            .applicationName("my-frontend-app")
            .domain("example.com")
            .mappingsPath("/app")
            .repository(ApplicationModel.GitLabRepository.builder()
                .assetId(456)
                .build())
            .tags(Map.of("LEVEL", "production"))
            .build())
        .build();
```

## 最佳实践

### 1. 命名规范
- 应用名称使用小写字母和连字符
- 避免使用特殊字符和空格

### 2. 标签管理
- 确保设置合适的级别标签
- 使用标准化的标签键值

### 3. 仓库管理
- 确保GitLab项目资产已正确配置
- 验证SSH URL的可访问性

### 4. 路径规划
- 合理规划映射路径
- 避免路径冲突

## 相关文件

- **主类**: `cratos-workorder/src/main/java/com/baiyi/cratos/workorder/entry/impl/CreateFrontEndApplicationTicketEntryProvider.java`
- **基类**: `cratos-workorder/src/main/java/com/baiyi/cratos/workorder/entry/base/BaseTicketEntryProvider.java`
- **构建器**: `cratos-workorder/src/main/java/com/baiyi/cratos/workorder/builder/entry/CreateFrontEndApplicationTicketEntryBuilder.java`

## 扩展点

### 1. 自定义验证
可以通过重写 `verifyEntryParam` 方法添加自定义验证逻辑

### 2. 处理流程扩展
可以在 `processEntry` 方法中添加额外的处理步骤

### 3. 表格渲染定制
可以自定义 `getEntryTableRow` 方法的输出格式

这个提供者为前端应用的自动化创建提供了完整的工单流程支持，确保了应用创建的标准化和自动化。
