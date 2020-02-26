package com.jxlg.service;

import com.jxlg.po.Blog;
import com.jxlg.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    //根据id获取Blog
    Blog getBlog(Long id);

    //将博客获取并转换
    Blog getAndConvert(Long id);

//    根据博客id获取该博客的作者id
    int getUserId(Long id);

    //查询Blog
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    //只传递不查询
    Page<Blog> listBlog(Pageable pageable);

    //根据tagId查询
    Page<Blog> listBlog(Long tagId,Pageable pageable);

    //搜索查询
    Page<Blog> listBlog(String query, Pageable pageable);

    //推荐博客列表
    List<Blog> listRecommendBlogTop(Integer size);

    //将blog归档到Map里面
    Map<String,List<Blog>> archiveBlog();

    //计算Blog表中的条数
    Long countBlog();

    //新增Blog
    Blog saveBlog(Blog blog);

    //修改Blog
    Blog updateBlog(Long id, Blog blog);

    //删除Blog
    void deleteBlog(Long id);
}
