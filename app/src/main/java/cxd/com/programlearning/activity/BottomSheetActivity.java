package cxd.com.programlearning.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cxd.com.programlearning.R;
import cxd.com.programlearning.widgets.bottomsheet.BottomSheetDialog;

/**
 * Created by chenxiandiao on 17/11/27.
 */

public class BottomSheetActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);

        BottomSheetDialog dialog = new BottomSheetDialog();
        dialog.show(getSupportFragmentManager(), "show");
    }
}
