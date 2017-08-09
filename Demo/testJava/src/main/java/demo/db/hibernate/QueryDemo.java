package demo.db.hibernate;

import java.util.List;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import demo.db.hibernate.vo.Employee;

public class QueryDemo {

    public static void main(String[] args) {
//        testQuery();
        testQuery2();
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

            for (Employee emp : employees) {
                System.out.println(emp.toString());
            }

            // Commit data.
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 查询实体部分字段
     */
    static void testQuery2() {
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

            // Execute query.
            List<Integer> employeeIds = query.getResultList();

            for (Integer id : employeeIds) {
                System.out.println(id);
            }

            // Commit data.
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
