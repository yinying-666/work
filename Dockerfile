# 基于JDK8 + Maven打包SpringBoot项目
FROM maven:3.8.4-jdk-8 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# 跳过测试打包Jar包，使用阿里云Maven镜像加速下载依赖
RUN mvn clean package -DskipTests -s <(echo '<?xml version="1.0"?>
<settings>
<mirrors>
<mirror>
<id>aliyun</id>
<mirrorOf>central</mirrorOf>
<url>https://maven.aliyun.com/repository/public</url>
</mirror>
</mirrors>
</settings>')

# 运行镜像：轻量JDK8运行环境
FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
# 适配Railway动态端口变量，限制内存防止免费额度OOM
ENTRYPOINT ["java","-Xmx256m","-jar","app.jar","-Dserver.port=$PORT"]