package com.lirongyuns.happyrupees.bean;

/**
 * 内容实体
 * @param <T>
 */
public class ContentBean<T> extends HttpBean
{
    private T srxdContent;

    public T getObj() {
        return srxdContent;
    }
}
