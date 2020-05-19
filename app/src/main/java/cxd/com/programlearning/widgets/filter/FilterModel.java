package cxd.com.programlearning.widgets.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiandiao on 17/8/31.
 */

public class FilterModel {

    public boolean selected;
    public Object mSelectedData;
    public List<Object> filterData;

    public FilterModel() {
        filterData = new ArrayList<>();
    }
}
