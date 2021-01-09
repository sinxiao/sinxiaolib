package com.xu.sinxiao.common.view

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.xu.sinxiao.common.R
import com.xu.sinxiao.common.databinding.ViewSeemoreBinding


public class SeeMoreTextView : FrameLayout {
    //是否展开和收起的标记
    private var mIsShowAll: Boolean = false
    private var viewSeeMore: ViewSeemoreBinding;
    private var minLines: Int = 3;
    private var textColor: Int = Color.BLACK;
    private var textSize: Int = 16;
    private var textMoreLable: String = ""
    private var textLessLable: String = ""

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        viewSeeMore = DataBindingUtil.inflate<ViewSeemoreBinding>(LayoutInflater.from(context), R.layout.view_seemore, this, true);
//        View.inflate(context, R.layout.view_seemore, this)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SeeMoreTextView)
        if (attributes != null) {
            minLines = attributes.getInteger(R.styleable.SeeMoreTextView_minLine, 3)

            val text = attributes.getString(R.styleable.SeeMoreTextView_text)
            text?.let { setText(it) }
            textColor = attributes.getColor(R.styleable.SeeMoreTextView_textColor, Color.BLACK)
            textSize = attributes.getInteger(R.styleable.SeeMoreTextView_textSize, 16)

            textMoreLable = attributes.getString(R.styleable.SeeMoreTextView_textMoreLable).toString()
            if (TextUtils.isEmpty(textMoreLable)) {
                textMoreLable = context.getString(R.string.see_more)
            }
            textLessLable = attributes.getString(R.styleable.SeeMoreTextView_textLessLable).toString()
            if (TextUtils.isEmpty(textLessLable)) {
                textLessLable = context.getString(R.string.pack_up)
            }

//            <attr name="textColor" format="color" />
            attributes.recycle()
        }
        initListener()
    }

    private fun initListener() {
        //查看更多的点击事件
        viewSeeMore.viewSeemoreTvSeemore.setOnClickListener {
            if (mIsShowAll) {
                //这是收起的关键代码，将最大行数设置为你想展示的最小行数即可
                viewSeeMore.viewSeemoreTvcontent.maxLines = minLines
                viewSeeMore.viewSeemoreTvSeemore.text = textMoreLable

            } else {
                //这是查看更多的关键代码，将最大行数设置一个大数即可
                viewSeeMore.viewSeemoreTvcontent.maxLines = 50
                viewSeeMore.viewSeemoreTvSeemore.text = textLessLable

            }
            mIsShowAll = !mIsShowAll
        }
        //attachedToWindow之后执行操作
        post {

            //这里必须这样写，这是在attachedToWindow之后执行操作，否则获取行数会出问题
            Log.e("测试", "OnLayout${viewSeeMore.viewSeemoreTvlinecount.lineCount}")
            if (viewSeeMore.viewSeemoreTvlinecount.lineCount > minLines) {
                viewSeeMore.viewSeemoreTvSeemore.visibility = View.VISIBLE
            } else {
                viewSeeMore.viewSeemoreTvSeemore.visibility = View.GONE
            }

        }

    }

    /**
     * 设置文字
     */
    fun setText(text: String) {
        //每次设置文字后都要进行重置
        viewSeeMore.viewSeemoreTvcontent.text = text
        viewSeeMore.viewSeemoreTvlinecount.text = text
        viewSeeMore.viewSeemoreTvSeemore.text = textMoreLable
        viewSeeMore.viewSeemoreTvcontent.maxLines = minLines
        mIsShowAll = false

        viewSeeMore.viewSeemoreTvcontent.setTextColor(textColor)
        viewSeeMore.viewSeemoreTvlinecount.setTextColor(textColor)

        viewSeeMore.viewSeemoreTvcontent.textSize = textSize.toFloat()
        viewSeeMore.viewSeemoreTvlinecount.textSize = textSize.toFloat()


//        viewSeeMore.viewSeemoreTvSeemore.setTextColor(textColor)

        if (viewSeeMore.viewSeemoreTvlinecount.lineCount > minLines) {
            viewSeeMore.viewSeemoreTvSeemore.visibility = View.VISIBLE
        } else {
            viewSeeMore.viewSeemoreTvSeemore.visibility = View.GONE
        }
    }

}