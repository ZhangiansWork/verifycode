package cn.createsoft.util;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * 常用工具类，包含多个正则表达式验证方法
 *
 * @author 王凡
 * @ClassName StringUtil
 * @Description
 * @date Mar 11, 2011 3:22:17 PM
 */

public class StringUtil {
    protected static Logger log = Logger.getLogger("UTIL");

    /**
     * 段格式
     */
    static String partRegx = "\\w+:\"[^\"]*\"";

    /**
     * 段元素分解
     */
    static String partEleRegx = "(\\w+)(:\")([^\"]*)(\")";

    /**
     * 组格式
     */
    static String groupRegx = "\\{((" + partRegx + ",?)+)\\}";

    /**
     * 全部组格式
     */
    static String groupAllRegx = groupRegx + "+";

    /**
     * 判断字符串为空或null
     *
     * @param obj
     * @return
     * @Title isNullorEmpty
     * @Description 不为空返回true，为空返回false
     * @author 王凡
     * @date 2011-3-28 下午02:11:31
     */
    public static boolean isNotNullorEmpty(Object obj) {
        if (obj != null && !obj.equals(""))
            return true;
        else
            return false;
    }

    /**
     * 判断字符串为空或null
     *
     * @param obj
     * @return
     * @Title isNullorEmpty
     * @Description 为空返回true，不为空返回false
     * @author 王凡
     * @date 2011-3-28 下午02:11:31
     */
    public static boolean issNullorEmpty(Object obj) {
        if (obj == null || obj.equals("") || obj.equals(null) || obj.equals("") || "null".equals(obj))
            return true;
        else
            return false;
    }

    /**
     * 空字符串验证转换
     *
     * @param obj
     * @return
     * @Title nullToStr
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:22:24 PM
     */
    public static String null2String(Object obj) {
        if (obj == null)
            return "";
        else
            return obj.toString();
    }

