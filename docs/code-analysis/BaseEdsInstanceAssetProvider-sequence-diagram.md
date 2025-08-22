# BaseEdsInstanceAssetProvider 完整时序图

## 🔄 完整资产导入流程时序图

```mermaid
sequenceDiagram
    participant Scheduler as 定时调度器
    participant Factory as EdsInstanceProviderFactory
    participant Provider as BaseEdsInstanceAssetProvider
    participant Lock as 分布式锁
    participant External as 外部数据源
    participant AssetService as EdsAssetService
    participant IndexFacade as EdsAssetIndexFacade
    participant BusinessHandler as UpdateBusinessFromAssetHandler
    participant Facade as SimpleEdsFacade
    
    Note over Scheduler,Facade: 资产导入完整流程
    
    Scheduler->>Factory: 触发资产导入任务
    Factory->>Provider: importAssets(instance)
    
    Note over Provider,Lock: 分布式锁保护
    Provider->>Lock: 尝试获取锁 @EdsTaskLock
    Lock-->>Provider: 锁获取成功
    
    Note over Provider,External: 获取外部数据
    Provider->>Provider: listEntities(instance)
    Provider->>External: 调用外部API
    External-->>Provider: 返回实体列表 List<A>
    
    Note over Provider,AssetService: 获取现有资产
    Provider->>AssetService: queryInstanceAssets()
    AssetService-->>Provider: 现有资产列表
    Provider->>Provider: 构建现有资产ID集合
    
    Note over Provider,BusinessHandler: 处理每个实体
    loop 遍历每个外部实体
        Provider->>Provider: processAndRecordEntity()
        
        Note over Provider: 实体转换
        Provider->>Provider: convertToEdsAsset(instance, entity)
        Provider-->>Provider: EdsAsset对象
        
        Note over Provider,AssetService: 保存或更新资产
        Provider->>AssetService: getByUniqueKey(newEdsAsset)
        
        alt 资产不存在
            AssetService-->>Provider: null
            Provider->>AssetService: add(newEdsAsset)
            AssetService-->>Provider: 保存成功
        else 资产存在
            AssetService-->>Provider: 现有资产
            Provider->>Provider: equals(oldAsset, newAsset)
            
            alt 资产有变更
                Provider->>AssetService: updateByPrimaryKey(newEdsAsset)
                AssetService-->>Provider: 更新成功
                Provider->>BusinessHandler: update(newEdsAsset)
                BusinessHandler-->>Provider: 业务更新完成
            else 资产无变更
                Provider->>Provider: 跳过更新
            end
        end
        
        Note over Provider,IndexFacade: 创建资产索引
        Provider->>Provider: createEdsAssetIndices()
        Provider->>Provider: convertToEdsAssetIndexList()
        Provider->>Provider: convertToEdsAssetIndex()
        Provider->>IndexFacade: saveAssetIndexList(assetId, indices)
        IndexFacade-->>Provider: 索引保存成功
        
        Provider->>Provider: 从ID集合中移除已处理的资产ID
    end
    
    Note over Provider,Facade: 清理不存在的资产
    alt 存在需要删除的资产
        loop 遍历剩余的资产ID
            Provider->>Facade: deleteEdsAssetById(assetId)
            Facade-->>Provider: 删除成功
        end
    end
    
    Note over Provider: 后处理
    Provider->>Provider: postProcessEntities(instance)
    
    Note over Provider,Lock: 释放锁
    Provider->>Lock: 释放分布式锁
    Lock-->>Provider: 锁释放成功
    
    Provider-->>Factory: 导入完成
    Factory-->>Scheduler: 任务执行完成
```

## 🔧 配置加载流程时序图

```mermaid
sequenceDiagram
    participant Client as 客户端
    participant Provider as BaseEdsInstanceAssetProvider
    participant CredService as CredentialService
    participant Template as ConfigCredTemplate
    participant Utils as ConfigUtils
    participant Generics as Generics工具
    
    Note over Client,Generics: 配置加载流程
    
    Client->>Provider: produceConfig(edsConfig)
    
    alt 配置包含凭据ID
        Provider->>CredService: getById(credentialId)
        CredService-->>Provider: Credential对象
        Provider->>Template: renderTemplate(configContent, credential)
        Template-->>Provider: 渲染后的配置内容
    else 配置不包含凭据ID
        Provider->>Provider: 直接使用原始配置内容
    end
    
    Provider->>Provider: configLoadAs(configContent)
    Provider->>Generics: find(thisClass, BaseClass, 0)
    Generics-->>Provider: 泛型类型C的Class对象
    Provider->>Utils: loadAs(configContent, clazz)
    Utils-->>Provider: 配置对象C
    
    Provider-->>Client: 返回配置对象
```

