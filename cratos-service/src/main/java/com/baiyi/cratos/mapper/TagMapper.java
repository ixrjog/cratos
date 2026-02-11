package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Tag;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface TagMapper extends Mapper<Tag> {
}