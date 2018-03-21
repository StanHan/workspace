package demo.hadoop.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor.Builder;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;

public class Client {

    public static Configuration configuration = HBaseConfiguration.create();

    static {
        configuration.set("hbase.zookeeper.quorum", "10.240.1.181,10.240.1.182");// zookeeper地址
        configuration.set("hbase.zookeeper.property.clientPort", "2181");// zookeeper端口
        configuration.set("zookeeper.znode.parent", "/hbase");
    }
    // public static Configuration configuration = new Configuration();

    public static void main(String[] args) throws IOException {
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("table1"));
        try {
            // Use the table as needed, for a single operation and a single
            // thread
        } finally {
            table.close();
            connection.close();
        }

        System.out.println("Usage: hadoop jar Test.jar test.hadoop.hbase.Client ...");
        if (args == null || args.length == 0) {
            System.out.println("--------------------select options--------------------");
            System.out.println("createTable tableName family1;family2...");
            System.out.println("listTables");
            System.out.println("insert tableName rowKey family1:qualifier1=value1;family2:qualifier2=value2...");
            System.out.println("queryDataByRowKey tableName rowKey");
            System.out.println("listData tableName");
            System.out.println("dropTable tableName");
            System.out.println("deleteByRowkey tableName rowKey");
            System.exit(-1);
        }

