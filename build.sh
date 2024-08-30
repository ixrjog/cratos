echo $JAVA_HOME
java -version

# apache-maven-3.9.7
~/Documents/workspace/apache-maven-3.9.7/bin/mvn -Dmaven.test.skip=true clean package -e -T 2C -P prod -U -am

md5 ./cratos-manage/target/cratos-manage-prod.jar
# Upload to OSS
ossutil -c ~/.ossutilconfig cp -f -u ./cratos-manage/target/cratos-manage-prod.jar oss://opscloud4-web-hz/package/
