##首先目前github上没有一个完整的公司级别的定制键盘 于是决定自己撸一个kotlin的自定义键盘 目前这个键盘已经运用到公司项目上了

#好不废话了 由于是本人花了精力和时间 所以不能免费开源 直接上图：
![image](http://bmob-cdn-12859.b0.upaiyun.com/2018/05/26/d08c5dc5409048538021b25a673ae861.png)
总共实现了3种类型的键盘 纯数字，字母，特殊符号 接入的时候可以自己设置

- 使用方式：
在XML中布局这个自定义键盘控件如下：

```

 <com.renyu.keyboarddemo.KeyBoardEditText
            android:id="@+id/ed_main"
            android:layout_width="match_parent"
            android:layout_height="50dip"
            android:background="@android:color/holo_orange_dark"
            KeyBoard:keyboardgroup="@layout/keyboardgroup"
            KeyBoard:type="1"
            />

 
  KeyBoard:type="1" 数字键盘
  KeyBoard:type="2" 字母键盘
  KeyBoard:type="3" 特殊符号键盘
 

```

- 联系方式 有兴趣  加入下面知识星球
![image](http://bmob-cdn-12859.b0.upaiyun.com/2018/05/28/b4f828f140ad818f80af345344dee5e1.png)