        Client client = new Client();
        String type = args[0];
        if ("createTable".equals(type)) {
            String tableName = args[1];
            String familyNames = args[2];
            client.createTable(tableName, familyNames.split(";"));
        }
        if ("listTables".equals(type)) {
            client.listTables();
        }
        if ("insert".equals(type)) {
            String tableName = args[1];
            String rowKey = args[2];
            String keyValues = args[3];
            client.insert(tableName, rowKey, keyValues.split(";"));
        }
        if ("queryDataByRowKey".equals(type)) {
            String tableName = args[1];
            String rowKey = args[2];
            client.queryDataByRowKey(tableName, rowKey);
        }
        if ("listData".equals(type)) {
            String tableName = args[1];
            client.listData(tableName);
        }
        if ("dropTable".equals(type)) {
            String tableName = args[1];
            client.dropTable(tableName);
        }
        if ("deleteByRowkey".equals(type)) {
            String tableName = args[1];
            String rowKey = args[2];
            client.deleteByRowkey(tableName, rowKey);
        }
        System.exit(0);
    }

    /**
     * 创建表命名空间
     * 
     * @param namespace
     * @throws IOException
     */
    public void createNamespace(String namespace) throws IOException {
        Connection connection = null;
        Admin admin = null;
        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
            Builder builder = NamespaceDescriptor.create(namespace);
            NamespaceDescriptor namespaceDescriptor = builder.build();
            admin.createNamespace(namespaceDescriptor);
        } finally {
            if (admin != null) {
                admin.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * 创建HBase表
     * 
     * @param tableName
     *            <table namespace>:<table qualifier>
     * @param familyNames
     * @throws IOException
     */
    public void createTable(String tableName, String[] familyNames) throws IOException {

        if (tableName == null || "".equals(tableName)) {
            throw new IllegalArgumentException("tableName can't be null");
        }

        Connection connection = null;
        Admin admin = null;

        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
            TableName tname = TableName.valueOf(tableName);

            HTableDescriptor hTableDescriptor = new HTableDescriptor(tname);

            for (String familyName : familyNames) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(familyName);
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            System.out.println("HTableDescriptor=" + hTableDescriptor.toString());
            admin.createTable(hTableDescriptor);

            System.out.println("table " + tname.getNameAsString() + " is created.");
        } finally {
            if (admin != null) {
                admin.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * 打印出HBase中所有的表
     * 
     * @throws IOException
     */
    public void listTables() throws IOException {
        Connection connection = null;
        Admin admin = null;

        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
            TableName[] tableNames = admin.listTableNames();

            for (TableName tableName : tableNames) {
                System.out.println(tableName.getNameAsString());
            }

        } finally {
            if (admin != null) {
                admin.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * 插入数据
     * 
     * @param tableName
     * @param rowKey
     * @param keyValues
     *            [family:qualifier=value...]
     * @throws IOException
     */
    public void insert(String tableName, String rowKey, String[] keyValues) throws IOException {
        Connection connection = null;
        Table table = null;

        try {
            connection = ConnectionFactory.createConnection(configuration);
            TableName tname = TableName.valueOf(tableName);
            table = connection.getTable(tname);

            Put put = new Put(Bytes.toBytes(rowKey));
            for (String keyValue : keyValues) {
                String[] aa = keyValue.split("=");
                String familyColumn = aa[0];
                String value = aa[1];
                String[] bb = familyColumn.split(":");
                String familyName = bb[0];
                String qualifier = bb[1];

                put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            }

            table.put(put);

        } finally {
            if (table != null) {
                table.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * 根据rowkey查表数据
     * 
     * @param tableName
     * @param rowKey
     * @throws IOException
     */
    public void queryDataByRowKey(String tableName, String rowKey) throws IOException {
        Connection connection = null;
        Table table = null;

        try {
            connection = ConnectionFactory.createConnection(configuration);
            TableName tname = TableName.valueOf(tableName);
            table = connection.getTable(tname);

            Get get = new Get(Bytes.toBytes(rowKey));

            Result result = table.get(get);
            System.out.println("----------------------begin-------------------------");
            if (result.isEmpty()) {
                System.out.println("result is empty.");
            }

            System.out.println(result.toString());

            List<Cell> cellList = result.listCells();
            for (Cell cell : cellList) {
                System.out.println("CellKey=" + CellUtil.getCellKeyAsString(cell));
                System.out.println("TypeByte=" + cell.getTypeByte());
                System.out.println("SequenceId=" + cell.getSequenceId());
                System.out.println("Family="
                        + Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()));
                System.out.println("Qualifier=" + Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
                        cell.getQualifierLength()));
                System.out.println(
                        "Value=" + Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                System.out
                        .println("Row=" + Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength()));
            }

            System.out.println("----------------------end-------------------------");
        } finally {
            if (table != null) {
                table.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * 查找数据
     * 
     * @param tableName
     * @param startRowKey
     * @param endRowKey
     * @param limit
     * @param conditions
     * @param operator
     * @param columns
     * @throws IOException
     */
    public void queryData(String tableName, String startRowKey, String endRowKey, int limit, List<String[]> conditions,
            FilterList.Operator operator, String columns) throws IOException {

        Connection connection = null;
        Table table = null;

        connection = ConnectionFactory.createConnection(configuration);
        TableName tablename = TableName.valueOf(tableName);
        table = connection.getTable(tablename);
        Scan scan = new Scan();
        if (startRowKey != null && !startRowKey.isEmpty()) {
            scan.setStartRow(Bytes.toBytes(startRowKey));
        }
        if (endRowKey != null && !endRowKey.isEmpty()) {
            scan.setStopRow(Bytes.toBytes(endRowKey + "0"));
        }
        // 仅取行键过滤器
        KeyOnlyFilter keyOnlyFilter = null;

        // 解析需要查询的列族或列
        if (columns != null && !columns.isEmpty()) {
            if (columns.equalsIgnoreCase("ROW")) {
                keyOnlyFilter = new KeyOnlyFilter();
            } else {
                String[] columnArray = columns.split(";");
                for (String column : columnArray) {
                    if (column.contains(":")) {
                        String[] family_qualifier = column.split(":");
                        scan.addColumn(Bytes.toBytes(family_qualifier[0]), Bytes.toBytes(family_qualifier[1]));
                    } else {
                        scan.addFamily(Bytes.toBytes(column));// 单列族
                    }
                }
            }
        }

        FilterList filterList = null;
        // 如果带过滤条件时，将所有的过滤条件都加入
        if (conditions != null && conditions.size() > 0) {

            // 传入的过滤条件之间的关系，AND或OR关系
            if (operator != null) {
                filterList = new FilterList(operator);
            } else {
                filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
            }

            SingleColumnValueFilter singleColumnValueFilter = null;
            String familyName;
            String qualifierName;
            String compareOp;
            String value;
            CompareFilter.CompareOp compareEnum = null;

            for (String[] condition : conditions) {
                if (condition.length == 4) {
                    familyName = condition[0];
                    qualifierName = condition[1];
                    compareOp = condition[2];
                    value = condition[3];

                    switch (compareOp) {
                    case ">=":
                        compareEnum = CompareFilter.CompareOp.GREATER_OR_EQUAL;
                        break;
                    case ">":
                        compareEnum = CompareFilter.CompareOp.GREATER;
                        break;
                    case "<=":
                        compareEnum = CompareFilter.CompareOp.LESS_OR_EQUAL;
                        break;
                    case "<":
                        compareEnum = CompareFilter.CompareOp.LESS;
                        break;
                    case "=":
                        compareEnum = CompareFilter.CompareOp.EQUAL;
                        break;
                    case "!=":
                        compareEnum = CompareFilter.CompareOp.NOT_EQUAL;
                        break;
                    default:
                        compareEnum = CompareFilter.CompareOp.NO_OP;
                        break;
                    }

                    singleColumnValueFilter = new SingleColumnValueFilter(Bytes.toBytes(familyName),
                            Bytes.toBytes(qualifierName), compareEnum, new SubstringComparator(value));
                    singleColumnValueFilter.setFilterIfMissing(true);
                    filterList.addFilter(singleColumnValueFilter);
                }
            }
        }
        if (keyOnlyFilter != null) {
            filterList.addFilter(keyOnlyFilter);
        }
        if (filterList != null) {
            scan.setFilter(filterList);
        }

        // scan.setCaching(100);

        ResultScanner resultScanner = table.getScanner(scan);
        List<LinkedHashMap<String, String>> resultList = new ArrayList<LinkedHashMap<String, String>>();
        // 获取结果集
        if (limit > 0) {// 数量限制，<=0 为获取所有
            for (Result result : resultScanner) {
                LinkedHashMap<String, String> resultMap = getResult(result);
                resultList.add(resultMap);
                if (resultList.size() > limit) {
                    break;
                }
            }
        } else {
            for (Result result : resultScanner) {
                resultList.add(getResult(result));
            }
        }
        int i = 1;
        for (LinkedHashMap<String, String> map : resultList) {
            System.out.println("---------------result" + i + "--------------");
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
                System.out.println(entry.getKey() + "=" + entry.getValue());
                System.out.println();
            }
        }

    }

    private LinkedHashMap<String, String> getResult(Result result) {
        List<Cell> listCell = result.listCells();

        if (listCell == null) {
            return null;
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("ROWKEY", Bytes.toString(result.getRow()));
        String colFamily, column, value;

        for (Cell cell : listCell) {
            colFamily = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
            column = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
            map.put(colFamily + ":" + column, value);
        }
        return map;
    }

    /**
     * 打印出表里所有数据
     * 
     * @param tableName
     * @throws IOException
     */
    public void listData(String tableName) throws IOException {
        Connection connection = null;
        Table table = null;

        try {
            connection = ConnectionFactory.createConnection(configuration);
            TableName tname = TableName.valueOf(tableName);
            table = connection.getTable(tname);

            Scan scan = new Scan();
            ResultScanner resultScanner = table.getScanner(scan);

            for (Result result : resultScanner) {
                System.out.println("Result : " + result);
                List<Cell> cellList = result.listCells();
                for (Cell cell : cellList) {
                    System.out.println("CellKey=" + CellUtil.getCellKeyAsString(cell));
                    System.out.println("TypeByte=" + cell.getTypeByte());
                    System.out.println("SequenceId=" + cell.getSequenceId());
                    String family = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(),
                            cell.getFamilyLength());
                    System.out.println("Family=" + family);
                    String qualifir = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
                            cell.getQualifierLength());
                    System.out.println("Qualifier=" + qualifir);
                    System.out.println("Value="
                            + Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                    System.out.println(
                            "Row=" + Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength()));
                }
            }
        } finally {
            if (table != null) {
                table.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * 删除表
     * 
     * @param tableName
     * @throws IOException
     */
    public void dropTable(String tableName) throws IOException {
        if (tableName == null || "".equals(tableName)) {
            throw new IllegalArgumentException("tableName can't be null");
        }

        Connection connection = null;
        Admin admin = null;

        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();

            TableName tname = TableName.valueOf(tableName);

            admin.disableTable(tname);
            admin.deleteTable(tname);

            System.out.println("table " + tname.getNameAsString() + " is droped.");
        } finally {
            if (admin != null) {
                admin.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * 根据rowkey删除表数据
     * 
     * @param tableName
     * @param rowKey
     * @throws IOException
     */
    public void deleteByRowkey(String tableName, String rowKey) throws IOException {
        Connection connection = null;
        Table table = null;

        try {
            connection = ConnectionFactory.createConnection(configuration);
            TableName tname = TableName.valueOf(tableName);
            table = connection.getTable(tname);

            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);

        } finally {
            if (table != null) {
                table.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

    }
}
