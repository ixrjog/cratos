package com.baiyi.cratos.eds.aws.util;

import com.amazonaws.services.ec2.model.Tag;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2022/1/24 6:52 PM
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class AmazonEc2Util {

    public static String getName(List<Tag> tags) {
        return tags.stream().filter(tag -> tag.getKey().equals("Name")).findFirst().map(Tag::getValue).orElse("");
    }

}
