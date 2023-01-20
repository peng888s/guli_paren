package com.pengguli.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pengguli.eduservice.pojo.EduSubject;
import com.pengguli.eduservice.pojo.excel.SubjectData;
import com.pengguli.eduservice.service.EduSubjectService;

/**
 * 添加课程分类，读取excel的监听器
 */
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    private EduSubjectService eduSubjectService;

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    public SubjectExcelListener() {}

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        // 判断数据是否为空
        if (subjectData == null){
            throw new RuntimeException("文件数据为空");
        }

        // 一行一行读取，每次读取两值，第一个值：一级分类，第二个值：二级分类
        // 一行记录，两条sql install语句，判断添加数据是否重复
        // 判断一级分类是否重复
        EduSubject oneEduSubject = this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());
        // 为空，一级分类名称没有重复，将excel中每行数据的第一列，依次添加至数据路
        if (oneEduSubject == null){
            oneEduSubject = new EduSubject();
            oneEduSubject.setParentId("0");
            oneEduSubject.setTitle(subjectData.getOneSubjectName());
            eduSubjectService.save(oneEduSubject);
        }

        // 判断二级分类是否重复
        EduSubject twoEduSubject = this.existTwoSubject(eduSubjectService, subjectData.getTwoSubjectName(), oneEduSubject.getId());
        if (twoEduSubject == null){
            twoEduSubject = new EduSubject();
            twoEduSubject.setTitle(subjectData.getTwoSubjectName());
            twoEduSubject.setParentId(oneEduSubject.getId());
            eduSubjectService.save(twoEduSubject);
        }
    }

    /**
     * 一级分类是否重复
     * @param eduSubjectService 查询数据库判断是否重复
     * @param name 一级分类名称
     * @return
     */
    public EduSubject existOneSubject(EduSubjectService eduSubjectService,String name){
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",name);
        queryWrapper.eq("parent_id","0");
        EduSubject one = eduSubjectService.getOne(queryWrapper);
        return one;
    }

    /**
     * 二级分类是否重复
     * @param eduSubjectService 查询数据库判断是否重复
     * @param name 一级分类名称
     * @param pid   一级分类id
     * @return
     */
    public EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String pid){
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",name);
        queryWrapper.eq("parent_id",pid);
        EduSubject two = eduSubjectService.getOne(queryWrapper);
        return two;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
