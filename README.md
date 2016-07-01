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

更多介绍以及其他，请看：http://blog.csdn.net/u014614038/article/details/51799180
