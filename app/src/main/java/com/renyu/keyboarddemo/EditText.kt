package com.renyu.keyboarddemo

import android.app.Activity
import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.TextView
import com.blankj.utilcode.util.KeyboardUtils
import android.view.WindowManager
//import com.tencent.bugly.proguard.y
//import com.tencent.bugly.proguard.x
import android.view.Gravity
import android.graphics.PixelFormat
import android.graphics.Rect


/**
 * Created by Administrator on 2018/3/8 0008.
 */
class KeyBoardEditText : AppCompatEditText {

    private var context_: Context? = null
    private var keyboradNumber: Keyboard? = null
    private var keyboradLetter: Keyboard? = null
    private var keyboardSymbols:Keyboard?=null
    private var keyboardView: KeyboardView? = null
    private var viewGroup: ViewGroup? = null

    // 是否发生键盘切换
    var changeLetter = false
    // 是否为大写字母
    var isCapital = false
    var listener: OnKeyboardStateChangeListener? = null
    constructor(context: Context) : super(context) {
        context_ = context
        initEditView(null)
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        context_ = context
        initEditView(attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        context_ = context
        initEditView(attrs)
    }


    private fun initEditView(attrs: AttributeSet?) {
        //实例化数字键盘布局
        keyboradNumber = Keyboard(context_, R.xml.keyboard_number)
        //实例化字母键盘布局
        keyboradLetter = Keyboard(context_, R.xml.keyboard_letter)
        //实例化特殊符号键盘布局
       keyboardSymbols=Keyboard(context,R.xml.keyboard_symbols)

        //读取自定义控件属性
       var typedArray=context.obtainStyledAttributes(attrs,R.styleable.KeyBoard)
       var  resid=typedArray.getResourceId(R.styleable.KeyBoard_keyboardgroup,0)
        var  type=typedArray.getInteger(R.styleable.KeyBoard_type,2)//默认数字键盘
        var mViewGroup=LayoutInflater.from(context).inflate(resid!!,null) as ViewGroup
        var keyboardView=mViewGroup?.findViewById<CustomKeyBoardView>(R.id.view_keyboard)
        typedArray.recycle()

        //初始化keyboardview键盘布局
        setKeyboardType(mViewGroup!!,keyboardView!!,type)

    }
    fun setKeyboardType(viewGroup: ViewGroup, keyboardView: KeyboardView, number: Int) {
        this.keyboardView = keyboardView
        this.viewGroup = viewGroup
       when(number){
           1 ->  keyboardView.keyboard = keyboradNumber
           2 ->  keyboardView.keyboard = keyboradLetter
           3 ->  keyboardView.keyboard = keyboardSymbols
           else -> keyboardView.keyboard = keyboradLetter
       }
        keyboardView.isPreviewEnabled = true
        keyboardView.setOnKeyboardActionListener(object : KeyboardView.OnKeyboardActionListener {
            override fun swipeRight() {
                 Log.d("---ActionListener---->","-------swipeRight------->");
            }
            override fun swipeLeft() {
                Log.d("---ActionListener---->","-------swipeLeft------->");

            }
            override fun swipeUp() {
                Log.d("---ActionListener---->","-------swipeUp------->");

            }
            override fun swipeDown() {
                Log.d("---ActionListener---->","-------swipeDown------->");

            }
            override fun onPress(primaryCode: Int) {
                Log.d("---ActionListener---->","-------onPress------->");
                canShowPreview(primaryCode)
            }
            override fun onRelease(primaryCode: Int) {
                Log.d("---ActionListener---->","-------onRelease------->");
                keyboardView.isSoundEffectsEnabled = true
            }
            override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
                Log.d("---ActionListener---->","-------onKey------->");
                val editable = text
                val start = selectionStart
                // 删除功能
                if (primaryCode == Keyboard.KEYCODE_DELETE) {
                    if (editable.isNotEmpty() && start>0) {
                        editable.delete(start-1, start)
                    }
                }
                // 字母键盘与数字键盘切换
                else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
                    changeKeyBoard(!changeLetter,false)
                }
                //符号盘切数字盘
                else if(primaryCode == -22){
                    changeKeyBoard(false,false)
                }
                //切换符号盘
                else if(primaryCode == -44){
                    changeKeyBoard(!changeLetter,true)
                }
                //符号---->字母
                else if(primaryCode == -10){
                    changeKeyBoard(true,false)
                }
                // 完成
                else if (primaryCode == Keyboard.KEYCODE_DONE) {
                    keyboardView.visibility = View.GONE
                    viewGroup.visibility = View.GONE
                    listener?.hide()
                }
                // 切换大小写
                else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
                    changeCapital(!isCapital)
                    keyboardView.keyboard = keyboradLetter
                }
                else {
                    editable.insert(start, Character.toString(primaryCode.toChar()))
                }

            }


            override fun onText(text: CharSequence?) {


            }
        })
        viewGroup.findViewById<TextView>(R.id.done).setOnClickListener { v ->delKeyBoardFromWindow(context_,viewGroup) }
    }

    fun changeKeyBoard(letter: Boolean,issymbols:Boolean) {
        changeLetter = letter
        if(issymbols){//特殊符号情况
            keyboardView?.keyboard = keyboardSymbols

        }else{//非特殊符号情况
            if (changeLetter) {
                keyboardView?.keyboard = keyboradLetter
            }
            else {
                keyboardView?.keyboard = keyboradNumber
            }
        }
    }
    /**
     * 过滤不显示的功能按键
     */
    fun canShowPreview(primaryCode: Int) {
        val nolists = arrayOf(Keyboard.KEYCODE_SHIFT, Keyboard.KEYCODE_MODE_CHANGE, Keyboard.KEYCODE_CANCEL,
                Keyboard.KEYCODE_DONE, Keyboard.KEYCODE_DELETE, Keyboard.KEYCODE_ALT, 32,-10,-44,-22)
        val isNumber=  keyboardView?.keyboard == keyboradNumber
        keyboardView?.isPreviewEnabled = !nolists.contains(primaryCode)&&!isNumber
        keyboardView?.setPopupOffset(-7,0)//解决连续点击出现动画问题
    }


    fun changeCapital(isCapital: Boolean) {
        val lowercase = "abcdefghijklmnopqrstuvwxyz"

        val keyList = keyboradLetter?.keys
        keyList?.forEach {
            if (it?.label != null && lowercase.indexOf(it.label.toString().toLowerCase()) != -1) {
                if (isCapital) {
                    it.label = it.label.toString().toUpperCase()
                    it.codes[0] -= 32
                }
                else {
                    it.label = it.label.toString().toLowerCase()
                    it.codes[0] += 32
                }
            }
//            if (it?.label!=null && it?.label == "小写" && isCapital) {
//                it.label = "大写"
//            }
//            else if (it?.label!=null && it?.label == "大写" && !isCapital) {
//                it.label = "小写"
//            }
        }
        this.isCapital = isCapital
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //屏蔽系统键盘
        KeyboardUtils.hideSoftInput(context as Activity?)
        requestFocus()
        findFocus()
        showKeyBoardToWindow(context_!!,viewGroup!!)
        return true
    }


    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)

        if(focused){//最新的edittext获取焦点
            showKeyBoardToWindow(context_!!, viewGroup!!)
        }else{//上一个edittext失去焦点
            delKeyBoardFromWindow(context_!!,viewGroup!!)
        }

    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK && keyboardView?.visibility != View.GONE
