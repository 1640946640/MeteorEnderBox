package com.meteor.meteorenderbox.lib.util;

import java.util.*;

/**
 * 分页工具类
 * 用于处理列表的分页操作
 */
public class PageUtil
{
    /**
     * 获取分页列表
     * @param list 原始列表
     * @param page 页码
     * @param size 每页大小
     * @param <T> 列表元素类型
     * @return 分页后的列表
     */
    public static <T> List<T> getPageList(final List<T> list, final int page, final int size) {
        final List<T> result = new ArrayList<T>();
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
    }
}