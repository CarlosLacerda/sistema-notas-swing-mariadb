package dao;

import model.TipoProva;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoProvaDAO {
    public List<TipoProva> listarTodos() throws SQLException {
        String sql = "SELECT id_tipo_prova, nome_prova " +
                     "FROM tipos_provas ORDER BY nome_prova";
        List<TipoProva> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new TipoProva(
                    rs.getInt("id_tipo_prova"),
                    rs.getString("nome_prova")));
            }
        }
        return lista;
    }
}