package jamk.l3326.excercise3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSearchClick(View view) {
        Intent intent = new Intent(MainActivity.this, ShowImages.class);
        intent.putExtra("tags", ((EditText)findViewById(R.id.editText)).getText().toString());
        startActivity(intent);
    }
}