## 🏗️ 资产构建流程时序图

```mermaid
sequenceDiagram
    participant Provider as BaseEdsInstanceAssetProvider
    participant Builder as EdsAssetBuilder
    participant AssetUtils as AssetUtils
    participant Comparer as EdsAssetComparer
    
    Note over Provider,Comparer: 资产构建和比较流程
    
    Provider->>Provider: convertToEdsAsset(instance, entity)
    Provider->>Provider: newEdsAssetBuilder(instance, entity)
    Provider->>Builder: EdsAssetBuilder.newBuilder()
    Builder-->>Provider: Builder实例
    
    Provider->>Builder: assetTypeOf(getAssetType())
    Provider->>Builder: assetKeyOf(entityKey)
    Provider->>Builder: nameOf(entityName)
    Provider->>Builder: regionOf(entityRegion)
    Provider->>Builder: build()
    
    Builder->>AssetUtils: 序列化原始模型
    AssetUtils-->>Builder: JSON字符串
    Builder-->>Provider: EdsAsset对象
    
    Note over Provider,Comparer: 资产比较
    Provider->>Provider: equals(oldAsset, newAsset)
    Provider->>Comparer: SAME.compare(a1, a2)
    Comparer-->>Provider: 比较结果boolean
    
    Provider-->>Provider: 返回构建的资产
```

## 🔍 异常处理流程时序图

```mermaid
sequenceDiagram
    participant Provider as BaseEdsInstanceAssetProvider
    participant External as 外部数据源
    participant Logger as 日志系统
    participant Exception as 异常处理器
    
    Note over Provider,Exception: 异常处理流程
    
    Provider->>Provider: importAssets(instance)
    
    alt 查询实体异常
        Provider->>External: listEntities(instance)
        External-->>Provider: 抛出异常
        Provider->>Logger: log.error("Query entities failed")
        Provider->>Exception: throw EdsQueryEntitiesException
    else 转换资产异常
        Provider->>Provider: convertToEdsAsset(instance, entity)
        Provider-->>Provider: 抛出转换异常
        Provider->>Logger: log.error("Asset conversion error")
        Provider->>Exception: throw EdsAssetException
    else 保存资产异常
        Provider->>Provider: saveAsset(edsAsset)
        Provider-->>Provider: 数据库异常
        Provider->>Logger: log.error("Save asset error")
        Provider->>Exception: 继续处理下一个资产
    end
    
    Exception-->>Provider: 异常处理完成
```

## 📊 性能监控流程时序图

```mermaid
sequenceDiagram
    participant Monitor as 监控系统
    participant Provider as BaseEdsInstanceAssetProvider
    participant Metrics as 指标收集器
    participant AlertManager as 告警管理器
    
    Note over Monitor,AlertManager: 性能监控流程
    
    Monitor->>Provider: 开始监控资产导入
    Provider->>Metrics: 记录开始时间
    
    Provider->>Provider: importAssets(instance)
    
    loop 处理过程中
        Provider->>Metrics: 记录处理进度
        Provider->>Metrics: 记录错误次数
        Provider->>Metrics: 记录资产数量
    end
    
    Provider->>Metrics: 记录结束时间
    Provider->>Metrics: 计算总耗时
    
    Metrics->>Monitor: 上报性能指标
    
    alt 性能异常
        Monitor->>AlertManager: 触发告警
        AlertManager-->>Monitor: 发送告警通知
    else 性能正常
        Monitor->>Monitor: 记录正常状态
    end
```

---

**说明**: 这些时序图展示了BaseEdsInstanceAssetProvider在不同场景下的完整交互流程，包括正常流程、异常处理、配置加载和性能监控等方面，为理解系统运行机制提供了详细的可视化参考。
