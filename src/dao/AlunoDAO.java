package dao;

import model.Aluno;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
	
	public boolean existeRa(String ra) throws SQLException {
		String sql = "SELECT 1 FROM alunos WHERE ra = ?";
		try (Connection con = Conexao.abrir();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, ra);
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	public boolean existeRg(String rg) throws SQLException {
		String sql = "SELECT 1 FROM alunos WHERE rg = ?";
		try (Connection con = Conexao.abrir();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, rg);
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	public void cadastrar(Aluno aluno) throws SQLException {
		String sql = "INSERT INTO alunos (ra, nome, data_nascimento, rg) " +
					"VALUES (?, ?, ?, ?)";
		try (Connection con = Conexao.abrir();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, aluno.getRa());
			stmt.setString(2, aluno.getNome());
			stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));
			stmt.setString(4, aluno.getRg());
			stmt.executeUpdate();
		}
	}
	
	public List<Aluno> listarTodos() throws SQLException {
		String sql = "SELECT ra, nome, data_nascimento, rg " +
					"FROM alunos ORDER BY nome";
		List<Aluno> lista = new ArrayList<>();
		try (Connection con = Conexao.abrir();
				PreparedStatement stmt = con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				Aluno a = new Aluno(
						rs.getString("ra"),
						rs.getString("nome"),
						rs.getDate("data_nascimento").toLocalDate(),
						rs.getString("rg")
						);
						lista.add(a);
				}
			}
			return lista;
	}
}