package hw.txtreader;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bifan.txtreaderlib.main.LoadListenerAdapter;
import com.bifan.txtreaderlib.main.TxtReaderView;

/**
 * created by ： bifan-wei
 */
public class MoreDisplayActivity extends AppCompatActivity {
    private final int[] backgroundColors = new int[]{
            Color.parseColor("#ccebcc"),
            Color.parseColor("#d4c7a5"),
            Color.parseColor("#393330"),
            Color.parseColor("#00141f"),
    };
    private final int[] textColors = new int[]{
            Color.parseColor("#505550"),
            Color.parseColor("#453e33"),
            Color.parseColor("#8f8e88"),
            Color.parseColor("#27576c")
    };

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.activity_display_more);
        setReaderView((TxtReaderView) findViewById(R.id.txtReaderView_g_1), backgroundColors[0], textColors[0], 20);
        setReaderView((TxtReaderView) findViewById(R.id.txtReaderView_g_2), backgroundColors[1], textColors[1], 200);
        setReaderView((TxtReaderView) findViewById(R.id.txtReaderView_g_3), backgroundColors[2], textColors[2], 400);
        setReaderView((TxtReaderView) findViewById(R.id.txtReaderView_g_4), backgroundColors[3], textColors[3], 600);
    }

    private void setReaderView(final TxtReaderView readerView, final int background, final int textColor, long delatLoadTime) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String path = getIntent().getStringExtra("filePath");
                readerView.loadTxtFile(path, new LoadListenerAdapter() {
                    @Override
                    public void onSuccess() {
                        readerView.setStyle(background, textColor);
                        //readerView.setPageSwitchByTranslate();
                    }
                });
            }
        }, delatLoadTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
