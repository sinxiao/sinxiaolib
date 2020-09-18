package ae.sinxiao.android.selectimage.vo;

import android.graphics.Bitmap;

import ae.payby.android.saladin.utils.ImageUtils;


/**
 * 图片实体
 *
 * @author zhang.zheng
 * @version 2018-06-25
 */
public class ImageVo {

    // 拍照
    public final static int TYPE_CAMERA = 0;
    // 图片
    public final static int TYPE_IMAGE = 1;

    // 图片名称
    private String name;
    // 图片路径
    private String path;
    // 生产时间
    private long time;

    private int type;

    private Bitmap bitmap;


    public ImageVo(String path, String name, long time, int type) {
        this.path = path;
        this.name = name;
        this.time = time;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Bitmap getBitmap() {
        if (bitmap == null) {
            try {
                bitmap = ImageUtils.decodeLocalImage(path, 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public boolean equals(Object o) {
        try {
            ImageVo other = (ImageVo) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }


}
