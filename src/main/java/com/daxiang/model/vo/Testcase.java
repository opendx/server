package com.daxiang.model.vo;

import com.daxiang.mbg.po.Action;
import lombok.Data;

import java.util.Date;

/**
 * Created by jiangyitao.
 */
@Data
public class Testcase extends Action {

    public static final Integer FAIL_STATUS = 0;
    public static final Integer PASS_STATUS = 1;
    public static final Integer SKIP_STATUS = 2;

    private Date startTime;
    private Date endTime;
    /**
     * 失败截图
     */
    private String failImgUrl;
    /**
     * 失败信息
     */
    private String failInfo;
    /**
     * 运行视频
     */
    private String videoUrl;
    private Integer status;
}
