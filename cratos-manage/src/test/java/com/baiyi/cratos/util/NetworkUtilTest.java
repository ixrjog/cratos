package com.baiyi.cratos.util;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/3 17:04
 * &#064;Version 1.0
 */
public class NetworkUtilTest extends BaseUnit {

    @Test
    void test() {
        String ip = "172.31.0.4";//ip
        String mask = "24";//位数，如果只知道子网掩码不知道位数的话在参考getMaskMap()方法
        String subMark = IpUtils.getMaskMap(mask);
        System.out.println("子网掩码为：" + subMark);
        //获得起始IP和终止IP的方法（包含网络地址和广播地址）
        String startIp = IpUtils.getBeginIpStr(ip, mask);
        String endIp = IpUtils.getEndIpStr(ip, mask);
        System.out.println("起始IP：" + startIp + "终止IP：" + endIp);
        //根据位数查询IP数量
        int ipCount = IpUtils.getIpCount("24");
        System.out.println("ip个数：" + ipCount);
        //获得起始IP和终止IP的方法（不包含网络地址和广播地址）
        String subStart = startIp.split("\\.")[0] + "." + startIp.split("\\.")[1] + "." + startIp.split("\\.")[2] + ".";
        String subEnd = endIp.split("\\.")[0] + "." + endIp.split("\\.")[1] + "." + endIp.split("\\.")[2] + ".";
        startIp = subStart + (Integer.parseInt(startIp.split("\\.")[3]) + 1);
        endIp = subEnd + (Integer.parseInt(endIp.split("\\.")[3]) - 1);
        System.out.println("起始IP：" + startIp + "终止IP：" + endIp);

        //判断一个IP是否属于某个网段
        boolean flag = IpUtils.isInRange("10.2.0.0", "10.3.0.0/17");
        System.out.println(flag);

        //判断是否是一个IP
        System.out.println(IpUtils.isIP("192.168.1.0"));

        //把ip转换为数字(mysql中inet_aton()的实现)
        System.out.println(IpUtils.ipToDouble("192.168.1.1"));

        //打印IP段所有IP（IP过多会内存溢出）
//      List<String> list = parseIpMaskRange(ip, mask);
//      for (String s : list){
//          System.out.println(s);
//      }
    }

    //         int resourceTotal = NetworkUtil.getIpCount(StringUtils.substringAfter(globalNetwork.getCidrBlock(),"/"));
    @Test
    void test2() {
        String mask = "192.168.0.0/24";
        int resourceTotal = IpUtils.getIpCount(StringUtils.substringAfter(mask, "/"));
        System.out.println(resourceTotal);
    }

}
