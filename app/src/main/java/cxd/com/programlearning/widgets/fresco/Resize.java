package cxd.com.programlearning.widgets.fresco;


import cxd.com.programlearning.utils.Utils;

/**
 * Created by Rancune@126.com on 2017/3/7.
 */

public class Resize {
    private Resize() {
    }

    /**
     * 当前dpi与与设计时的dpi的比例，设计时为1080*720,设计dpi为2
     */
    private static final double radio;

    /**
     * 满屏横向的width
     */
    public static final int screen_width;

    static {
        final double density = Utils.mContext.getResources().getDisplayMetrics().density;
        radio = density / 2;

        /**
         * 设计dpi为2，px应当为720*1080,减去两边的间距，中间图片的区域大约为660,
         * 为了作一定的图片压缩，假定dpi为2的情况下满屏宽度的图片为500，
         * 这样图片看起来与不做缩小没有太明显的区别，并且也做了一定的缩小
         */
        screen_width = (int) (radio * 500);
    }

    public static final class avatar {
        /**
         * 列表中的头像，比如房间，im中的头像, 动态中的头像，
         */
        public static final int small;

        /**
         * 推荐主播
         */
        public static final int medium;

        static {
            small = (int) (90 * radio);
            medium = (int) (120 * radio);
        }

    }

    public static final class item {
        /**
         * 一行有两个item的情况，比如room,视频，item的背景
         */
        public static final int double_width;

        public static final int double_height;

        static {
            double_width = screen_width / 2;
            double_height = double_width * 9 / 16;
        }
    }

    public static final class icon {

        /**
         * 比如list中header中的icon,topic bar中的icon
         */
        public static final int small;

        /**
         * 比如banner左上角的icon,room左上角的icon等,动态分类的icon
         */
        public static final int medium;

        /**
         * 比如搜索出来的game zone icon
         */
        public static final int big;

        public static final int topic;

        static {
            small = (int) (50 * radio);
            medium = (int) (90 * radio);
            big = (int) (120 * radio);
            topic = (int) (150 * radio);
        }
    }

}
