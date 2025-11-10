package com.example.crossword.model;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * Represents an individual crossword clue.
 */
public class CrosswordClue {
    public enum Direction {
        ACROSS,
        DOWN
    }

    private final int number;
    private final Direction direction;
    private final String text;
    private final List<Position> positions;

    private boolean solved;

    public CrosswordClue(int number, Direction direction, String text, List<Position> positions) {
        this.number = number;
        this.direction = direction;
        this.text = text;
        this.positions = positions;
    }

    public int getNumber() {
        return number;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getText() {
        return text;
    }

    public List<Position> getPositions() {
        return Collections.unmodifiableList(positions);
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    /**
     * Simple immutable coordinate holder for cells that belong to the clue.
     */
    public static class Position {
        private final int row;
        private final int column;

        public Position(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        @NonNull
        @Override
        public String toString() {
            return "(" + row + "," + column + ")";
        }
    }
}
