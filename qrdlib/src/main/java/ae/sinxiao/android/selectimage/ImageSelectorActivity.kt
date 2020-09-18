package ae.sinxiao.android.selectimage

import ae.sinxiao.android.qrd.R
import ae.sinxiao.android.selectimage.adapter.ImageAdapter
import ae.sinxiao.android.selectimage.adapter.PhotoAdapter
import ae.sinxiao.android.selectimage.listener.OnImageSelectedListener
import ae.sinxiao.android.selectimage.vo.ImageVo
import ae.sinxiao.android.selectimage.vo.PhotoVo
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by silence on 16/04/2020
 * Describe:
 */
class ImageSelectorActivity : BaseActivity() {

    // 查询所有相册
    private val QUERY_ALL = 0

    // 查询单个相册
    private val QUERY_ONE = 1

    // 相册数据
    private val mPhotoList = ArrayList<PhotoVo>()

    // 图片数据
    private val mImageList = ArrayList<ImageVo>()

    // 选择的图片
    private val mSelectedImages = ArrayList<String>()

    private var mPopupPhotoWindow: ListPopupWindow? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var mPhotoAdapter: PhotoAdapter? = null
    private var mImageAdapter: ImageAdapter? = null

    // 选择配置
    private var mSelectorConfig: SelectorConfig? = null

    override val contentLayoutId: Int get() = R.layout.activity_image_selector

    override fun initViewModel() {
    }

    override fun initObserve() {
    }

