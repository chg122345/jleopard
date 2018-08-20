# jleopard
orm（bug提交：80588183@qq.com）
前言：代码写的很乱，只是实现了各种功能，没有去做优化，后续会有大量的修改优化。

 极速开发-
  快速入手-
    简单的逆向工程-
	  快速将数据库表生成javabean-
	    支持jdbc事物，每次对数据库更新操作都要提交事物-
	      支持多表连接，外键设置（陆续优化中）-
	       详情见开发文档
	  JLEOPARD
一.	jleopard快速上手：
1.	引入核心jar包 jleopard-xx.jar
2.	引入所依赖的jar包 ，常用到c3p0连接池，文件操作
3.	在类路径下创建配置文件，配置如下：
 （配置文件的头文件要写清楚，因为dtd我是放在自己的服务器上作为公共的dtd，便于维护。）
  数据源配置二选一 id固定为dataSource ， class为插件的完整类名。
  实体对象包一定要配 ，不然扫描不到对象。
  逆向工程只用一次 用的时候配就行 ，因为获取路径问题，所有要配置项目根径。
  包名与实体对象包保持一致，不然会出错。
  
	<?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE jleopard-configuration  PUBLIC "-// jleopard.org//DTD Config 1.0//EN"
		"http://www.jleopard.org/jleopard/jleopard.dtd">
	<jleopard-configuration>
		<config>
			<!--实体对象所在包-->
			<entityScan value="test.entity"></entityScan>
			<dev value="true"></dev>
		</config>
		<!--逆向工程配置  包要配置为完整的路径-->
		<generator>
	       		<target package="com.leopardframework.entity" project="/src/main/java/"/>
		</generator>
		<!--数据源配置-->
		<dataSource class="org.jleopard.jdbc.BaseDataSource" id="dataSource">
			<property name="driver" value="com.mysql.jdbc.Driver"/>
			<property name="url" value="jdbc:mysql://127.0.0.1:3306/jleopardDemo?characterEncoding=UTF-8"/>
			<property name="username" value="root"/>
			<property name="password" value="123"/>
		</dataSource>
	</jleopard-configuration>
 


二.	配置好环境后，配置entity类：
@Table 标志该类对应数据库的一张表 - value值为表名 ，不写则默认取类名作为表名。
@Column 对应表中的字段名 - value值为表中的字段名 ，不写则默认取变量名。
 IsPrimary 是否为主键 ，有三种类型： NO(不是主键，也是默认的属性)- YES(是主键)-
AUTOINCREMENT(是主键，且自增)-
AllowNull ( 是否允许为空，默认为false)-
relation （外键联系）

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
        SessionFactory factory=Factory.getSessionFactory("classpath:config.xml"); //获取session  传入我们的配置文件
        SqlSession session=factory.openSession();
            User user=new User();
            user.setId(10086);
            user.setName("JLeopard");
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
            GeneratorFactory factory=Factory.getGeneratorFactory("classpath:config.xml");  // 开启Generator工厂
            try {
               factory.openGenerator();    //执行逆向工程
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	
四.	方法介绍，分页查询：

    @Test
    public void PageTest(){
        /*目前仅封装了我们开发中常用的一些数据信息。
        获取分页信息 ：getPage();  // 获取当前查询的页数
        getTotalPages(); //获取总页数
        getPageSize();   //获取每页显示的数据数量
        getTotalRows();  //获取总记录数
        getList();       //获取目标数据，也就是我们要查询的数据*/
        SessionFactory factory=Factory.getSessionFactory("classpath:config.xml");  //获取session工厂
        SqlSession session=factory.openSession();  //打开session连接 开始操作
        try {
            PageInfo temp=session.Get(User.class,3,10);  //分页查询开始 用封装好的pageInfo接收查询结果
            session.Stop();
            session.Close();
            List<User> users=temp.getList();
            for (User u :users){
                System.out.println(u.toString());
            }
            temp.description();
            //  System.out.println(" 结果："+temp);
        } catch (SqlSessionException e) {
            e.printStackTrace();
        }
    }

	
五.	与Spring整合配置（只需要配置数据源和sqlSessionFactoryBean）：

   1.配置文件详情：
   
	<bean id="dataSource" class="org.jleopard.jdbc.BaseDataSource">
    	<property name="driver" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://127.0.0.1:3306/jleopardDemo?characterEncoding=UTF-8"/>
        <property name="username" value="root"/>
        <property name="password" value="123"/>
        </bean>
        <bean id="sqlSessionFactoryBean" class="org.jleopard.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
    	<property name="entityPackage" value="com.leopardframework.entity"></property>
    	<property name="useColumnLabel" value="false"></property>
    	<property name="autoCommit" value="true"></property>
    	<property name="useGeneratedKeys" value="false"></property>
    	<property name="dev" value="true"></property>
   	</bean>
	
   2.注入SqlSessionFactoryBean
   
   	@Autowired
	private SqlSessionFactoryBean sqlSessionFactoryBean;
	
   3.获取SqlSession
   
   	SqlSession session =sqlSessionFactoryBean.getSessionFactory().openSession();
	
   4.极速开发
   
   	User user = new User();
	user.setName("测试spring");
	user.setId(9441);
	user.setAddress("江西南昌");
	user.setPhone("10086");
	
   	int temp = session.Save(user);
		session.Commit();
		session.Stop();
