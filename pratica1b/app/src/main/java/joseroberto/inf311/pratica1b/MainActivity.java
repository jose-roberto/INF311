package joseroberto.inf311.pratica1b;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    boolean discardLastValue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private boolean verifyLastChar(String expr) {
        if (expr.isEmpty()) {
            return true;
        }

        expr = expr.replace(" ", "");
        char lastChar = expr.charAt(expr.length() - 1);

        return lastChar == '.' || lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/';
    }

    private double calculate(double value_1, double value_2, String operator) {
        switch (operator) {
            case "+":
                return value_1 + value_2;
            case "-":
                return value_1 - value_2;
            case "*":
                return value_1 * value_2;
            case "/":
                return value_1 / value_2;
            default:
                return 0;
        }
    }

    public void buttonAction(View v) {
        String expr = ((EditText) findViewById(R.id.visor_text)).getText().toString();
        String symbol = v.getTag().toString();

        if (!expr.equals("ERROR")) switch (symbol) {
            case "add":
                if (!verifyLastChar(expr)) {
                    expr += " + ";
                    discardLastValue = false;
                }
                break;
            case "sub":
                if (!verifyLastChar(expr)) {
                    expr += " - ";
                    discardLastValue = false;
                }
                break;
            case "mult":
                if (!verifyLastChar(expr)) {
                    expr += " * ";
                    discardLastValue = false;
                }
                break;
            case "div":
                if (!verifyLastChar(expr)) {
                    expr += " / ";
                    discardLastValue = false;
                }
                break;
            case "decimalDot":
                if (!verifyLastChar(expr) && !expr.contains(".")) {
                    expr += ".";
                }
                break;
            case "equal":
                if (!verifyLastChar(expr) && expr.contains(" ")) {
                    double value_1 = Double.parseDouble(expr.split(" ")[0]);
                    double value_2 = Double.parseDouble(expr.split( " ")[2]);
                    String operator = expr.split(" ")[1];

                    if (operator.equals("/") && value_2 == 0) {
                        expr = "ERROR";
                        break;
                    }

                    double result = calculate(value_1, value_2, operator);
                    expr = String.valueOf(result);

                    discardLastValue = true;
                }
                break;
            case "clear":
                expr = "";
                break;
            case "backspace":
                if (verifyLastChar(expr) && expr.contains(" ")) {
                    expr = expr.substring(0, expr.length() - 3);
                } else if (!expr.isEmpty() && !discardLastValue) {
                    expr = expr.substring(0, expr.length() - 1);
                }
                break;
            default:
                if (discardLastValue || expr.length() == 1 && expr.equals("0")) {
                    expr = symbol;
                    discardLastValue = false;
                } else {
                    expr += symbol;
                }
                break;
        }
        else {
            expr = "";
        }

        EditText visor = findViewById(R.id.visor_text);
        visor.setText(expr);
    }
}