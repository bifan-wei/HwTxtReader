package hw.txtreader;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bifan.txtreaderlib.main.TxtConfig;
import com.bifan.txtreaderlib.ui.HwTxtPlayActivity;

import java.io.File;

public class DemoActivity extends AppCompatActivity {
    private String tag = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0x01;
    private String FilePath = Environment.getExternalStorageDirectory() + "/test4.txt";
    private Boolean Permit = false;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.setText(FilePath);
        if (CheckPermission()) {
            Permit = true;
        } else {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String[] pros = {MediaStore.Files.FileColumns.DATA};
            try {
                Cursor cursor = managedQuery(uri, pros, null, null, null);
                int actual_txt_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(actual_txt_column_index);
                mEditText.setText(path);
            } catch (Exception e) {
                toast("选择出错了");
            }
        }
    }

    public void chooseFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");//设置类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 3);
    }

    public void loadFile(View view) {
        if (Permit) {
            TxtConfig.saveIsOnVerticalPageMode(this,false);
            FilePath = mEditText.getText().toString().trim();
            if (TextUtils.isEmpty(FilePath) || !(new File(FilePath)).exists()) {
                toast("文件不存在");
            } else {
                HwTxtPlayActivity.loadTxtFile(this, FilePath);
            }
        }
    }

    public void onVerticalMode(View view) {
        if (Permit) {
            FilePath = mEditText.getText().toString().trim();
            if (TextUtils.isEmpty(FilePath) || !(new File(FilePath)).exists()) {
                toast("文件不存在");
            } else {
                TxtConfig.saveIsOnVerticalPageMode(this,true);
                HwTxtPlayActivity.loadTxtFile(this, FilePath);
            }
        }
    }

    public void loadStr(View view) {
        if (Permit) {
            String str = mEditText.getText().toString();
            if (TextUtils.isEmpty(str)) {
                toast("输入为空字符");
            } else {
                HwTxtPlayActivity.loadStr(this, str);
            }
        }
    }


    public void displayMore(View view) {
        if (Permit) {
            FilePath = mEditText.getText().toString().trim();
            if (TextUtils.isEmpty(FilePath) || !(new File(FilePath)).exists()) {
                toast("文件不存在");
            } else {
                Intent intent = new Intent(this, MoreDisplayActivity.class);
                intent.putExtra("filePath", FilePath);
                startActivity(intent);
            }
        }
    }

    private Boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Permit = true;
                loadFile(null);
            } else {
                // Permission Denied
                Toast.makeText(DemoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private Toast t;

    private void toast(String msg) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        t.show();
    }
}
