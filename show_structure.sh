#!/bin/bash

echo "========================================="
echo "  allbs-excel 测试项目结构"
echo "========================================="
echo ""

echo "📦 项目文件统计:"
echo "  Java 文件: $(find src -name "*.java" -type f | wc -l | tr -d ' ')"
echo "  实体类: 5"
echo "  控制器: 3"
echo "  服务类: 2"
echo "  测试接口: 16+"
echo ""

echo "📁 项目目录结构:"
tree -L 3 -I 'target|.idea|.git' || ls -R

echo ""
echo "📄 文档文件:"
ls -lh *.md *.sh 2>/dev/null | awk '{print "  " $9 " (" $5 ")"}'

echo ""
echo "✅ 项目已完成，可以开始测试！"
