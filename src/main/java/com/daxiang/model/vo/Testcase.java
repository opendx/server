package com.daxiang.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.daxiang.mbg.po.Action;
import com.daxiang.utils.HttpServletUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by jiangyitao.
 */
@Data
public class Testcase extends Action {

    public static final Integer FAIL_STATUS = 0;
    public static final Integer PASS_STATUS = 1;
    public static final Integer SKIP_STATUS = 2;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date endTime;
    /**
     * 失败截图
     */
    private String failImgName;
    @JSONField(serialize = false) // 不会序列化的表中
    private String failImgUrl;
    /**
     * 失败信息
     */
    private String failInfo;
    /**
     * 运行视频
     */
    private String videoName;
    @JSONField(serialize = false) // 不会序列化的表中
    private String videoUrl;

    private Integer status;

    public String getFailImgUrl() {
        if (!StringUtils.isEmpty(failImgName))
            return HttpServletUtil.getStaticResourcesBaseUrl() + failImgName;
        return null;
    }

    public String getVideoUrl() {
        if (!StringUtils.isEmpty(videoName))
            return HttpServletUtil.getStaticResourcesBaseUrl() + videoName;
        return null;
    }
}
