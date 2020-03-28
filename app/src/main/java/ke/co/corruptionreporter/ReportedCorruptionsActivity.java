package ke.co.corruptionreporter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ReportedCorruptionsActivity extends AppCompatActivity {

    private CorruptionsAdapter adapter;
    private List<CorruptionObject> records;
    private RecyclerView recyclerView;
    private TextView generic_empty_view;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_corruptions);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( ReportedCorruptionsActivity.this, ReportCorruptionActivity.class));
            }
        });

        recyclerView = findViewById(R.id.recycler);
        generic_empty_view = findViewById(R.id.generic_empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        loadCorruptions();

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String description = adapter.get(position).getDescription() + "- " + adapter.get(position).getCounty();
                share(description);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void loadCorruptions(){

        records = new ArrayList<>();

        RealmResults<Corruption> allRecords = realm.where(Corruption.class).findAll().sort("id", Sort.DESCENDING);

        if (!allRecords.isEmpty()){

            for (Corruption record : allRecords){

                CorruptionObject recordObject = new CorruptionObject();

                recordObject.setId(record.getId());
                recordObject.setCounty(record.getLocation());
                recordObject.setDate(Helpers.getCurrentDateFormatted());
                recordObject.setDescription(record.getTitle());

                records.add(recordObject);

            }
        }

        adapter = new CorruptionsAdapter(this, records);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        if (adapter.getItemCount() > 0){

            recyclerView.setVisibility(View.VISIBLE);
            generic_empty_view.setVisibility(View.GONE);
        }

    }

    private void share(String message){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "From Corruption Reporter");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }
}
