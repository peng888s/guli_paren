package com.pengguli.eduservice.service;

import com.pengguli.eduservice.pojo.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pengguli.eduservice.pojo.subject.OneSubject;
import com.pengguli.eduservice.pojo.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author peng
 * @since 2022-12-29
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file,EduSubjectService eduSubjectService);

    // 全部课程福分类（树形）
    List<OneSubject> getAllSubject();
}
