package com.pengguli.eduservice.pojo.subject;

import lombok.Data;

import java.util.List;

/**
 * 一级分类
 */
@Data
public class OneSubject {
    private String id;
    private String title;

    private List<TwoSubject> children;
}
