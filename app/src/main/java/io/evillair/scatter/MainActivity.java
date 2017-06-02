package io.evillair.scatter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private final int SELECT_FILE = 1;
    Spinner mediaTypeSpinner;

    TextView uriView;

    private Uri fileUri;

    private final int TEXT = 1;
    private final int IMAGE = 2;
    private final int VIDEO = 3;
    private final int AUDIO = 4;
    private final int EXT_LINK = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mediaTypeSpinner = (Spinner)findViewById(R.id.mediaTypeSpinner);
        uriView = (TextView)findViewById(R.id.uriLabel);

        Button fileButton = (Button)findViewById(R.id.fileButton);
        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mediaIndex = mediaTypeSpinner.getSelectedItemPosition() + 1;
                Intent intent = null;

                if (mediaIndex == IMAGE) {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                } else if (mediaIndex == VIDEO) {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                } else if (mediaIndex == AUDIO) {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                }

                if (intent != null) {
                    startActivityForResult(intent, SELECT_FILE);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select a media type first...", Toast.LENGTH_SHORT).show();
                }

            }
        });



        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            } else if (type.startsWith("video/")) {
                handleSendImage(intent); // Handle single image being sent
            } else if (type.startsWith("audio/")) {
                handleSendImage(intent); // Handle single image being sent
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                fileUri = Uri.parse(data.toUri(0));
                uriView.setText("File selected...");
            }
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            Toast.makeText(getApplicationContext(), sharedText, Toast.LENGTH_SHORT).show();
            uriView.setText(sharedText);
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            uriView.setText(imageUri.toString());
            Toast.makeText(getApplicationContext(), imageUri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
