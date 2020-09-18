package ae.sinxiao.android.selectimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

import ae.sinxiao.android.qrd.R;
import ae.sinxiao.android.selectimage.vo.PhotoVo;

/**
 * Created by silence on 16/04/2020
 * Describe:
 */
public class PhotoAdapter extends BaseAdapter {

    // 选中相册索引
    private int checkedIndex = 0;

    private RequestOptions options;

    public void setCheckedIndex(int checkedIndex) {
        this.checkedIndex = checkedIndex;
    }

    public int getCheckedIndex() {
        return checkedIndex;
    }

    private Context context;

    private List<PhotoVo> dataList;

    public PhotoAdapter(Context context, List<PhotoVo> dataList) {
        this.context = context;
        this.dataList = dataList;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.img_default_error)
                .error(R.drawable.img_default_error)
                .priority(Priority.HIGH);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoViewHolder holder;
        if (convertView == null) {
            holder = new PhotoViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fresco_photo_list, parent, false);
            holder.imageView = convertView.findViewById(R.id.item_photo_cover);
            holder.name = convertView.findViewById(R.id.item_photo_name);
            holder.size = convertView.findViewById(R.id.item_photo_size);
            holder.indicator = convertView.findViewById(R.id.item_photo_indicator);
            convertView.setTag(holder);
        } else {
            holder = (PhotoViewHolder) convertView.getTag();
        }

        PhotoVo itemVo = dataList.get(position);
        if (checkedIndex == position) {
            holder.indicator.setVisibility(View.VISIBLE);
        } else {
            holder.indicator.setVisibility(View.GONE);
        }
        // 相册名称
        holder.name.setText(itemVo.name);
        // 图片个数
        holder.size.setText(String.valueOf(itemVo.images.size()));
        // 相册缩略图
        Glide.with(context).load(new File(itemVo.cover.getPath())).apply(options).into(holder.imageView);

        return convertView;
    }


    static class PhotoViewHolder {
        ImageView imageView;
        TextView name;
        TextView size;
        ImageView indicator;

    }

    /**
     * 刷新相册
     */
    public void refreshPhotos(List<PhotoVo> photos) {
        if (photos != null && photos.size() > 0) {
            dataList = photos;
        } else {
            dataList.clear();
        }
        notifyDataSetChanged();
    }
}
