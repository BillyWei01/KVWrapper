# KVWrapper

## 一、 概述
本项目演示了一套以kotlin委托属性为基础的KV存储封装方案。 <br>
项目中是基于``SharePreferences``封装的，但这套方案也适用于其他类型的KV存储框架。<br>

## 二、 封装方案

此方案封装了两类委托：

1. **基础类型** <br>
基础类型包括 `[boolean, int, float, long, double, String, Set<String>, Object]` 等类型。<br>
其中，`Set<String>` 本可以通过 Object 类型囊括，<br>
但因为`Set<String>`是 `SharePreferences` 内置支持的类型，这里我们就直接内置支持了。<br>

2. **扩展key的基础类型** <br>
基础类型的委托，定义属性时需传入常量的`key`，通过委托所访问到的是`key`对应的`value`； <br>
而开发中有时候需要【常量+变量】的key，基础类型的委托无法实现。<br>
为此，方案中实现了一个`CombineKV`类，通过`CombineKV`也可以访问`value`, 但是需要额外多传一个`extKey`。 <br>
`CombineKV`通过组合`[key+extKey]`实现通过两级key来访问`value`的效果。<br>
此外，方案基于`CombineKV`封装了各种基础类型的`ExtKV`，用于简化API，以及约束所访问的value的类型。
相应对，方案中也封装了`CombineKV`以及各种基础类型的`ExtKV`的委托。

