package com.abubusoft.kripton.quickstart;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.abubusoft.kripton.KriptonBinder;
import com.abubusoft.kripton.KriptonJsonContext;
import com.abubusoft.kripton.android.sqlite.DataSourceOptions;
import com.abubusoft.kripton.android.sqlite.DatabaseLifecycleHandler;
import com.abubusoft.kripton.quickstart.persistence.BindQuickStartDaoFactory;
import com.abubusoft.kripton.quickstart.persistence.BindQuickStartDataSource;
import com.abubusoft.kripton.quickstart.persistence.QuickStartDataSource;
import com.abubusoft.kripton.quickstart.persistence.UserDaoImpl;
import com.abubusoft.quickstart.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart=(Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserActivity.class);
                Bundle b = new Bundle();
                intent.putExtras(b);
                v.getContext().startActivity(intent);
            }
        });

        BindQuickStartDataSource ds = BindQuickStartDataSource.instance();

        ds.execute(new BindQuickStartDataSource.SimpleTransaction() {
            @Override
            public boolean onExecute(BindQuickStartDaoFactory daoFactory) throws Throwable {
                UserDaoImpl dao = daoFactory.getUserDao();
                String[] p={"hello"};
                dao.sortedFind("name asc");
                return true;
            }
        });
    }
}
