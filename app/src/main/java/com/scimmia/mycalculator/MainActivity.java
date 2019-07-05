package com.scimmia.mycalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.lab_jine)
    TextView labJine;
    @Bind(R.id.et_jine)
    EditText etJine;
    @Bind(R.id.bt_jine)
    Button btJine;
    @Bind(R.id.lab_nianlilv)
    TextView labNianlilv;
    @Bind(R.id.et_nianlilv)
    EditText etNianlilv;
    @Bind(R.id.bt_nianlilv)
    Button btNianlilv;
    @Bind(R.id.lab_yuelilv)
    TextView labYuelilv;
    @Bind(R.id.et_yuelilv)
    EditText etYuelilv;
    @Bind(R.id.bt_yuelilv)
    Button btYuelilv;
    @Bind(R.id.lab_tiexianriqi)
    TextView labTiexianriqi;
    @Bind(R.id.et_tiexianriqi)
    Button etTiexianriqi;
    @Bind(R.id.bt_tiexianriqi)
    Button btTiexianriqi;
    @Bind(R.id.lab_daoqiriqi)
    TextView labDaoqiriqi;
    @Bind(R.id.et_daoqiriqi)
    Button etDaoqiriqi;
    @Bind(R.id.bt_daoqiriqi)
    Button btDaoqiriqi;
    @Bind(R.id.lab_tiaozhengtianshu)
    TextView labTiaozhengtianshu;
    @Bind(R.id.et_tiaozhengtianshu)
    EditText etTiaozhengtianshu;
    @Bind(R.id.bt_tiaozhengtianshu)
    Button btTiaozhengtianshu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        etTiexianriqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etTiexianriqi.setText(String.format("%d-%d-%d",year,month+1,dayOfMonth));
                            }
                        },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        etDaoqiriqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etDaoqiriqi.setText(String.format("%d-%d-%d",year,month+1,dayOfMonth));
                            }
                        },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
