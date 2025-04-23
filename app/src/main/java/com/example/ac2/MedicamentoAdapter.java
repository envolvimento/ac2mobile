import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.ViewHolder> {

    private List<Medicamento> lista;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditar(Medicamento m);
        void onExcluir(Medicamento m);
        void onMarcarConsumido(Medicamento m);
    }

    public MedicamentoAdapter(List<Medicamento> lista, Context context, OnItemClickListener listener) {
        this.lista = lista;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MedicamentoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_medicamento, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MedicamentoAdapter.ViewHolder holder, int position) {
        Medicamento m = lista.get(position);
        holder.txtNome.setText(m.getNome());
        holder.txtDescricao.setText(m.getDescricao());
        holder.txtHorario.setText("Horário: " + m.getHorario());
        holder.btnConsumido.setText(m.isConsumido() ? "Já tomado" : "Marcar como Tomado");
        holder.btnConsumido.setEnabled(!m.isConsumido());

        holder.itemView.setOnClickListener(v -> listener.onEditar(m));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onExcluir(m);
            return true;
        });
        holder.btnConsumido.setOnClickListener(v -> listener.onMarcarConsumido(m));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtDescricao, txtHorario;
        Button btnConsumido;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNome);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtHorario = itemView.findViewById(R.id.txtHorario);
            btnConsumido = itemView.findViewById(R.id.btnConsumido);
        }
    }
}