# EdsComputerOpsCommand 帮助文档

## 概述

`EdsComputerOpsCommand` 是一个Spring Shell命令组件，用于对云计算机资源进行操作管理，包括启动、停止和重启等操作。

## 命令信息

- **命令组**: "Eds CloudComputer Commands"
- **主命令**: `computer-ops` (别名: `cops`)
- **权限要求**: `/computer/ops`
- **清屏**: 执行前自动清屏

## 命令语法

```bash
computer-ops [--id <id>] [--stop] [--start] [--reboot]
# 或使用别名
cops [--id <id>] [--stop] [--start] [--reboot]
```

## 参数说明

| 参数 | 类型 | 默认值 | 必需 | 说明 |
|------|------|--------|------|------|
| `--id` | int | 1 | 否 | 计算机资源ID |
| `--stop` | boolean | false | 否 | 停止计算机 |
| `--start` | boolean | false | 否 | 启动计算机 |
| `--reboot` | boolean | false | 否 | 重启计算机 |

## 使用示例

### 1. 停止计算机
```bash
computer-ops --id 1 --stop
cops --id 2 --stop
```

### 2. 启动计算机
```bash
computer-ops --id 1 --start
cops --id 3 --start
```

### 3. 重启计算机
```bash
computer-ops --id 1 --reboot
cops --id 4 --reboot
```

## 执行流程

1. **验证权限**: 检查用户是否有 `/computer/ops` 权限
2. **获取上下文**: 从 `ComputerAssetContext` 获取计算机资源映射
3. **验证资源**: 检查指定ID的计算机是否存在
4. **执行操作**: 根据参数调用相应的操作方法
5. **记录日志**: 记录操作用户、资源信息和操作类型

## 前置条件

- 必须先执行 `computer-list` 命令加载计算机资源列表
- 用户需要具备相应的操作权限
- 指定的计算机ID必须存在于当前上下文中

## 错误处理

- **资源不存在**: 提示用户先执行 `computer-list` 命令
- **参数缺失**: 提示用户必须指定至少一个操作参数
- **权限不足**: 系统会自动进行权限验证

## 注意事项

1. **互斥操作**: 每次只能执行一种操作（停止、启动或重启）
2. **资源状态**: 操作前建议确认计算机当前状态
3. **日志记录**: 所有操作都会记录详细的审计日志
4. **异步执行**: 操作可能是异步的，需要通过其他命令查看状态

## 配置要求

- 需要启用相关配置: `cratos.ssh-shell.commands.computer.create=true`
- 支持国际化配置: `cratos.language`
- 支持通知配置: `cratos.notification`

## 相关命令

- `computer-list`: 列出可用的计算机资源
- `computer-login`: 登录到指定计算机
- `computer-status`: 查看计算机状态

## 代码位置

```
cratos-shell/src/main/java/com/baiyi/cratos/shell/command/custom/eds/EdsComputerOpsCommand.java
```
