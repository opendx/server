import macaca.client.MacacaClient;
import org.testng.annotations.Test;
import com.fgnb.actions.utils.MacacaUtil;
import org.testng.annotations.BeforeSuite;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;

public class ${testClassName} {
    <#--projectType: 1.android 2.ios 3.web-->
    <#if projectType == 1>
    private MacacaClient driver;
    <#elseif projectType = 3>
    private WebDriver driver;
    </#if>

    <#-- 全局变量 变量名格式为g_xxx -->
    <#if globalVars?? && (globalVars?size>0)>
        <#list globalVars as globalVar>
            <#lt>    public static final String g_${globalVar.name} = "${globalVar.value}";
        </#list>
    </#if>

    <#if isBeforeSuite>
        <#lt>    @BeforeSuite
    <#else>
        <#--非debug-->
        <#if testTaskId?? && testTaskReportId?? && deviceId?? && testCaseId?? && testCaseName??>
            <#lt>    @Test(description = "${testTaskId?c}@&@${testTaskReportId?c}@&@${deviceId}@&@${testCaseId?c}@&@${testCaseName}")
        <#--debug-->
        <#else>
            <#lt>    @Test
        </#if>
    </#if>
    public void test() throws Exception {
        <#-- ${port?c} 去除数字逗号分隔 -->
        <#if projectType == 1>
        driver = MacacaUtil.createMacacaClient("${deviceId}",${port?c});
        <#elseif projectType = 3>
        driver = new RemoteWebDriver(new URL("http://localhost:${port?c}"),DesiredCapabilities.chrome());
        </#if>
        ${testMethod}
    }

    <#if methods?? && (methods?size>0)>
        <#list methods as method>
            <#--方法注释-->
            <#if method.methodDescription?? && method.methodDescription!=''>
                <#lt>    //${method.methodDescription}
            </#if>
            <#lt>    public <#rt>
            <#if method.hasReturnValue>
                <#lt>String <#rt>
            <#else>
                <#lt>void <#rt>
            </#if>
            <#lt>${method.methodName}<#rt>
            <#lt>(<#rt>
                <#-- 方法参数，参数格式p_xxx -->
                <#if method.methodParams?? && (method.methodParams?size>0)>
                    <#list method.methodParams as methodParam>
                        <#lt>String p_${methodParam}<#rt>
                        <#if methodParam_has_next>
                            <#lt>, <#rt>
                        </#if>
                    </#list>
                </#if>
            <#lt>) throws Exception {
            <#--方法体-->
            <#-- 基础action -->
            <#if method.className?? && method.className!=''>
                <#lt>        <#if method.hasReturnValue>return </#if>new ${method.className}(<#if method.needDriver>driver</#if>).excute(<#rt>
                <#if method.methodParams?? && (method.methodParams?size>0)>
                    <#list method.methodParams as methodParam>
                        <#lt>p_${methodParam}<#rt>
                        <#if methodParam_has_next>
                            <#lt>, <#rt>
                        </#if>
                    </#list>
                </#if><#lt>);
            <#-- 非基础action -->
            <#else>
                <#--方法里的局部变量-->
                <#if method.vars?? && (method.vars?size>0)>
                     <#list method.vars as var>
                         <#list var?keys as key>
                             <#-- 局部变量，格式v_xxx -->
                             <#lt>        String v_${key} = <#if var[key]?? && var[key]!=''>"${var[key]}"<#else>null</#if>;
                         </#list>
                     </#list>
                </#if>
                <#--步骤调用-->
                <#if method.methodSteps?? && (method.methodSteps?size>0)>
                    <#list method.methodSteps as methodStep>
                        <#--步骤注释-->
                        <#lt>        //${methodStep.stepNumber}.<#if methodStep.methodStepName?? && methodStep.methodStepName!=''>${methodStep.methodStepName}</#if>
                        <#-- 方法内的步骤 使用局部变量v_xxx赋值-->
                        <#lt>        <#if methodStep.evaluation?? && methodStep.evaluation!=''>v_${methodStep.evaluation} = </#if>${methodStep.methodName}(<#rt>
                        <#if methodStep.methodParamValues?? && (methodStep.methodParamValues?size>0)>
                            <#list methodStep.methodParamValues as methodParamValue>
                                <#if methodParamValue?? && methodParamValue!=''>
                                    <#-- 全局变量 -->
                                    <#if methodParamValue?starts_with('${') && methodParamValue?ends_with('}')>
                                        <#lt>g_${methodParamValue?substring(2,(methodParamValue)?length-1)}<#rt>
                                    <#-- 方法参数 -->
                                    <#elseif methodParamValue?starts_with('#{') && methodParamValue?ends_with('}')>
                                        <#lt>p_${methodParamValue?substring(2,(methodParamValue)?length-1)}<#rt>
                                    <#-- 局部变量 -->
                                    <#elseif methodParamValue?starts_with('@{') && methodParamValue?ends_with('}')>
                                        <#lt>v_${methodParamValue?substring(2,(methodParamValue)?length-1)}<#rt>
                                    <#-- 普通字符串 -->
                                    <#else>
                                        <#lt>"${methodParamValue}"<#rt>
                                    </#if>
                                <#else>
                                <#lt>null<#rt>
                                </#if>
                                <#if methodParamValue_has_next>
                                    <#lt>, <#rt>
                                </#if>
                            </#list>
                        </#if><#lt>);
                    </#list>
                </#if>
                <#-- 方法返回值 使用局部变量v_xxx返回-->
                <#if method.returnValue?? && method.returnValue!=''>
                    <#lt>        return v_${method.returnValue};
                </#if>
            </#if>
    }
        </#list>
    </#if>
}