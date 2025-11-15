#!/bin/bash

echo "========================================="
echo "  allbs-excel 测试项目启动脚本"
echo "========================================="

# 检查是否安装了 Maven
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到 Maven，请先安装 Maven"
    exit 1
fi

# 编译并运行项目
echo "正在启动项目..."
mvn spring-boot:run
