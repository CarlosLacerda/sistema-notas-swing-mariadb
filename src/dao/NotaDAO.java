package dao;

import model.Nota;
import model.ResultadoConsulta;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotaDAO {

    public boolean existeNota(String ra, int idDisciplina, int idTipoProva)
            throws SQLException {
        String sql = "SELECT 1 FROM notas " +
                     "WHERE ra = ? AND id_disciplina = ? AND id_tipo_prova = ?";
        try (Connection con = Conexao.abrir();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ra);
            stmt.setInt(2, idDisciplina);
            stmt.setInt(3, idTipoProva);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void cadastrar(Nota nota) throws SQLException {
        String sql = "INSERT INTO notas (ra, id_disciplina, id_tipo_prova, nota) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection con = Conexao.abrir();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nota.getRaAluno());
            stmt.setInt(2, nota.getDisciplinaId());
            stmt.setInt(3, nota.getTipoProvaId());
            stmt.setBigDecimal(4, nota.getValor());
            stmt.executeUpdate();
        }
    }

    public ResultadoConsulta consultar(String ra, int idDisciplina, int idTipoProva)
            throws SQLException {
        String sql =
            "SELECT a.nome, a.ra, d.nome_disciplina AS disciplina, " +
            "       t.nome_prova AS prova, n.nota " +
            "FROM notas n " +
            "JOIN alunos a ON a.ra = n.ra " +
            "JOIN disciplinas d ON d.id_disciplina = n.id_disciplina " +
            "JOIN tipos_provas t ON t.id_tipo_prova = n.id_tipo_prova " +
            "WHERE n.ra = ? AND n.id_disciplina = ? AND n.id_tipo_prova = ?";

        try (Connection con = Conexao.abrir();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ra);
            stmt.setInt(2, idDisciplina);
            stmt.setInt(3, idTipoProva);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new ResultadoConsulta(
                    rs.getString("nome"),
                    rs.getString("ra"),
                    rs.getString("disciplina"),
                    rs.getString("prova"),
                    rs.getBigDecimal("nota")
                );
            }
        }
    }

    public List<Object[]> listarBoletim(String ra) throws SQLException {
        String sql =
            "SELECT d.nome_disciplina AS disciplina, t.nome_prova AS prova, n.nota " +
            "FROM notas n " +
            "JOIN disciplinas d ON d.id_disciplina = n.id_disciplina " +
            "JOIN tipos_provas t ON t.id_tipo_prova = n.id_tipo_prova " +
            "WHERE n.ra = ? " +
            "ORDER BY d.nome_disciplina, t.nome_prova";

        List<Object[]> linhas = new ArrayList<>();
        try (Connection con = Conexao.abrir();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ra);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    linhas.add(new Object[] {
                        rs.getString("disciplina"),
                        rs.getString("prova"),
                        rs.getBigDecimal("nota")
                    });
                }
            }
        }
        return linhas;
    }
}