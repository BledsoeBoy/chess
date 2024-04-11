package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class ChessGame {
    private static final int BOARD_SIZE_IN_SQUARES = 8;

    public static void run(String playerColor) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        out.print(SET_TEXT_BOLD);
        if (playerColor.equals("WHITE")) {
            drawHeader1(out);

            drawChessBoard1(out);

            drawHeader1(out);
        } else {
            drawHeader2(out);

            drawChessBoard2(out);

            drawHeader2(out);
        }

        out.print("\u001B[0m");
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_TEXT_FAINT);
    }

    private static void drawHeader1(PrintStream out) {
        String header = "    h  g  f  e  d  c  b  a    ";
        out.print(SET_TEXT_BOLD);
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(header);
        out.print("\u001B[0m");
        out.println();
    }
    private static void drawHeader2(PrintStream out) {
        String header = "    a  b  c  d  e  f  g  h    ";
        out.print(SET_TEXT_BOLD);
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(header);
        out.print("\u001B[0m");
        out.println();
    }

    private static void drawChessBoard1(PrintStream out) {
        for (int squareRow = 1; squareRow <= BOARD_SIZE_IN_SQUARES; ++squareRow) {
            String row = " " + squareRow + " ";
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.printf(row);
            for (int boardCol = 1; boardCol <= BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if (squareRow == 1) {
                    out.print(SET_TEXT_COLOR_RED);
                    if (boardCol % 2 == 0) {
                        out.print(SET_BG_COLOR_BLACK);
                        if (boardCol == 2) {
                            out.print(" N ");
                        }
                        else if (boardCol == 4) {
                            out.print(" K ");
                        }
                        else if (boardCol == 6) {
                            out.print(" B ");
                        }
                        else if (boardCol == 8) {
                            out.print(" R ");
                        }
                    }
                    else {
                        out.print(SET_BG_COLOR_WHITE);
                        if (boardCol == 1) {
                            out.print(" R ");
                        }
                        else if (boardCol == 3) {
                            out.print(" B ");
                        }
                        else if (boardCol == 5) {
                            out.print(" Q ");
                        }
                        else if (boardCol == 7) {
                            out.print(" N ");
                        }
                    }
                }
                else if (squareRow == 2) {
                    out.print(SET_TEXT_COLOR_RED);
                    if (boardCol % 2 == 0) {
                        out.print(SET_BG_COLOR_WHITE);
                        out.print(" P ");
                    }
                    else {
                        out.print(SET_BG_COLOR_BLACK);
                        out.print(" P ");
                    }
                }
                else if (squareRow == 8) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    if (boardCol % 2 == 0) {
                        out.print(SET_BG_COLOR_WHITE);
                        if (boardCol == 2) {
                            out.print(" N ");
                        }
                        else if (boardCol == 4) {
                            out.print(" K ");
                        }
                        else if (boardCol == 6) {
                            out.print(" B ");
                        }
                        else if (boardCol == 8) {
                            out.print(" R ");
                        }
                    }
                    else {
                        out.print(SET_BG_COLOR_BLACK);
                        if (boardCol == 1) {
                            out.print(" R ");
                        }
                        else if (boardCol == 3) {
                            out.print(" B ");
                        }
                        else if (boardCol == 5) {
                            out.print(" Q ");
                        }
                        else if (boardCol == 7) {
                            out.print(" N ");
                        }
                    }
                }
                else if (squareRow == 7) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    if (boardCol % 2 == 0) {
                        out.print(SET_BG_COLOR_BLACK);
                        out.print(" P ");
                    }
                    else {
                        out.print(SET_BG_COLOR_WHITE);
                        out.print(" P ");
                    }
                }
                else {
                    if (squareRow % 2 == 0) {
                        if (boardCol % 2 == 0) {
                            out.print(SET_BG_COLOR_WHITE);
                            out.print("   ");
                        } else {
                            out.print(SET_BG_COLOR_BLACK);
                            out.print("   ");
                        }
                    } else {
                        if (boardCol % 2 == 0) {
                            out.print(SET_BG_COLOR_BLACK);
                            out.print("   ");
                        } else {
                            out.print(SET_BG_COLOR_WHITE);
                            out.print("   ");
                        }
                    }
                }
            }
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.printf(row);
            out.print("\u001B[0m");
            out.println();
        }
    }

    private static void drawChessBoard2(PrintStream out) {
        for (int squareRow = 8; squareRow >= 1; --squareRow) {
            String row = " " + squareRow + " ";
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.printf(row);
            for (int boardCol = 1; boardCol <= BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if (squareRow == 1) {
                    out.print(SET_TEXT_COLOR_RED);
                    if (boardCol % 2 == 0) {
                        out.print(SET_BG_COLOR_BLACK);
                        if (boardCol == 2) {
                            out.print(" N ");
                        }
                        else if (boardCol == 4) {
                            out.print(" K ");
                        }
                        else if (boardCol == 6) {
                            out.print(" B ");
                        }
                        else if (boardCol == 8) {
                            out.print(" R ");
                        }
                    }
                    else {
                        out.print(SET_BG_COLOR_WHITE);
                        if (boardCol == 1) {
                            out.print(" R ");
                        }
                        else if (boardCol == 3) {
                            out.print(" B ");
                        }
                        else if (boardCol == 5) {
                            out.print(" Q ");
                        }
                        else if (boardCol == 7) {
                            out.print(" N ");
                        }
                    }
                }
                else if (squareRow == 2) {
                    out.print(SET_TEXT_COLOR_RED);
                    if (boardCol % 2 == 0) {
                        out.print(SET_BG_COLOR_WHITE);
                        out.print(" P ");
                    }
                    else {
                        out.print(SET_BG_COLOR_BLACK);
                        out.print(" P ");
                    }
                }
                else if (squareRow == 8) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    if (boardCol % 2 == 0) {
                        out.print(SET_BG_COLOR_WHITE);
                        if (boardCol == 2) {
                            out.print(" N ");
                        }
                        else if (boardCol == 4) {
                            out.print(" K ");
                        }
                        else if (boardCol == 6) {
                            out.print(" B ");
                        }
                        else if (boardCol == 8) {
                            out.print(" R ");
                        }
                    }
                    else {
                        out.print(SET_BG_COLOR_BLACK);
                        if (boardCol == 1) {
                            out.print(" R ");
                        }
                        else if (boardCol == 3) {
                            out.print(" B ");
                        }
                        else if (boardCol == 5) {
                            out.print(" Q ");
                        }
                        else if (boardCol == 7) {
                            out.print(" N ");
                        }
                    }
                }
                else if (squareRow == 7) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    if (boardCol % 2 == 0) {
                        out.print(SET_BG_COLOR_BLACK);
                        out.print(" P ");
                    }
                    else {
                        out.print(SET_BG_COLOR_WHITE);
                        out.print(" P ");
                    }
                }
                else {
                    if (squareRow % 2 == 0) {
                        if (boardCol % 2 == 0) {
                            out.print(SET_BG_COLOR_WHITE);
                            out.print("   ");
                        } else {
                            out.print(SET_BG_COLOR_BLACK);
                            out.print("   ");
                        }
                    } else {
                        if (boardCol % 2 == 0) {
                            out.print(SET_BG_COLOR_BLACK);
                            out.print("   ");
                        } else {
                            out.print(SET_BG_COLOR_WHITE);
                            out.print("   ");
                        }
                    }
                }
            }
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.printf(row);
            out.print("\u001B[0m");
            out.println();
        }
    }
}