    /**
     * 空字符串数组验证转换
     *
     * @param obj
     * @return
     * @Title nullToStr
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:23:43 PM
     */
    public static Object[] null2String(Object[] obj) {
        if (obj != null) {
            Object[] objs = new Object[obj.length];
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] == null) {
                    objs[i] = "";
                } else
                    objs[i] = obj[i];
            }
            return objs;
        } else {
            Object[] objects = new Object[0];
            return objects;
        }
    }

    /**
     * 将一个字符串数组转换为逗号分割的字符串 Demo:array2String(new String[]{"a","b","c"},"|")
     *
     * @param arrays
     * @return
     * @Title array2String
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:24:05 PM
     */
    public static String array2String(String[] arrays, String sign) {
        String resultString = "";
        if (arrays != null && arrays.length != 0) {
            StringBuffer tmpstring = new StringBuffer();
            boolean flag = false;
            for (String tmps : arrays) {
                if (flag)
                    tmpstring.append(sign);
                tmpstring.append(tmps.trim());
                flag = true;
            }
            resultString = tmpstring.toString();
        }
        return resultString;
    }

    /**
     * 将逗号分割的字符串转换为一个字符串数组 Demo:string2Array("a,b,c","|")
     *
     * @param string
     * @return return new String[]{"a","b","c"}
     * @Title string2Array
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:25:46 PM
     */
    public static String[] string2Array(String string, String sign) {
        String[] tmpArray = null;
        if (string != null && !"".equals(string.trim())) {
            tmpArray = string.split(sign);
        }
        return tmpArray;
    }

    /**
     * 将指定字符串转换为目标字符集
     * Demo:changeEncoding(request.getParameter("msg").toString(),"GBK","UTF-8")
     *
     * @param input
     * @param sourceEncoding
     * @param targetEncoding
     * @return string
     * @Title changeEncoding
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:26:01 PM
     */
    public static String changeEncoding(String input, String sourceEncoding, String targetEncoding) {
        if (input == null || input.equals("")) {
            return input;
        }

        try {
            byte[] bytes = input.getBytes(sourceEncoding);
            return new String(bytes, targetEncoding);
        } catch (Exception ex) {
        }
        return input;
    }

    /**
     * 身份证验证 Demo:isIdCard("410103198608040000")
     *
     * @param arrIdCard
     * @return return true/false
     * @Title isIdCard
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:27:10 PM
     */
    public static boolean isIdCard(String arrIdCard) {
        int sigma = 0;
        Integer[] a = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        String[] w = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        for (int i = 0; i < 17; i++) {
            int ai = Integer.parseInt(arrIdCard.substring(i, i + 1));
            int wi = a[i];
            sigma += ai * wi;
        }
        int number = sigma % 11;
        String check_number = w[number];
        if (!arrIdCard.substring(17).equals(check_number)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 电子邮件验证 Demo:isEmail("cccc@qq.com")
     *
     * @param mail
     * @return return true/false
     * @Title isEmail
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:27:17 PM
     */
    public static boolean isEmail(String mail) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mail);
        return m.find();
    }

    /**
     * 整数验证 Demo:isNumber("aaaa")
     *
     * @param str
     * @return return true/false
     * @Title isNumber
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:27:24 PM
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 小数验证 Demo:isFloat("1.5")
     *
     * @param str
     * @return return true/false
     * @Title isFloat
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:27:33 PM
     */
    public static boolean isFloat(String str) {
        return str.matches("[\\d.]+");
    }

    /**
     * 固定电话验证
     *
     * @param value
     * @return
     * @Title isTel
     * @Description Demo:isTel("0371-66610502") return true/false
     * @author 王凡
     * @date Mar 11, 2011 3:27:42 PM
     */
    public static boolean isTel(String value) {
        return value.matches("\\d{4}-\\d{8}|\\d{4}-\\d{7}|\\d(3)-\\d(8)");
    }

    /**
     * 手机号验证 Demo:isMobile("13800138000")
     *
     * @param value
     * @return return true/false
     * @Title isMobile
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:27:48 PM
     */
    public static boolean isMobile(String value) {
        return value.matches("^[1][3,5]+\\d{9}");
    }

    /**
     * 中文字符验证 Demo:isChinese("创元网络",4)
     *
     * @param value
     * @param length
     * @return true/false
     * @Title isChinese
     * @Description return true/false
     * @author 王凡
     * @date Mar 11, 2011 3:27:53 PM
     */
    public static boolean isChinese(String value, int length) {
        return value.matches("^[\u4e00-\u9fa5]+$") && value.length() <= length;
    }

    /**
     * QQ验证 Demo:isQQ("8178121")
     *
     * @param value
     * @return return true/false
     * @Title isQQ
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:28:00 PM
     */
    public static boolean isQQ(String value) {
        return value.matches("[1-9][0-9]{4,13}");
    }

    /**
     * 邮政编码验证 Demo:isPostCode("450000")
     *
     * @param value
     * @return return true/false
     * @Title isPostCode
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:28:05 PM
     */
    public static boolean isPostCode(String value) {
        return value.matches("[1-9]\\d{5}(?!\\d)");
    }

    /**
     * 日期验证 Demo:isDate("2010-01-01")
     *
     * @param dt
     * @return return true/false
     * @Title isDate
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:28:11 PM
     */
    public static boolean isDate(String dt) {
        String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(dt);
        return m.matches();
    }

    /**
     * 验证指定字符串是否超出长度限制 Demo:isMaxLength("asdf",2)
     *
     * @param str
     * @param length
     * @return return true/false
     * @Title isMaxLength
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 3:28:15 PM
     */
    public static boolean isMaxLength(String str, Integer length) {
        if (str != null && str.length() > 0) {
            if (str.length() > length)
                return false;
            else
                return true;
        }
        return false;
    }

    /**
     * 检测字符串组是否有为空的 Demo:isBlank(new String[]{"a","b","","c"})
     *
     * @param strs
     * @return return true/false
     * @Title isBlank
     * @Description
     * @author 王凡
     * @date Mar 11, 2011 4:54:37 PM
     */
    public static boolean isBlank(String... strs) {
        if (strs == null)
            return true;
        for (int i = 0; i < strs.length; i++) {
            if (isBlank(strs[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换字符串数组成为Sql IN 形式
     *
     * @param strs
     * @return
     * @Title getSqlInByArray
     * @Description
     * @author 王凡
     * @date Mar 12, 2011 8:54:37 AM
     */
    public static String getSqlInByArray(String[] strs) {
        if (strs == null)
            return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            sb.append("'").append(strs[i]).append("'");
            if (i < strs.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * 转换字符串数组成为Sql IN 形式
     *
     * @param ints
     * @return
     * @Title getSqlInByArray
     * @Description
     * @author 王凡
     * @date Mar 12, 2011 8:54:12 AM
     */
    public static String getSqlInByArray(int[] ints) {
        if (ints == null)
            return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ints.length; i++) {
            sb.append(ints[i]);
            if (i < ints.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * 空字符串检查 Demo:isBlank("a")
     *
     * @param str
     * @return return true/false
     * @Title isBlank
     * @Description
     * @author 王凡
     * @date Mar 12, 2011 8:53:35 AM
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测字符串组是否为空或者是脏数据 主要作用是过滤sql注入，Demo:isDirty("a")
     *
     * @param str
     * @return return string
     * @Title isDirty
     * @Description
     * @author 王凡
     * @date Mar 12, 2011 8:53:54 AM
     */
    public static boolean isDirty(String str) {

        if (str != null) {
            if (str.indexOf(";") >= 0 || str.indexOf("'") >= 0 || Pattern.compile("and .*", Pattern.CASE_INSENSITIVE).matcher(str).find() || Pattern.compile("or .*", Pattern.CASE_INSENSITIVE).matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为脏数据
     *
     * @param str
     * @return
     * @Title isDirty
     * @Description
     * @author 王凡
     * @date Mar 12, 2011 8:54:01 AM
     */
    public static boolean isDirty(String... str) {
        if (str == null)
            return false;
        for (int i = 0; i < str.length; i++) {
            if (isDirty(str[i]))
                return true;
        }
        return false;
    }

    /**
     * 检测字符串组是否为空或者是脏数据
     *
     * @param strs
     * @return
     * @Title isDirtyOrBlank
     * @Description
     * @author 王凡
     * @date Mar 12, 2011 9:00:28 AM
     */
    public static boolean isDirtyOrBlank(String... strs) {
        if (strs == null)
            return true;
        for (int i = 0; i < strs.length; i++) {
            if (isBlank(strs[i])) {
                return true;
            }
            if (isDirty(strs[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取boolean值
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(String value, boolean defaultValue) {

        try {
            return Boolean.valueOf(value).booleanValue();
        } catch (Exception e) {
            log.error(e);
            return defaultValue;
        }
    }

    /**
     * 转换成数字
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static int getInt(String value, int defaultValue) throws NumberFormatException {
        try {
            if (value == null || "".equals(value))
                return defaultValue;
            return Integer.valueOf(value).intValue();
        } catch (Exception e) {
            log.error(e);
            return defaultValue;
        }
    }

    /**
     * 转换成short
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static short getShrot(String value, short defaultValue) {
        try {
            if (value == null || "".equals(value))
                return defaultValue;
            return Short.valueOf(value);
        } catch (Exception e) {
            log.error(e);
            return defaultValue;
        }
    }

    /**
     * @param value
     * @param defaultValue
     * @return
     */
    public static BigDecimal getBigDecimal(String value, int defaultValue) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            log.error(e);
            return new BigDecimal(defaultValue);
        }

    }

    public static long getLong(String value, long defaultValue) {
        try {
            return Long.valueOf(value).longValue();
        } catch (Exception e) {
            log.error(e);
            return defaultValue;
        }
    }

    public static float getFloat(String value, float defaultValue) {
        try {
            return Float.valueOf(value).floatValue();
        } catch (Exception e) {
            log.error(e);
            return defaultValue;
        }
    }

    public static double getDouble(String value, double defaultValue) {
        try {
            return Double.valueOf(value).doubleValue();
        } catch (Exception e) {
            log.error(e);
            return defaultValue;
        }
    }

    /**
     * 时间比较
     *
     * @param effEndTime
     * @param temp
     * @return
     */
    public static String compareDate(String effEndTime, Integer temp) {
        String res = null;
        if (null != effEndTime) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // 定义将日期格式要换成的格式
            int i = compare_date(formatter.format(new Date()), effEndTime);
            if (i == 0) {
                res = "当天停用";
            } else if (i == -1 && temp == 0) {
                res = "正常";
            } else if (i == 1 || temp != 0) {
                res = "停用";
            }
        } else {
            res = "正常";
        }
        return res;
    }

    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static Map transStringToMap(String mapString){
        Map map = new HashMap();
        java.util.StringTokenizer items;
        for(StringTokenizer entrys = new StringTokenizer(mapString, ","); entrys.hasMoreTokens();
            map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
            items = new StringTokenizer(entrys.nextToken(), ":");
        return map;
    }


    public static String transMapToString(Map map){
        java.util.Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for(Iterator iterator = map.entrySet().iterator(); iterator.hasNext();)
        {
            entry = (java.util.Map.Entry)iterator.next();
            sb.append(entry.getKey().toString()).append( ":" ).append(null==entry.getValue()?"":
                    entry.getValue().toString()).append (iterator.hasNext() ? "," : "");
        }
        return sb.toString();
    }
}
