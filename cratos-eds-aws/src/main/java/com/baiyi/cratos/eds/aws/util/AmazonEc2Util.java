package com.baiyi.cratos.eds.aws.util;

import com.amazonaws.services.ec2.model.Tag;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/24 6:52 PM
 * @Version 1.0
 */
public class AmazonEc2Util {

    public static String getInstanceName(List<Tag> tags) {
        return tags.stream().filter(tag -> tag.getKey().equals("Name")).findFirst().map(Tag::getValue).orElse("");
    }

}
