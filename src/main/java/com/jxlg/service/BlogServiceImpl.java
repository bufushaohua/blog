package com.jxlg.service;

import com.jxlg.NotFoundException;
import com.jxlg.dao.BlogRepository;
import com.jxlg.po.Blog;
import com.jxlg.po.Type;
import com.jxlg.util.MapKeyComparator;
import com.jxlg.util.MarkdownUtils;
import com.jxlg.util.MyBeanUtils;
import com.jxlg.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

//    根据博客id获取该博客的作者id
    @Override
    public int getUserId(Long id) {
        return blogRepository.findUserIdByBlogId(id);
    }

//    这里的Transactional可以覆盖到BlogRepository中的Transactional
    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).get();  //等同于blogRepository.findOne(id);
        if(blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //检测标题是否为空
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));  //模糊查询
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()])); //查询
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

//    将t_blog、t_tag和t_blog_tags三表联系起来
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
//        Map<String, List<Blog>> map = new TreeMap<String, List<Blog>>();
        Map<String,List<Blog>>  dataMap = new TreeMap<String,List<Blog>>();
        Iterator iterator = years.iterator();
        while(iterator.hasNext()){
            String year = (String) iterator.next();
//            map.put(year, blogRepository.findByYear(year));
            dataMap.put(year, blogRepository.findByYear(year));
        }
        dataMap = ((TreeMap) dataMap).descendingMap();
//        Map<String, List<Blog>> resultMap = sortMapByKey(map);    //按Key进行排序
        return dataMap;
    }

    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, List<Blog>> sortMapByKey(Map<String, List<Blog>> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, List<Blog>> sortMap = new TreeMap<String, List<Blog>>(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        //保存的时候判断是新增Blog还是修改Blog
        if(blog.getId() == null){  //是新增
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);  //初始化阅读量为0
        }else{
            blog.setUpdateTime(new Date()); //不需要重新创建时间，直接更新时间就行
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).get();
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));  // 将blog中的属性值copy到b中
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
