package ae.sinxiao.android.selectimage.listener;

import java.util.List;

import ae.sinxiao.android.selectimage.vo.ImageVo;

public interface OnImageSelectedListener {

    // 单选
    void onSingleImageCallback(ImageVo image);

    // 多选
    void onMultipleImageCallback(List<ImageVo> images);
}
