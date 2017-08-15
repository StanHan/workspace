package demo.db.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import demo.db.hibernate.vo.Employee;
import demo.db.hibernate.vo.Employee2;

public class SaveDemo {

    public static void main(String[] args) {
//        demoPersist2();
        demoUpdate();
    }
    
    static void demoUpdate(){
        Transaction t = null;
        try (SessionFactory sessionFactory = HibernateUtils.getSessionFactory2();
                Session session = sessionFactory.openSession();) {// 从会话工厂获取一个session
            t = session.beginTransaction();

            String sql = "Select e from " + Employee2.class.getName() + " e " + " order by e.id ";

            // Create Query object.
            Query<Employee2> query = session.createQuery(sql);

            // Execute query.
            List<Employee2> employees = query.getResultList();
            
            for (Employee2 employee : employees) {
                employee.setAge(10+employee.getAge());
            }
            session.flush();

            t.commit();
            session.close();
            System.out.println("successfully saved");
        } catch (Exception e) {
            e.printStackTrace();
            // Rollback in case of an error occurred.
            t.rollback();
        }
    }

    static void demoPersist2() {
        Transaction t = null;
        try (SessionFactory sessionFactory = HibernateUtils.getSessionFactory2();
                Session session = sessionFactory.openSession();) {// 从会话工厂获取一个session
            t = session.beginTransaction();

            Employee2 e1 = new Employee2();
            e1.setId(10011);
            e1.setFirstName("Yii");
            e1.setLastName("Bai");

            Employee e2 = new Employee();
            e2.setId(10012);
            e2.setFirstName("Min");
            e2.setLastName("Su");

            session.persist(e1);
            session.persist(e2);

            t.commit();
            session.close();
            System.out.println("successfully saved");
        } catch (Exception e) {
            e.printStackTrace();
            // Rollback in case of an error occurred.
            t.rollback();
        }
    }

    static void demoPersist() {
        Transaction t = null;

        try (SessionFactory factory = HibernateUtils.getSessionFactory();
                Session session = factory.getCurrentSession();) {

            // creating transaction object
            t = session.beginTransaction();

            Employee e1 = new Employee();
            e1.setId(100);
            e1.setFirstName("Max");
            e1.setLastName("Su");

            session.persist(e1);// persisting the object

            t.commit();// transaction is committed
            session.close();

            System.out.println("successfully saved");
        } catch (Exception e) {
            e.printStackTrace();
            // Rollback in case of an error occurred.
            t.rollback();
        }
    }

}
