
 # 2016-10-16 说一下
鉴于蛮多人喜欢这个项目的，收到不少同学的咨询，表示真的满开心的。目前这个工程项目是第2个版本了，还是有不少问题，所以打算接下来的时间抽时间重写该项目三个版本，目前正在考虑整个项目的代码架构。会重新认真考虑改善目前存在的问题，首先优化分页问题，目前已经有一定的思路，另外还会加入保存上次已读位置、尝试加入按章节跳转、书签、文字复制等等功能，尽量考虑拓展性，努力打造一个简单易用的轻量级txt文本阅读器，有兴趣的同学可以fork and start~

 # 2016-9-30 第一次更新
 
1.完善上下翻页时交界阴影条可能不出现的问题。
2.修复分页没完成退出导致崩溃的问题。
3.修复调节字体大小过快可能导致崩溃的问题。
4.更改调节字体类型后自动跳转到第一页进行重新分页。
5.增加了退出后释放内存。




# HWTxtReader

1.介绍。
这是轻量级Txt阅读器，支持windows下的常见几种编码格式，使用翻页阅读

2.使用。
当前的demo已经封装成一个非常简单去调用的的类BookPlayActivity
Intent intent = new Intent();  
intent.putExtra("bookname", "testbookname");  
intent.putExtra("bookpath", editText.getText().toString());  
intent.setClass(TestActivity.this, BookPlayActivity.class);  
TestActivity.this.startActivity(intent);  
只要通过Intent,传入要显示的书籍名称，书籍本地路径，跳转到BookPlayActivity就可以了，这个BookPlayActivity包含了上面的各自菜单操作，会自动完成加载书籍、分页等各种工作。


gif动态效果图如下（模拟器太卡了，其实蛮流畅的）：

![image](https://github.com/bifan-wei/HWTxtReader/blob/master/reader.gif)


开源不容易，如果还喜欢，给个start~啦啦啦,你的支持是我更新的动力\(≧▽≦)/~

更多介绍以及其他，请看：http://blog.csdn.net/u014614038/article/details/51799180
