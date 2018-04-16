# leopard
orm（出错提示 ： 80588183@qq.com）
前言：代码写的很乱，只是实现了各种功能，没有去做优化，后续会有大量的修改优化。
  本次仅采用简单的工厂模式，单例模式进行开发的。

 极速开发-
  快速入手-
    简单的逆向工程-
	  快速将数据库表生成javabean-
	    支持jdbc事物，每次对数据库更新操作都要提交事物-
	      暂不支持多表连接，外键设置（陆续优化中）-
	  
	  LEOPARD
一.	leopard快速上手：
1.	引入核心jar包 leopard-orm.jar
2.	引入所依赖的jar包 ，常用到c3p0连接池，文件操作
3.	在类路径下创建配置文件，配置如下：
 （配置文件的头文件要写清楚，因为dtd我是放在自己的服务器上作为公共的dtd，便于维护。）
  数据源配置二选一 id固定为dataSource ， class为插件的完整类名。
  实体对象包一定要配 ，不然扫描不到对象。
  逆向工程只用一次 用的时候配就行 ，因为获取路径问题，所有要配置项目根径。
  包名与实体对象包保持一致，不然会出错。
  <?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE leopard-config  PUBLIC "-//leopard.com//DTD Config 1.0//EN"
        "http://120.78.131.95/leopard/config/leopard.dtd">
<leopard-config>
   <!-- 数据源的配置-->
   <bean class="com.leopardframework.plugins.DBPlugin" id="dataSource">
        <property name="driver" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://127.0.0.1:3306/myshop?characterEncoding=UTF-8"/>
        <property name="username" value="root"/>
        <property name="password" value="chg122345"/>
    </bean>
   <!-- c3p0数据源的配置-->
    <!--<bean class="com.leopardframework.plugins.c3p0.C3p0Plugin" id="dataSource">
        <property name="driver" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://127.0.0.1:3306/myshop?characterEncoding=UTF-8"/>
        <property name="username" value="root"/>
        <property name="password" value="chg122345"/>
        <property name="maxPoolSize" value="100"/>
        <property name="minPoolSize" value="20"/>
    </bean>-->
    <!--实体对象所在包-->
    <entity-package value="com.leopardframework.test.entity"/>
    <!--逆向工程配置  包要配置为完整的路径-->
<generator>
    <target package="src.main.java.com.leopardframework.test.entity"/>
</generator>
</leopard-config>
 


二.	配置好环境后，配置entity类：
@Table 标志该类对应数据库的一张表 - value值为表名 ，不写则默认取类名作为表名。
@Column 对应表中的字段名 - value值为表中的字段名 ，不写则默认取变量名。
 IsPrimary 是否为主键 ，有三种类型： NO(不是主键，也是默认的属性)- YES(是主键)-
AUTOINCREMENT(是主键，且自增)-
AllowNull ( 是否允许为空，默认为false)-
isForeginKey （外键 暂不支持）-

@Table("user")
public class User{

    @Column(isPrimary = Primary.YSE)
    private long id;

    @Column(value = "name",allowNull = true)
    private String name;

    @Column
    private String phone;

    @Column
    private String address;
	//省略getset方法 构造方法
  }
 


三.	方法介绍，极速开发模式：

    @Test
    public void Test(){
        Factory factory=new SessionFactory("classpath:config.xml");  //获取session  传入我们的配置文件
        SqlSession session=factory.openSession();
            User user=new User();
            user.setId(10086);
            user.setName("Leopard");
            user.setPhone("10010");
            user.setAddress("China");
            List list=new ArrayList();
            list.add(user);
        try {   //所有操作均有SqlSessionException异常
            session.Save(user);  //传一个具体的对象
            session.SaveMore(list);  //多个对象放入list 好比批量操作，实际上并没有用到批量
            session.Delete(user); //删除条件即为对象的数据
            session.Delete(User.class, 10086, 10010, 10000); //根据唯一主键删除数据 ,传一个或多个主键值
            session.Update(user,10086);//根据主键修改数据  目标数据是该对象里的数据
            session.Get(User.class); // 查询所有数据
            session.Get(user);   //查询单条数据 查询条件即为对象的数据  如果匹配到多条数据，则只返回第一条
            session.Get(User.class,10000,10086);// 一样按主键查找
            session.Get(User.class,"where id=? order by id desc",10086);  //自定义条件查询 动态sql
            session.Get(User.class,1,5);  //分页查询  查询第一页数据  每页显示5 条数据 PageInfo来接收（下问文详细介绍）
            session.Get("","");  //自定义动态sql 返回的是结果集


            session.Commit();  //每一次对更新数据库操作都要提交事物  不然数据不会写入数据库
            session.Stop();  //每执行完一次都要将其暂停
            session.Close();  // 关闭此次Session 下次要用时要重新获取
           
        } catch (SqlSessionException e) {
            e.printStackTrace();
        }

    }

    逆向工程生成JavaBean   简单两行代码解决
    @Test
        public void GeneratorTest(){
            Factory factory=new GeneratorFactory("classpath:config.xml");  // 开启Generator工厂
            try {
               factory.openGenerator();    //执行逆向工程
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
 
/****----------------分页查询详细介绍------------------***/
    @Test
    public void PageTest(){
        Factory factory=new SessionFactory("classpath:config.xml");  //获取session工厂
        SqlSession session=factory.openSession();           //打开session连接 开始操作
        try {
            PageInfo temp=session.Get(User.class,3,10);      //分页查询开始 用封装好的pageInfo接收查询结果
            session.Stop();                                  //传三个参数分别代为 ：1.实体对象类，2.查询第几页，3.每页显示多少条数据
            session.Close();                                 //目前仅封装了我们开发中常用的一些数据信息。
                                                             //获取分页信息 ：getPage();  // 获取当前查询的页数
                                                             //            getTotalPages(); //获取总页数
                                                             //            getPageSize();   //获取每页显示的数据数量
                                                             //            getTotalRows();  //获取总记录数
                                                             //            getList();       //获取目标数据，也就是我们要查询的数据
            List<User> users=temp.getList();
            for (User u :users){
                System.out.println(u.toString());
            }
            temp.description();
        } catch (SqlSessionException e) {
            e.printStackTrace();
        }

    }
