package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.domain.param.robot.RobotParam;
import com.baiyi.cratos.mapper.RobotMapper;
import com.baiyi.cratos.service.RobotService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/13 14:09
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class RobotServiceImpl implements RobotService {

    private final RobotMapper robotMapper;

    @Override
    public DataTable<Robot> queryRobotPage(RobotParam.RobotPageQuery pageQuery) {
        Page<Robot> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Robot> data = robotMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public Robot getByUniqueKey(@NonNull Robot record) {
        Example example = new Example(Robot.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("token", record.getToken());
        return robotMapper.selectOneByExample(example);
    }

    @Override
    public int countResourcesAuthorizedByToken(String token, String resource) {
        return robotMapper.countResourcesAuthorizedByToken(token, resource);
    }

    @Override
    public List<Robot> queryByUsername(String username) {
        Example example = new Example(Robot.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return robotMapper.selectByExample(example);
    }

}
