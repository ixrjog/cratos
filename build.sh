echo $JAVA_HOME
java -version
/Users/liangjian/Documents/workspace/maven-3.3.3/bin/mvn -Dmaven.test.skip=true clean package -e -T 2C -P prod -U -am

# Upload to OSS
ossutil -c ~/.ossutilconfig cp -f -u ./cratos-manage/target/cratos-manage-prod.jar oss://opscloud4-web-hz/package/