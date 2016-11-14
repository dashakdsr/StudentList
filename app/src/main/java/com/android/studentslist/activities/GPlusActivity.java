package com.android.studentslist.activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.studentslist.BroadcastToaster;
import com.android.studentslist.BuildConfig;
import com.android.studentslist.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GPlusActivity extends AppCompatActivity implements View.OnClickListener {
    final String LOG_SPAWNER = getClass().getSimpleName();

    final BroadcastToaster broadcastToaster = new BroadcastToaster();
    String user_id;
    static final int ACTION_REQUEST_GALLERY = 0;
    static final int ACTION_REQUEST_CAMERA = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gplus_activity);
        String path = getIntent().getData().getPath();
        user_id = path.substring(1, path.length());
        new FetchGPlusInfo().execute();
        findViewById(R.id.image_gplus).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(broadcastToaster, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastToaster);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_gplus: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Image Source");
                builder.setItems(new CharSequence[]{"Gallery", "Camera"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("image/*");
                                        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                                        startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
                                        break;
                                    case 1:
                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(takePictureIntent, ACTION_REQUEST_CAMERA);
                                        break;
                                }
                            }
                        });
                builder.show();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ImageView imageView = (ImageView) findViewById(R.id.image_gplus);
            switch (requestCode) {
                case ACTION_REQUEST_GALLERY:
                    Picasso.with(getBaseContext()).load(data.getData()).into(imageView);
                    break;
                case ACTION_REQUEST_CAMERA:
                    imageView.setImageBitmap((Bitmap) data.getExtras().get("data"));
                    break;
            }
        }
    }

    public class FetchGPlusInfo extends AsyncTask<Void, Void, String[]> {
        final private String LOG_SPAWNER = FetchGPlusInfo.class.getSimpleName();

        @Override
        protected String[] doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                urlConnection = (HttpURLConnection)
                        new URL(Uri.parse("https://www.googleapis.com/plus/v1/people").buildUpon()
                                .appendPath(user_id)
                                .appendQueryParameter("key", BuildConfig.G_API_KEY)
                                .build().toString()).openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                if (buffer.length() == 0) {
                    return null;
                }
                JSONObject InfoJSONObj = new JSONObject(buffer.toString());
                String url = InfoJSONObj.getJSONObject("image").getString("url");
                String displayName = InfoJSONObj.getString("displayName");
                return new String[]{url.substring(0, url.lastIndexOf("?")), displayName};
            } catch (IOException e) {
                Log.e(LOG_SPAWNER, "error", e);
                return null;
            } catch (JSONException e) {
                Log.e(LOG_SPAWNER, "error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_SPAWNER, "error", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            Picasso.with(getBaseContext()).load(result[0]).into((ImageView) findViewById(R.id.image_gplus));
            ((TextView) findViewById(R.id.text_gplus)).setText(result[1]);
        }
    }
}
