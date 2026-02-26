package com.meteor.meteorenderbox.lib.util;

import java.util.*;

/**
 * 分页工具类
<<<<<<< HEAD
 * 用于处理列表的分页操作
=======
 * 用于处理末影箱列表的分页
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
 */
public class PageUtil
{
    /**
<<<<<<< HEAD
     * 获取分页列表
     * @param list 原始列表
     * @param page 页码
     * @param size 每页大小
     * @param <T> 列表元素类型
=======
     * 获取分页后的列表
     * @param list 原始列表
     * @param page 页码
     * @param size 每页大小
     * @param <T> 泛型类型
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
     * @return 分页后的列表
     */
    public static <T> List<T> getPageList(final List<T> list, final int page, final int size) {
        final List<T> result = new ArrayList<T>();
<<<<<<< HEAD
        if (list == null || list.isEmpty()) {
            return result;
        }
        final int total = list.size();
        final int fromIndex = (page - 1) * size;
        if (fromIndex >= total) {
            return result;
        }
        final int toIndex = Math.min(fromIndex + size, total);
        return list.subList(fromIndex, toIndex);
=======
        final int start = (page - 1) * size;
        final int end = Math.min(start + size, list.size());
        for (int i = start; i < end; ++i) {
            result.add(list.get(i));
        }
        return result;
    }
    
    /**
     * 获取总页数
     * @param list 原始列表
     * @param size 每页大小
     * @param <T> 泛型类型
     * @return 总页数
     */
    public static <T> int getTotalPage(final List<T> list, final int size) {
        return (int)Math.ceil((double)list.size() / (double)size);
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    }
}