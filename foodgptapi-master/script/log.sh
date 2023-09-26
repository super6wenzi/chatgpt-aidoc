#!/bin/bash
# 日志拆分脚本、使用前需替换
# 1、日志根目录
# 2、项目名
# 3、服务器添加定时任务

# 项目名
APP_BASE_NAME="替换这个地方为项目名"

#接口项目站点路径（目录按照各自配置），如：/www/wwwroot/test.weegoo.cn/api
APP_PATH="替换这个地方为项目路径"

# 拷贝日志文件到昨天的log中
cp $APP_PATH/$APP_BASE_NAME.log $APP_PATH/nohup`date -d yesterday +%Y%m%d`-`date '+%s'`.log

# 清空日志
cat /dev/null > $APP_PATH/$APP_BASE_NAME.log

# 删除90天以前的日志
find $APP_PATH -mtime +90 -name 'nohup*.log' -exec rm -rf {} \;