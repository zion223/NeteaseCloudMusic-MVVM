### 仿网易云客户端V6.0 MVVM版本

根据开源项目[Jetpack-MVVM-Best-Practice](https://github.com/KunMinX/Jetpack-MVVM-Best-Practice)架构重构之前使用MVC架构的项目, 原项目的[地址](https://github.com/zion223/NeteaseCloudMusic)。



对Jetpack架构的思考

1. layout.xml文件复用问题，layout文件中引入变量，尽可能的抽取出公共的变量才能复用布局文件，若引入ViewModel则尽可能引入CommomViewModel否则会导致语义不清晰
2. BindingAdapter中绑定方法调用，若两次变量相同则不会被调用，绑定刷新状态方法若两次刷新状态都是True则第二次不会响应，需考虑其他方法(new Object(state))
3. 需要绑定生命周期的方法调用时不太优雅
4. 尽可能避免在layout文件中写逻辑操作
