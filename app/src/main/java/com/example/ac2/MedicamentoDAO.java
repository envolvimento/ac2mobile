import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MedicamentoDAO {
    private DBHelper dbHelper;

    public MedicamentoDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void inserir(Medicamento m) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nome", m.getNome());
        valores.put("descricao", m.getDescricao());
        valores.put("horario", m.getHorario());
        valores.put("consumido", m.isConsumido() ? 1 : 0);
        db.insert("medicamentos", null, valores);
        db.close();
    }

    public ArrayList<Medicamento> listarTodos() {
        ArrayList<Medicamento> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM medicamentos", null);
        if (c.moveToFirst()) {
            do {
                Medicamento m = new Medicamento(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getInt(4) == 1
                );
                lista.add(m);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return lista;
    }

    public void atualizar(Medicamento m) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nome", m.getNome());
        valores.put("descricao", m.getDescricao());
        valores.put("horario", m.getHorario());
        valores.put("consumido", m.isConsumido() ? 1 : 0);
        db.update("medicamentos", valores, "id = ?", new String[]{String.valueOf(m.getId())});
        db.close();
    }

    public void excluir(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("medicamentos", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
