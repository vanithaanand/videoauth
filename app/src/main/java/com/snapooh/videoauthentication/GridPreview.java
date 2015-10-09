package com.snapooh.videoauthentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.snapooh.videoauthentication.ui.FloatingActionButton;
import com.snapooh.videoauthentication.ui.FloatingActionMenu;

import java.io.File;
import java.util.List;

public class GridPreview extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = GridPreview.class.getSimpleName();
    private RecyclerView recyclerView;
    private PreviewAdapter previewAdapter;
    private FloatingActionMenu menu1;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            final List<Media> mediaList = bundle.getParcelableArrayList("media");
            Toast.makeText(this,"size: "+mediaList.size(),Toast.LENGTH_SHORT).show();

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            previewAdapter = new PreviewAdapter(this,mediaList);
            previewAdapter.setOnItemClickListener(new PreviewAdapter.OnItemClickListener() {
                @Override
                public void onClick(View v, int p) {
                   /* Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse(mediaList.get(p).getUri());
                    intent.setDataAndType(data, "video*//*");
                    startActivity(intent);*/
                    Toast.makeText(GridPreview.this,"data "+
                            mediaList.get(p).getUri(),Toast.LENGTH_SHORT).show();
                    File file=new File(mediaList.get(p).getUri());
                    if(file.exists()){
                        Toast.makeText(GridPreview.this,"exists",Toast.LENGTH_SHORT).show();
                        String path=mediaList.get(p).getUri();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                        intent.setDataAndType(Uri.parse(path), "video/mp4");
                        startActivity(intent);
                    }
                }
            });
            recyclerView.setAdapter(previewAdapter);


            menu1 = (FloatingActionMenu) findViewById(R.id.menu1);
            fab1 = (FloatingActionButton) findViewById(R.id.fab1);
            fab2 = (FloatingActionButton) findViewById(R.id.fab2);
            fab1.setOnClickListener(this);
            fab2.setOnClickListener(this);

        }else{
            Toast.makeText(this,"null",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preview, menu);
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


    @Override
    public void onBackPressed() {

        if(menu1!=null&&menu1.isOpened()){
            menu1.close(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab1:
                // TODO: photo

                break;
            case R.id.fab2:
                 // TODO: video

                break;
        }
    }


}
