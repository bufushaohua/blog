package com.jxlg.service;

import com.jxlg.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    // 保存一个type
    Type saveType(Type type);
    // 根据一个Long类型id查询type
    Type getType(Long id);

    //通过名称查询Type
    Type getTypeByName(String name);

    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    List<Type> listTypeTop(Integer size);

    Type updateType(Long id, Type type);

    void deleteType(Long id);

}
