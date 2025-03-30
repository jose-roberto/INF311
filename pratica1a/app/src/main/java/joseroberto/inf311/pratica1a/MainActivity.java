package joseroberto.inf311.pratica1a;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonAction(View v) {
        EditText edtValue_1 = findViewById(R.id.value_1);
        EditText edtValue_2 = findViewById(R.id.value_2);
        TextView resultView = findViewById(R.id.resultView);

        if (edtValue_1.getText().toString().isEmpty() || edtValue_2.getText().toString().isEmpty()) {
            resultView.setText("Preencha os dois valores!");
            return;
        }

        try {
            double value_1 = Double.parseDouble(edtValue_1.getText().toString());
            double value_2 = Double.parseDouble(edtValue_2.getText().toString());

            edtValue_1.setText("");
            edtValue_2.setText("");

            String tag = v.getTag().toString();

            double result = 0;

            switch (tag) {
                case "1":
                    result = value_1 + value_2;
                    break;
                case "2":
                    result = value_1 - value_2;
                    break;
                case "3":
                    result = value_1 * value_2;
                    break;
                case "4":
                    if (value_2 == 0) {
                        resultView.setText("Erro: divisão por zero!");
                        return;
                    }

                    result = value_1 / value_2;
                    break;
            }

            resultView.setText("O Resultado é: " + result);

        } catch (NumberFormatException e) {
            resultView.setText("Erro: Insira números válidos!");
        }
    }
}
