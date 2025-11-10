package com.example.crossword.model;

import androidx.annotation.NonNull;

/**
 * Represents a single cell in the crossword grid.
 */
public class CrosswordCell {
    private final int row;
    private final int column;
    private final char solution;
    private final boolean block;
    private final int number;

    public CrosswordCell(int row, int column, char solution, boolean block, int number) {
        this.row = row;
        this.column = column;
        this.solution = solution;
        this.block = block;
        this.number = number;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public char getSolution() {
        return solution;
    }

    public boolean isBlock() {
        return block;
    }

    public int getNumber() {
        return number;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", column=" + column +
                ", solution=" + solution +
                ", block=" + block +
                ", number=" + number +
                '}';
    }
}
