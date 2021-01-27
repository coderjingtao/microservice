package com.joseph.contentcenter.dao.content;

import com.joseph.contentcenter.domain.entity.content.Share;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ShareMapper extends Mapper<Share> {
    /**
     * 通过文章的title来查询分享文章的列表
     * @param title 文章的标题（可为空）
     * @return 分享文章的列表
     */
    List<Share> selectByParam(@Param("title") String title);
}