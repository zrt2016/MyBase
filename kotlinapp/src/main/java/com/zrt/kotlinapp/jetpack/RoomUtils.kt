package com.zrt.kotlinapp.jetpack

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * @author：Zrt
 * @date: 2022/8/6
 * Room主要由Entity、Dao和Database组成
 * Entity：用于定义封装时间数据的实体类，每个实体类都会在数据库有一张对应的表，并且表中的列是根据实体类中的字段自动生成的
 * Dao：数据访问对象，通常会在此处对数据库的各项操作进行封装。一般逻辑层不需要和底层数据库交互，直接和Dao层进行交互
 * Database：用于定义数据库中的关键信息，包括数据库的版本号，包含哪些实体类以及提供Dao的层的访问实例
 */
@Entity
data class UserLV(var firstName: String, var lastName: String, var age: Int) {
    // 添加一个ID，并通过注解设置为主键，并通过autoGenerate=true设置为自动生成
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
}
/**
 * 封装数据库的访问操作
 */
@Dao
interface UserLVDao{
    @Insert
    fun insertUser(user:UserLV):Long
    @Update
    fun updateUser(newUser: UserLV)
    @Query("select * from UserLV")
    fun loadAllUsers(): List<UserLV>
    @Query("select * from UserLV where age > :age")
    fun loadUsersOlderThan(age: Int): List<UserLV>
    @Delete
    fun deleteUser(user: UserLV)
    @Query("delete from UserLV where lastName = :lastName")
    fun deleteUserByLastName(lastName: String): Int
}

@Entity
data class BookLV(var name: String, var pages: Int, var author: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0L
}
@Dao
interface BookDao {
    @Insert
    fun insertBook(book: BookLV): Long
    @Query("select * from BookLV")
    fun loadAllBooks(): List<BookLV>
}
/**
 * Database声明版本号以及包含哪些实体类，多个使用逗号隔开
 */
@Database(version = 3, entities = [UserLV::class, BookLV::class])
abstract class AppDatabase: RoomDatabase(){
    abstract fun userDao(): UserLVDao
    abstract fun bookDao(): BookDao
    companion object{
        private var instance: AppDatabase? = null
        @Synchronized
        fun getDatabase(context: Context):AppDatabase{
            instance?.let {
                return it
            }
            // context.applicationContext 使用全局，防止出现内存泄漏
            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "app_database")
//                    .allowMainThreadQueries()     // 允许增删改查功能可以放在主线程，只建议在测试环境下使用
//                    .fallbackToDestructiveMigration() // 如果数据库发生升级，那么Room会将当前数据库销毁重建
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build().apply {
                        instance = this
                    }
        }
        // version 1升2 新增bookLV 表
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("create table BookLV (id integer primary key autoincrement not null, name text not null, pages integer not null)")
            }
        }
        // version 2升3 bookLV 表新增author字段
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table BookLV add column author text not null default 'unknown'")
            }
        }
    }
}


class RoomViewModel: ViewModel(){
    // map和switchMap
    private val userLiveData = MutableLiveData<UserLV>()
    // map将UserLV类型的LiveData转换为任意其他类型的LiveData
    // 此处将user转换为一个字符串
    val userName: LiveData<String> = Transformations.map(userLiveData){
        user ->
        "${user.firstName} ${user.lastName}"
    }
    //    fun getUser(userId: String):LiveData<UserLV>{
//        // 每次调用都是返回一个新的LiveData，而userLiveData一直都是老的LiveData，因此无法观察数据的变化
//        return RepositoryLV.getUser(userId)
//    }
    // switchMap
    private val userIdLiveData = MutableLiveData<String>()
    val userLV: LiveData<UserLV> = Transformations.switchMap(userIdLiveData){
        userID -> RepositoryLV.getUser(userID)
    }
    // 每次调用getUser，都会userLV.switchMap执行，并调用我们编写的转换函数
    fun getUser(userId: String){
        userIdLiveData.value = userId
    }
}
class RoomViewModelFactory: ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RoomViewModel() as T;
    }

}