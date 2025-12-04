package com.baiyi.cratos.util;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.MarkdownUtils;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/14 15:09
 * &#064;Version 1.0
 */
public class MarkdownUtilsTest extends BaseUnit {


    private static final String MD_TEST = """
            #### 服务
            ```
            /opt/bin/appctl start
            ```
            
            #### 升级
            ```
            /opt/bin/appctl.sh stop
            cd /opt/cratos/
            rm -f cratos-manage-prod.jar
            wget https://opscloud4-web.oss-eu-west-1.aliyuncs.com/package/cratos-manage-prod.jar
            /opt/bin/appctl.sh start
            tail -n 200 -f /opt/outlog/out.log\s
            ```
            
            #### Command Mode Asset Management
            
            <img src="https://opscloud4-res.oss-cn-hangzhou.aliyuncs.com/github/cratos/def/007.jpg" width="830"></img>
            
            #### Services & Ports
            
            | service    | protocol | port | startup parameter     |
            |------------|----------|------|-----------------------|
            | web-api    | http     | 8080 | --server.port=8080    |
            | ssh-server | ssh      | 2222 | --ssh.shell.port=2222 |
            
            """;

    @Test
    void test() {
       System.out.println(MarkdownUtils.toPlainText(MD_TEST));
    }


}
