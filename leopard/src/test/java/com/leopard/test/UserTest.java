package com.leopard.test;

import com.leopard.entity.User;
import com.leopardframework.core.SqlBuilder;
import com.leopardframework.core.annotation.Column;
import com.leopardframework.core.annotation.Table;
import com.leopardframework.core.enums.Primary;
import com.leopardframework.core.get.PrimaryKeyName;
import com.leopardframework.core.session.Session;
import com.leopardframework.core.session.sessionFactory.SessionFactory;
import com.leopardframework.core.sql.*;
import com.leopardframework.core.util.TableUtil;
import com.leopardframework.page.PageInfo;
import org.junit.Test;

import java.lang.reflect.Field;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class UserTest {

    @Test
    public void TTTT(){
        try {
           Class<?> cls= Class.forName("com.leopard.entity.User");
            Table table=cls.getAnnotation(Table.class);
            String tableName=table.value();
            if(tableName==null||"".equals(tableName)){
                tableName=cls.getSimpleName();
            }
            System.out.println(tableName);
            String columnName=null;
            Primary pk=null;
            boolean un;
            Field[] fields =cls.getDeclaredFields();
            for(Field field:fields){
                Column column=field.getDeclaredAnnotation(Column.class);
                columnName=column.value().toUpperCase();
                pk=column.isPrimary();
                un=column.unique();
                if(columnName==null||"".equals(columnName)){
                    columnName=field.getName().toUpperCase();
                }if(pk==Primary.NO){
                      //  continue;
                }
                System.out.println(field.getType()==Long.class);
                System.out.println(columnName+"   "+ pk + " "+un);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sqlTest(){
        User user=new User();
        user.setId(111);
        user.setName("AAAAA");
        user.setPhone("15770549440");
       // SqlBuilder SS=new SqlBuilder();
        Map<String,List<Object>> sqlv= SqlBuilder.getSaveSqlValues(user);
        List<Object> values=null;
        for (Map.Entry<String,List<Object>> sql : sqlv.entrySet()) {
            System.out.println(sql.getKey());
            values = sql.getValue();
        }
        for(int i=0;i<values.size();++i){
            System.out.println(values.get(i)+"  "+ (i+1));
        }
     //   String sql=sqlv.
       // System.out.println(sql);
    }

    @Test
    public void SaveUserTest(){
        Session session=SessionFactory.openSession();
        User u=new User();
        u.setId(6);
        u.setName("leopard");
        u.setPhone("15770549440");
        u.setAddress("China");
        try {
            int i=session.Save(u);
            System.out.println(" Success "+i);
            session.closeSession();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void SaveUserTest2(){
        Session session=SessionFactory.openSession();
        List<User> users=new ArrayList<>();
        for(int id=10;id<20;id++){
            users.add(new User(id,"leopard","15770549440"));
        }
        int temp;
        try {
            temp=session.SaveMore(users);
            System.out.println(temp);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void getPK(){
        Session session=SessionFactory.openSession();
        try {
           int temp=session.Delete(User.class,1,2,3,4,5,6,10085);
           System.out.println(" 结果："+temp);
            session.closeSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(PrimaryKeyName.getPrimaryKeyName(User.class));
    }

    @Test
    public void Delobj(){
        Session session=SessionFactory.openSession();
        User user=new User();
        user.setId(9);
       user.setPhone("15770549440");
       user.setName("leopard");
        try {
            int temp=session.Delete(user);
            System.out.println(" 结果："+temp);
            session.closeSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void InsertSqlTest(){
        User user=new User();
     //   user.setId(9);
        user.setPhone("15770549440");
        user.setName("leopard");
      //  user.setAddress("China");
       // System.out.println("Sql value："+FieldUtil.getAllColumnName_Value(user));
        Sql insert=new SelectSql(user);

        System.out.println("Sql 语句："+insert.getSql());
        System.out.println("Sql value："+insert.getValues());
       /* for(int i=0;i<insert.getValues().size();++i){

            System.out.print(insert.getValues().get(i)+" ");
        }*/


    }

    @Test
    public void tableUTest(){

        System.out.println(TableUtil.getAllTableName("com.leopardframework").size());
        URL xmlpath = this.getClass().getClassLoader().getResource("");
        System.out.println(xmlpath);
    }

    @Test
    public void newTest(){
        Session session=SessionFactory.openSession();
        User user=new User();
      //  user.setId(100);
        user.setPhone("15770549440");
       // user.setName("newleopard");
      //  user.setAddress("GXF");
        try {
            int temp=session.Save(user);
           /* for (User u :temp){
                System.out.println(u.toString());
            }*/
            System.out.println(" 结果："+temp);
            session.closeSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void pageTest(){
        Session session=SessionFactory.openSession();
        try {
            PageInfo temp=session.Get(User.class,3,8);
            List <User>users=temp.getList();
            for (User u :users){
                System.out.println(u.toString());
            }
            temp.description();
           //  System.out.println(" 结果："+temp);
            session.closeSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
