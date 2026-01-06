#!/bin/bash

# 组件版本分析脚本
echo "=== Cratos 项目组件版本分析 ==="
echo "生成时间: $(date)"
echo "总组件数: $(wc -l < component-versions.txt)"
echo ""

echo "=== 主要框架版本 ==="
grep -E "(spring-boot|spring-|jackson-|netty-)" component-versions.txt | head -10

echo ""
echo "=== 云服务SDK版本 ==="
grep -E "(aws-|aliyun|azure|google|huaweicloud)" component-versions.txt | head -10

echo ""
echo "=== 数据库相关组件 ==="
grep -E "(mysql|redis|mongodb|druid|hikari)" component-versions.txt

echo ""
echo "=== 安全相关组件 ==="
grep -E "(security|crypto|auth|jwt|oauth)" component-versions.txt

echo ""
echo "=== 版本统计 ==="
echo "Spring Boot 相关:"
grep "spring-boot" component-versions.txt | wc -l
echo "AWS SDK 相关:"
grep "aws-" component-versions.txt | wc -l
echo "阿里云 SDK 相关:"
grep "aliyun" component-versions.txt | wc -l
echo "Jackson 相关:"
grep "jackson" component-versions.txt | wc -l
