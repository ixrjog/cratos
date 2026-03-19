# EDS 资产导入流程

## 概述

`BaseEdsAssetProvider` 采用**全量同步**模式，从外部数据源拉取全量实体，与本地数据库进行比对后执行新增、更新、删除操作。

## 流程图

```text
importAssets(instance)              ← 入口，带 @EdsTaskLock 分布式锁
│
├── listEntities(instance)          ← 子类实现，从外部数据源拉取实体列表
│
└── syncEntities(instance, entities)
    │
    ├── getExistingAssetIds()       ← 查询本地已有资产ID集合
    │
    ├── forEach → syncEntity(instance, idSet, entity)
    │   │
    │   ├── importEntityAsAsset(instance, entity)
    │   │   │
    │   │   ├── convertToEdsAsset()            ← 子类实现，实体 → EdsAsset
    │   │   │
    │   │   ├── upsertAsset(edsAsset)
    │   │   │   │
    │   │   │   ├── doUpsertAsset()
    │   │   │   │   ├── getByUniqueKey()       ← 查库判断是否已存在
    │   │   │   │   ├── [不存在] insertAsset()
    │   │   │   │   │   ├── add()
    │   │   │   │   │   └── afterAssetCreated()        ← 扩展点
    │   │   │   │   └── [已存在 & 有变更]
    │   │   │   │       ├── updateByPrimaryKey()
    │   │   │   │       └── assetToBusinessObjectUpdater.update()
    │   │   │   │
    │   │   │   └── postProcessAsset()         ← 扩展点
    │   │   │
    │   │   ├── mergeAssetIndices()
    │   │   │   ├── buildIndexes()             ← 扩展点，构建多个索引
    │   │   │   └── buildIndex()               ← 扩展点，构建单个索引
    │   │   │
    │   │   ├── saveAssetIndexList()            ← 持久化索引
    │   │   │
    │   │   └── processAssetTags()             ← 扩展点，处理标签
    │   │
    │   └── idSet.remove(asset.id)              ← 从待删除集合中移除
    │
    ├── [idSet 非空] 删除本地多余资产           ← 外部已不存在的资产
    │   └── forEach → deleteEdsAssetById()
    │
    └── postProcessEntities()                  ← 扩展点
```

## 同步策略

| 场景 | 操作 |
|---|---|
| 外部存在，本地不存在 | 插入新资产 |
| 外部存在，本地存在且有变更 | 更新资产及关联业务对象 |
| 外部存在，本地存在且无变更 | 跳过 |
| 外部不存在，本地存在 | 删除本地资产 |

## 子类必须实现

| 方法 | 说明 |
|---|---|
| `listEntities` | 从外部数据源拉取实体列表 |
| `convertToEdsAsset` | 将外部实体转换为 EdsAsset |

## 子类可选扩展点

| 方法 | 说明 |
|---|---|
| `buildIndex` / `buildIndexes` | 构建资产索引 |
| `processAssetTags` | 处理资产标签 |
| `afterAssetCreated` | 新增资产后的回调 |
| `postProcessAsset` | 单个资产 upsert 后的回调 |
| `postProcessEntities` | 全量同步完成后的回调 |
| `isAssetUnchanged` | 自定义资产变更比较逻辑 |
