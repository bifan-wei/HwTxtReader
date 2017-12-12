# HwTxtReader
## 前言：
之前项目中有播放txt文件的功能，虽然是一个不难的功能，但是也不简单，陆陆续续做了很久，于是结合之前的经验，决定重新设计代码，
打造一个轻量级Txt阅读器组件，用于提供快速集成加载txt文件的功能，从2017-11月开始，空余时间慢慢做，目前把基本功能实现，剩下的就是优化了。

## 该库的作用：
针对于要求不是特别高的，想快速实现一个效果还不错的加载txt文件功能，这个应该还是不错的。如果是想做专业的阅读器，你可以去啃fbReader之类的。

## 目前实现了的功能：
1.字体设置与调节。包括大小、颜色、是否加粗。<br> 
2.页面风格设置，夜间模式等。<br> 
3.进度跳转与当前进度获取。<br> 
4.章节获取与章节跳转。<br> 
5.可以滑盖翻页与平移翻页切换。<br> 
6.可以进行长按滑动选择文字。<br> 
7.数字与英文字符显示特点颜色。<br> 


## 目前demo效果图：
![image](https://github.com/bifan-wei/HwTxtReader/blob/master/pics/ic_reader1.png)

## 章节列表效果图：
![image](https://github.com/bifan-wei/HwTxtReader/blob/master/pics/ic_chaper.png)

## 覆盖翻页效果：
![image](https://github.com/bifan-wei/HwTxtReader/blob/master/pics/ic_cover.png)

## 平移翻页效果：
![image](https://github.com/bifan-wei/HwTxtReader/blob/master/pics/ic_translate.png)


# 使用方法：
<br>
<br>
  ## 添加hwtxtreaderlib依赖 <br> 
  compile project(':hwtxtreaderlib') <br> 
  
  ##  使用默认界面使用的话，只需要一句代码： <br> 
   HwTxtPlayActivity.LoadTxtFile(this, FilePath); <br> 
   
   ##  使用自定义view的话，直接使用TxtReaderView <br> 
   
     ###  xml中：<br> 
     <com.hw.txtreaderlib.main.TxtReaderView <br> 
        android:id="@+id/activity_hwtxtplay_readerView" <br> 
        android:layout_width="match_parent" <br> 
        android:layout_height="match_parent" <br> 
       /><br> 
       
      ### 代码中调用loadTxtFile方法直接加载文件：<br> 
      mTxtReaderView.loadTxtFile(FilePath, new ILoadListener() { <br> 
            @Override <br> 
            public void onSuccess() { <br> 
             //加载成功回调 <br> 
                initWhenLoadDone(); <br> 
            } <br> 

            @Override <br> 
            public void onFail(TxtMsg txtMsg) { <br> 
               //加载失败回调 <br> 
            } <br> 

            @Override <br> 
            public void onMessage(String message) { <br> 
            //加载过程信息回调 <br> 
            } <br> 
        }); <br> 

<br> 
<br> 
<br> 

后话：想认真做好这个库，陆陆续续空余时间折腾了一个月才把基本功能做完。忽然发现好累，想想开源也不容易，为那些开源的大神致敬，也希望自己也能做点贡献。<br> 

