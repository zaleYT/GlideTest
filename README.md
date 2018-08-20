# GlideTest

===8-20, 2018 修改说明===
1、滑到某个位置后，退出时，从当前位置使用shareElement方式退出
2、在两个activity中增加SharedElementCallback，辅助（1）中描述的功能
3、在mainactivity中注册setExitSharedElementCallback，在detail界面中注册setEnterSharedElementCallback
4、在main Activity中增加onActivityReenter方法

===8-18,2018 修改说明===
增加postponeEnterTransition和startPostponedEnterTransition两个方法，
使转场动画更加平滑,而不是一闪就变大
注意一点：
startPostponedEnterTransition要在给imageView设置了src之后调用，否则
也不会有效果


===8-18,2018 修改说明===
1. 动态的设置imageView的transitionname
2. 修改minSdkVersion为21
3. 去掉一些注释掉的代码


===8-17，2018 修改说明===
1. 增加ShareElment效果 
2. 增加ViewPager，页面切换浏览图片 
3. 使大图浏览界面状态栏透明，产生沉浸式状态栏效果

