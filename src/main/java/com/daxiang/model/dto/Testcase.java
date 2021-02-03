package com.daxiang.model.dto;

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

    public static final int FAIL_STATUS = 0;
    public static final int PASS_STATUS = 1;
    public static final int SKIP_STATUS = 2;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date endTime;
    /**
     * 失败截图
     */
    private String failImgPath;
    @JSONField(serialize = false) // 不会序列化的表中
    private String failImgUrl;
    /**
     * 失败信息
     */
    private String failInfo;
    /**
     * 运行视频
     */
    private String videoPath;
    @JSONField(serialize = false) // 不会序列化的表中
    private String videoUrl;

    private String logPath;
    @JSONField(serialize = false) // 不会序列化的表中
    private String logUrl;

    private Integer status;

    public String getFailImgUrl() {
        if (!StringUtils.isEmpty(failImgPath))
            return HttpServletUtil.getStaticResourceUrl(failImgPath);
        return null;
    }

    public String getVideoUrl() {
        if (!StringUtils.isEmpty(videoPath))
            return HttpServletUtil.getStaticResourceUrl(videoPath);
        return null;
    }

    public String getLogUrl() {
        if (!StringUtils.isEmpty(logPath))
            return HttpServletUtil.getStaticResourceUrl(logPath);
        return null;
    }
}
