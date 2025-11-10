package com.example.crossword.ui;

import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crossword.R;
import com.example.crossword.model.CrosswordCell;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter that renders the crossword grid.
 */
public class CrosswordGridAdapter extends RecyclerView.Adapter<CrosswordGridAdapter.CellViewHolder> {

    public interface OnCellValueChangedListener {
        void onCellValueChanged(int row, int column, String value);
    }

    private final List<CellState> cells = new ArrayList<>();
    private final OnCellValueChangedListener listener;

    public CrosswordGridAdapter(OnCellValueChangedListener listener) {
        this.listener = listener;
    }

    public void submitCells(List<CellState> newCells) {
        cells.clear();
        cells.addAll(newCells);
        notifyDataSetChanged();
    }

    public void updateCellStatus(int position, CellStatus status) {
        if (position >= 0 && position < cells.size()) {
            cells.get(position).setStatus(status);
            notifyItemChanged(position);
        }
    }

    public CellState getCellState(int position) {
        return cells.get(position);
    }

    @NonNull
    @Override
    public CellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crossword_cell, parent, false);
        return new CellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CellViewHolder holder, int position) {
        CellState state = cells.get(position);
        CrosswordCell cell = state.getCell();

        holder.bind(cell, state, listener);
    }

    @Override
    public int getItemCount() {
        return cells.size();
    }

    static class CellViewHolder extends RecyclerView.ViewHolder {
        private final TextView numberView;
        private final EditText inputView;
        private final View container;
        private TextWatcher watcher;

        CellViewHolder(@NonNull View itemView) {
            super(itemView);
            numberView = itemView.findViewById(R.id.cellNumber);
            inputView = itemView.findViewById(R.id.cellInput);
            container = itemView;

            inputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        }

        void bind(CrosswordCell cell, CellState state, OnCellValueChangedListener listener) {
            if (watcher != null) {
                inputView.removeTextChangedListener(watcher);
            }

            if (cell.isBlock()) {
                inputView.setVisibility(View.INVISIBLE);
                numberView.setVisibility(View.INVISIBLE);
                container.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.grid_blocked));
                return;
            }

            inputView.setVisibility(View.VISIBLE);
            numberView.setVisibility(cell.getNumber() > 0 ? View.VISIBLE : View.INVISIBLE);
            if (cell.getNumber() > 0) {
                numberView.setText(String.valueOf(cell.getNumber()));
            }

            String value = state.getValue();
            if (value == null) {
                value = "";
            }
            if (!value.equals(inputView.getText().toString())) {
                inputView.setText(value);
            }

            updateBackground(state.getStatus());

            watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String letter = s.toString().trim().toUpperCase();
                    if (!letter.equals(s.toString())) {
                        inputView.removeTextChangedListener(this);
                        inputView.setText(letter);
                        if (!letter.isEmpty()) {
                            inputView.setSelection(letter.length());
                        }
                        inputView.addTextChangedListener(this);
                    }
                    listener.onCellValueChanged(cell.getRow(), cell.getColumn(), letter);
                }
            };

            inputView.addTextChangedListener(watcher);
        }

        private void updateBackground(CellStatus status) {
            int colorRes;
            switch (status) {
                case CORRECT:
                    colorRes = R.color.grid_match;
                    break;
                case INCORRECT:
                    colorRes = R.color.grid_mismatch;
                    break;
                case HINTED:
                    colorRes = R.color.teal_200;
                    break;
                case DEFAULT:
                default:
                    colorRes = R.color.grid_filled;
                    break;
            }

            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(ContextCompat.getColor(container.getContext(), colorRes));
            drawable.setStroke(2, ContextCompat.getColor(container.getContext(), R.color.grid_border));
            container.setBackground(drawable);
        }
    }

    public static class CellState {
        private final CrosswordCell cell;
        private String value;
        private CellStatus status;

        public CellState(CrosswordCell cell) {
            this.cell = cell;
            this.status = CellStatus.DEFAULT;
        }

        public CrosswordCell getCell() {
            return cell;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public CellStatus getStatus() {
            return status;
        }

        public void setStatus(CellStatus status) {
            this.status = status;
        }
    }
}
