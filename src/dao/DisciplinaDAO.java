package dao;

import model.Disciplina;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {
    public List<Disciplina> listarTodos() throws SQLException {
        String sql = "SELECT id_disciplina, nome_disciplina " +
                     "FROM disciplinas ORDER BY nome_disciplina";
        List<Disciplina> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Disciplina(
                    rs.getInt("id_disciplina"),
                    rs.getString("nome_disciplina")));
            }
        }
        return lista;
    }
}