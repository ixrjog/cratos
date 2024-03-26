package com.baiyi.cratos.mytest;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.google.common.base.Splitter;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/2/13 21:47
 * @Version 1.0
 */
public class MyTest extends BaseUnit {

    @Test
    void test() {

        EdsAsset edsAsset = EdsAsset.builder()
                .kind("test")
                .assetId("test")
                .valid(true)
                .assetKey("test")
                .build();

        EdsAssetVO.Asset asset = BeanCopierUtil.copyProperties(edsAsset, EdsAssetVO.Asset.class);

        System.out.println(asset);
    }

    @Test
    void test2() {
        System.out.println(matchesDC("ddd-asdgdsg-dc1"));
        System.out.println(matchesDC("ddd-asdgdsgdc1"));
        System.out.println(matchesDC("ddd-asdgdsg-dc"));
        System.out.println(matchesDC("ddd-asdgdsg-dc22222"));
    }

    private boolean matchesDC(String name) {
        return name.matches(".*-dc\\d+");
    }


    private final static String X = """
            """;

    @Data
    @Builder
    public static class Conf {

        private String location;
        private String wl;
        private String pass;
    }

    @Test
    void test3() {
        Iterable<String> xxx = Splitter.on("\n")
                .split(X);

        boolean flag = false;
        Conf conf = Conf.builder()
                .build();
        for (String s : xxx) {
            if (s.startsWith("location")) {
                flag = true;
                conf.setLocation(s.replace("location ", ""));

            } else if (s.startsWith("proxy_pass")) {
                flag = true;
                conf.setPass(s.replace("proxy_pass ", ""));

            } else if (s.startsWith("wl")) {
                flag = true;
                conf.setWl(s.replace("wl ", ""));
            } else {
                flag = false;
                System.out.println(conf.getLocation() + "," + conf.pass + "," + conf.getWl());
            }
            if (!flag) {
                conf = Conf.builder()
                        .build();
            }

        }


    }

}
