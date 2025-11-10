package com.example.crossword.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crossword.R;
import com.example.crossword.model.CrosswordClue;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter that displays crossword clues.
 */
public class ClueAdapter extends RecyclerView.Adapter<ClueAdapter.ClueViewHolder> {

    private final List<CrosswordClue> clues = new ArrayList<>();

    public void submitClues(List<CrosswordClue> newClues) {
        clues.clear();
        clues.addAll(newClues);
        notifyDataSetChanged();
    }

    public void notifyProgressChanged() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clue, parent, false);
        return new ClueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClueViewHolder holder, int position) {
        holder.bind(clues.get(position));
    }

    @Override
    public int getItemCount() {
        return clues.size();
    }

    static class ClueViewHolder extends RecyclerView.ViewHolder {
        private final TextView numberView;
        private final TextView clueView;

        ClueViewHolder(@NonNull View itemView) {
            super(itemView);
            numberView = itemView.findViewById(R.id.clueNumber);
            clueView = itemView.findViewById(R.id.clueText);
        }

        void bind(CrosswordClue clue) {
            numberView.setText(String.valueOf(clue.getNumber()));
            clueView.setText(clue.getText());

            int colorRes = clue.isSolved() ? R.color.grid_match : android.R.color.black;
            clueView.setTextColor(ContextCompat.getColor(clueView.getContext(), colorRes));
            numberView.setTextColor(ContextCompat.getColor(numberView.getContext(), colorRes));
        }
    }
}
