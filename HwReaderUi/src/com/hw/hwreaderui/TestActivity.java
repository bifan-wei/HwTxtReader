package com.hw.hwreaderui;

import com.example.hwreaderui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.txt_testactivity_layout);
		Button button = (Button) findViewById(R.id.txt_testactivity_loadbook);
		final EditText editText = (EditText) findViewById(R.id.txt_testactivity_edittext);
		String path = Environment.getExternalStorageDirectory() + "/test1.txt";
		editText.setText(path);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.putExtra("bookname", "testbookname");
				intent.putExtra("bookpath", editText.getText().toString());
				intent.setClass(TestActivity.this, HwReaderPlayActivity.class);
				TestActivity.this.startActivity(intent);

			}
		});
	}

}