    private var recycler_view: RecyclerView? = null;
    private var photo_name_btn: Button? = null;
    private var overlay_bg: ImageView? = null;
    private var float_date_bar: TextView? = null;
    private var footer_lay: View? = null;
    override fun initView(savedInstanceState: Bundle?) {

        // 获取配置参数，为空默认单选
        if (mSelectorConfig == null) {
            mSelectorConfig = SelectorConfig()
        }
        if (recycler_view == null) {
            recycler_view = findViewById(R.id.recycler_view)
        }
        if (photo_name_btn == null) {
            photo_name_btn = findViewById(R.id.photo_name_btn);
        }
        if (float_date_bar == null) {
            float_date_bar = findViewById(R.id.float_date_bar);
        }
        if (footer_lay == null) {
            footer_lay = findViewById(R.id.footer_lay);
        }

        mLayoutManager = GridLayoutManager(context, mSelectorConfig!!.colCount)
        recycler_view?.layoutManager = mLayoutManager
        recycler_view?.addItemDecoration(GridItemDivider(context))
//        mPreviewBtn.setEnabled(false)
        mPhotoAdapter = PhotoAdapter(context, mPhotoList)
        mImageAdapter = ImageAdapter(context, mImageList, mSelectorConfig)
        mImageAdapter?.setOnImageSelectedListener(object : OnImageSelectedListener {


            override fun onSingleImageCallback(image: ImageVo) {
//                LogUtils.d(this, "[单选：" + image.path.toString() + "]")
                sendSingleResult(image.path)
            }

            override fun onMultipleImageCallback(images: List<ImageVo>) {
//                LogUtils.d(this, "[多选：" + images.size + "]")
//                if (images.size > 0) {
//                    mPreviewBtn.setText("预览(" + images.size + ")")
//                    mPreviewBtn.setEnabled(true)
//                    mFinishBtn.setText("完成(" + images.size + "/" + mSelectorConfig!!.maxCount + ")")
//                    mFinishBtn.setEnabled(true)
//                    mSelectedImages.clear()
//                    for (img in images) {
//                        mSelectedImages.add(img.path)
//                    }
//                } else {
//                    mPreviewBtn.setText("预览")
//                    mPreviewBtn.setEnabled(false)
//                    mFinishBtn.setText("完成")
//                    mFinishBtn.setEnabled(false)
//                }
            }
        })
        recycler_view?.adapter = mImageAdapter

        // 切换相册
        photo_name_btn?.setOnClickListener {
            if (mPopupPhotoWindow == null) {
                createSelectPhotoWindow()
                mPopupPhotoWindow!!.show()
                overlay_bg?.visibility = View.VISIBLE
            } else {
                if (mPopupPhotoWindow?.isShowing == true) {
                    mPopupPhotoWindow?.dismiss()
                } else {
                    mPopupPhotoWindow?.show()
                    overlay_bg?.visibility = View.VISIBLE
                    var index = mPhotoAdapter!!.checkedIndex
                    index = if (index == 0) index else index - 1
                    val listView = mPopupPhotoWindow?.listView
                    listView?.setSelection(index)
                }
            }
        }

        // 滚动监听
        recycler_view?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // 停止滑动，日期指示条消失
                    float_date_bar?.visibility = View.GONE
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) { // 正在滑动，日期指示条显示
                    float_date_bar?.visibility = View.VISIBLE
                    val position = mLayoutManager!!.findFirstVisibleItemPosition()
//                    LogUtils.e(this, "first visible mPosition: $position")
                    // 设置时间条日期
                    resetDateBar(mImageAdapter!!.dataList, position)
                }
            }

        })

        // 加载所有图片
        LoaderManager.getInstance(this).initLoader(QUERY_ALL, null, mLoaderCallback)
    }

    override fun initData() {
    }

    /**
     * 选择相册弹窗
     */
    private fun createSelectPhotoWindow() {
        mPopupPhotoWindow = ListPopupWindow(context)
        mPopupPhotoWindow?.setAdapter(mPhotoAdapter!!)
        mPopupPhotoWindow?.width = ViewGroup.LayoutParams.MATCH_PARENT
        mPopupPhotoWindow?.height = DensityUtil.getScreenHeight(context) * 5 / 8
        mPopupPhotoWindow?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#dbdbdb")))
        //        mPopupPhotoWindow.setBackgroundDrawable(null);
        mPopupPhotoWindow?.anchorView = footer_lay
        mPopupPhotoWindow?.isModal = true
        mPopupPhotoWindow?.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, _, position, _ ->
            if (position == mPhotoAdapter!!.checkedIndex) {
                mPopupPhotoWindow?.dismiss()
                return@OnItemClickListener
            }
            mPhotoAdapter!!.checkedIndex = position
            Handler().post {
                mPopupPhotoWindow?.dismiss()
                // 滑动到最初始位置
                recycler_view?.smoothScrollToPosition(0)
                if (position == 0) {
                    float_date_bar?.text = getString(R.string.all_images)
                    LoaderManager.getInstance(this).restartLoader(
                            QUERY_ALL,
                            null,
                            mLoaderCallback
                    )
                } else {
                    val photo = adapterView.adapter.getItem(position) as PhotoVo
                    float_date_bar?.text = photo.name
                    mImageAdapter?.refreshImages(photo.images, mImageAdapter?.selectedImages)
                }
            }
        })
        mPopupPhotoWindow?.setOnDismissListener {
            overlay_bg?.visibility = View.GONE
        }
    }

    /**
     * 发送回传单选图片结果
     */
    private fun sendSingleResult(imgPath: String) {
        if (!TextUtils.isEmpty(imgPath)) {
            val intent = Intent()
            intent.putExtra("image_data_single", imgPath)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }

    /**
     * 设置时间条
     *
     * @param dataList 图片列表
     * @param position 索引位置
     */
    private fun resetDateBar(dataList: List<ImageVo>?, position: Int) {
        if (dataList != null && dataList.isNotEmpty()) {
            val image = dataList[position]
            try { // 照片日期
                val imgDate: String = TimeUtils.millis2String(image.time, "yyyy/MM/dd")
                val cal = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.CHINESE)
                val today = sdf.format(Date()) //今天的日期
                cal[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
                val monday = sdf.format(cal.time) //本周一的日期
                cal.add(Calendar.WEEK_OF_YEAR, 1) // 周加1
                cal[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
                val sunday = sdf.format(cal.time) //本周日的日期
                //                    LogUtils.e(TAG, "今天：" + today + ", 本周一：" + monday + ", 本周日：" + sunday);
                var result = imgDate.substring(0, 7)
                if (today.startsWith(result)) {
                    result =
                            if (imgDate >= monday && imgDate <= sunday) {
                                "This Week"
                            } else {
                                "This Month"
                            }
                }
//                LogUtils.e(TAG, "图片日期：$imgDate, 显示结果：$result")
                if (!TextUtils.isEmpty(result)) {
                    float_date_bar?.text = result
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 查询手机图片
     */
    private val mLoaderCallback: LoaderManager.LoaderCallbacks<Cursor> =
            object : LoaderManager.LoaderCallbacks<Cursor> {
                private val IMAGE_PROJECTION =
                        arrayOf( // 图片ID
                                MediaStore.Images.Media._ID,  // 图片路径
                                MediaStore.Images.Media.DATA,  // 图片名称
                                MediaStore.Images.Media.DISPLAY_NAME,  // 生成日期
                                MediaStore.Images.Media.DATE_ADDED
                        )

                override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                    val cursorLoader: CursorLoader
                    // 备注：部分机型png图片无法正常显示（原因未知），必要时可以在此过滤掉
                    val where = (MediaStore.Images.Media.MIME_TYPE + "='image/jpeg' "
                            + "or "
                            + MediaStore.Images.Media.MIME_TYPE + "='image/png'")
                    when (id) {
                        QUERY_ALL -> {
//                            LogUtils.e(this, "--查询图片->所有--")
                            cursorLoader = CursorLoader(
                                    this@ImageSelectorActivity,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    IMAGE_PROJECTION,
                                    where,
                                    null, IMAGE_PROJECTION[3] + " DESC"
                            )
                        }
                        QUERY_ONE -> {
//                            LogUtils.e(this, "--查询图片->部分--")
                            cursorLoader = CursorLoader(
                                    this@ImageSelectorActivity,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    IMAGE_PROJECTION,
                                    IMAGE_PROJECTION[1] + " like '%" + args!!.getString("path") + "%'",
                                    null,
                                    IMAGE_PROJECTION[3] + " DESC"
                            )
                        }
                        else -> {
//                            LogUtils.e(this, "--查询图片->所有--")
                            cursorLoader = CursorLoader(
                                    this@ImageSelectorActivity,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    IMAGE_PROJECTION,
                                    where,
                                    null, IMAGE_PROJECTION[3] + " DESC"
                            )
                        }
                    }
                    return cursorLoader
                }

                override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {

                    if (cursor == null) return
                    if (cursor.count > 0) {
                        mImageList.clear()
                        mPhotoList.clear()
                        cursor.moveToFirst()
                        while (cursor.moveToNext()) {
                            val path: String =
                                    cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                            val name: String =
                                    cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
                            val dateTime: Long =
                                    cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[3]))
                            //                    LogUtils.e(TAG, "时间：" + dateTime);
                            val image = ImageVo(path, name, dateTime, ImageVo.TYPE_IMAGE)
                            mImageList.add(image)
                            // 获取文件夹名称
                            val imageFile = File(path)
                            if (imageFile.exists()) {
                                val folderFile = imageFile.parentFile
                                folderFile?.let {
                                    val folder = PhotoVo()
                                    folder.name = folderFile.name
                                    folder.path = folderFile.absolutePath
                                    folder.cover = image // 文件夹缩略图
                                    if (!mPhotoList.contains(folder)) {
                                        val imageList: MutableList<ImageVo> =
                                                java.util.ArrayList()
                                        imageList.add(image)
                                        folder.images = imageList
                                        mPhotoList.add(folder)
                                    } else { // 更新
                                        val f = mPhotoList[mPhotoList.indexOf(folder)]
                                        f.images.add(image)
                                    }
                                }
                            }
                        }
                        // 构造虚拟的所有图片目录
                        if (mImageList.isNotEmpty()) {
                            val all = PhotoVo()
                            all.cover = mImageList[0]
                            all.name = "All Images"
                            all.path = "/"
                            all.images = mImageList
                            mPhotoList.add(0, all)
                        }
                        // 刷新数据
                        mPhotoAdapter?.let {
                            if (it.checkedIndex < mPhotoList.size - 1) {
                                mImageAdapter?.refreshImages(
                                        mPhotoList[it.checkedIndex].images,
                                        mImageAdapter!!.selectedImages
                                )
                            } else {
                                mImageAdapter?.refreshImages(mImageList, mImageAdapter!!.selectedImages)
                            }
                            it.refreshPhotos(mPhotoList)
                        }
                    }
                }

                override fun onLoaderReset(loader: Loader<Cursor>) {

                }
            }
}