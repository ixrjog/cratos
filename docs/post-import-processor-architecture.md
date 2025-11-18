# 后导入处理器架构文档

## 概述

后导入处理器是一个基于AOP的自动化处理系统，用于在业务对象导入后执行相关的后处理操作，包括资产绑定和外部系统用户创建。

## 架构流程图

```mermaid
graph TB
    A[业务方法调用] --> B[@PostImportProcessor注解]
    B --> C[PostImportProcessorAspect切面拦截]
    
    C --> D[doAfterReturning方法]
    D --> E[资产绑定处理]
    D --> F[按类型处理器]
    
    E --> G[postImportAssetBinding]
    G --> H[创建BusinessAssetBind]
    H --> I[保存到数据库]
    
    F --> J[postByTypeProcessor]
    J --> K[PostImportAssetProcessorFactory.process]
    
    K --> L{处理器类型判断}
    
    L -->|DINGTALK_USER + USER| M[UserToLdapPostImportProcessor]
    L -->|DINGTALK_USER + USER| N[UserToOcPostImportProcessor]
    
    M --> O[查询LDAP实例]
    O --> P[检查用户是否存在]
    P -->|不存在| Q[创建LDAP用户]
    P -->|存在| R[跳过创建]
    
    N --> S[查询OpsCloud实例]
    S --> T[构建用户参数]
    T --> U[调用OcUserRepo.addUser]
    
    Q --> V[生成密码]
    U --> W[生成密码]
    
    V --> X[EdsIdentityFacade.createLdapIdentity]
    W --> Y[OpsCloud API调用]
    
    style A fill:#e1f5fe
    style B fill:#f3e5f5
    style C fill:#fff3e0
    style M fill:#e8f5e8
    style N fill:#fff8e1
    style X fill:#fce4ec
    style Y fill:#f1f8e9
```

## 核心组件

### 1. PostImportProcessorAspect (切面处理器)

**职责：**
- 拦截带有`@PostImportProcessor`注解的方法
- 在方法执行后进行资产绑定和类型处理

**关键方法：**
- `doAfterReturning()`: 主入口方法
- `postImportAssetBinding()`: 处理资产绑定
- `postByTypeProcessor()`: 按业务类型分发处理

### 2. 处理器实现

#### UserToLdapPostImportProcessor
- **执行顺序**: @Order(1)
- **业务类型**: USER
- **资产类型**: DINGTALK_USER
- **功能**: 在LDAP系统中创建用户

#### UserToOcPostImportProcessor  
- **执行顺序**: @Order(2)
- **业务类型**: USER
- **资产类型**: DINGTALK_USER
- **功能**: 在OpsCloud系统中创建用户

## 处理流程

### 第一阶段：资产绑定
1. 提取方法参数中的`HasImportFromAsset`对象
2. 获取资产ID并验证有效性
3. 创建`BusinessAssetBind`记录
4. 保存绑定关系到数据库

### 第二阶段：后处理
1. 根据注解中的业务类型进行分发
2. 通过工厂模式调用对应的处理器
3. 并行执行LDAP和OpsCloud用户创建
4. 统一密码管理，确保系统间密码一致

## 关键特性

### 密码管理
- 使用`PasswordGenerator.generatePassword()`生成随机密码
- 通过context传递密码，确保多个系统使用相同密码
- 支持外部传入密码覆盖默认生成

### 错误处理
- **LDAP处理器**: 检查用户存在性，避免重复创建
- **OpsCloud处理器**: 使用try-catch捕获异常，记录错误日志
- **切面层**: 统一异常捕获，不影响主业务流程

### 扩展性设计
- 基于接口`BasePostImportAssetProcessor`的插件化架构
- 工厂模式支持动态添加新的处理器
- `@Order`注解控制处理器执行顺序

## 配置说明

### 注解使用
```java
@PostImportProcessor(ofType = BusinessTypeEnum.USER)
public User importUser(ImportUserParam param) {
    // 业务逻辑
}
```

### 处理器注册
```java
@Component
@Order(1)
public class CustomPostImportProcessor implements BasePostImportAssetProcessor {
    @Override
    public BusinessTypeEnum getBusinessType() {
        return BusinessTypeEnum.CUSTOM;
    }
    
    @Override
    public EdsAssetTypeEnum fromAssetType() {
        return EdsAssetTypeEnum.CUSTOM_ASSET;
    }
}
```

## 监控与日志

- 所有处理器操作都有详细的日志记录
- 支持通过日志追踪处理流程
- 异常情况会记录错误日志但不中断主流程

## 最佳实践

1. **幂等性**: 处理器应支持重复执行而不产生副作用
2. **异常处理**: 单个处理器异常不应影响其他处理器执行
3. **性能考虑**: 避免在处理器中执行耗时操作
4. **日志记录**: 关键操作应有适当的日志记录
