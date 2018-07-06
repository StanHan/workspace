package demo.json;

import java.util.Map;
import java.util.Set;

public class JSONUtil {

    public static void main(String[] args) {
        String a = "{\"cardDataList\":[{\"cardNo\":\"622155******0006\",\"cardData\":{\"c_cancel_consume_unit_L9m\":\"0\",\"c_mcc1_fin_amt_L6m\":\"0.00\",\"c_max_mchnt_unit_L1m_r\":\"0.1020\",\"c_mcc2_fin_financing_unit_L6m\":\"0\",\"c_mcc2_comm_air_amt_L6m\":\"15.06\",\"c_mob_adj\":\"98\",\"c_cr_setl_amt_L12m\":\"600.00\",\"c_mcc2_fin_cash_unit_L3m\":\"0\",\"c_setl_unit_L9m\":\"556\",\"c_mcc1_hotel_unit_L9m\":\"4\",\"c_cr_no_m_L12m\":\"3\",\"c_oversea_amt_L1m\":\"0.00\",\"c_mcc2_fin_financing_amt_L6m\":\"0.00\",\"c_cr_setl_amt_L1m\":\"200.00\",\"c_tr_collect_amt_L9m\":\"0.00\",\"c_mcc1_credit_unit_L6m\":\"6\",\"c_mcc1_comm_amt_L12m\":\"1601.53\",\"c_max_setl_mchnt_cd\":\"386533156947901\",\"c_mcc2_fin_cash_amt_L6m\":\"0.00\",\"c_mcc2_fin1_amt_L3m\":\"0.00\",\"c_mcc1_hotel_amt_L6m\":\"492.00\",\"c_tr_divpay_unit_L3m\":\"0\",\"c_mcc2_comm_air_unit_L3m\":\"1\",\"c_tr_divpay_amt_L6m\":\"0.00\",\"c_first_trans_dt\":\"20100301\",\"c_no_setl_m_L9m\":\"9\",\"c_mcc2_fin_ins_amt_L12m\":\"0.00\",\"c_cancel_consume_amt_L3m\":\"0.00\",\"c_no_mcc_L1m\":\"5\",\"c_dr_setl_unit_L12m\":\"694\",\"c_oversea_unit_L1m\":\"0\",\"c_tr_collect_unit_L3m\":\"0\",\"c_card_brand_cd\":\"1\",\"c_mcc1_credit_amt_L9m\":\"180.00\",\"c_mcc2_comm_oil_amt_L6m\":\"600.00\",\"c_unit_atr_thd_rank_L9m\":\"120\",\"c_dr_no_m_L12m\":\"12\",\"c_oversea_amt_L3m\":\"298.00\",\"c_no_setl_m_L6m\":\"6\",\"c_mcc1_hotel_amt_L3m\":\"369.00\",\"c_mcc2_fin_financing_unit_L3m\":\"0\",\"c_tr_divpay_unit_L6m\":\"0\",\"c_m_trx_prov\":\"null\",\"c_mcc1_comm_cnt_L3m\":\"6\",\"c_setl_unit_L6m\":\"319\",\"c_tr_divpay_amt_L9m\":\"0.00\",\"c_cancel_consume_unit_L3m\":\"0\",\"c_tr_collect_amt_L3m\":\"0.00\",\"c_dr_setl_amt_L12m\":\"21980.51\",\"c_card_grade\":\"1\",\"c_unit_atr_thd_rank_L3m\":\"211\",\"c_mcc2_comm_air_amt_L9m\":\"30.12\",\"c_tr_pay_unit_L3m\":\"0\",\"c_mcc2_comm_oil_amt_L9m\":\"900.00\",\"c_mcc1_credit_unit_L12m\":\"12\",\"c_mcc1_credit_amt_L6m\":\"120.00\",\"c_setl_amt_L12m\":\"22580.51\",\"c_up_trx_idx\":\"312\",\"c_setl_amt_L6m\":\"12952.34\",\"c_mcc1_hotel_unit_L3m\":\"3\",\"c_mcc1_fin_amt_L3m\":\"0.00\",\"c_tr_pay_amt_L6m\":\"0.00\",\"c_mcc1_credit_unit_L3m\":\"3\",\"c_mcc2_comm_oil_unit_L3m\":\"3\",\"c_b_to_tot_amt_L6m_r\":\"0.4483\",\"c_mcc2_fin1_unit_L9m\":\"0\",\"c_cr_setl_unit_L12m_r\":\"0.008571428571428572\",\"c_amt_atr_thd_rank_L6m\":\"342\",\"c_cr_incr_amt_L3m_r\":\"1.0000\",\"c_ave_no_mcc_L3m\":\"5\",\"c_unit021_L6m_r\":\"0.1079\",\"c_tr_collect_unit_L6m\":\"0\",\"c_oversea_unit_L3m\":\"0\",\"c_m_trx_city\":\"长春市\",\"c_mcc2_fin_cash_unit_L9m\":\"0\",\"c_cr_setl_unit_L12m\":\"6\",\"c_unit_atr_thd_rank_L6m\":\"432\",\"c_ave_no_prov_L3m\":\"1\",\"c_max_mchnt_amt_L1m_r\":\"0.1890\",\"c_mcc1_credit_unit_L9m\":\"9\",\"c_mcc2_comm_oil_amt_L3m\":\"300.00\",\"c_tr_divpay_amt_L3m\":\"0.00\",\"c_cs_var_L6m\":\"0.3065\",\"c_mcc2_comm_air_unit_L6m\":\"2\",\"c_unit_atr_thd_rank_L12m\":\"311\",\"c_max_setl_mchnt_nm\":\"屈臣氏1店\",\"c_dr_incr_amt_L3m_r\":\"0.1900\",\"c_tr_pay_unit_L9m\":\"0\",\"c_mcc2_fin_cash_amt_L3m\":\"0.00\",\"c_mcc1_comm_cnt_L9m\":\"17\",\"c_mcc1_fin_amt_L9m\":\"0.00\",\"c_card_bank\":\"null\",\"c_no_setl_m_L3m\":\"3\",\"c_cancel_consume_amt_L9m\":\"0.00\",\"c_mcc2_comm_air_amt_L3m\":\"7.53\",\"c_incr_setl_unit_L3m_r\":\"-0.3032\",\"c_amt_atr_thd_rank_L12m\":\"342\",\"c_tr_collect_unit_L9m\":\"0\",\"c_dr_setl_amt_L1m\":\"2831.91\",\"c_mcc1_credit_amt_L12m\":\"240.00\",\"c_amt_atr_thd_rank_L3m\":\"244\",\"c_mcc2_fin_cash_unit_L6m\":\"0\",\"c_cancel_consume_unit_L6m\":\"0\",\"c_setl_unit_L12m\":\"700\",\"c_mcc1_fin_unit_L3m\":\"0\",\"c_dr_setl_unit_L1m\":\"47\",\"c_setl_amt_L3m\":\"7112.10\",\"c_mcc1_comm_amt_L3m\":\"307.88\",\"c_mcc2_fin_financing_amt_L3m\":\"0.00\",\"c_mcc1_fin_unit_L9m\":\"0\",\"c_mcc1_hotel_unit_L6m\":\"4\",\"c_mcc2_comm_oil_unit_L9m\":\"9\",\"c_mcc2_fin1_unit_L3m\":\"0\",\"c_tr_pay_amt_L12m\":\"0.00\",\"c_cancel_consume_amt_L6m\":\"0.00\",\"c_cancel_consume_amt_L12m\":\"0.00\",\"c_cr_setl_unit_L1m\":\"2\",\"c_amt_atr_thd_rank_L9m\":\"121\",\"c_up_trx_adj_idx\":\"301\",\"c_mcc1_comm_amt_L9m\":\"1586.47\",\"c_tr_pay_amt_L3m\":\"0.00\",\"settle_month\":\"201805\",\"c_oversea_cs_amt_L6m_r\":\"0.0297\",\"c_mcc1_fin_unit_L6m\":\"0\",\"c_no_prov_L1m\":\"1\",\"c_mcc2_comm_oil_unit_L6m\":\"6\",\"c_mcc2_fin_financing_unit_L9m\":\"0\",\"c_no_setl_m_L12m\":\"12\",\"c_setl_amt_L9m\":\"18226.01\",\"c_setl_unit_L3m\":\"131\",\"c_mcc2_fin_cash_amt_L9m\":\"0.00\",\"c_card_attr_cd\":\"03\",\"c_tr_pay_amt_L9m\":\"0.00\",\"c_incr_setl_amt_L3m_r\":\"0.2178\",\"c_tr_divpay_unit_L9m\":\"0\",\"c_tr_pay_unit_L6m\":\"0\",\"c_mcc2_fin_financing_amt_L12m\":\"0.00\",\"c_mcc2_comm_air_unit_L9m\":\"4\",\"c_mcc1_hotel_amt_L9m\":\"492.00\",\"c_mcc2_fin1_amt_L9m\":\"0.00\",\"c_mcc2_fin_financing_amt_L9m\":\"0.00\",\"c_card_prod\":\"0\",\"c_max_setl_mchnt_nm_L3m\":\"屈臣氏1店\",\"c_setl_amt_p_u_Ll2m\":\"32.26\",\"c_mcc1_credit_amt_L3m\":\"60.00\",\"c_tr_collect_amt_L6m\":\"0.00\"}}]}";
        System.out.println(a);
        
    }

