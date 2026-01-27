# Cratos 端到端加密实现文档

## 概述

Cratos 实现了基于 RSA + AES 的混合加密方案，支持多版本密钥管理，确保前后端通信安全。

## 加密流程

```
前端                                    后端
  |                                      |
  | 1. 生成随机 AES-256 密钥              |
  | 2. 用 AES-GCM 加密请求体              |
  | 3. 用 RSA-OAEP 加密 AES 密钥          |
  |                                      |
  | POST /api/endpoint                   |
  | Headers:                             |
  |   X-Body-Encrypted: true             |
  |   X-Encryption-Key-Version: v1       |
  | Body:                                |
  |   {                                  |
  |     encryptedBody: "base64...",      |
  |     encryptedKey: "base64..."        |
  |   }                                  |
  | -----------------------------------> |
  |                                      | 4. 根据版本号获取私钥
  |                                      | 5. 用 RSA 私钥解密 AES 密钥
  |                                      | 6. 用 AES 密钥解密请求体
  |                                      | 7. 处理业务逻辑
  |                                      |
  | <----------------------------------- |
  |         正常响应                      |
```

## 前端实现

### 1. 配置文件

**文件**: `src/app/@core/config/encryption.config.ts`

```typescript
export const EncryptionConfig = {
  enabled: true,
  keyVersion: 'v1',
  publicKeyBase64: 'LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0K...',
  encryptionHeader: 'X-Body-Encrypted',
  keyVersionHeader: 'X-Encryption-Key-Version',
};
```

### 2. 加密服务

**文件**: `src/app/@core/services/encryption.service.ts`

核心方法：
- `encryptBody(data)` - 加密请求体
- `generateAESKey()` - 生成 AES-256 密钥
- `encryptWithAES()` - AES-GCM 加密
- `encryptWithRSA()` - RSA-OAEP 加密 AES 密钥

### 3. API 集成

**文件**: `src/app/@core/services/api.service.ts`

自动拦截 POST/PUT/DELETE 请求，加密后发送。

## 后端实现

### 1. 配置文件

**文件**: `cratos-manage/src/main/resources/application.yml`

