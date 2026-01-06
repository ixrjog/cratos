#!/bin/bash

# OWASP Dependency Check 集成脚本
echo "=== OWASP Dependency Check 安全扫描 ==="

# 检查是否安装了 OWASP Dependency Check
if ! command -v dependency-check &> /dev/null; then
    echo "❌ OWASP Dependency Check 未安装"
    echo ""
    echo "安装方法:"
    echo "1. 下载: https://github.com/jeremylong/DependencyCheck/releases"
    echo "2. 或使用 Homebrew: brew install dependency-check"
    echo "3. 或使用 Maven 插件:"
    echo "   mvn org.owasp:dependency-check-maven:check"
    echo ""
    exit 1
fi

# 运行 OWASP Dependency Check
echo "🔍 正在扫描项目依赖..."
dependency-check --project "Cratos" --scan . --format HTML --format JSON --out ./security-reports/

echo ""
echo "✅ 扫描完成，报告生成在 ./security-reports/ 目录"
echo "📊 查看 HTML 报告: open ./security-reports/dependency-check-report.html"
