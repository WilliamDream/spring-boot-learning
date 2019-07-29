package com.william.annotation;

import com.william.contants.Version;

import java.lang.annotation.*;

/**
 * @Auther: williamdream
 * @Date: 2019/7/29 18:20
 * @Description:
 */
@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelExport {
    /**
     * 功能:导出excel版本,默认导出 2007 版本 <br/>
     * @return
     */

    Version version() default Version.EXCEL2007;

    /**
     * 功能:最顶部的标题行高度,需要设置 title 才能使其生效 <br/>
     * @return
     */
    short titleRowHeight() default 40;

    /**
     * 功能: 头标题行高度,以像素为单位<br/>
     * @return
     */
    short headRowHeight() default 30;

    /**
     * 功能:内容行高度,以像素为单位 <br/>
     * @return
     */
    short bodyRowHeight() default 25;

    /**
     * 功能:是否自动宽度,默认为 true <br/>
     * @return
     */
    boolean autoWidth() default true;

    /**
     * 功能:一个sheet 页的最大记录数,默认是不限制的,如果是 2003 版本,限制为 60000 行 <br/>
     * @return
     */
    int sheetMaxRow() default -1;
}
