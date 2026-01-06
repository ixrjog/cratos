#!/bin/bash

# 安全漏洞检查脚本
echo "=== Cratos 项目安全漏洞分析 ==="
echo "分析时间: $(date)"
echo ""

# 提取干净的组件列表
sed 's/\[[0-9;]*m//g' dependencies.csv | \
grep -E "^\s+[a-zA-Z0-9]" | \
sed 's/^\s*//' | \
sed 's/ -- module.*$//' | \
sed 's/:jar:/:/' | \
sed 's/:compile$//' | \
sed 's/:runtime$//' | \
sed 's/:test$//' | \
sed 's/ (optional)$//' | \
cut -d':' -f1,2,3 > temp_components.txt

echo "总组件数: $(wc -l < temp_components.txt)"
echo ""

# 已知高危漏洞组件检查
echo "=== 高危漏洞组件检查 ==="

# Log4j 漏洞 (CVE-2021-44228, CVE-2021-45046)
echo "🔍 检查 Log4j 漏洞 (CVE-2021-44228):"
grep -E "log4j.*:(1\.|2\.0|2\.1[0-6])" temp_components.txt || echo "✅ 未发现 Log4j 高危版本"

# Jackson 漏洞
echo ""
echo "🔍 检查 Jackson 反序列化漏洞:"
jackson_versions=$(grep "jackson" temp_components.txt)
if [ -n "$jackson_versions" ]; then
    echo "$jackson_versions"
    echo "⚠️  请检查 Jackson 版本是否存在反序列化漏洞"
else
    echo "✅ 未使用 Jackson"
fi

# Spring 漏洞
echo ""
echo "🔍 检查 Spring 相关漏洞:"
spring_versions=$(grep -E "spring-(core|web|webmvc)" temp_components.txt)
if [ -n "$spring_versions" ]; then
    echo "$spring_versions"
    echo "⚠️  请检查 Spring 版本安全公告"
else
    echo "✅ 未发现 Spring 核心组件"
fi

# Fastjson 漏洞 (CVE-2022-25845)
echo ""
echo "🔍 检查 Fastjson 漏洞 (CVE-2022-25845):"
fastjson_version=$(grep "fastjson" temp_components.txt)
if [ -n "$fastjson_version" ]; then
    echo "$fastjson_version"
    echo "🚨 Fastjson 存在多个高危漏洞，建议替换为 Jackson"
else
    echo "✅ 未使用 Fastjson"
fi

# Netty 漏洞
echo ""
echo "🔍 检查 Netty 漏洞:"
netty_versions=$(grep "netty" temp_components.txt | head -3)
if [ -n "$netty_versions" ]; then
    echo "$netty_versions"
    echo "⚠️  请检查 Netty 版本安全公告"
else
    echo "✅ 未使用 Netty"
fi

# MySQL Connector 漏洞
echo ""
echo "🔍 检查 MySQL Connector 漏洞:"
mysql_version=$(grep "mysql-connector" temp_components.txt)
if [ -n "$mysql_version" ]; then
    echo "$mysql_version"
    echo "⚠️  请检查 MySQL Connector 版本安全公告"
else
    echo "✅ 未使用 MySQL Connector"
fi

# 过时组件检查
echo ""
echo "=== 可能存在漏洞的过时组件 ==="

# 检查一些常见的过时组件
echo "🔍 检查过时的安全相关组件:"
grep -E "(commons-collections:commons-collections:[1-3]\.|commons-beanutils:commons-beanutils:1\.[0-8]\.|struts|xstream)" temp_components.txt || echo "✅ 未发现常见过时安全组件"

echo ""
echo "=== 建议 ==="
echo "1. 定期使用 OWASP Dependency Check 扫描"
echo "2. 关注组件安全公告和 CVE 数据库"
echo "3. 及时更新组件版本"
echo "4. 考虑使用 Snyk 或 GitHub Security Advisories"

# 清理临时文件
rm -f temp_components.txt