```yaml
encryption:
  enabled: true
  keys:
    - version: v1
      privateKey: LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2UUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktjd2dnU2pBZ0VBQW9JQkFRREFlYnppNDhIQ05kV1QKenl6cEhQMWNZSUliZDkxT0hQaUp5M25qVmExRlhndkFzN3ZMSkgyVXQ1bkN0eUtMQzJUSUJMbzFSbXcrZHRIWAo0SHQ2Q3NhNGsvR2ZYY04xVFRrV0hGMVprVitxb1pQa0hUTGV4c1F1Y09sLzB6L05qQ3laeWJxWXBiNFV5Y1dMCkN6bkJhRjl1c1BmZlVYZWxnODNqa0tCNEZyNkhhYTR2SStBdTdaeWdyaFp2RVJoSTZpMCs5UnJlWWhuaWpmU1gKSGVRUVR3ekZTWndIZlFUblg4STZvUkdWQ1FPcWYrU0tMRTRhK2Yrc21saFVQZ0g2NERMZzlWQkZsQXJzMkFHaApxcmg5YTl4SFdDVkNBODlrMkdBbXdqQndLVVlkNmJpZ1dMUGZYeURwYkxDL2hwN3hJaEVreXlGcGl5cXp3dHY1CksvM2FLd0RkQWdNQkFBRUNnZ0VBTWt3ZHhoVEtmMXZlRWlBamxrOVREaDdTSVNkemw0UU9sc09yZUhMZE1yRmUKMU42akJuVGt0RVkrVWhuVlZ3eUZLNWZBcVVkYjVmN2EyMVQ0TDI0OWUrSjlVNjRHODlKWVJxVG02QThMTnVXKwpaZkFUYVEvOEdENjlaZi9vUG1pUkJ5Y21NZG11RUhTaHdMSVFSN0JrcitKakErS2dKNHFIOVIzSU1WbVFOSVkvClQvbk5vYmtkamRPT3l3K3JNTmR6STd2VWpYUU1OSm9IM2tWYkJtT3ZtMFN6UUVWR1NDbTVoeDhGd0pzQzRMbFAKbVdYZmE1dlpZSDBTYjZOSTNJYVRBdTF4T0p5YUtxeG1OZnQ5bUZlSE4yMmdmS1hXMGNoSmQxU0pyY3RzSVQxcwpwaVNjM28xZkRZOXBKUnZ4QTJSeDF3WmNJdm44Z0kvS1FSNGlyWHU0YVFLQmdRRGZaMy9tZVlqaldVTGtuZzJQCmJTazZNY0MveTVOR1pvdDJmclNqUmlEcjBXWDNUNnNEYkxKcjYreXY4dC80SjdaSnVRNGM1Q1JSYld6eTdZd2MKUzd0YW9yYnlmT3lJeVlXazNpTGF5SEFFWHZ1WTlNc2t5cjZGL04wd3c1SkloamQ0QkoyK2RlWmdtQjkrQnlUZgppK1ZQNDNkYk9nOXU0dUR0MUxhRkxCR3dNd0tCZ1FEY2p3QXVSZDYxNHZzeDFreHEzWEs5MlVwYTMvbkREOC8yCkVGSW5hVzBIdE1Zb2ZFSnZ4akhJQlBrQ2V0WHBaMGZDNVpySmJEMXZablE4WnkxcmwybzE0cE5VS0E0bzZMZU0KSFF0RCsrRmtXRmJ0eWVJUXd6RUlheWhXNStyQ3IvcURTSE51bjRVcC9IcS95c0J0T0p4UWIzUXhxL2Y5VDE1UQp5UzRzSHpnNnJ3S0JnUUM3K1JNVjhyYnFUSVhMWk8yVis4eVBxYU03L2VlYWVkZm95UzNtbjdBOW00QkhPaUp6CmpEY0lOWWQrZUVaWlA5alNhVUhscjFTYnF3M2J6V0p4d0lzUTlRNG9ORksxdVpLWXFVdjhlR0JBWFVjbmFQbXcKcUdMc3pGbTBtU254bWZUOEtDNHVQbmVzV1BOT2tHWkhMaWV4TTN5UEp1Z3JiZnpDUUpPZXMvamlId0tCZ0NRSgphV29tR0dGM21MdG4zRXlKcWpuQ3l4ZFdDU1VKN0Z5MEJmaFNqWEg0NDNleDlkajFkU1ROU3pKUWg2ci9LUVo3CkpYTkxzQzRIZTI1ekhVUzdGS3VCUGJrK1lqSGs5RGg4ZGw3QlJPNTBVSi9Cb0tKRWdqcHg1OEZyV3p3OExKNU8KdEMyYSt0TUZZYmo4azd4RXg2V00wRnRkVWNPZHVEZFM1NnhsM1hZWkFvR0FEdFZ0c1BDU3MzVnpDYWpML2dGVAo4dnBVU0x1amxGaENoWVN1Mnd4Z1IxZXdmVms4NEJnOWpyYysxcFJ5RDUvODlPclpDQzNEa0FYSkVsY0h2eGVUCm53cDR6eHdFUkJ0aFJZd1Q1U3RVQkhOd1hDNzVzVmg3Z0RPM2RwYTMzLzhsS1RabWhzQS9SMEc1bnZJY3lSUkQKM3lGWVVKNEVUK1NrNmdmZDFPdlhvcmM9Ci0tLS0tRU5EIFBSSVZBVEUgS0VZLS0tLS0K
```

### 2. 配置类

**文件**: `com.baiyi.cratos.facade.auth.config.EncryptionConfig`

```java
@Data
@Component
@ConfigurationProperties(prefix = "encryption")
public class EncryptionConfig {
    private boolean enabled = true;
    private List<KeyConfig> keys = new ArrayList<>();

    @Data
    public static class KeyConfig {
        private String version;
        private String privateKey; // Base64 编码
    }
}
```

### 3. 密钥管理服务

**文件**: `com.baiyi.cratos.facade.auth.service.KeyManagementService`

核心方法：
- `getPrivateKey(version)` - 获取指定版本私钥
- `hasVersion(version)` - 检查版本是否存在
- `getDefaultVersion()` - 获取默认版本

### 4. 解密工具

**文件**: `com.baiyi.cratos.facade.auth.util.BodyDecryptionUtil`

核心方法：
- `decryptBody()` - 解密请求体
- `decryptWithRSA()` - RSA-OAEP 解密 AES 密钥
- `decryptWithAES()` - AES-GCM 解密请求体

### 5. 认证过滤器

**文件**: `com.baiyi.cratos.facade.auth.filter.AuthenticationTokenFilter`

在认证前自动解密请求体。

## 密钥管理

### 当前密钥（v1）

**公钥**:
```
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwHm84uPBwjXVk88s6Rz9
XGCCG3fdThz4ict541WtRV4LwLO7yyR9lLeZwrciiwtkyAS6NUZsPnbR1+B7egrG
uJPxn13DdU05FhxdWZFfqqGT5B0y3sbELnDpf9M/zYwsmcm6mKW+FMnFiws5wWhf
brD331F3pYPN45CgeBa+h2muLyPgLu2coK4WbxEYSOotPvUa3mIZ4o30lx3kEE8M
xUmcB30E51/COqERlQkDqn/kiixOGvn/rJpYVD4B+uAy4PVQRZQK7NgBoaq4fWvc
R1glQgPPZNhgJsIwcClGHem4oFiz318g6Wywv4ae8SIRJMshaYsqs8Lb+Sv92isA
3QIDAQAB
-----END PUBLIC KEY-----
```

