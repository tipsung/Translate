package tipsung.example.com.translate;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;



public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyD6Rm3n8Ygq6wygh2515sqiRo60D1i24kc";
    String targetLanguage;
    Handler textViewHandler;
    TextView textView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerTarget);
        final String[] target = getResources().getStringArray(R.array.Target_arrays);
        ArrayAdapter<String> adapterTarget = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, target);
        spinner.setAdapter(adapterTarget);

        final EditText editText = (EditText) findViewById(R.id.editText);
        final Button button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        textViewHandler = new Handler();



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //String selectedItem = parent.getItemAtPosition(position).toString();
               //targetLanguage = selectedItem;

                String target = parent.getItemAtPosition(position).toString();
                if( target.equals("Thai")) {
                    targetLanguage = "th";
                }
                if( target.equals("English")) {
                    targetLanguage = "en";
                }
                if( target.equals("Catalan")) {
                    targetLanguage = "ca";
                }
                if( target.equals("Chinese (Simplified)")) {
                    targetLanguage = "zh-CN";
                }
                if( target.equals("Japanese")) {
                    targetLanguage = "ja";
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

//editText.setOnClickListener();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TranslateText().execute(editText.getText().toString());
            }
        });
    }

    private class TranslateText extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Translating...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            TranslateOptions options = TranslateOptions.newBuilder()
                    .setApiKey(API_KEY)
                    .build();
            Translate translate = options.getService();
            final Translation translation =
                    translate.translate(params[0], //ส่งตัวแปรแรกที่เราต้องการแปล
                            Translate.TranslateOption.targetLanguage(targetLanguage));  //ภาษาที่เราแปลเสร็จ
            textViewHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (textView != null) {
                        textView.setText(translation.getTranslatedText());
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void arg) {
            super.onPostExecute(arg);
            if(progressDialog.isShowing()) { progressDialog.dismiss(); }
        }
    }
}
