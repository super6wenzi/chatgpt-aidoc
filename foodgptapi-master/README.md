## 插件说明
* calendar、datascreen、ureport、wps、echarts、flowable、form,tools、mail、notify已移除依赖，
* 需要使用自行查找git历史记录的请自行引入
* 另外需要pom依赖对应部分的代码
* 1、weegoo-web-> pom.xml
* 2、weegoo-plugin-> pom.xml
* 3、原始数据库/sql/weegoo-platform-937.sql。自行找表
* 4、精简后的数据库/sql/weegoo-platform-base.sql

# 代码说明
* 前端接口默认放在front包下，swagger默认扫描front/controller,需要修改自行调整
* 

## 数据库
> sql目录下 -> weegoo-platform_base.sql

## 代码规范
* 插件 - Alibaba Java Coding Guidelines
* 工具类 - Hutool - > https://www.hutool.cn/
* VO\DTO 字段添加注解
  @ApiModelProperty(value = "字段说明（String）",required = 是否必填（Boolean）)

## 部署
* linux 下脚本放在/script下
* 单域名部署，nginx 配置参考。各端独立域名部署自行处理
```
    location ^~ /railwayTransportation/ {
      # 替换成自己jar运行的端口跟项目名
      proxy_pass              http://127.0.0.1:端口/项目名/;
      proxy_set_header        Host 127.0.0.1;
      proxy_set_header        X-Real-IP $remote_addr;
      proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
     }
    
    location ^~ /userfiles/ {
      # 替换成自己附件上传的路径
      alias /usr/tomcat/test/userfiles/;
    }
    
    location /admin/ {
        # 替换成自己管理端存放的路径
        alias   /www/wwwroot/test/admin/;
    }
```