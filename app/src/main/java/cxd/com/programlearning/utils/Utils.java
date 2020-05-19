package cxd.com.programlearning.utils;

import android.content.Context;
import androidx.annotation.Nullable;

import java.util.Collection;

/**
 * Created by chenxiandiao on 17/9/21.
 */

public class Utils {


    public static Context mContext;


    public static void initialize(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * @param str string
     * @return whether the string is empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty() || str.equals("null");
    }

    /**
     * @param collection collection
     * @return whether the collection is empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * @param reference object
     * @param <T>       T
     * @return the none null object
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * @param reference object
     * @param message   message of the null pointer exception
     * @param <T>       T
     * @return the none null object
     */
    public static <T> T checkNotNull(T reference, @Nullable Object message) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(message));
        }
        return reference;
    }

}
