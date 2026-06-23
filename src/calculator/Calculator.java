package calculator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame {

    // ── Display ──────────────────────────────────────────────────────────────
    private JLabel expressionLabel;   // shows running expression
    private JLabel resultLabel;       // shows current number / result

    // ── State ────────────────────────────────────────────────────────────────
    private double firstOperand  = 0;
    private String operator      = "";
    private boolean newInput     = true;   // next digit starts fresh
    private boolean justEvaled   = false;  // result was just calculated
    private String  expression   = "";     // text shown in small label

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG          = new Color(18, 18, 24);
    private static final Color DISPLAY_BG  = new Color(24, 24, 34);
    private static final Color BTN_NUM     = new Color(36, 36, 50);
    private static final Color BTN_OP      = new Color(50, 100, 200);
    private static final Color BTN_EQ      = new Color(80, 170, 255);
    private static final Color BTN_SPEC    = new Color(55, 55, 75);
    private static final Color TEXT_MAIN   = new Color(230, 230, 255);
    private static final Color TEXT_DIM    = new Color(130, 130, 170);
    private static final Color BTN_HOVER_NUM  = new Color(52, 52, 72);
    private static final Color BTN_HOVER_OP   = new Color(65, 115, 220);
    private static final Color BTN_HOVER_EQ   = new Color(100, 185, 255);
    private static final Color BTN_HOVER_SPEC = new Color(70, 70, 95);

    // ─────────────────────────────────────────────────────────────────────────
    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setBackground(BG);
        getContentPane().setBackground(BG);

        setLayout(new BorderLayout(0, 0));
        add(buildDisplay(), BorderLayout.NORTH);
        add(buildButtons(), BorderLayout.CENTER);

        pack();
        setMinimumSize(new Dimension(320, 480));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Display panel ─────────────────────────────────────────────────────────
    private JPanel buildDisplay() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(DISPLAY_BG);
        panel.setBorder(new EmptyBorder(24, 20, 16, 20));

        expressionLabel = new JLabel(" ");
        expressionLabel.setFont(new Font("SFMono-Regular", Font.PLAIN, 14));
        expressionLabel.setForeground(TEXT_DIM);
        expressionLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        expressionLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        resultLabel = new JLabel("0");
        resultLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 48));
        resultLabel.setForeground(TEXT_MAIN);
        resultLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        resultLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Wrap labels in right-aligned wrappers
        JPanel exprRow = new JPanel(new BorderLayout());
        exprRow.setBackground(DISPLAY_BG);
        exprRow.add(expressionLabel, BorderLayout.EAST);

        JPanel resultRow = new JPanel(new BorderLayout());
        resultRow.setBackground(DISPLAY_BG);
        resultRow.add(resultLabel, BorderLayout.EAST);

        panel.add(exprRow);
        panel.add(Box.createVerticalStrut(4));
        panel.add(resultRow);

        return panel;
    }

    // ── Button grid ───────────────────────────────────────────────────────────
    private JPanel buildButtons() {
        JPanel grid = new JPanel(new GridLayout(5, 4, 8, 8));
        grid.setBackground(BG);
        grid.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Row 1: AC  +/-  %  ÷
        grid.add(btn("AC",  BTN_SPEC, BTN_HOVER_SPEC));
        grid.add(btn("+/-", BTN_SPEC, BTN_HOVER_SPEC));
        grid.add(btn("%",   BTN_SPEC, BTN_HOVER_SPEC));
        grid.add(btn("÷",   BTN_OP,   BTN_HOVER_OP));

        // Row 2: 7  8  9  ×
        grid.add(btn("7", BTN_NUM, BTN_HOVER_NUM));
        grid.add(btn("8", BTN_NUM, BTN_HOVER_NUM));
        grid.add(btn("9", BTN_NUM, BTN_HOVER_NUM));
        grid.add(btn("×", BTN_OP,  BTN_HOVER_OP));

        // Row 3: 4  5  6  −
        grid.add(btn("4", BTN_NUM, BTN_HOVER_NUM));
        grid.add(btn("5", BTN_NUM, BTN_HOVER_NUM));
        grid.add(btn("6", BTN_NUM, BTN_HOVER_NUM));
        grid.add(btn("−", BTN_OP,  BTN_HOVER_OP));

        // Row 4: 1  2  3  +
        grid.add(btn("1", BTN_NUM, BTN_HOVER_NUM));
        grid.add(btn("2", BTN_NUM, BTN_HOVER_NUM));
        grid.add(btn("3", BTN_NUM, BTN_HOVER_NUM));
        grid.add(btn("+", BTN_OP,  BTN_HOVER_OP));

        // Row 5: 0 (wide)  .  =
        JButton zeroBtn = btn("0", BTN_NUM, BTN_HOVER_NUM);
        grid.add(zeroBtn);
        grid.add(btn("⌫", BTN_SPEC, BTN_HOVER_SPEC));
        grid.add(btn(".", BTN_NUM,  BTN_HOVER_NUM));
        grid.add(btn("=", BTN_EQ,   BTN_HOVER_EQ));

        return grid;
    }

    // ── Button factory ────────────────────────────────────────────────────────
    private JButton btn(String text, Color bg, Color hover) {
        JButton b = new JButton(text);
        b.setFont(new Font("Helvetica Neue", Font.BOLD, 22));
        b.setForeground(TEXT_MAIN);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(70, 70));

        // Rounded appearance via UI override
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 18, 18);
                g2.dispose();
                super.paint(g, c);
            }
        });

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            @Override public void mouseExited (MouseEvent e) { b.setBackground(bg);    }
        });

        b.addActionListener(e -> handleInput(text));
        return b;
    }

    // ── Logic ─────────────────────────────────────────────────────────────────
    private void handleInput(String cmd) {
        switch (cmd) {
            case "AC"  -> reset();
            case "+/-" -> negate();
            case "%"   -> percent();
            case "⌫"  -> backspace();
            case "."   -> appendDot();
            case "+"   -> setOperator("+");
            case "−"   -> setOperator("−");
            case "×"   -> setOperator("×");
            case "÷"   -> setOperator("÷");
            case "="   -> evaluate();
            default    -> appendDigit(cmd);
        }
    }

    private void reset() {
        firstOperand = 0;
        operator     = "";
        newInput     = true;
        justEvaled   = false;
        expression   = "";
        resultLabel.setText("0");
        expressionLabel.setText(" ");
    }

    private void negate() {
        double val = currentValue();
        if (val != 0) {
            val = -val;
            resultLabel.setText(format(val));
        }
    }

    private void percent() {
        double val = currentValue() / 100.0;
        resultLabel.setText(format(val));
        newInput = true;
    }

    private void backspace() {
        String s = resultLabel.getText();
        if (s.equals("0") || s.equals("Error") || newInput) return;
        s = s.length() > 1 ? s.substring(0, s.length() - 1) : "0";
        if (s.equals("-")) s = "0";
        resultLabel.setText(s);
    }

    private void appendDot() {
        if (newInput || justEvaled) {
            resultLabel.setText("0.");
            newInput   = false;
            justEvaled = false;
            return;
        }
        String s = resultLabel.getText();
        if (!s.contains(".")) resultLabel.setText(s + ".");
    }

    private void appendDigit(String d) {
        if (newInput || justEvaled) {
            resultLabel.setText(d);
            newInput   = false;
            justEvaled = false;
        } else {
            String s = resultLabel.getText();
            resultLabel.setText(s.equals("0") ? d : s + d);
        }
    }

    private void setOperator(String op) {
        if (!operator.isEmpty() && !newInput) {
            // chain: evaluate previous before storing new operator
            double result = compute(firstOperand, currentValue(), operator);
            firstOperand = result;
            resultLabel.setText(format(result));
        } else {
            firstOperand = currentValue();
        }
        operator   = op;
        newInput   = true;
        justEvaled = false;
        expression = format(firstOperand) + " " + op;
        expressionLabel.setText(expression);
    }

    private void evaluate() {
        if (operator.isEmpty()) return;
        double second = currentValue();
        expression = format(firstOperand) + " " + operator + " " + format(second) + " =";
        expressionLabel.setText(expression);

        double result = compute(firstOperand, second, operator);
        resultLabel.setText(format(result));

        firstOperand = result;
        operator     = "";
        newInput     = true;
        justEvaled   = true;
    }

    private double compute(double a, double b, String op) {
        return switch (op) {
            case "+"  -> a + b;
            case "−"  -> a - b;
            case "×"  -> a * b;
            case "÷"  -> b == 0 ? Double.NaN : a / b;
            default   -> b;
        };
    }

    private double currentValue() {
        try {
            return Double.parseDouble(resultLabel.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** Format: drop .0 for whole numbers, cap at 10 significant digits */
    private String format(double v) {
        if (Double.isNaN(v) || Double.isInfinite(v)) return "Error";
        if (v == Math.floor(v) && Math.abs(v) < 1e10) return String.valueOf((long) v);
        String s = String.format("%.10g", v);
        // strip trailing zeros after decimal
        if (s.contains(".")) {
            s = s.replaceAll("0+$", "").replaceAll("\\.$", "");
        }
        return s;
    }

    // ── Entry ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(Calculator::new);
    }
}
