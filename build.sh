echo $JAVA_HOME
java -version

# springboot 3.5.0 要求maven版本 3.6.3+, JDK 17 ~ 24
# apache-maven-3.9.7
~/Documents/workspace/apache-maven-3.9.7/bin/mvn -Dmaven.test.skip=true clean package -e -T 2C -P prod -U -am

# Upload to OSS
ossutil -c ~/.ossutilconfig cp -f -u ./cratos-manage/target/cratos-manage-prod.jar oss://opscloud4-web-hz/package/
