package ae.sinxiao.android.selectimage;

import java.io.Serializable;

/**
 * 选择图片配置类
 *
 * @author zhang.zheng
 * @version 2018-06-22
 */
public class SelectorConfig implements Serializable {

    // 可选数量
    private int maxCount = 5;
    // 显示列数
    private int colCount = 3;
    // 是否多选
    private boolean isMultiple;


    public SelectorConfig() {
    }

    public SelectorConfig(boolean isMultiple, int maxCount) {
        this.isMultiple = isMultiple;
        this.maxCount = maxCount;
    }

    public SelectorConfig(boolean isMultiple, int maxCount, int colCount) {
        this.isMultiple = isMultiple;
        this.maxCount = maxCount;
        this.colCount = colCount;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multi) {
        this.isMultiple = multi;
    }
}
