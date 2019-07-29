package com.william.contants;

/**
 * @Auther: william
 * @Date: 2019/7/29 18:22
 * @Description:
 */
public enum Version {
    EXCEL2007(2007),EXCEL2003(2003);

    private int version;
    private Version(int version){
        this.version = version;
    }

    public int getVersion(int version) {
        return this.version;
    }
}
