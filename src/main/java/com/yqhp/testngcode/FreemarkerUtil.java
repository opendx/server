package com.yqhp.testngcode;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;

/**
 * Created by jiangyitao.
 */
public class FreemarkerUtil {

    public static String process(String basePackagePath, String ftlFileName, Object dataModel) throws Exception {
        StringWriter stringWriter = new StringWriter();
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
            cfg.setClassForTemplateLoading(FreemarkerUtil.class, basePackagePath);
            cfg.setDefaultEncoding("UTF-8");

            Template template = cfg.getTemplate(ftlFileName);
            template.process(dataModel, stringWriter);
        } finally {
            stringWriter.close();
        }
        return stringWriter.toString();
    }
}
