package com.example.crossword.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Factory class that builds crossword puzzles bundled with the application.
 */
public final class PuzzleData {

    private PuzzleData() {
        // Utility class.
    }

    /**
     * Builds a crossword puzzle based on the classic Latin Sator word square.
     */
    public static CrosswordPuzzle createSatorSquarePuzzle() {
        String[] rows = {
                "SATOR",
                "AREPO",
                "TENET",
                "OPERA",
                "ROTAS"
        };

        List<String> acrossClueTexts = Arrays.asList(
                "Ancient Latin for \"sower\" that opens a famous word square",
                "Latin name that follows the opening word in the square",
                "Palindromic middle word of the enduring Latin puzzle",
                "Performers referenced on the fourth row of the square",
                "Plural noun that closes out the Latin square"
        );

        List<String> downClueTexts = Arrays.asList(
                "Seed scatterer written vertically at 1-Down (Latin)",
                "Mysterious name spelled downward in the classic square",
                "Vertical palindrome anchoring the puzzle's center",
                "Art form appearing on the downward fourth column",
                "Wheels rolling through the word square's last column"
        );

        int rowsCount = rows.length;
        int columnsCount = rows[0].length();
        CrosswordCell[][] grid = new CrosswordCell[rowsCount][columnsCount];
        List<CrosswordClue> acrossClues = new ArrayList<>();
        List<CrosswordClue> downClues = new ArrayList<>();

        int clueNumber = 1;
        int acrossIndex = 0;
        int downIndex = 0;

        for (int r = 0; r < rowsCount; r++) {
            for (int c = 0; c < columnsCount; c++) {
                char raw = rows[r].charAt(c);
                boolean isBlock = raw == '#';

                int number = 0;
                boolean startAcross = false;
                boolean startDown = false;

                if (!isBlock) {
                    if (c == 0 || rows[r].charAt(c - 1) == '#') {
                        startAcross = true;
                    }
                    if (r == 0 || rows[r - 1].charAt(c) == '#') {
                        startDown = true;
                    }
                    if (startAcross || startDown) {
                        number = clueNumber++;
                    }
                }

                grid[r][c] = new CrosswordCell(r, c, raw, isBlock, number);

                if (!isBlock) {
                    if (startAcross) {
                        List<CrosswordClue.Position> positions = new ArrayList<>();
                        int cc = c;
                        while (cc < columnsCount && rows[r].charAt(cc) != '#') {
                            positions.add(new CrosswordClue.Position(r, cc));
                            cc++;
                        }
                        String clueText = acrossClueTexts.get(acrossIndex++);
                        acrossClues.add(new CrosswordClue(number, CrosswordClue.Direction.ACROSS, clueText, positions));
                    }

                    if (startDown) {
                        List<CrosswordClue.Position> positions = new ArrayList<>();
                        int rr = r;
                        while (rr < rowsCount && rows[rr].charAt(c) != '#') {
                            positions.add(new CrosswordClue.Position(rr, c));
                            rr++;
                        }
                        String clueText = downClueTexts.get(downIndex++);
                        downClues.add(new CrosswordClue(number, CrosswordClue.Direction.DOWN, clueText, positions));
                    }
                }
            }
        }

        return new CrosswordPuzzle(rowsCount, columnsCount, grid, acrossClues, downClues);
    }
}
