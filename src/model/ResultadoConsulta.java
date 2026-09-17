package model;

import java.math.BigDecimal;

public class ResultadoConsulta {
	private final String nomeAluno;
	private final String ra;
	private final String disciplina;
	private final String prova;
	private final BigDecimal nota;
	
	public ResultadoConsulta(String nomeAluno, String ra, String disciplina, String prova, BigDecimal nota) {
		this.nomeAluno = nomeAluno;
		this.ra = ra;
		this.disciplina = disciplina;
		this.prova = prova;
		this.nota = nota;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public String getRa() {
		return ra;
	}

	public String getDisciplina() {
		return disciplina;
	}

	public String getProva() {
		return prova;
	}

	public BigDecimal getNota() {
		return nota;
	}
	
	
	
	

}