    static String string2Json(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 20);
        sb.append('\"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
            case '\"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '/':
                sb.append("\\/");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            default:
                sb.append(c);
            }
        }
        sb.append('\"');
        return sb.toString();
    }

    static String number2Json(Number number) {
        return number.toString();
    }

    static String boolean2Json(Boolean bool) {
        return bool.toString();
    }

    static String array2Json(Object[] array) {
        if (array.length == 0)
            return "[]";
        StringBuilder sb = new StringBuilder(array.length << 4);
        sb.append('[');
        for (Object o : array) {
            sb.append(toJson(o));
            sb.append(',');
        }
        // 将最后添加的 ',' 变为 ']':
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }

    static String map2Json(Map<String, Object> map) {
        if (map.isEmpty())
            return "{}";
        StringBuilder sb = new StringBuilder(map.size() << 4);
        sb.append('{');
        Set<String> keys = map.keySet();
        for (String key : keys) {
            Object value = map.get(key);
            sb.append('\"');
            sb.append(key);
            sb.append('\"');
            sb.append(':');
            sb.append(toJson(value));
            sb.append(',');
        }
        // 将最后的 ',' 变为 '}':
        sb.setCharAt(sb.length() - 1, '}');
        return sb.toString();
    }

    public static String toJson(Object o) {
        if (o == null) {
            return "null";
        } else if (o instanceof String) {
            return string2Json((String) o);
        } else if (o instanceof Boolean) {
            return boolean2Json((Boolean) o);
        } else if (o instanceof Number) {
            return number2Json((Number) o);
        } else if (o instanceof Map) {
            return map2Json((Map<String, Object>) o);
        } else if (o instanceof Object[]) {
            return array2Json((Object[]) o);
        }
        return null;
    }

    /**
     * 对JSON字符串中的特殊字符进行转义
     * 
     * @param s
     * @return
     */
    public static String normalizeString(String s) {

        StringBuffer str = new StringBuffer();
        int len = (s != null) ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            final char lChar = s.charAt(i);
            if (lChar == '\n')
                str.append("\\n");
            else if (lChar == '\r')
                str.append("\\r");
            else if (lChar == '\t')
                str.append("\\t");
            else if (lChar == '\b')
                str.append("\\b");
            else if (lChar == '\f')
                str.append("\\f");
            // else if(lChar == '/') lBuf.append("\\/");
            else if (lChar == '\'')
                str.append("\\'");
            else if (lChar == '\"')
                str.append("\\\"");
            else if (lChar == '\\')
                str.append("\\\\");
            else
                str.append(lChar);
        }
        return (str.toString());
    }

    /**
     * 判断字符串是否是JS或JSON中的非法字符串
     * 
     * @param s
     * @return
     */
    public static boolean isInvalidate(String s) {

        // 如果为空或空格，则为合法字符串
        if (s == null || "".equals(s.trim()))
            return false;

        // Javascript保留字列表
        if ("break".equals(s) || "delete".equals(s) || "function".equals(s) || "return".equals(s) || "typeof".equals(s)
                || "case".equals(s) || "do".equals(s) || "if".equals(s) || "switch".equals(s) || "var".equals(s)
                || "catch".equals(s) || "else".equals(s) || "in".equals(s) || "this".equals(s) || "void".equals(s)
                || "continue".equals(s) || "false".equals(s) || "instanceof".equals(s) || "throw".equals(s)
                || "while".equals(s) || "debugger".equals(s) || "finally".equals(s) || "new".equals(s)
                || "true".equals(s) || "with".equals(s) || "default".equals(s) || "for".equals(s) || "null".equals(s)
                || "try".equals(s) || "enum".equals(s) || "super".equals(s) || "export".equals(s) || "import".equals(s)
                || "extends".equals(s) || "class".equals(s) || "const".equals(s))
            return true;

        // Javascript未来保留字(可以考虑在未来时进行判断)
        /*
         * if ("abstract".equals(s) || "double".equals(s) || "goto".equals(s) || "native".equals(s) ||
         * "static".equals(s) || "boolean".equals(s) || "implements".equals(s) || "package".equals(s) ||
         * "byte".equals(s) || "private".equals(s) || "synchronized".equals(s) || "char".equals(s) || "int".equals(s) ||
         * "protected".equals(s) || "throws".equals(s) || "final".equals(s) || "interface".equals(s) ||
         * "public".equals(s) || "transient".equals(s) || "float".equals(s) || "long".equals(s) || "short".equals(s) ||
         * "volatile".equals(s)) return true;
         */
        return false;
    }
}