//                && viewGroup?.visibility != View.GONE) {
//            keyboardView?.visibility = View.GONE
//            viewGroup?.visibility = View.GONE
//            listener?.hide()
//            return true
//        }

        if(keyCode == KeyEvent.KEYCODE_BACK&&viewGroup!!.parent!=null){
            delKeyBoardFromWindow(context_!!,viewGroup!!)
            listener?.hide()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }





    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        KeyboardUtils.hideSoftInput(context as Activity?)



    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        KeyboardUtils.hideSoftInput(context as Activity?)
    }




    fun showKeyBoardToWindow(context: Context, viewGroup: ViewGroup){

        if(viewGroup.parent!=null)return
        //添加布局到windows
        if(context is Activity){
            val mLayoutParams = WindowManager.LayoutParams()
            mLayoutParams.format = PixelFormat.TRANSLUCENT            //图片之外其他地方透明
            mLayoutParams.gravity = Gravity.BOTTOM
            mLayoutParams.alpha = 1f
            mLayoutParams.windowAnimations= android.R.style.Animation_InputMethod//设置输入法动画
            //设置透明度
            mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE


            (context as Activity).windowManager.addView(viewGroup,mLayoutParams)
        }
        //显示布局
//        if (keyboardView?.visibility != View.VISIBLE) {
//            keyboardView?.visibility = View.VISIBLE
//            viewGroup?.visibility = View.VISIBLE
//            listener?.show()
//        }
    }


    fun delKeyBoardFromWindow(context: Context?, viewGroup: ViewGroup){
        //添加布局到windows
        if(context is Activity){
            try {
                (context as Activity).windowManager.removeView(viewGroup)
            }catch (e:Exception){

            }

        }
//        //不显示布局
//        if (keyboardView?.visibility == View.VISIBLE) {
//            keyboardView?.visibility = View.GONE
//            viewGroup?.visibility = View.GONE
//            listener?.hide()
//        }
    }



    interface OnKeyboardStateChangeListener {
        fun show()
        fun hide()
    }



}