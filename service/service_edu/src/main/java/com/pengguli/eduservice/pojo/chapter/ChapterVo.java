package com.pengguli.eduservice.pojo.chapter;

import lombok.Data;

import java.util.List;

/**
 * 章节-->章节里包含很多小节List<VideVo>
 */
@Data
public class ChapterVo {

    private String id;

    private String title;

    private List<VideVo> videVoList;
}
