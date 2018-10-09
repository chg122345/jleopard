# Jleopard-MVC
#### bug提交(jleopard@126.com)
* 版本
	* [x] <a href="#v1" target="_self">v1.0.0</a>


	
  * <span id = "v1">v1.0.0功能简介</span>
  	* 开始使用
  		* 引入依赖包(maven依赖)
  		```
  		<dependency>
		   <groupId>org.jleopard</groupId>
		   <artifactId>jleopard-mvc</artifactId>
		   <version>1.0.0</version>
		</dependency>
  		```
  		* 配置`web.xml`
  		```
  		<servlet>
		    <servlet-name>dispatcherServlet</servlet-name>
		    <servlet-class>org.jleopard.mvc.servlet.DispatcherServlet</servlet-class>
		</servlet>
	       <servlet-mapping>
	    	    <servlet-name>dispatcherServlet</servlet-name>
	   	    <url-pattern>/</url-pattern>
	      </servlet-mapping>
  		```
    * [x] `Spring MVC`风格化
        1.使用`@Controller`注解标记
		2. 请求映射方法自定义 `@RequestMapping(value = "",method = Method.POST)`(默认允许所有请求方法)
		3. 自动初始化方法内参数(复杂类型自动匹配字段名赋值,简单类型用`@RequestParam`注解，上传文件用`MultipartFile`接收)
		4. 支持返回json数据，只需标注`@RenderJson`注解即可
		```java
        @Controller
        public class TableController {
          @Inject
          private DinnerTableService service;
                                       				   
          @RequestMapping("/table")
          @RenderJson
          public List<DinnerTable> list(@RequestParam("name") String name){   
            return service.query();
          }
                                        				   
           @RequestMapping(value = "/table1",method = Method.POST)
           @RenderJson
           public DinnerTable table(DinnerTable t){   
            System.out.println("获取到的参数-->" + t);
            return t;
          }
        } 				            
        ```     
    * [x] 实现`IOC`，`DI`功能
       1. 在类上标注`@Service`，`@Controller`,`@Component`注解就会扫描添加到bean容器内
       2. 运用时只需在字段上标注`@Inject`注解即可完成自动注入
	    ```java
		    
		    @Service
		    public class OrderDetailService {
		    
		        @Inject
		        private OrderDetialDao dao;
		    
		        public int add(OrderDetail od) {
		            return dao.insert(od);
		    
		        }
		    
		        public List<OrderDetail> query() {
		            return dao.select();
		        }
		    
		        public List<OrderDetail> findByOrderid(Integer id) {
		            OrderDetail var1 = new OrderDetail();
		            var1.setId(id);
		            return dao.select(var1);
		        }
		    
		    }		 
        ```
		    
	* [x] 拦截器实现
		* 创建拦截器(实现`Interceptor`接口的preHandle方法)
		
		```
		
			public class TInter implements Interceptor {

			    @Override
			    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, InterceptorRegistration register) throws Exception {
				return false;
			    }
			}
		```
		
		* 使用拦截器(在controller类上或者要映射的方法上标注`@Before`注解，传入自定义拦截器，如果某方法不需要使用拦截器，则用`@Clear`注解清除拦截器)
		```java
        @Controller
        @Before(TInter.class)
        public class TableController {
        
            @Inject
            private DinnerTableService service;
        
            @RequestMapping("/table")
            @RenderJson
            @Clear(TInter.class)
            public List<DinnerTable> list(@RequestParam("name") String name, @RequestParam("pass") String pass){
                System.out.println(name+pass);
                return service.query();
            }
        
            @RequestMapping(value = "/tt",method = Method.GET)
            @RenderJson
            @Clear(TInter.class)
            public Integer list1(@RequestParam("name") String name, @RequestParam("pass") Integer pass){
                System.out.println(name+pass);
                return pass;
            }
        
            @RequestMapping(value = "/table1",method = Method.POST)
            @RenderJson
            public DinnerTable table(DinnerTable t){
                System.out.println("获取到的参数-->" + t);
                service.add(t);
                return t;
            }
        }
		```
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		