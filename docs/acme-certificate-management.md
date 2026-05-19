# ACME 自动化证书管理

## 概述

Cratos 平台集成了 ACME（Automatic Certificate Management Environment）协议，支持自动化申请、签发和部署 SSL/TLS 证书。支持 Let's Encrypt、ZeroSSL 等 ACME 提供商。

## 核心流程

```
创建账户 → 添加域名 → 签发证书 → 自动部署
```

## 1. 创建 ACME 账户

**接口：** `POST /api/acme/account/create`

**流程：**
1. 验证邮箱（使用提供的邮箱或当前用户邮箱）
2. 根据 ACME Provider 创建密钥对
3. 向 ACME 服务器注册账户（支持 EAB 认证）
4. 保存账户信息（密钥对、账户 URL、服务器地址）

**支持的 Provider：**
- Let's Encrypt
- ZeroSSL（需要 EAB Kid + HMAC Key）

## 2. 添加域名

**接口：** `POST /api/acme/domain/add`

**流程：**
1. 关联 DNS 解析实例（用于自动添加 DNS 验证记录）
2. 获取 DNS Zone ID
3. 配置域名列表（默认生成 `*.domain.com` + `domain.com`）

**关键字段：**
- `domain` — 主域名
- `domains` — 证书包含的域名列表（逗号分隔）
- `dnsResolverInstanceId` — DNS 解析服务实例
- `zoneId` — DNS Zone ID
- `dcvType` — 域名验证类型
- `dcvDelegationTarget` — DCV 委托目标

## 3. 签发证书

**接口：** `POST /api/acme/certificate/issue`（异步）

**完整流程：**

```
1. 创建 Order
   ├── 生成域名密钥对（RSA 2048）
   ├── 向 ACME 服务器提交订单
   └── 获取 DNS Challenge 记录

2. DNS 验证
   ├── 删除冲突的 _acme-challenge 记录
   ├── 添加 DNS TXT 记录（challenge digest）
   ├── 等待 DNS 传播（60s）
   └── 触发验证并轮询状态

3. 提交 CSR
   ├── 构建 CSR（包含所有域名）
   ├── 使用域名密钥对签名
   └── 提交给 ACME 服务器

4. 等待签发
   └── 轮询 Order 状态直到 VALID（最多 30s）

5. 下载证书
   ├── 获取证书链
   ├── 分离域名证书和中间证书
   ├── 保存到数据库
   └── 记录有效期

6. 自动部署
   └── 部署到所有标记了 ACME tag 的 EDS 实例

7. 清理
   ├── 删除 DNS Challenge 记录
   └── 恢复 DCV 委托记录
```

## 4. 恢复订单签发

**接口：** `PUT /api/acme/order/resume`

用于 PENDING 状态的订单恢复签发（如 DNS 验证超时后重试）：
- 仅 PENDING 且未过期的订单可恢复
- 重新绑定已有 Order，继续验证流程

## 5. 自动部署

**触发时机：** 证书签发成功后自动执行

**部署逻辑：**
1. 查询所有标记了 `ACME` tag 的 EDS 实例
2. 按实例类型获取对应的 Deployer
3. 逐个部署证书

**支持的部署目标：**
- 由 `EdsInstanceTypeEnum.ACME_TYPES` 定义
- 通过 `AcmeDeployerFactory` 获取对应实现

## 6. 订单管理

**删除规则：**
- `INVALID` 状态 → 直接删除
- `PENDING` 状态且创建超过 1 小时 → 允许删除
- `PENDING` 状态且创建未超过 1 小时 → 禁止删除
- 已关联证书 → 禁止删除

## 数据模型

| 实体 | 说明 |
|------|------|
| AcmeAccount | ACME 账户（密钥对、服务器地址） |
| AcmeDomain | 域名配置（DNS 解析实例、Zone ID） |
| AcmeOrder | 签发订单（状态、Challenge 记录、域名密钥对） |
| AcmeCertificate | 证书（证书链、私钥、有效期） |

## DNS 验证机制

使用 DNS-01 Challenge：
1. ACME 服务器要求在 `_acme-challenge.<domain>` 添加 TXT 记录
2. 系统通过 DNS Resolver 实例自动添加记录
3. 验证通过后自动清理记录
4. 支持 DCV 委托（将验证委托给指定 CNAME）
