![image](https://github.com/bifan-wei/HwTxtReader/blob/master/pics/ic_txt_logo.png) <br> 
**想要快速实现文本播放显示功能么？HwTxtReader 是一个轻量级文本播放控件，能帮助你快速集成加载播放小说文本文件功能，同时内置默认的播放页面，能让你快速集成一个简洁漂亮的小说阅读器**<br> 
<br> <br> 
#### 目前实现了的功能：
+  字体设置与调节。包括大小、颜色、是否加粗。<br>
+   页面风格设置，夜间模式等。<br>
+   进度跳转与当前进度获取。<br>
+   章节获取与章节跳转。<br>
+   可以滑盖翻页、平移翻页切换、剪切翻页切换，支持轻击翻页。<br>
+   可以进行长按滑动选择复制文字。<br>
+   数字与英文字符显示特点颜色。<br>
+   自动跳转到上次阅读进度。<br>
+   支持设置段落间距。<br>
+  支持竖向排版

<br>

#### 2019/06/30最新更新：增加剪切翻页风格，需要直接下代码。周末不陪女朋友宅宿舍更新这个，良心维护啊！！你的点赞是对我最大的支持，欢迎start issue！<br><br>
![image](https://github.com/bifan-wei/bifanResource/blob/master/git/shear.gif)<br>

#### 演示demo
<br><br>
**[apk下载](https://github.com/bifan-wei/HwTxtReader/tree/master/demoapk)** <br>
![image](https://github.com/bifan-wei/bifanResource/blob/master/videos/demo_video.gif)
#### 更新日志请查看 updateLog.txt文件
<br>

#### 目前demo效果图：
![image](https://github.com/bifan-wei/HwTxtReader/blob/master/pics/ic_reader1.png)
<br><br>
![image](https://github.com/bifan-wei/HwTxtReader/blob/master/pics/ic_show.png)
<br><br>

#### 使用方法：
<br>

 **添加hwtxtreaderlib依赖**
<br> 
<br> 
 ```java
 allprojects {
repositories {
	...
	maven { url 'https://jitpack.io' }
}
 
dependencies {
	        implementation 'com.github.bifan-wei:HwTxtReader:V2.1.1'
	        //注意Android support 版本请使用 implementation 'com.android.support:appcompat-v7:28.0.0'，
		}
		
```

**使用默认界面使用的话，只需要一句代码:**

  ```java
     HwTxtPlayActivity.loadTxtFile(this, FilePath);
       
```
**使用自定义view的话，直接使用TxtReaderView (详细请看Wiki)**
**xml中:**
```java
      <com.bifan.txtreaderlib.main.TxtReaderView 
        android:id="@+id/activity_hwtxtplay_readerView" 
        android:layout_width="match_parent"  
        android:layout_height="match_parent" 
       />
       
```
**代码中调用loadTxtFile方法直接加载文件：**<br>
 
 ```java
 
 mTxtReaderView.loadTxtFile(FilePath, new ILoadListener() { 
            @Override 
            public void onSuccess() { 
             //加载成功回调 
                initWhenLoadDone(); 
            } 

            @Override 
            public void onFail(TxtMsg txtMsg) { 
               //加载失败回调 
            } 

            @Override 
            public void onMessage(String message) {  
            //加载过程信息回调 
            } 
        }); 
	
```
<br> 
<br> 

#### 如果你觉得还不错，欢迎start支持。

