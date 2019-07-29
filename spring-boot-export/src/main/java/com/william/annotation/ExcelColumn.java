package com.william.annotation;

import java.lang.annotation.*;

/**
 * @Auther: williamdream
 * @Date: 2019/7/29 18:26
 * @Description:
 */
@Target(value={ElementType.METHOD,ElementType.FIELD})
@Retention(value= RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    /**
     * 功能:导出,导入的单元格标题 ,必填<br/>
     * @return
     */
    String value() ;
    /**
     * 功能:导入,导出时的索引配置,从 0 开始,必须提供 <br/>
     * @return
     */
    int index();

    /**
     *
     * 功能: 列的宽度配置,如果这里有配置,则使用这里的配置,否则使用自动宽度(如果配置为 true 的话,为 false 不设置)<br/>
     * 注:使用 excel 的宽度设置,一个中文字对应 2 * 256 长度单位
     * @return
     */
    int width() default -1;

    /**
     * 功能:使用字符宽度,一个中文字填写 1  <br/>
     * @return
     */
    int charWidth() default -1;

    /**
     * 功能:使用像素宽度, 1 像素填写 1 <br/>
     * @return
     */
    int pxWidth() default -1;

    /**
     *
     * 功能:由于自动宽度对中文支持不太好,所以这里加个中文的自动宽度支持,这个只在自动宽度设置为 true 时生效<br/>
     */
    boolean chineseWidth() default false;

    /**
     * 功能: 当前列是否隐藏 默认 false<br/>
     * @return
     */
    boolean hidden() default false;

    /**
     * 功能: 时间格式化,默认 yyyy-MM-dd <br/>
     * @return
     */
    String pattern() default "yyyy-MM-dd";
}
