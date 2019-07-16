package com.scimmia.mycalculator;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.tuple.MutablePair;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks{


    @BindView(R.id.sw_pjlx)
    Switch swPjlx;
    @BindView(R.id.lab_jine)
    TextView labJine;
    @BindView(R.id.et_jine)
    EditText etJine;
    @BindView(R.id.bt_jine)
    ImageButton btJine;
    @BindView(R.id.lab_nianlilv)
    TextView labNianlilv;
    @BindView(R.id.et_nianlilv)
    EditText etNianlilv;
    @BindView(R.id.bt_nianlilv)
    ImageButton btNianlilv;
    @BindView(R.id.lab_yuelilv)
    TextView labYuelilv;
    @BindView(R.id.et_yuelilv)
    EditText etYuelilv;
    @BindView(R.id.bt_yuelilv)
    ImageButton btYuelilv;
    @BindView(R.id.lab_tiexianriqi)
    TextView labTiexianriqi;
    @BindView(R.id.et_tiexianriqi)
    Button etTiexianriqi;
    @BindView(R.id.bt_tiexianriqi)
    ImageButton btTiexianriqi;
    @BindView(R.id.lab_daoqiriqi)
    TextView labDaoqiriqi;
    @BindView(R.id.et_daoqiriqi)
    Button etDaoqiriqi;
    @BindView(R.id.bt_daoqiriqi)
    ImageButton btDaoqiriqi;
    @BindView(R.id.lab_tiaozhengtianshu)
    TextView labTiaozhengtianshu;
    @BindView(R.id.et_tiaozhengtianshu)
    EditText etTiaozhengtianshu;
    @BindView(R.id.bt_tiaozhengtianshu)
    ImageButton btTiaozhengtianshu;
    @BindView(R.id.et_mswsxf)
    EditText etMswsxf;

    @BindView(R.id.tv_result)
    TextView tvResult;
    // 语音听写UI
    private RecognizerDialog mIatDialog;

    private Toast mToast;
    private static String TAG = MainActivity.class.getSimpleName();

    private void showTip(final String str) {
        mToast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        mToast.show();
    }

    Calendar dateFrom;
    Calendar dateTo;

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dateFrom = Calendar.getInstance();
        dateTo = Calendar.getInstance();
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(MainActivity.this, mInitListener);
        mIatDialog.setParameter(SpeechConstant.ASR_PTT, "0");
        mIatDialog.setListener(mRecognizerDialogListener);


    }

    @OnFocusChange({R.id.et_jine, R.id.et_nianlilv, R.id.et_yuelilv, R.id.et_tiaozhengtianshu})
    void focusChanged(View view,boolean hasFocus) {
        if (view.getId() == R.id.et_nianlilv && !hasFocus){
            DecimalFormat df = new DecimalFormat("0.000");
            Double nlilv = NumberUtils.toDouble(etNianlilv.getText().toString());
            if (nlilv > 0){
                etYuelilv.setText(df.format(nlilv * 1.2));
            }
        }else if (view.getId() == R.id.et_yuelilv && !hasFocus){
            DecimalFormat df = new DecimalFormat("0.00");
            Double ylilv = NumberUtils.toDouble(etYuelilv.getText().toString());
            if (ylilv > 0){
                etNianlilv.setText(df.format(ylilv / 1.2));
            }
        }
    }


    @OnClick({R.id.et_tiexianriqi, R.id.et_daoqiriqi})
    void getDate(View v) {
        Calendar calendar = Calendar.getInstance();
        if (v.getId() == R.id.et_tiexianriqi){
            calendar = dateFrom;
        }else if (v.getId() == R.id.et_daoqiriqi){
            calendar = dateTo;
        }
        DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        c.set(year,month,dayOfMonth,0,0,0);
                        c.set(Calendar.MILLISECOND,0);
                        String text = FastDateFormat.getInstance("yyyyMMdd").format(c);
                        if (v.getId() == R.id.et_tiexianriqi){
                            dateFrom = c;
                            etTiexianriqi.setText(text);
                        }else if (v.getId() == R.id.et_daoqiriqi){
                            dateTo = c;
                            etDaoqiriqi.setText(text);
                        }
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    int mCurrentBt = 0;

    @OnClick({R.id.bt_jine, R.id.bt_nianlilv, R.id.bt_yuelilv, R.id.bt_tiexianriqi, R.id.bt_daoqiriqi, R.id.bt_tiaozhengtianshu,R.id.bt_mswsxf})
    void startVoice(View view) {
        mCurrentBt = view.getId();
        mIatDialog.show();
    }

    @OnClick(R.id.bt_submit)
    void dealIt() {
        if (TextUtils.isEmpty(etJine.getText())) {
            showTip("请输入金额");
        } else if (TextUtils.isEmpty(etNianlilv.getText()) && TextUtils.isEmpty(etYuelilv.getText())) {
            showTip("请输入利率");
        } else if (TextUtils.isEmpty(etTiexianriqi.getText())) {
            showTip("请输入贴现日期");
        } else if (TextUtils.isEmpty(etDaoqiriqi.getText())) {
            showTip("请输入到期日期");
        } else {
            Double jine = NumberUtils.toDouble(etJine.getText().toString());
            Double nlilv = NumberUtils.toDouble(etNianlilv.getText().toString());
            Double ylilv = NumberUtils.toDouble(etYuelilv.getText().toString());
            Integer tzts = NumberUtils.toInt(etTiaozhengtianshu.getText().toString());
            Double sxf = NumberUtils.toDouble(etMswsxf.getText().toString());
            MutablePair<Integer,Integer> tzrq = getTzrq();
            int tztsTemp = tzrq.left + tzts;
            int jxtxTemp = tzrq.right + tzts;
            if (!swPjlx.isChecked()){
                tztsTemp += 3;
                jxtxTemp += 3;
            }
            double txlx = jine * jxtxTemp * nlilv / 3.6 + jine * sxf / 10;
            double tzje = jine * 10000 - txlx;
            double mswjg = txlx * 10 / jine;
//            showTip(txlx+"--"+tzje+"--"+mswjg);
            DecimalFormat df = new DecimalFormat("0.000");

            String htmlText =
                    "计息天数：<big><font color=\"red\">"+jxtxTemp+"</font></big>天<br>"+
                    "调整天数：<big><font color=\"red\">"+tztsTemp+"</font></big>天<br>"+
                    "贴现利息：<big><font color=\"red\">"+new BigDecimal(txlx).setScale(2, BigDecimal.ROUND_HALF_UP)+"</font></big>元<br>"+
                    "贴现金额：<big><font color=\"red\">"+new BigDecimal(tzje).setScale(2, BigDecimal.ROUND_HALF_UP)+"</font></big>元<br>"+
                    "每十万价：<big><font color=\"red\">"+new BigDecimal(mswjg).setScale(2, BigDecimal.ROUND_HALF_UP)+"</font></big>元<br>"
                    ;
            tvResult.setText(Html.fromHtml(htmlText));
        }
    }

    @OnClick(R.id.bt_reset)
    void resetEts() {
        swPjlx.setChecked(true);
        etJine.setText("");
        etNianlilv.setText("");
        etYuelilv.setText("");
        etTiexianriqi.setText("");
        etDaoqiriqi.setText("");
        etTiaozhengtianshu.setText("");
        mCurrentBt = -1;
    }

    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            String result = getRecongnizeResult(results);
            Log.e(TAG, "re:--" + result);
            String re = "";
            if (!TextUtils.isEmpty(result)) {
//                showTip(result);
                try {
                    switch (mCurrentBt) {
                        case R.id.bt_jine:
                            Log.e(TAG, "bt_jine");
                            re = result;
//                            re = LDataFormat.format(result);
                            etJine.setText(re);
                            break;
                        case R.id.bt_nianlilv:
                            Log.e(TAG, "bt_nianlilv");
                            re = result;
                            DecimalFormat df = new DecimalFormat("0.000");
                            double nlilv = NumberUtils.toDouble(re);
                            if (nlilv > 0){
                                etYuelilv.setText(df.format(nlilv * 1.2));
                                etNianlilv.setText(re);
                            }
                            break;
                        case R.id.bt_yuelilv:
                            Log.e(TAG, "bt_yuelilv");
                            re = result;
                            DecimalFormat dfa = new DecimalFormat("0.00");
                            double ylilv = NumberUtils.toDouble(re);
                            if (ylilv > 0){
                                etYuelilv.setText(re);
                                etNianlilv.setText(dfa.format(ylilv / 1.2));
                            }
                            break;
                        case R.id.bt_tiexianriqi:
                            Log.e(TAG, "bt_tiexianriqi");
                            re = result;
                            try {
                                dateFrom.setTime(FastDateFormat.getInstance("yyyyMMdd").parse(re));
                                etTiexianriqi.setText(re);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.bt_daoqiriqi:
                            Log.e(TAG, "bt_daoqiriqi");
                            re = result;
                            try {
                                dateTo.setTime(FastDateFormat.getInstance("yyyyMMdd").parse(re));
                                etDaoqiriqi.setText(re);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.bt_tiaozhengtianshu:
                            Log.e(TAG, "bt_tiaozhengtianshu");
                            etTiaozhengtianshu.setText(result);
                            break;
                        case R.id.bt_mswsxf:
                            Log.e(TAG, "bt_mswsxf");
                            etMswsxf.setText(result);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

    };

    private String getRecongnizeResult(RecognizerResult results) {
        String result = "";

        // 读取json结果中的sn字段
        try {
            HashMap<String, String> latResults = new LinkedHashMap<String, String>();
            String text = JsonParser.parseIatResult(results.getResultString());

            JSONObject resultJson = new JSONObject(results.getResultString());
            String sn = resultJson.optString("sn");
            latResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : latResults.keySet()) {
                resultBuffer.append(latResults.get(key));
            }
            result = resultBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.setFocusable(false);
                    v.setFocusableInTouchMode(true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    String[] workdays = {"2018-02-11",
            "2018-02-24",
            "2018-04-08",
            "2018-04-28",
            "2018-09-29",
            "2018-09-30",
            "2018-12-29",
            "2019-02-02",
            "2019-02-03",
            "2019-04-28",
            "2019-05-05",
            "2019-09-29",
            "2019-10-12",
            "2020-01-19",
            "2020-02-01",
            "2020-06-28",
            "2020-10-10"
    };
    HashMap<String ,String> holidays = new HashMap<String ,String>(){
        {
            put("2017-12-30","2018-01-02");
            put("2017-12-31","2018-01-02");
            put("2018-01-01","2018-01-02");
            put("2018-02-15","2018-02-22");
            put("2018-02-16","2018-02-22");
            put("2018-02-17","2018-02-22");
            put("2018-02-18","2018-02-22");
            put("2018-02-19","2018-02-22");
            put("2018-02-20","2018-02-22");
            put("2018-02-21","2018-02-22");
            put("2018-04-05","2018-04-08");
            put("2018-04-06","2018-04-08");
            put("2018-04-07","2018-04-08");
            put("2018-04-29","2018-05-02");
            put("2018-04-30","2018-05-02");
            put("2018-05-01","2018-05-02");
            put("2018-06-16","2018-06-19");
            put("2018-06-17","2018-06-19");
            put("2018-06-18","2018-06-19");
            put("2018-09-22","2018-09-25");
            put("2018-09-23","2018-09-25");
            put("2018-09-24","2018-09-25");
            put("2018-10-01","2018-10-08");
            put("2018-10-02","2018-10-08");
            put("2018-10-03","2018-10-08");
            put("2018-10-04","2018-10-08");
            put("2018-10-05","2018-10-08");
            put("2018-10-06","2018-10-08");
            put("2018-10-07","2018-10-08");
            put("2018-12-30","2019-01-02");
            put("2018-12-31","2019-01-02");
            put("2019-01-01","2019-01-02");
            put("2019-02-04","2019-02-11");
            put("2019-02-05","2019-02-11");
            put("2019-02-06","2019-02-11");
            put("2019-02-07","2019-02-11");
            put("2019-02-08","2019-02-11");
            put("2019-02-09","2019-02-11");
            put("2019-02-10","2019-02-11");
            put("2019-04-05","2019-04-08");
            put("2019-04-06","2019-04-08");
            put("2019-04-07","2019-04-08");
            put("2019-04-27","2019-04-28");
            put("2019-05-01","2019-05-05");
            put("2019-05-02","2019-05-05");
            put("2019-05-03","2019-05-05");
            put("2019-05-04","2019-05-05");
            put("2019-06-07","2019-06-10");
            put("2019-06-08","2019-06-10");
            put("2019-06-09","2019-06-10");
            put("2019-09-13","2019-09-16");
            put("2019-09-14","2019-09-16");
            put("2019-09-15","2019-09-16");
            put("2019-10-01","2019-10-08");
            put("2019-10-02","2019-10-08");
            put("2019-10-03","2019-10-08");
            put("2019-10-04","2019-10-08");
            put("2019-10-05","2019-10-08");
            put("2019-10-06","2019-10-08");
            put("2019-10-07","2019-10-08");
            put("2020-01-01","2020-01-02");
            put("2020-01-24","2020-01-31");
            put("2020-01-25","2020-01-31");
            put("2020-01-26","2020-01-31");
            put("2020-01-27","2020-01-31");
            put("2020-01-28","2020-01-31");
            put("2020-01-29","2020-01-31");
            put("2020-01-30","2020-01-31");
            put("2020-04-04","2020-04-07");
            put("2020-04-05","2020-04-07");
            put("2020-04-06","2020-04-07");
            put("2020-05-01","2020-05-04");
            put("2020-05-02","2020-05-04");
            put("2020-05-03","2020-05-04");
            put("2020-06-25","2020-06-28");
            put("2020-06-26","2020-06-28");
            put("2020-06-27","2020-06-28");
            put("2020-10-01","2020-10-08");
            put("2020-10-02","2020-10-08");
            put("2020-10-03","2020-10-08");
            put("2020-10-04","2020-10-08");
            put("2020-10-05","2020-10-08");
            put("2020-10-06","2020-10-08");
            put("2020-10-07","2020-10-08");
        }
    };

    MutablePair<Integer,Integer> getTzrq(){
        MutablePair<Integer,Integer> result = new MutablePair<Integer,Integer>(0,0);
        String endDate = DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(dateTo);
        String endDateChanged = endDate;
        boolean isholiday = false;
        boolean isworkday = false;
        if (dateTo != null){
            if (ArrayUtils.contains(workdays,endDate)){
                isworkday = true;
            }else if (holidays.containsKey(endDate)){
                isholiday = true;
                endDateChanged = holidays.get(endDate);
            }
            try {
                result.right = (int) ((DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.parse(endDateChanged).getTime()-dateFrom.getTimeInMillis())/(24 * 60 * 60 * 1000));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!isworkday && !isholiday){
                switch (dateTo.get(Calendar.DAY_OF_WEEK)){
                    case Calendar.SATURDAY:
                        result.left += 2;
                        result.right += result.left;
                        break;
                    case Calendar.SUNDAY:
                        result.left += 1;
                        result.right += result.left;
                        break;
                }
            }else if (isholiday){
                try {
                    Date dateChanged = DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.parse(endDateChanged);
                    result.left = (int) ((dateChanged.getTime()-dateTo.getTimeInMillis())/(24 * 60 * 60 * 1000));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        showTip("请允许权限，否则应用将无法正常使用");
    }
}
