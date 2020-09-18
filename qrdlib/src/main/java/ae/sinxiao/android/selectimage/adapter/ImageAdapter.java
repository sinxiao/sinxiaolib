package ae.sinxiao.android.selectimage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ae.sinxiao.android.qrd.R;
import ae.sinxiao.android.selectimage.SelectorConfig;
import ae.sinxiao.android.selectimage.listener.OnImageSelectedListener;
import ae.sinxiao.android.selectimage.vo.ImageVo;

/**
 * Created by silence on 16/04/2020
 * Describe:
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private SelectorConfig mSelectorConfig;
    // 默认选择图片
    private List<ImageVo> mDefaultImages = new ArrayList<>();
    // 最终选择图片
    private List<ImageVo> mSelectedImages = new ArrayList<>();

    private List<ImageVo> dataList;

    // 图片大小参数
    private FrameLayout.LayoutParams mImageParams;

    private RequestOptions options;

    private Context mContext;

    private OnImageSelectedListener mOnImageSelectedListener;

    public void setOnImageSelectedListener(OnImageSelectedListener onImageSelectedListener) {
        this.mOnImageSelectedListener = onImageSelectedListener;
    }

    public ImageAdapter(Context context, List<ImageVo> dataList, SelectorConfig selectorConfig) {
        this.mContext = context;
        this.dataList = dataList;
        mSelectorConfig = selectorConfig;
        initImageWidthHeight();

        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.img_default_error)
                .error(R.drawable.img_default_error)
                .priority(Priority.HIGH);
    }

    /**
     * 初始化图片宽高
     */
    private void initImageWidthHeight() {
        mImageParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        mImageParams.width = (int) ((displayMetrics.widthPixels / 3) - displayMetrics.density * 1);
        mImageParams.height = (int) ((displayMetrics.widthPixels / 3) - displayMetrics.density * 1);
//        LogUtils.e(this, "图片显示宽高：" + mImageParams.width + "*" + mImageParams.height);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_fresco_image_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final ImageVo image = dataList.get(position);
        holder.mImageView.setLayoutParams(mImageParams);
        holder.mImageView.setColorFilter(null);


        Glide.with(mContext).load(new File(image.getPath())).apply(options).into(holder.mImageView);
        final ImageViewHolder holdero   = holder;
        if (mSelectorConfig.isMultiple()) {
            // 多选
            holder.mCheckBox.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(R.drawable.img_unselected).into(holder.mCheckBox);
            // 设置默认选中图
            if (mDefaultImages != null && !mDefaultImages.isEmpty()) {
                if (mDefaultImages.contains(image)) {
                    Glide.with(mContext).load(R.drawable.img_selected).into(holder.mCheckBox);
                    holder.mImageView.setColorFilter(Color.parseColor("#77000000"));
                }
            }
            // 复选框点击监听
            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handlerMultiSelect(holdero.mImageView, holdero.mCheckBox, image);
                }
            });
        } else {
            // 单选
            holder.mCheckBox.setVisibility(View.GONE);
        }

        // 图片点击监听
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectorConfig.isMultiple()) {
                    // 多选
                    handlerMultiSelect(holdero.mImageView, holdero.mCheckBox, image);
                } else {
                    // 单选
                    if (mOnImageSelectedListener != null) {
                        mOnImageSelectedListener.onSingleImageCallback(image);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    /**
     * 处理多选图片
     */
    private void handlerMultiSelect(ImageView imageView, ImageView checkBox, ImageVo image) {
        if (mSelectedImages.contains(image)) {
            // 设置图片未选中
            mSelectedImages.remove(image);
            Glide.with(mContext).load(R.drawable.img_unselected).into(checkBox);
            imageView.setColorFilter(null);
        } else {
            if (mSelectedImages.size() >= mSelectorConfig.getMaxCount()) {
//                ToastHelper.getInstance().showCenter(mContext, "您最多能添加" + mSelectorConfig.getMaxCount() + "张图片");
                return;
            }
            // 设置图片选中
            mSelectedImages.add(image);
            Glide.with(mContext).load(R.drawable.img_selected).into(checkBox);
            imageView.setColorFilter(Color.parseColor("#77000000"));
        }
        // 多选回调
        if (mOnImageSelectedListener != null) {
            mOnImageSelectedListener.onMultipleImageCallback(mSelectedImages);
        }
    }

    /**
     * 刷新数据
     */
    public void refreshImages(List<ImageVo> images, List<ImageVo> defaultImages) {
        this.dataList = images;
        this.mDefaultImages = defaultImages;
        notifyDataSetChanged();
    }

    public List<ImageVo> getSelectedImages() {
        return mSelectedImages;
    }

    public List<ImageVo> getDataList() {
        return dataList;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        ImageView mCheckBox;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image_view);
            mCheckBox = itemView.findViewById(R.id.item_image_checkbox);
        }
    }
}