### 2.1 委托实现
**基础类型**：[BasicDelegate.kt](https://github.com/BillyWei01/KVWrapper/blob/main/kvwrapper/src/main/java/io/github/kvwrapper/BasicDelegate.kt) <br>
**扩展key的基础类型**： [ExtDelegate.kt](https://github.com/BillyWei01/KVWrapper/blob/main/kvwrapper/src/main/java/io/github/kvwrapper/ExtDelegate.kt)

这里举例一下基础类型中的`Boolean`类型的委托实现：

```kotlin
class BooleanProperty(private val key: String, private val defValue: Boolean) :
    ReadWriteProperty<KVData, Boolean> {
    override fun getValue(thisRef: KVData, property: KProperty<*>): Boolean {
        return thisRef.kv.getBoolean(key, defValue)
    }

    override fun setValue(thisRef: KVData, property: KProperty<*>, value: Boolean) {
        thisRef.kv.putBoolean(key, value)
    }
}

class NullableBooleanProperty(private val key: String) :
    ReadWriteProperty<KVData, Boolean?> {
    override fun getValue(thisRef: KVData, property: KProperty<*>): Boolean? {
        return thisRef.kv.getBoolean(key)
    }

    override fun setValue(thisRef: KVData, property: KProperty<*>, value: Boolean?) {
        thisRef.kv.putBoolean(key, value)
    }
}

```

经典的 `ReadWriteProperty` 实现: <br>
分别重写 `getValue` 和 `setValue` 方法，方法中调用KV存储的读写API。<br>
由于kotlin区分了可空类型和非空类型，方案中也分别封装了可空和非空两种委托。

### 2.2 基类定义
实现了委托之后，我们将各种委托API封装到一个基类中：[KVData.kt](https://github.com/BillyWei01/KVWrapper/blob/main/kvwrapper/src/main/java/io/github/kvwrapper/KVData.kt)

```kotlin
abstract class KVData {
    // 存储接口
    abstract val kv: KVStore

    // 基础类型
    protected fun boolean(key: String, defValue: Boolean = false) = BooleanProperty(key, defValue)
    protected fun int(key: String, defValue: Int = 0) = IntProperty(key, defValue)
    protected fun float(key: String, defValue: Float = 0f) = FloatProperty(key, defValue)
    protected fun long(key: String, defValue: Long = 0L) = LongProperty(key, defValue)
    protected fun double(key: String, defValue: Double = 0.0) = DoubleProperty(key, defValue)
    protected fun string(key: String, defValue: String = "") = StringProperty(key, defValue)
    protected fun stringSet(key: String, defValue: Set<String> = emptySet()) = StringSetProperty(key, defValue)
    protected fun <T> obj(key: String, encoder: ObjectEncoder<T>, defValue: T) = ObjectProperty(key, encoder, defValue)

    // 可空的基础类型
    protected fun nullableBoolean(key: String) = NullableBooleanProperty(key)
    protected fun nullableInt(key: String) = NullableIntProperty(key)
    protected fun nullableFloat(key: String) = NullableFloatProperty(key)
    protected fun nullableLong(key: String) = NullableLongProperty(key)
    protected fun nullableDouble(key: String) = NullableDoubleProperty(key)
    protected fun nullableString(key: String) = NullableStringProperty(key)
    protected fun nullableStringSet(key: String) = NullableStringSetProperty(key)
    protected fun <T> nullableObj(key: String, encoder: NullableObjectEncoder<T>, ) = NullableObjectProperty(key, encoder)

    // 扩展key的基础类型
    protected fun extBoolean(key: String, defValue: Boolean = false) = ExtBooleanProperty(key, defValue)
    protected fun extInt(key: String, defValue: Int = 0) = ExtIntProperty(key, defValue)
    protected fun extFloat(key: String, defValue: Float = 0f) = ExtFloatProperty(key, defValue)
    protected fun extLong(key: String, defValue: Long = 0L) = ExtLongProperty(key, defValue)
    protected fun extDouble(key: String, defValue: Double = 0.0) = ExtDoubleProperty(key, defValue)
    protected fun extString(key: String, defValue: String = "") = ExtStringProperty(key, defValue)
    protected fun extStringSet(key: String, defValue: Set<String> = emptySet()) = ExtStringSetProperty(key, defValue)
    protected fun <T> extObj(key: String, encoder: ObjectEncoder<T>, defValue: T) = ExtObjectProperty(key, encoder, defValue)

    // 扩展key的可空的基础类型
    protected fun extNullableBoolean(key: String) = ExtNullableBooleanProperty(key)
    protected fun extNullableInt(key: String) = ExtNullableIntProperty(key)
    protected fun extNullableFloat(key: String) = ExtNullableFloatProperty(key)
    protected fun extNullableLong(key: String) = ExtNullableLongProperty(key)
    protected fun extNullableDouble(key: String) = ExtNullableDoubleProperty(key)
    protected fun extNullableString(key: String) = ExtNullableStringProperty(key)
    protected fun extNullableStringSet(key: String) = ExtNullableStringSetProperty(key)
    protected fun <T> extNullableObj(key: String, encoder: NullableObjectEncoder<T>) = ExtNullableObjectProperty(key, encoder)
    
    // CombineKV 
    protected fun combineKV(key: String) = CombineKVProperty(key)
}
```

使用时，继承`KVData`，然后实现`kv`, 返回一个[KVStore.kt](https://github.com/BillyWei01/KVWrapper/blob/main/kvwrapper/src/main/java/io/github/kvwrapper/KVStore)的实现类即可。
接下来说明一下具体的使用方法。

## 三、 使用方法
### 3.1 读写API的实现
上面我们提到，`KVData`需要一个`KVStore`的实现类。
我们以`SharedPreferences`为例，其实现如下：

```kotlin
class SpKV(name: String): KVStore {
    private val sp: SharedPreferences =
        AppContext.context.getSharedPreferences(name, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sp.edit()

    override fun putBoolean(key: String, value: Boolean?) {
        if (value == null) {
            editor.remove(key).apply()
        } else {
            editor.putBoolean(key, value).apply()
        }
    }

    // 基本类型，要实现不存在value时返回null, 需要先调用 contains 判断
    override fun getBoolean(key: String): Boolean? {
        return if (sp.contains(key)) sp.getBoolean(key, false) else null
    }

    // 如果有默认值，直接调用带默认值的方法会更有效率
    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sp.getBoolean(key, defValue)
    }
    
    // ...... 其他类型 
}
```

抽象来看，访问KV存储，和访问[Map]是类似的。<br>
常用的API，包括 put，get，remove, contains等。 <br>
而 put(key, null) 等价于 remove(key) ；<br>
get(key) != null 等价于 contains(key) == true。<br>
故此，在定义[KVStore]接口时，我们可以简化接口，省略 remove 和 contains。<br>

对于基础类型, 如果要实现不存在value时返回null, 需要先调用存储API contains 判断value是否存在。<br>
因此，基于效率的考虑，我们同时声明 get(key):T? 和 get(key, defValue): T 接口。


### 3.2 具体使用
#### 3.2.1 基本用法
在实现了`KVStore`接口之后，先继承`KVData`, 返回 `KVStore` 实例。

```kotlin
open class GlobalKV(name: String) : KVData() {
    override val kv: KVStore by lazy {
        SpKV(name)
    }
}
```

以上代码定义了一个`GlobalKV`, 用于保存“全局数据”。<br>
和“全局数据”对应，有”个人数据“，这个我们稍后再分析，目前先分析一下基本用法。

`GlobalKV`实例：
```kotlin
object LocalSetting : GlobalKV("local_setting") {
    // 是否开启开发者入口
    var enableDeveloper by boolean("enable_developer")

    // 用户ID
    var userId by long("user_id")

    // id -> name 的映射。
    val idToName by extNullableString("id_to_name")

    // 收藏
    val favorites by extStringSet("favorites")

    var gender by obj("gender", Gender.CONVERTER, Gender.UNKNOWN)
}
```

定义委托属性的方法很简单：<br>
和定义变量类似，需要声明变量名和类型；<br>
和变量声明不同，需要传入key, 然后有的类型需要传入转换器（实现字符串和对象类型的转换），以及默认值。

基本类型的读写的方法，和读写变量一样。<br>
例如：
```kotlin
fun test1(){
    // 写入
    LocalSetting.userId = 10001L
    LocalSetting.gender = Gender.FEMALE

    // 读取
    val uid = LocalSetting.userId
    val gender = LocalSetting.gender
}

```

读写扩展key的基本类型，则和Map的语法类似:
```kotlin
fun test2() {
    if (LocalSetting.idToName[1] == null || LocalSetting.idToName[2] == null) {
        Log.d("TAG", "Put values to idToName")
        LocalSetting.idToName[1] = "Jonn"
        LocalSetting.idToName[2] = "Mary"
    } else {
        Log.d("TAG", "There are values in idToName")
    }
    Log.d("TAG", "idToName values: " +
                "1 -> ${LocalSetting.idToName[1]}, " +
                "2 -> ${LocalSetting.idToName[2]}"
    )
}
```

扩展key的基本类型，extKey是Any类型，也就是说，以上代码的`[]`，可以传入任意类型的参数。

#### 3.2.2 数据隔离

不同环境（开发环境/测试环境），不同用户，最好数据实例是分开的，相互不干扰。<br>
比方说有 uid='001' 和 uid='002' 两个用户的数据，如果需要隔离两者的数据，有多种方法，例如：

1. 拼接uid到key中。

   如果是在原始的``SharePreferences``的基础上，是比较好实现的，直接``put(key+uid, value)``即可； <br>
   但是如果用委托属性定义，可以用上面定义的扩展key的类型。
2. 拼接uid到文件名中。

   但是不同用户的数据糅合到一个文件中，对性能多少有些影响：
    - 在多用户的情况下，实例的数据膨胀；
    - 每次访问value, 都需要拼接uid到key上。<br>

   因此，可以将不同用户的数据保存到不同的实例中。<br>
   具体的做法，就是拼接uid到路径或者文件名上。<br>

基于此分析，我们定义两种类型的基类：
- **GlobalKV**: 全局数据，切换环境和用户，不影响GlobalKV所访问的数据实例。
- **UserKV**: 用户数据，需要同时区分 “服务器环境“ 和 ”用户ID“。

`GlobalKV`前面已经说明了，这里我们看下`UserKV`的实现：

```kotlin
abstract class UserKV(
    private val name: String,
    private val userId: Long
) : KVData() {
    override val kv: SpKV by lazy {
        val fileName = "${name}_${userId}_${AppContext.env.tag}"
        if (AppContext.debug) {
            SpKV(fileName)
        } else {
            // 如果是release包，可以对文件名做个md5，以便匿藏uid等信息
            SpKV(Utils.getMD5(fileName.toByteArray()))
        }
    }
}
```


`UserKV`实例：

```kotlin
/**
 * 用户信息
 */
class UserInfo(uid: Long) : UserKV("user_info", uid) {
    companion object {
        private val map = ArrayMap<Long, UserInfo>()
        
        // 返回当前用户的实例
        fun get(): UserInfo {
            return get(AppContext.uid)
        }

        // 根据uid返回对应的实例
        @Synchronized
        fun get(uid: Long): UserInfo {
            return map.getOrPut(uid) {
                UserInfo(uid)
            }
        }
    }
    
    var gender by intEnum("gender", Gender.CONVERTER)
    var isVip by boolean("is_vip")
    
    // ... 其他变量
}

```

``UserKV``的实例不能是单例（不同的``uid``对应不同的实例）。<br>
因此，可以定义``companion``对象，用来缓存实例，以及提供获取实例的API。<br>

保存和读取方法如下：<br>
先调用`get()`方法获取，然后其他用法就和前面描述的用法一样了。

```kotlin
UserInfo.get().gender = Gender.FEMALE

val gender = UserInfo.get().gender
```

#### 3.2.3 环境相关的实例
有一类数据，需要区分环境，但是和用户无关。<br>
这种情况，可以用`UserKV`, 然后`uid`传0（或者其他的`uid`用不到的数值）。

```kotlin
/**
 * 远程设置
 */
object RemoteSetting : UserKV("remote_setting", 0L) {
    // 某项功能的AB测试分组
    val fun1ABTestGroup by int("fun1_ab_test_group")
    
    // 服务端下发的配置项
    val setting by combineKV("setting")
}
```

## 四、 相关链接
掘金博客:
https://juejin.cn/post/7323449163420303370

FastKV:
https://juejin.cn/post/7018522454171582500


## License
See the [LICENSE](LICENSE) file for license rights and limitations.



