# Cratos Body 解密功能使用说明

## 功能概述

自动解密前端发送的加密 Body（混合加密：RSA + AES-GCM）

## 已创建文件

### 1. 核心工具类
- `BodyDecryptionUtil.java` - 解密工具类
- `EncryptionProperties.java` - 配置类
- `BodyDecryptionFilter.java` - 解密过滤器
- `DecryptedRequestWrapper.java` - 请求包装器

### 2. 配置示例
- `ENCRYPTION_CONFIG_EXAMPLE.yml` - 配置示例

## 配置方法

### 1. 在 application.yml 中添加配置

```yaml
cratos:
  encryption:
    # 启用解密
    enabled: true
    
    # RSA 私钥（PEM 格式）
    private-key-v1: |
      -----BEGIN PRIVATE KEY-----
      MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDEly6M6kN93mC1
      ...（完整私钥）...
      -----END PRIVATE KEY-----
```

### 2. 使用环境变量（推荐生产环境）

```yaml
cratos:
  encryption:
    enabled: true
    private-key-v1: ${ENCRYPTION_PRIVATE_KEY_V1}
```

然后设置环境变量：
```bash
export ENCRYPTION_PRIVATE_KEY_V1="-----BEGIN PRIVATE KEY-----
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDEly6M6kN93mC1
...
-----END PRIVATE KEY-----"
```

## 工作流程

```
前端请求
  ↓ Headers: X-Body-Encrypted: true, X-Encryption-Key-Version: v1
  ↓ Body: {encryptedBody, encryptedKey}
  ↓
BodyDecryptionFilter（过滤器）
  ↓ 检测加密 Headers
  ↓ 读取加密 Body
  ↓ 调用 BodyDecryptionUtil.decryptBody()
  ↓ RSA 解密 AES 密钥
  ↓ AES 解密 Body
  ↓ 创建 DecryptedRequestWrapper
  ↓ 替换原始请求 Body
  ↓
Controller
  ↓ 接收明文 JSON
  ↓ 正常处理业务逻辑
```

## 使用示例

### Controller 代码无需修改

```java
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginParam loginParam) {
        // 直接使用，Body 已自动解密
        return Result.success(userService.login(loginParam));
    }
}
```

### 前端请求示例

```typescript
// 前端发送加密请求
this.apiService.post('/user', '/login', {
  username: 'test',
  password: '123456'
}).subscribe(response => {
  console.log(response);
});
```

## 密钥轮换

### 1. 添加新版本密钥

```yaml
cratos:
  encryption:
    enabled: true
    private-key-v1: |
      -----BEGIN PRIVATE KEY-----
      ...（旧密钥）...
      -----END PRIVATE KEY-----
    
    private-key-v2: |
      -----BEGIN PRIVATE KEY-----
      ...（新密钥）...
      -----END PRIVATE KEY-----
```

### 2. 前端切换到新版本

```typescript
// encryption.config.ts
export const EncryptionConfig = {
  keyVersion: 'v2',  // 改为 v2
  // ...
};
```

### 3. 逐步迁移后移除旧密钥

## 测试

### 1. 单元测试

```java
@Test
public void testDecryption() {
    String encryptedBody = "iv.ciphertext";
    String encryptedKey = "rsa_encrypted_key";
    String privateKey = "-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----";
    
    String decrypted = BodyDecryptionUtil.decryptBody(encryptedBody, encryptedKey, privateKey);
    
    assertEquals("{\"username\":\"test\"}", decrypted);
}
```

### 2. 集成测试

```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -H "X-Body-Encrypted: true" \
  -H "X-Encryption-Key-Version: v1" \
  -d '{
    "encryptedBody": "...",
    "encryptedKey": "..."
  }'
```

## 注意事项

1. ✅ 过滤器自动处理，Controller 无需修改
2. ✅ 支持多版本密钥，便于密钥轮换
3. ✅ 只处理 POST/PUT/DELETE 请求
4. ✅ GET 请求不受影响
5. ⚠️ 私钥必须保密，不要提交到 Git
6. ⚠️ 生产环境使用环境变量或密钥管理服务
7. ⚠️ 启用前确保前端已配置对应的公钥

## 性能影响

- 解密耗时: ~5-10ms
- 对 API 响应时间影响: 可忽略
- 建议在生产环境启用

## 故障排查

### 问题：解密失败

**检查：**
1. 前端公钥和后端私钥是否匹配
2. 密钥版本是否正确
3. 查看日志：`log.error("Body decryption failed", e)`

### 问题：配置不生效

**检查：**
1. `enabled: true` 是否设置
2. 私钥格式是否正确（包含 BEGIN/END 行）
3. 是否重启应用

## 安全建议

1. ✅ 使用环境变量存储私钥
2. ✅ 定期轮换密钥（3-6个月）
3. ✅ 监控解密失败率
4. ✅ 记录解密日志（不记录密钥）
5. ⚠️ 不要在代码中硬编码私钥
6. ⚠️ 不要提交私钥到 Git
