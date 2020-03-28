package ke.co.corruptionreporter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CorruptionsAdapter extends RecyclerView.Adapter<CorruptionViewHolder> {

    private List<CorruptionObject> recordObjects;
    private Context context;

    public CorruptionsAdapter(Context context, List<CorruptionObject> records){
        this.context = context;
        this.recordObjects = records;
    }

    @NonNull
    @Override
    public CorruptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.corruption_layout_card, parent, false);
        return new CorruptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CorruptionViewHolder holder, final int position) {

        CorruptionObject record = recordObjects.get(position);

        holder.description.setText(record.getDescription());
        holder.county.setText(record.getCounty());
        holder.date.setText(record.getDate());

    }

    public CorruptionObject get(int position){

        return recordObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return recordObjects.size();
    }

}
