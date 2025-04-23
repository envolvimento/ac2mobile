import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicamentoAdapter adapter;
    private MedicamentoDAO dao;
    private ArrayList<Medicamento> lista;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
        dao = new MedicamentoDAO(this);

        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarLista();
    }

    private void atualizarLista() {
        lista = dao.listarTodos();
        adapter = new MedicamentoAdapter(lista, this, new MedicamentoAdapter.OnItemClickListener() {
            @Override
            public void onEditar(Medicamento m) {
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
                intent.putExtra("id", m.getId());
                startActivity(intent);
            }

            @Override
            public void onExcluir(Medicamento m) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Excluir")
                        .setMessage("Deseja excluir este medicamento?")
                        .setPositiveButton("Sim", (d, w) -> {
                            dao.excluir(m.getId());
                            atualizarLista();
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }

            @Override
            public void onMarcarConsumido(Medicamento m) {
                m.setConsumido(true);
                dao.atualizar(m);
                atualizarLista();
                Toast.makeText(MainActivity.this, "Marcado como tomado!", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
