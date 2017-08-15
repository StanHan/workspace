package demo.db.hibernate;

import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import demo.db.hibernate.vo.Employee;
import demo.db.hibernate.vo.Employee2;

/**
 * 查询
 * @author hanjy
 *
 */
public class QueryDemo {

    public static void main(String[] args) {
        // testQuery();
        // testQuery2();
        // testQueryIn();
//        testCount();
        testQueryMultiColumn();
    }

    /**
     * count查询
     */
    static void testCount() {
        try (SessionFactory factory = HibernateUtils.getSessionFactory2();
                Session session = factory.getCurrentSession();) {

            Transaction transaction = session.getTransaction();
            transaction.begin();

            String sql = "Select count(1) from employee where last_name =? and age in (:ages) ";

            // Create Query object.
            NativeQuery nativeQuery = session.createNativeQuery(sql);
            Integer[] ages = { 31, 30, 34 };
            nativeQuery.setParameterList("ages", ages);
            nativeQuery.setParameter(1, "Su");

            String queryString = nativeQuery.getQueryString();
            System.err.println(queryString);
            // Execute query.
            Number count = (Number) nativeQuery.uniqueResult();
            System.err.println(count);

            // Commit data.
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * IN查询
     */
    static void testQueryIn() {
        try (SessionFactory factory = HibernateUtils.getSessionFactory();
                Session session = factory.getCurrentSession();) {

            Transaction transaction = session.getTransaction();
            transaction.begin();

            String sql = "Select * from employee where last_name =? and age in (:ages) order by id ";

            // Create Query object.
            NativeQuery<Employee2> nativeQuery = session.createNativeQuery(sql, Employee2.class);
            Integer[] ages = { 31, 30, 34 };
            nativeQuery.setParameterList("ages", ages);
            nativeQuery.setParameter(1, "Su");
            // Execute query.
            List<Employee2> employees = nativeQuery.getResultList();

            employees.forEach(System.out::println);

            // Commit data.
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询实体
     */
    static void testQuery() {
        try (SessionFactory factory = HibernateUtils.getSessionFactory();
                Session session = factory.getCurrentSession();) {

            // All the action with DB via Hibernate
            // must be located in one transaction.
            // Start Transaction.
            Transaction transaction = session.getTransaction();
            transaction.begin();

            // Create an HQL statement, query the object.
            // Equivalent to the SQL statement:
            // Select e.* from EMPLOYEE e order by e.EMP_NAME, e.EMP_NO
            String sql = "Select e from " + Employee.class.getName() + " e " + " order by e.id ";

            // Create Query object.
            Query<Employee> query = session.createQuery(sql);

            // Execute query.
            List<Employee> employees = query.getResultList();

            employees.forEach(System.out::println);

            // Commit data.
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询实体单个字段
     */
    static void testQuerySingleColumn() {
        try (SessionFactory factory = HibernateUtils.getSessionFactory();
                Session session = factory.getCurrentSession();) {

            // All the action with DB via Hibernate
            // must be located in one transaction.
            // Start Transaction.
            Transaction transaction = session.getTransaction();
            transaction.begin();

            // Create an HQL statement, query the object.
            // Equivalent to the SQL statement:
            // Select e.* from EMPLOYEE e order by e.EMP_NAME, e.EMP_NO
            String sql = "Select e.id from " + Employee.class.getName() + " e " + " order by e.id ";

            // Create Query object.
            Query<Integer> query = session.createQuery(sql);
            query.setMaxResults(2);

            // Execute query.
            List<Integer> employeeIds = query.getResultList();

            employeeIds.forEach(System.out::println);

            // Commit data.
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 查询实体多个字段
     */
    static void testQueryMultiColumn() {
        try (SessionFactory factory = HibernateUtils.getSessionFactory();
                Session session = factory.getCurrentSession();) {

            // All the action with DB via Hibernate
            // must be located in one transaction.
            // Start Transaction.
            Transaction transaction = session.getTransaction();
            transaction.begin();

            // Create an HQL statement, query the object.
            // Equivalent to the SQL statement:
            // Select e.* from EMPLOYEE e order by e.EMP_NAME, e.EMP_NO
            String sql = "Select e.id,e.age from " + Employee.class.getName() + " e " + " order by e.id ";

            // Create Query object.
            Query<Object[]> query = session.createQuery(sql);
            query.setMaxResults(2);

            // Execute query.
            List<Object[]> employeeIds = query.getResultList();

            for (Object[] objects : employeeIds) {
                Arrays.stream(objects).forEach(System.err::println);
            }

            // Commit data.
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
