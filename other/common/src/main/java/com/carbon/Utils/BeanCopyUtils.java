package com.carbon.Utils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils() {
    }
    /** 1、单个对象*/
    public static <V> V copyBean(Object source, Class<V> clazz) {
        /** 创建目标对象 实现属性拷贝*/
        V result = null;
        try {
            result = clazz.newInstance();
            /** 拷贝*/
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /** 2、集合*/
    public static <O, V> List<V> copyBeanList(List<O> list, Class<V> clazz) {
        /** 创建目标对象 实现属性拷贝*/
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
