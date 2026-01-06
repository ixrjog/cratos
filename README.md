![SVG Image](https://raw.githubusercontent.com/ixrjog/cratos/main/logo/cratos-logo.svg)

Start in sync with the world

> A secure general development framework for operations and maintenance

+ OpenJDK 21
+ [SpringBoot 3.5.9 (GA)](https://spring.io/projects/spring-boot#learn)
+ MySql 8+

#### 简介

实现多云管理: AWS、Aliyun & AlibabaCloud、GoogleCloud、Huaweicloud、CloudFlare、Gandi、Godaddy、Kubernetes(EKS、ACK)
聚合多云资产统一管理虚拟机、网络规划、域名、证书、流量层等

#### Multi external data source management

<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/github/cratos/def/001.jpg" width="830"></img>

<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/github/cratos/def/002.jpg" width="830"></img>

#### Data source management, configuration management

<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/github/cratos/def/003.jpg" width="830"></img>

#### Data source asset management

<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/github/cratos/def/004.jpg" width="830"></img>

#### Traffic layer management (ALB/ELB & Ingress Rules)

<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/github/cratos/def/005.jpg" width="830"></img>

#### SSH terminal (Command mode)

<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/github/cratos/def/006.png" width="830"></img>

#### Command Mode Asset Management

<img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/github/cratos/def/007.jpg" width="830"></img>

#### Services & Ports

| service    | protocol | port | startup parameter     |
|------------|----------|------|-----------------------|
| web-api    | http     | 8080 | --server.port=8080    |
| ssh-server | ssh      | 2222 | --ssh.shell.port=2222 |

#### Build

```bash
# OpenJDK21 & apache-maven-3.9.7
$ mvn -Dmaven.test.skip=true clean package -e -P prod -U -am
# jar path: cratos-manage/target/cratos-manage-prod.jar
```

#### Run

```bash
# {YOUR_SECRET_KEY} Program startup must specify a high-strength secret key for encrypting sensitive data
$ java -Xms2048m -Xmx2048m -Xmn1024m -Xss256k \
 -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=256M -XX:+DisableExplicitGC \
 -Djasypt.encryptor.password={YOUR_SECRET_KEY} \
 -jar ./cratos-manage-prod.jar
```

#### Robot calls API

```bash
$ curl -X 'GET' \
'http://127.0.0.1:8081/api/user/username/get?username=baiyi' \
-H 'content-type: application/json' -H "Authorization: Robot {ROBOT_TOKEN}"
```

