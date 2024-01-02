package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.SysTag;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 13:28
 * @Version 1.0
 */
public interface SysTagService {

    void add(SysTag sysTag);

    void updateByPrimaryKey(SysTag sysTag);

    void updateByPrimaryKeySelective(SysTag sysTag);

    void deleteById(int id);

    List<SysTag> selectAll();

}