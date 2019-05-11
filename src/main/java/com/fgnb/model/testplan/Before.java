package com.fgnb.model.testplan;

import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class Before {

    //BeforeSuite
    public static final Integer BEFORE_SUITE_TYPE = 1;
    //BeforeMethod
    public static final Integer BEFORE_METHOD_TYPE = 2;

    private Integer actionId;
    private Integer type;
}
