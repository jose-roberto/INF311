package com.inf311.pratica4_app1;

import android.content.Context;
import android.os.Vibrator;

public class MotorHelper {
    private Context ctx;

    public MotorHelper(Context ctx){
        this.ctx = ctx;
    }

    public void iniciarVibracao() {
        Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null && v.hasVibrator()) {
            // padrão: {delay, vibra, pausa}
            // significa: sem atraso inicial, vibra por 500ms, pausa 500ms, e repete.
            long[] pattern = {0, 500, 500};
            v.vibrate(pattern, 0); // 0 = repetir do início
        }
    }

    public void pararVibracao() {
        Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            v.cancel(); // interrompe a vibração
        }
    }
}
