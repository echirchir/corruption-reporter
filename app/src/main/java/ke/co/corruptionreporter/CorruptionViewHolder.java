package ke.co.corruptionreporter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CorruptionViewHolder extends RecyclerView.ViewHolder {

    public TextView county;

    public TextView date;

    public TextView description;

    public CorruptionViewHolder(@NonNull View itemView) {
        super(itemView);

        date = itemView.findViewById(R.id.date);
        county = itemView.findViewById(R.id.county);
        description = itemView.findViewById(R.id.description);
    }
}
