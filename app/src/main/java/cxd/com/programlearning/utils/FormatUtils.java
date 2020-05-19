package cxd.com.programlearning.utils;

import androidx.annotation.Nullable;
import android.widget.EditText;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unused", "WeakerAccess"})
public class FormatUtils {

    /**
     * @param template string format template %s%s%s%s
     * @param args     args
     * @return formatted string
     * eg. formatString("%s%s%s", 1, "test", map)
     */
    public static String formatString(@Nullable String template, @Nullable Object... args) {
        // if template string is null, template is converted into "null"
        template = template == null ? "null" : template;

        // start substituting the arguments into the '%s' placeholders
        int size = args == null ? 0 : args.length;
        StringBuilder builder = new StringBuilder(template.length() + 16 * size);
        int templateStart = 0;
        int i = 0;
        while (i < size) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template, templateStart, template.length());

        // if we run out of placeholders, append the extra args in square braces
        if (i < size) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * @param object object
     * @return format object
     */
    public static String formatObject(Object object) {
        if (object == null) {
            return "Object{object is null}";
        }

        final String simpleName = object.getClass().getSimpleName();
        if (object instanceof Collection) {
            Collection collection = (Collection) object;
            StringBuilder builder = new StringBuilder();
            builder.append(String.format(Locale.ENGLISH, "%s size = %d [\n", simpleName, collection.size()));
            String itemString = "[%d]:%s%s";
            if (!collection.isEmpty()) {
                Iterator iterator = collection.iterator();
                int flag = 0;
                while (iterator.hasNext()) {
                    Object item = iterator.next();
                    builder.append(String.format(Locale.ENGLISH, itemString,
                            flag,
                            String.valueOf(item),
                            flag++ < collection.size() - 1 ? ",\n" : "\n"));
                }
            }
            builder.append("\n]");
            return builder.toString();
        } else if (object instanceof Map) {
            StringBuilder builder = new StringBuilder();
            builder.append(simpleName)
                    .append(" {\n");
            Map map = (Map) object;
            String itemString = "[%s -> %s]\n";
            Set keys = map.keySet();
            for (Object key : keys) {
                Object value = map.get(key);
                builder.append(String.format(itemString, String.valueOf(key), String.valueOf(value)));
            }
            builder.append("}");
            return builder.toString();
        } else {
            return String.valueOf(object);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // format 时间
    ///////////////////////////////////////////////////////////////////////////
    private static final long IN_MIN = 60;
    private static final long IN_HOUR = 60 * 60;
    private static final long IN_DAY = 24 * 60 * 60;
    private static final long IN_MONTH = 24 * 60 * 60 * 30;

//    public static String formatTime(long time) {
//        Context context = Utils.mContext;
//        long delta = (System.currentTimeMillis() - time) / 1000;
//        String strTime;
//        int i;
//        if (delta <= IN_MIN) {
//            strTime = context.getString(R.string.zues_dynamics_time_format_01);
//        } else if (delta <= IN_HOUR) {
//            i = (int) (delta / IN_MIN);
//            strTime = String.format(context.getString(R.string.zues_str_min_ago), i);
//        } else if (delta <= IN_DAY) {
//            i = (int) (delta / IN_HOUR);
//            strTime = String.format(context.getString(R.string.zues_str_hour_ago), i);
//        } else if (delta <= IN_MONTH) {
//            i = (int) (delta / IN_DAY);
//            strTime = String.format(context.
//                    getString(R.string.zues_str_day_ago), i);
//        } else {
//            Calendar calendar = Calendar.getInstance();
//            int thisyear = calendar.get(Calendar.YEAR);
//            calendar.setTimeInMillis(time);
//            int thatyear = calendar.get(Calendar.YEAR);
//            SimpleDateFormat formatter;
//            if (thisyear != thatyear) {
//                formatter = new SimpleDateFormat(
//                        context.getString(R.string.zues_dynamics_time_format_02), Locale.CHINA);
//            } else {
//                formatter = new SimpleDateFormat(
//                        context.getString(R.string.zues_dynamics_time_format_03), Locale.CHINA);
//            }
//            Date curDate = new Date(time);
//            strTime = formatter.format(curDate);
//        }
//        return strTime;
//    }
//
//    public static String longToStringTime(long time) {
//        Context context = Utils.mContext;
//        SimpleDateFormat formatter = new SimpleDateFormat(
//                context.getString(R.string.zues_dynamics_time_format_02), Locale.CHINA);
//        Date curDate = new Date(time);
//        return formatter.format(curDate);
//    }

    public static String stringForTime(long timeMs, boolean bAbsTime) {

        StringBuilder sb = new StringBuilder();
        if (!bAbsTime) {
            int totalSeconds = (int) (timeMs / 1000);
            int seconds = totalSeconds % 60;
            int minutes = totalSeconds / 60;
            sb.append(minutes < 10 ? "0" + minutes : minutes);
            sb.append(":");
            sb.append(seconds < 10 ? "0" + seconds : seconds);
            return sb.toString();
        } else {
            String head;
            if (timeMs > 0) {
                head = "+";
            } else if (timeMs < 0) {
                head = "-";
                timeMs = 0 - timeMs;
            } else {
                head = "";
            }
            int totalSeconds = (int) (timeMs / 1000);
            int seconds = totalSeconds % 60;
            int minutes = totalSeconds / 60;
            sb.append("[");
            sb.append(head);
            sb.append(minutes < 10 ? "0" + minutes : minutes);
            sb.append(":");
            sb.append(seconds < 10 ? "0" + seconds : seconds);
            sb.append("]");
            return sb.toString();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // fomat 数字
    ///////////////////////////////////////////////////////////////////////////
    public static final int TYPE_WAN = 1;
    public static final int TYPE_BAI_WAN = 2;
    public static final int TYPE_QIAN = 3;

//    public static String formatNumber(String number) {
//        return formatNumber(number, TYPE_WAN);
//    }

//    public static String convertQianNumber(String number) {
//        return formatNumber(number, TYPE_QIAN);
//    }

//    public static String formatNumber(String number, int type) {
//        if (TextUtils.isEmpty(number)) {
//            number = "0";
//        }
//        float NUM_QIAN = 1000.0f;
//        float NUM_WAN = 10000.0f;
//        float NUM_BAI_WAN = 1000000.0f;
//        float NUM_YI = 100000000.0f;
//        long iNumber;
//        try {
//            iNumber = Long.parseLong(number);
//        } catch (NumberFormatException e) {
//            return number;
//        }
//
//        Context context = Utils.mContext;
//        java.text.DecimalFormat df = new java.text.DecimalFormat("#.#");
//
//        switch (type) {
//            case TYPE_WAN:
//                if (iNumber >= NUM_YI) {
//                    return df.format((iNumber / NUM_YI)) + context.getString(R.string.zues_str_yi);
//                } else if (iNumber >= NUM_WAN) {
//                    return df.format((iNumber / NUM_WAN)) + context.getString(R.string.zues_str_wan);
//                } else
//                    return number;
//
//            case TYPE_BAI_WAN:
//                if (iNumber >= NUM_YI) {
//                    return df.format((iNumber / NUM_YI)) + context.getString(R.string.zues_str_yi);
//                } else if (iNumber >= NUM_BAI_WAN) {
//                    return df.format((iNumber / NUM_BAI_WAN)) + context.getString(R.string.zues_str_bai_wan);
//                } else
//                    return number;
//            case TYPE_QIAN:
//                if (iNumber >= NUM_YI) {
//                    return df.format((iNumber / NUM_YI)) + context.getString(R.string.zues_str_yi);
//                } else if (iNumber >= NUM_BAI_WAN) {
//                    return df.format((iNumber / NUM_BAI_WAN)) + context.getString(R.string.zues_str_bai_wan);
//                } else if (iNumber >= NUM_WAN) {
//                    return df.format((iNumber / NUM_WAN)) + context.getString(R.string.zues_str_wan);
//                } else if (iNumber >= NUM_QIAN) {
//                    return df.format((iNumber / NUM_QIAN)) + context.getString(R.string.zues_str_qian);
//                } else
//                    return number;
//        }
//        return number;
//
//    }

    ///////////////////////////////////////////////////////////////////////////
    // format 容量大小
    ///////////////////////////////////////////////////////////////////////////
    public final static long K = 1024;
    public final static long M = 1024 * 1024;
    public final static long G = 1024 * 1024 * 1024;

    public static String parserSize(long size) { // B
        float f;
        float b;
        String ret;
        if (size > G) {
            f = ((float) size) / G;
            b = (float) (Math.round(f * 100)) / 100; //小数点后保留两位
            ret = b + "GB";
        } else if (size > M) {
            f = ((float) size) / M;
            b = (float) (Math.round(f * 100)) / 100;
            ret = b + "MB";
        } else if (size > K) {
            f = ((float) size) / K;
            b = (float) (Math.round(f * 100)) / 100;
            ret = b + "KB";
        } else {
            ret = size + "B";
        }

        return ret;
    }

    public static void formatPhoneNum(CharSequence s, int start, int before, EditText et_PhoneNum) {
        if (s == null || s.length() == 0) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            et_PhoneNum.setText(sb.toString());
            et_PhoneNum.setSelection(index);
        }
    }

    public static String formatPhoneNum(String contents) {
        int length = contents.length();
        if (length == 11) {
            return contents.substring(0, 3) + " "
                    + contents.substring(3, 7) + " "
                    + contents.substring(7, 10) +
                    contents.substring(10);
        }
        return contents;
    }
}