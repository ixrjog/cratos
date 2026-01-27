# 多版本证书管理实现指南

## 1. 在 application.yml 添加配置

```yaml
encryption:
  enabled: true
  keys:
    - version: v1
      privateKey: XXXXXXXXX
```

## 2. 创建配置类

文件: `cratos-manage/src/main/java/com/baiyi/cratos/facade/auth/config/EncryptionConfig.java`

## 3. 创建密钥管理服务

文件: `cratos-manage/src/main/java/com/baiyi/cratos/facade/auth/service/KeyManagementService.java`

## 4. 修改 AuthenticationTokenFilter

在 `AuthenticationTokenFilter` 中：
1. 注入 `KeyManagementService`
2. 从请求头 `X-Encryption-Key-Version` 获取版本号
3. 使用 `keyManagementService.getPrivateKey(version)` 获取对应私钥
4. 传递给 `BodyDecryptionUtil.decryptBody()`

## 5. 前端配置

前端已有版本号配置：
```typescript
// encryption.config.ts
keyVersion: 'v1'
```

请求时会自动添加 Header:
```
X-Encryption-Key-Version: v1
```

## 6. 密钥轮换流程

### 添加新版本密钥：

1. 生成新密钥对：
```bash
openssl genrsa -out private-v2.pem 2048
openssl rsa -in private-v2.pem -pubout -out public-v2.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt \
  -in private-v2.pem -out private-v2-pkcs8.pem
```

2. Base64 编码私钥：
```bash
cat private-v2-pkcs8.pem | base64 | tr -d '\n'
```

3. 添加到 application.yml：
```yaml
encryption:
  keys:
    - version: v1
      privateKey: BASE64_V1
    - version: v2
      privateKey: BASE64_V2  # 新版本
```

4. 更新前端公钥和版本号：
```typescript
keyVersion: 'v2'
publicKeyBase64: 'BASE64_PUBLIC_KEY_V2'
```

5. 重启后端，发布前端

### 优势：

- ✅ 支持多版本共存
- ✅ 平滑升级，不影响旧版本客户端
- ✅ 私钥 Base64 存储，便于配置管理
- ✅ 支持密钥轮换
- ✅ 缓存机制提升性能

## 7. 测试

```bash
# 测试 v1 版本
curl -X POST https://api.example.com/endpoint \
  -H "X-Body-Encrypted: true" \
  -H "X-Encryption-Key-Version: v1" \
  -H "Content-Type: application/json" \
  -d '{"encryptedBody":"...","encryptedKey":"..."}'

# 测试 v2 版本
curl -X POST https://api.example.com/endpoint \
  -H "X-Body-Encrypted: true" \
  -H "X-Encryption-Key-Version: v2" \
  -H "Content-Type: application/json" \
  -d '{"encryptedBody":"...","encryptedKey":"..."}'
```
