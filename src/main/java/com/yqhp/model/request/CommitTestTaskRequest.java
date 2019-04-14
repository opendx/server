package com.yqhp.model.request;

import com.yqhp.mbg.po.TestTask;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@Data
public class CommitTestTaskRequest extends TestTask{
    @NotEmpty(message = "至少要选一个设备")
    private List<String> deviceIds;
}