**私钥**: 存储在 `application.yml` 中（Base64 编码）

### 密钥轮换

#### 1. 生成新密钥对

```bash
# 生成私钥
openssl genrsa -out private-v2.pem 2048

# 生成公钥
openssl rsa -in private-v2.pem -pubout -out public-v2.pem

# 转换为 PKCS8 格式
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt \
  -in private-v2.pem -out private-v2-pkcs8.pem

# Base64 编码
cat private-v2-pkcs8.pem | base64 | tr -d '\n'
```

#### 2. 添加到配置

```yaml
encryption:
  keys:
    - version: v1
      privateKey: BASE64_V1
    - version: v2
      privateKey: BASE64_V2  # 新版本
```

#### 3. 更新前端

```typescript
// encryption.config.ts
keyVersion: 'v2'
publicKeyBase64: 'BASE64_PUBLIC_KEY_V2'
```

#### 4. 部署

1. 重启后端（支持 v1 和 v2）
2. 发布前端（使用 v2）
3. 旧版本客户端仍可使用 v1

## 加密算法

### RSA-OAEP
- **密钥长度**: 2048 位
- **Hash**: SHA-256
- **MGF**: MGF1 with SHA-256
- **用途**: 加密 AES 密钥

### AES-GCM
- **密钥长度**: 256 位
- **IV 长度**: 12 字节（随机生成）
- **Tag 长度**: 128 位
- **用途**: 加密请求体

## 安全特性

✅ **端到端加密** - 请求体在客户端加密，服务端解密  
✅ **混合加密** - RSA + AES，兼顾安全性和性能  
✅ **多版本支持** - 支持密钥轮换，平滑升级  
✅ **随机 IV** - 每次请求使用不同的 IV  
✅ **认证加密** - AES-GCM 提供完整性验证  
✅ **密钥缓存** - 提升解密性能  

## 测试

### 前端测试

```typescript
// 测试加密
const encrypted = await encryptionService.encryptBody({ username: 'test' });
console.log(encrypted);
// { encryptedBody: "...", encryptedKey: "..." }
```

### 后端测试

```bash
# 测试解密
curl -X POST http://localhost:8080/api/test \
  -H "Content-Type: application/json" \
  -H "X-Body-Encrypted: true" \
  -H "X-Encryption-Key-Version: v1" \
  -d '{
    "encryptedBody": "base64...",
    "encryptedKey": "base64..."
  }'
```

## 故障排查

### 1. BadPaddingException

**原因**: 公钥私钥不匹配或算法参数不一致

**解决**:
- 确认前后端使用相同的密钥对
- 检查 RSA 算法参数（SHA-256, MGF1）

### 2. Invalid key version

**原因**: 请求的版本号不存在

**解决**:
- 检查 `application.yml` 中是否配置了该版本
- 确认前端 `keyVersion` 配置正确

### 3. 解密失败

**原因**: 数据传输过程中被修改

**解决**:
- 检查 Base64 编码是否正确
- 确认没有中间代理修改请求体

## 性能优化

- ✅ 密钥缓存（避免重复解码）
- ✅ 只加密敏感接口
- ✅ 使用高效的加密算法（AES-GCM）
- ✅ 异步处理（前端）

## 未来改进

- [ ] 支持响应体加密
- [ ] 添加密钥过期机制
- [ ] 实现密钥自动轮换
- [ ] 支持客户端证书认证
- [ ] 添加加密性能监控

## 相关文件

### 前端
- `src/app/@core/config/encryption.config.ts`
- `src/app/@core/services/encryption.service.ts`
- `src/app/@core/services/api.service.ts`

### 后端
- `cratos-manage/src/main/java/com/baiyi/cratos/facade/auth/config/EncryptionConfig.java`
- `cratos-manage/src/main/java/com/baiyi/cratos/facade/auth/service/KeyManagementService.java`
- `cratos-manage/src/main/java/com/baiyi/cratos/facade/auth/util/BodyDecryptionUtil.java`
- `cratos-manage/src/main/java/com/baiyi/cratos/facade/auth/filter/AuthenticationTokenFilter.java`
- `cratos-manage/src/main/resources/application.yml`

---

**文档版本**: 1.0  
**最后更新**: 2026-01-27  
**维护者**: Cratos Team
