package com.example.crossword.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crossword.R;
import com.example.crossword.model.CrosswordCell;
import com.example.crossword.model.CrosswordClue;
import com.example.crossword.model.CrosswordPuzzle;
import com.example.crossword.model.PuzzleData;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CrosswordGridAdapter.OnCellValueChangedListener {

    private CrosswordPuzzle puzzle;
    private CrosswordGridAdapter gridAdapter;
    private ClueAdapter acrossAdapter;
    private ClueAdapter downAdapter;
    private final List<CrosswordGridAdapter.CellState> cellStates = new ArrayList<>();

    private char[][] userEntries;

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(android.R.id.content);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        puzzle = PuzzleData.createSatorSquarePuzzle();
        userEntries = new char[puzzle.getRows()][puzzle.getColumns()];

        setupGrid();
        setupClues();
        setupActions();
    }

    private void setupGrid() {
        RecyclerView gridView = findViewById(R.id.crosswordGrid);
        gridView.setLayoutManager(new GridLayoutManager(this, puzzle.getColumns()));

        gridAdapter = new CrosswordGridAdapter(this);
        gridView.setAdapter(gridAdapter);

        buildInitialCellStates();
        gridAdapter.submitCells(cellStates);
    }

    private void setupClues() {
        acrossAdapter = new ClueAdapter();
        RecyclerView acrossList = findViewById(R.id.acrossClues);
        acrossList.setLayoutManager(new LinearLayoutManager(this));
        acrossList.setAdapter(acrossAdapter);
        acrossAdapter.submitClues(puzzle.getAcrossClues());

        downAdapter = new ClueAdapter();
        RecyclerView downList = findViewById(R.id.downClues);
        downList.setLayoutManager(new LinearLayoutManager(this));
        downList.setAdapter(downAdapter);
        downAdapter.submitClues(puzzle.getDownClues());
    }

    private void setupActions() {
        ExtendedFloatingActionButton hintButton = findViewById(R.id.hintButton);
        ExtendedFloatingActionButton checkButton = findViewById(R.id.checkButton);

        hintButton.setOnClickListener(v -> provideHint());
        checkButton.setOnClickListener(v -> checkAnswers());
    }

    private void buildInitialCellStates() {
        cellStates.clear();
        for (CrosswordCell cell : puzzle.getAllCells()) {
            CrosswordGridAdapter.CellState state = new CrosswordGridAdapter.CellState(cell);
            state.setValue(cell.isBlock() ? null : "");
            cellStates.add(state);
        }
    }

    @Override
    public void onCellValueChanged(int row, int column, String value) {
        int index = toIndex(row, column);
        CrosswordGridAdapter.CellState state = cellStates.get(index);
        state.setValue(value);

        if (state.getStatus() != CellStatus.HINTED) {
            state.setStatus(CellStatus.DEFAULT);
            gridAdapter.notifyItemChanged(index);
        }

        if (value == null || value.isEmpty()) {
            userEntries[row][column] = '\0';
        } else {
            userEntries[row][column] = value.charAt(0);
        }

        updateClueSolvedStates();
    }

    private void checkAnswers() {
        boolean hasMistake = false;
        boolean allFilled = true;

        for (int i = 0; i < cellStates.size(); i++) {
            CrosswordGridAdapter.CellState state = cellStates.get(i);
            CrosswordCell cell = state.getCell();
            if (cell.isBlock()) {
                continue;
            }

            char solution = Character.toUpperCase(cell.getSolution());
            String value = state.getValue();
            if (value == null || value.isEmpty()) {
                state.setStatus(CellStatus.DEFAULT);
                allFilled = false;
            } else if (value.charAt(0) == solution) {
                if (state.getStatus() != CellStatus.HINTED) {
                    state.setStatus(CellStatus.CORRECT);
                }
            } else {
                state.setStatus(CellStatus.INCORRECT);
                hasMistake = true;
            }
            gridAdapter.notifyItemChanged(i);
        }

        updateClueSolvedStates();

        if (!hasMistake && allFilled && areAllCellsSolved()) {
            Snackbar.make(rootView, R.string.message_puzzle_complete, Snackbar.LENGTH_LONG).show();
        } else if (hasMistake) {
            Snackbar.make(rootView, R.string.message_incorrect_letters, Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(rootView, R.string.message_keep_going, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void provideHint() {
        for (int i = 0; i < cellStates.size(); i++) {
            CrosswordGridAdapter.CellState state = cellStates.get(i);
            CrosswordCell cell = state.getCell();
            if (cell.isBlock()) {
                continue;
            }

            char solution = Character.toUpperCase(cell.getSolution());
            String value = state.getValue();
            boolean matches = value != null && value.length() == 1 && value.charAt(0) == solution;
            if (!matches) {
                String letter = String.valueOf(solution);
                state.setValue(letter);
                state.setStatus(CellStatus.HINTED);
                userEntries[cell.getRow()][cell.getColumn()] = solution;
                gridAdapter.notifyItemChanged(i);
                updateClueSolvedStates();
                Snackbar.make(rootView,
                        getString(R.string.message_hint_revealed, cell.getRow() + 1, cell.getColumn() + 1, letter),
                        Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }
        }

        Snackbar.make(rootView, R.string.message_no_hints, Snackbar.LENGTH_SHORT).show();
    }

    private void updateClueSolvedStates() {
        updateClueList(puzzle.getAcrossClues());
        updateClueList(puzzle.getDownClues());

        acrossAdapter.notifyProgressChanged();
        downAdapter.notifyProgressChanged();
    }

    private void updateClueList(@NonNull List<CrosswordClue> clues) {
        for (CrosswordClue clue : clues) {
            boolean solved = true;
            for (CrosswordClue.Position position : clue.getPositions()) {
                CrosswordCell cell = puzzle.getGrid()[position.getRow()][position.getColumn()];
                CrosswordGridAdapter.CellState state = cellStates.get(toIndex(cell.getRow(), cell.getColumn()));
                char solution = Character.toUpperCase(cell.getSolution());
                String value = state.getValue();
                if (value == null || value.length() != 1 || value.charAt(0) != solution) {
                    solved = false;
                    break;
                }
            }
            clue.setSolved(solved);
        }
    }

    private boolean areAllCellsSolved() {
        for (CrosswordGridAdapter.CellState state : cellStates) {
            CrosswordCell cell = state.getCell();
            if (cell.isBlock()) {
                continue;
            }
            char solution = Character.toUpperCase(cell.getSolution());
            String value = state.getValue();
            if (value == null || value.length() != 1 || value.charAt(0) != solution) {
                return false;
            }
        }
        return true;
    }

    private int toIndex(int row, int column) {
        return row * puzzle.getColumns() + column;
    }
}
