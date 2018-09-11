# jleopard
（bug提交：jleopard@126.com）
  [JLeopard社区](http://www.jleopard.org)	

### 声明： 目前jleopard仅支持MySQL数据库

**前言**：代码写的很乱，只是实现了各种功能，没有去做优化，后续会有大量的修改优化。

**jleopard优势**
 1. 真正轻量级，小到仅100kb左右。
 2. 极易上手，不再需要手写sql。
 3. 功能强大，自动生成sql，除了常用的增删改查还支持逆向工程，自动建表 ，多表外键关联，分页查询。
 
**JLEOPARD**

**jleopard快速上手：**

**一 基本配置**
1. 引入核心jar包 jleopard-xx.jar。
2. maven开发引入依赖

	   <dependency>
	      <groupId>org.jleopard</groupId>
	      <artifactId>jleopard</artifactId>
	      <version>2.1.0</version>
	   </dependency>

3. 引入所依赖的jar包 ，如数据库驱动包，数据库连接池包等。
4. 创建配置文件，配置详情如下：
	（1）数据源配置id固定为dataSource ， class为插件的完整类名。
	（2）实体对象包一定要配 ，不然扫描不到对象。
  	（3）逆向工程只用一次 用的时候配就行。
	
<?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE jleopard-configuration  PUBLIC "-// jleopard.org//DTD Config 1.0//EN"
		"http://www.jleopard.org/dtd/jleopard.dtd">
	<jleopard-configuration>
		<config>
			<!--实体对象所在包 如：org.jleopard.demo.entity-->
			<entityScan value="org.jleopard.demo.entity"></entityScan>
			<dev value="true"></dev>
		</config>
		<!--逆向工程配置  包要配置为完整的路径 maven工程和web工程项目路径不同-->
		<generator>
	       		<target package="org.jleopard.demo.entity" project="/src/main/java/"/>
		</generator>
		<!--数据源配置 id固定为dataSource-->
		<dataSource class="org.jleopard.jdbc.BaseDataSource" id="dataSource">
			<property name="driver" value="com.mysql.jdbc.Driver"/>
			<property name="url" value="jdbc:mysql://127.0.0.1:3306/jleopardDemo?characterEncoding=UTF-8"/>
			<property name="username" value="root"/>
			<property name="password" value="123"/>
		</dataSource>
	</jleopard-configuration>
 


**二 Java注解配置**

1. @Table 标志该类对应数据库的一张表 - value值为表名 ，不写则默认取类名作为表名。

2. @Column 对应表中的字段名 
	- value值为表中的字段名 ，不写则默认取变量名。
	- id 是否为主键 ，有三种类型： 
	-  NO(不是主键，也是默认的属性)
	-  YES(是主键)
	-  AUTOINCREMENT(是主键，且自增)
	-  AllowNull ( 是否允许为空，默认为false)
	-  join （外键联系 默认为空则不是外键 如：join="Address.class" --> 表示关联Address类对应的表）
3. @OneToMany 一对多关系
	- column 关联另一张表的外键字段名 
	- join关联的表对应的对象类
	
```	

      @Table("user")
      public class User{
    
      @Column(id = EnumId.YSE)
      private long id;

      @Column(value = "name",allowNull = true)
      private String name;

      @Column
      private String phone;

      @OneToMany( join="Address.class",column="user_id")
      private List<Address> address;
      // 表示address表的user_id字段为外键对应该User类，关联着Address这个实体类对应表的主键。
    
	  //省略getset方法 构造方法
      }
 ```


**三 方法介绍，极速开发模式**

  **1 获取SqlSessionFactory**
  
  	SqlSessionFactory factory=SqlSessionFactory.Builder.build("classpath:config.xml"); //获取sessionFactory  传入我们的配置文件路径
 
 **2 获取sqlSession**
  	
        SqlSession session=factory.openSession();
        
**3 封装的方法使用**


         User user=new User();
         user.setId(10086);
         user.setName("JLeopard");
         user.setPhone("10010");
         user.setAddress("China");
         List list=new ArrayList();
         list.add(user);
         
        try {  //所有操作均有SqlSessionException异常
            session.save(user);  //传一个具体的实例对象
            
            session.saveMore(list);  //多个实例对象放入list 批量操作存入数据库
            
            session.delete(user); //删除条件即为实例对象的数据 如 ：上述user对象 删除的条件即为: delete from user表 where id = 10086 and name= 'jLeopard' and phone = ‘10010’ and address = 'China'
            
            session.delete(User.class, 10086, 10010, 10000); //根据唯一主键删除数据 ,传一个或多个主键值 如：delete from user表 where id in (10086,10010,10000)
            
            session.update(user,10086);//根据主键修改数据  目标数据是该对象里的数据 如： update user表 set name = xx（user对象的数据，该字段为空则跳过，不修改数据）where id = 10086
            
            session.get(User.class); // 查询所有user表中的数据 return List<T>
            
            session.get(user);   //查询单条数据 查询条件即为对象的数据  如果匹配到多条数据，则只返回第一条.。上述user对象 查询的条件即为: select 所有字段名  from user表 where id = 10086 and name= 'jLeopard' and phone = ‘10010’ and address = 'China'
   
            session.getById(User.class,10000,10086);// 按主键查找 select xx from user表 where id in ( 10000,10086)
            
            session.getByWhere(User.class,"where id=? order by id desc",10086);  //自定义条件查询 动态参数
            
            session.getToPage(User.class,1,5);  //分页查询  查询第一页数据  每页显示5 条数据 PageInfo来接收（下问文详细介绍）
            
            session.getBySql("select * from user where id = ? and name =?",10010,"jLeopard");  //自定义动态sql 返回的是ResultSet结果集。 


            session.commit();  //每一次对更新数据库操作都要提交事物  不然数据不会写入数据库
            
            session.stop();  //每执行完一次都要将其暂停
            
            session.close();  //关闭此次sqlSession 下次要用时要重新获取
            
        } catch (SqlSessionException e) {
            e.printStackTrace();
        }

**4 逆向工程生成JavaBean   简单两行代码解决**

            GeneratorFactory factory=GeneratorFactory.Builder.build("classpath:config.xml");  // 开启Generator工厂
            try {
               factory.openGenerator();    //执行逆向工程
            } catch (Exception e) {
                e.printStackTrace();
            }
  
	
**四 方法介绍，分页查询**


    public void PageTest(){
        /*目前仅封装了我们开发中常用的一些数据信息。
        获取分页信息 ：getPage();  // 获取当前查询的页数
        getTotalPages(); //获取总页数
        getPageSize();   //获取每页显示的数据数量
        getTotalRows();  //获取总记录数
        getList();       //获取目标数据，也就是我们要查询的数据*/
        SqlSessionFactory factory=SqlSessionFactory.Builder.build("classpath:config.xml");  //获取session工厂
        SqlSession session=factory.openSession();  //打开session连接 开始操作
        try {
            PageInfo temp=session.getToPage(User.class,3,10,"where sex = ?", "男");  //分页查询开始,查询性别为男的并分页 用封装好的pageInfo接收查询结果。 查询user表 第三页 每页显示10条数据。 
            session.stop();
            session.close();
        } catch (SqlSessionException e) {
            e.printStackTrace();
        }
    }

	
**五 与Spring整合配置（只需要配置数据源和sqlSessionFactoryBean）**

1.配置文件详情：
   
  
	<bean id="dataSource" class="org.jleopard.jdbc.BaseDataSource"> <!--数据源配置 这里我配置的是我自己写的数据源，这里可以配置所有spring支持的数据源-->
    	<property name="driver" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://127.0.0.1:3306/jleopardDemo?characterEncoding=UTF-8"/>
        <property name="username" value="root"/>
        <property name="password" value="123"/>
        </bean>
        <!--配置sqlSessionFactoryBean 引入数据源，配置实体类扫描包等-->
        <bean id="sqlSessionFactoryBean" class="org.jleopard.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
    	<property name="entityPackage" value="org.jleopard。demo.entity"></property>
    	<property name="useColumnLabel" value="false"></property>
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
	
   	int temp = session.save(user);
		session.commit();
		session.stop();
		session.close();
