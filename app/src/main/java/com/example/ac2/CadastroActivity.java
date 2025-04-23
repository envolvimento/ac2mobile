import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNome, editDescricao, editHorario;
    private Button btnSalvar;
    private MedicamentoDAO dao;
    private int idEditar = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editNome = findViewById(R.id.editNome);
        editDescricao = findViewById(R.id.editDescricao);
        editHorario = findViewById(R.id.editHorario);
        btnSalvar = findViewById(R.id.btnSalvar);
        dao = new MedicamentoDAO(this);

        if (getIntent().hasExtra("id")) {
            idEditar = getIntent().getIntExtra("id", -1);
            for (Medicamento m : dao.listarTodos()) {
                if (m.getId() == idEditar) {
                    editNome.setText(m.getNome());
                    editDescricao.setText(m.getDescricao());
                    editHorario.setText(m.getHorario());
                    break;
                }
            }
        }

        btnSalvar.setOnClickListener(v -> {
            String nome = editNome.getText().toString();
            String desc = editDescricao.getText().toString();
            String horario = editHorario.getText().toString();

            if (nome.isEmpty() || horario.isEmpty()) {
                Toast.makeText(this, "Preencha os campos obrigatórios!", Toast.LENGTH_SHORT).show();
                return;
            }

            Medicamento m = new Medicamento(nome, desc, horario, false);

            if (idEditar == -1) {
                dao.inserir(m);
            } else {
                m.setId(idEditar);
                dao.atualizar(m);
            }

            agendarAlarme(m);
            finish();
        });
    }

    private void agendarAlarme(Medicamento m) {
        try {
            String[] partes = m.getHorario().split(":");
            int hora = Integer.parseInt(partes[0]);
            int minuto = Integer.parseInt(partes[1]);

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hora);
            c.set(Calendar.MINUTE, minuto);
            c.set(Calendar.SECOND, 0);

            if (c.before(Calendar.getInstance())) {
                c.add(Calendar.DAY_OF_MONTH, 1); // agenda para o próximo dia
            }

            Intent i = new Intent(this, AlarmeReceiver.class);
            i.putExtra("nome", m.getNome());

            PendingIntent pi = PendingIntent.getBroadcast(this, m.getNome().hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            am.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
