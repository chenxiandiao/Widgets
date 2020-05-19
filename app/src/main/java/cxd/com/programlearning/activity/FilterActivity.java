package cxd.com.programlearning.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cxd.com.programlearning.R;
import cxd.com.programlearning.widgets.recyclerviewfilter.FilterItem;
import cxd.com.programlearning.widgets.recyclerviewfilter.FilterMenu;

/**
 * Created by chenxiandiao on 17/9/21.
 */

public class FilterActivity extends AppCompatActivity {

    private FilterMenu mFilterMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        mockData();

    }

    private void mockData() {
        mFilterMenu = (FilterMenu) findViewById(R.id.tree_filter);
        List<List<FilterItem>> data = new ArrayList<>();
        List<FilterItem> items = new ArrayList<>();
        FilterItem item0 = new FilterItem();
        item0.mFilterId = 0;
        item0.mFilterName = "全部";
        item0.mSelected = true;
        items.add(item0);

        FilterItem item1 = new FilterItem();
        item1.mFilterId = 1;
        item1.mFilterName = "男";
        item1.mSelected = false;
        items.add(item1);

        FilterItem item2 = new FilterItem();
        item2.mFilterId = 2;
        item2.mFilterName = "女";
        item2.mSelected = false;
        items.add(item2);

        data.add(items);

        mFilterMenu.setData(data);
    }

    @Override
    public void finish() {
        setResult(1);
        super.finish();
    }
}
