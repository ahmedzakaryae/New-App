package com.example.crossword.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for a crossword puzzle definition including the grid and associated clues.
 */
public class CrosswordPuzzle {
    private final int rows;
    private final int columns;
    private final CrosswordCell[][] grid;
    private final List<CrosswordClue> acrossClues;
    private final List<CrosswordClue> downClues;

    public CrosswordPuzzle(int rows, int columns, CrosswordCell[][] grid,
                           List<CrosswordClue> acrossClues, List<CrosswordClue> downClues) {
        this.rows = rows;
        this.columns = columns;
        this.grid = grid;
        this.acrossClues = acrossClues;
        this.downClues = downClues;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public CrosswordCell[][] getGrid() {
        return grid;
    }

    @NonNull
    public List<CrosswordCell> getAllCells() {
        List<CrosswordCell> cells = new ArrayList<>(rows * columns);
        for (CrosswordCell[] row : grid) {
            for (CrosswordCell cell : row) {
                cells.add(cell);
            }
        }
        return cells;
    }

    public List<CrosswordClue> getAcrossClues() {
        return acrossClues;
    }

    public List<CrosswordClue> getDownClues() {
        return downClues;
    }
}
