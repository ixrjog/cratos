#!/bin/bash

# 生成干净的组件版本列表（去除所有控制字符）
echo "正在生成干净的组件版本列表..."

# 方法1: 使用 Maven 命令直接输出
~/Documents/workspace/apache-maven-3.9.7/bin/mvn dependency:list -pl cratos-manage -q 2>/dev/null | \
grep -E "^\[INFO\]    [a-zA-Z]" | \
sed 's/\[INFO\]    //' | \
sed 's/\[[0-9;]*m//g' | \
sed 's/ -- module.*$//' | \
sed 's/:jar:/:/' | \
sed 's/:compile$//' | \
sed 's/:runtime$//' | \
sed 's/:test$//' | \
sed 's/ (optional)$//' | \
cut -d':' -f1,2,3 | \
sort | uniq > components-clean.txt

echo "生成完成: components-clean.txt"
echo "总组件数: $(wc -l < components-clean.txt)"
