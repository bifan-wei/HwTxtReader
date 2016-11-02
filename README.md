
# 关于 *HwReader*:
_________________________________________
 
 >>这是一个简单易于集成的阅读器模块，目前是第三个版本。
 
 >>该阅读器目前支持txt文件的预览，支持windows下常见的编码格式，比如：ANSA,Unicode,Utf-8,Utf-16 等等
 
 >>目前该模块还相比比较稳定的（相对之前的版本），适合项目需要集成文本阅读小模块以及适合想要学习做阅读器的同学。
 
 
# 目前已实现的功能：
__________________________________________________

>>### 1.翻页阅读。
>>### 2.调整字体大小。
>>### 3.调整字体类型。
>>### 4.跳转进度。
>>### 5.调整页显示风格。
>>### 6.竖向排版文字。
>>### 7.横向排版文字。
>>### 8.恢复历史阅读进度。
>>### 9.恢复历史设置样式。
 
 # 已实现功能效果图：
____________________________________________________
 
 >>### 菜单界面
 >>![image](https://github.com/bifan-wei/HWTxtReader/blob/master/menu.jpg)
 
 >>### 字体界面
 >>![image](https://github.com/bifan-wei/HWTxtReader/blob/master/text.jpg)
 
 >>### 字体界面--加粗
 >>![image](https://github.com/bifan-wei/HWTxtReader/blob/master/text_bold.jpg)
 
 >> ### 字体界面--娃娃体
 >>![image](https://github.com/bifan-wei/HWTxtReader/blob/master/text_2.jpg)
 
 >> ### 进度界面
 >>![image](https://github.com/bifan-wei/HWTxtReader/blob/master/progress.jpg)
 
>> ### 风格界面--夜间
>> ![image](https://github.com/bifan-wei/HWTxtReader/blob/master/style_night.jpg)
 
 >>### 风格界面--古风
 >>![image](https://github.com/bifan-wei/HWTxtReader/blob/master/style_ancien1.jpg)
 
 >>### 风格界面--古风娃娃体
 >>![image](https://github.com/bifan-wei/HWTxtReader/blob/master/style_ancien2.jpg)
 
 >>### 风格界面--换页风格1
>> ![image](https://github.com/bifan-wei/HWTxtReader/blob/master/page_switch1.jpg)
 
 >>### 风格界面--换页风格2
 >>![image](https://github.com/bifan-wei/HWTxtReader/blob/master/page_switch2.jpg)
 
 >> ### 亮度调整
 >>![image](https://github.com/bifan-wei/HWTxtReader/blob/master/light.jpg)
 




# 使用方法
_________________________________________

 >>### 已经集成一个很简单类，直接传人书名与书籍路径就可以了：
 
                                 Intent intent = new Intent();
				intent.putExtra("bookname", "testbookname");
				intent.putExtra("bookpath", editText.getText().toString());
				intent.setClass(TestActivity.this, HwReaderPlayActivity.class);
				TestActivity.this.startActivity(intent);
    
>> ### 其他如果自定义的话可以自行修改
 
 
___________________________________________________

# 其他
_________________________________________

>> ## 该阅读器模块还有不少不完善的地方，会不断完善修改，并且会陆续加入其他功能，比如章节跳转、保存书签等等，喜欢的话可以fork And  start,有问题可以直接Issue，我会努力跟进的。
 
  

______________________________________________________

 >>[旧版本的介绍以及源码](https://github.com/bifan-wei/HwtxtReader2/)
