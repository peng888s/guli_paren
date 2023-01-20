package com.pengguli.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pengguli.eduservice.listener.SubjectExcelListener;
import com.pengguli.eduservice.pojo.EduSubject;
import com.pengguli.eduservice.mapper.EduSubjectMapper;
import com.pengguli.eduservice.pojo.excel.SubjectData;
import com.pengguli.eduservice.pojo.subject.OneSubject;
import com.pengguli.eduservice.pojo.subject.OneSubject;
import com.pengguli.eduservice.pojo.subject.TwoSubject;
import com.pengguli.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author peng
 * @since 2022-12-29
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    // 添加课程分类，从Excel中读取添加至数据库中数据
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {
        try{
            EasyExcel.read(file.getInputStream(), SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 全部课程分类（树形结构）
    @Override
    public List<OneSubject> getAllSubject() {
        // 最终返回
        List<OneSubject> finallySubject = new ArrayList<>();

        // 1、全部一级分类
        QueryWrapper<EduSubject> oneQueryWrapper = new QueryWrapper<>();
        oneQueryWrapper.eq("parent_id","0");
        List<EduSubject> oneEduSubjects = baseMapper.selectList(oneQueryWrapper);

        // 2、全部二级分类
        QueryWrapper<EduSubject> twoQueryWrapper = new QueryWrapper<>();
        twoQueryWrapper.ne("parent_id","0");
        List<EduSubject> twoEduSubject = baseMapper.selectList(twoQueryWrapper);

        // 3、封装一级分类
        // 将一级分类List<EduSubject>变成List<oneSubject>
        oneEduSubjects.forEach(eduSubject -> {
            OneSubject one = new OneSubject();
            // 将EduSubject数据复制到oneSubject
            BeanUtils.copyProperties(eduSubject,one);
            finallySubject.add(one);
            // 4、封装二级分类
            // 存放在OneSubject中的TwoSubject
            List<TwoSubject> twoSubjectList = new ArrayList<>();
            twoEduSubject.forEach(twoEduSubjectOne ->{
                if (eduSubject.getId().equals(twoEduSubjectOne.getParentId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(twoEduSubjectOne,twoSubject);
                    twoSubjectList.add(twoSubject);
                }
            });
            one.setChildren(twoSubjectList);
        });
        return finallySubject;
    }
}
