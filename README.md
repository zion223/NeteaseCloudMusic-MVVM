### 仿网易云客户端V6.0 MVVM版本

根据开源项目[Jetpack-MVVM-Best-Practice](https://github.com/KunMinX/Jetpack-MVVM-Best-Practice)架构重构之前使用MVC架构的项目, 原项目的[地址](https://github.com/zion223/NeteaseCloudMusic)。  
 PS. 如果编译时提示找不到BR类,把 C:\Users\User\AndroidStudio4.0\config 路径下的studio64.exe.vmoptions文件加上-Dfile.encoding=UTF-8, 然后重启AndroidStudio就OK。


**对Jetpack架构的思考**

1. layout.xml文件复用问题，layout文件中引入变量，尽可能的抽取出公共的变量才能复用布局文件，若引入ViewModel则尽可能引入CommomViewModel否则会导致语义不清晰。
2. BindingAdapter中绑定方法调用，若两次变量相同则不会被调用，绑定刷新状态方法若两次刷新状态都是True则第二次不会响应，需考虑其他方法(new Object(state))。
3. 需要绑定生命周期的方法调用时不太优雅(调用时需要与View绑定)、使用动画时需要持有对View的引用。
4. 尽可能避免在layout文件中写逻辑操作。

**Room持久化存储框架的思考**

1. 保存单一数据不如SharePreference方便(如登陆时间、用户信息)。
2. Dao与实体类绑定，单一实体类只能作为一张表，若存在需保存相同实体类的数据不方便，例如AudioBean，既需要在保存音乐播放列表时使用也需要在保存到最近播放的歌曲时使用。目前的解决方法是用一个相同的类代替，有无更好的方法？？
3. Room在操作数据库时需要重新开线程访问，那么在无法开线程访问的情况下使用Room不方便(目前解决方法是在构建时调用allowMainThreadQueries())。
