package ae.sinxiao.android.selectimage.vo;

import java.util.List;

/**
 * 相册实体
 *
 * @author zhang.zheng
 * @version 2018-06-25
 */
public class PhotoVo {

    // 相册名称
    public String name;
    // 相册路径
    public String path;
    // 缩略图片
    public ImageVo cover;
    // 相册图集
    public List<ImageVo> images;

    @Override
    public boolean equals(Object o) {
        try {
            PhotoVo other = (PhotoVo) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
