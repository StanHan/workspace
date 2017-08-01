package demo.java.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultSetUtils {

    Logger log = LoggerFactory.getLogger(ResultSetUtils.class);

    /**
     * 转换ResultSet对象为List对象
     * 
     * @param resultSet
     * @param labels
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> buildResultSet(ResultSet resultSet, Set<String> labels)
            throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (String label : labels) {
                map.put(label, resultSet.getObject(label));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 转换ResultSet对象为List对象
     * 
     * @param resultSet
     * @return list(0) = labels, 其他的都是values
     * @throws SQLException
     */
    public static List<Object[]> buildResultSetWithLabel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnLabels = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnLabels[i] = metaData.getColumnLabel(i + 1);
        }
        List<Object[]> list = null;
        if(resultSet.last()){
            int rowNumber = resultSet.getRow();
            list = new ArrayList<Object[]>(rowNumber +1);
            list.add(columnLabels);
            resultSet.beforeFirst();
            while (resultSet.next()) {
                Object[] columnValues = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    columnValues[i] = resultSet.getObject(i + 1);
                }
                list.add(columnValues);
            }
            resultSet.beforeFirst();
        };
        
        return list;
    }

    /**
     * 将ResultSet转成list
     * 
     * @param resultSet
     * @param batch
     * @return
     * @throws SQLException
     */
    public static List<Object[]> buildResultSet(ResultSet resultSet, int initialCapacity) throws SQLException {
        List<Object[]> list = new ArrayList<>(initialCapacity);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            Object[] array = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                array[i] = resultSet.getObject(i + 1);
            }
            list.add(array);
        }
        return list;
    }

}
