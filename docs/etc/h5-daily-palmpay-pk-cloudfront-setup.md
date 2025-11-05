# h5-daily.palmpay.pk CloudFront 配置文档

## 架构概览

```
用户请求 → Route53 → CloudFront → 源站(h5-daily.palmpay.app)
```

## 1. Route53 DNS 配置

### CNAME 记录
```
域名: h5-daily.palmpay.pk
类型: CNAME
值: d34f06dumvemg3.cloudfront.net
TTL: 300秒
```

### 作用
- 将自定义域名指向CloudFront分发域名
- 支持巴基斯坦本地域名访问
- 提供更好的用户体验

## 2. CloudFront 分发配置

### 基本信息
- **分发ID**: E286QOH9H4NH
- **域名**: d34f06dumvemg3.cloudfront.net
- **自定义域名**: h5-daily.palmpay.pk
- **状态**: Deployed

### 源站配置
```yaml
源站ID: h5-daily-palmpay-app
域名: h5-daily.palmpay.app
协议: HTTPS Only
端口: 443
超时设置:
  - 读取超时: 30秒
  - 保持连接: 5秒
```

### SSL/TLS 证书
```yaml
证书类型: ACM证书
证书ARN: arn:aws:acm:us-east-1:502076313352:certificate/99bb45c6-d03d-41e6-9243-2f20ddc501f3
SSL支持方法: SNI Only
最低TLS版本: TLSv1.2_2021
```

## 3. 缓存策略

### 默认缓存行为
```yaml
TTL设置:
  - MinTTL: 0秒
  - DefaultTTL: 86400秒 (24小时)
  - MaxTTL: 31536000秒 (365天)

允许方法: GET, HEAD
缓存方法: GET, HEAD
压缩: 关闭
```

### 智能HTML缓存策略
使用CloudFront Function实现基于Content-Type的差异化缓存：

```javascript
// Function: no-cache-html-function
function handler(event) {
    var response = event.response;
    var headers = response.headers;
    
    if (headers['content-type'] && 
        headers['content-type'].value.includes('text/html')) {
        headers['cache-control'] = {
            value: 'no-cache, no-store, must-revalidate'
        };
    }
    
    return response;
}
```

### 缓存行为说明
- **HTML文件**: 不缓存 (实时更新)
- **静态资源** (JS/CSS/图片): 缓存24小时 (性能优化)

## 4. 请求转发配置

### 查询字符串和Headers
```yaml
查询字符串: 不转发到源站
Cookies: 不转发
自定义Headers: 无
```

### 地理限制
```yaml
限制类型: 无限制
允许所有国家/地区访问
```

## 5. 监控和日志

### 访问日志
```yaml
状态: 关闭
存储桶: 未配置
前缀: 未配置
```

### 监控指标
- 请求数量
- 缓存命中率
- 错误率
- 延迟统计

## 6. 运维操作

### 缓存清除
```bash
# 清除所有缓存
aws cloudfront create-invalidation \
  --distribution-id E286QOH9H4NH \
  --paths "/*"

# 清除特定路径
aws cloudfront create-invalidation \
  --distribution-id E286QOH9H4NH \
  --paths "/pk-h5/*"
```

### Function管理
```bash
# 发布Function到LIVE环境
aws cloudfront publish-function \
  --name no-cache-html-function \
  --if-match ETAG_VALUE
```

## 7. 性能优化

### 边缘位置
- 全球200+边缘位置
- 亚太地区就近访问
- 巴基斯坦用户低延迟

### 缓存优化
- HTML实时更新确保内容准确性
- 静态资源缓存减少源站压力
- 智能压缩提升传输效率

## 8. 故障排查

### 常见问题
1. **缓存不生效**: 检查Function是否在LIVE环境
2. **内容不更新**: 执行缓存清除操作
3. **SSL错误**: 验证证书配置和域名匹配

### 检查命令
```bash
# 检查响应头
curl -I https://h5-daily.palmpay.pk/

# 检查缓存状态
curl -I https://h5-daily.palmpay.pk/ | grep -E "(x-cache|age|cache-control)"
```

## 9. 安全配置

### HTTPS强制
- 所有HTTP请求重定向到HTTPS
- 支持HTTP/2协议
- 现代TLS加密

### 访问控制
- 基于地理位置: 无限制
- WAF集成: 未配置
- 自定义错误页面: 未配置

## 10. 成本优化

### 价格等级
```yaml
价格等级: PriceClass_All
覆盖范围: 全球所有边缘位置
成本: 最高但性能最佳
```

### 优化建议
- 监控流量模式调整价格等级
- 合理设置缓存TTL减少源站请求
- 定期清理不必要的缓存

---

## 总结

h5-daily.palmpay.pk 通过 CloudFront + Route53 的组合，实现了：
- ✅ 全球CDN加速
- ✅ 自定义域名支持
- ✅ 智能缓存策略
- ✅ HTTPS安全传输
- ✅ 高可用性保障

这套架构为巴基斯坦用户提供了快速、安全、可靠的H5应用访问体验。
