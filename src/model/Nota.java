package model;

import java.math.BigDecimal;

public class Nota {
	private int id;
	private String raAluno;
	private int disciplinaId;
	private int tipoProvaId;
	private BigDecimal valor;
	
	public Nota() { }

	public Nota(String raAluno, int disciplinaId, int tipoProvaId, BigDecimal valor) {
		this.raAluno = raAluno;
		this.disciplinaId = disciplinaId;
		this.tipoProvaId = tipoProvaId;
		this.valor = valor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRaAluno() {
		return raAluno;
	}

	public void setRaAluno(String raAluno) {
		this.raAluno = raAluno;
	}

	public int getDisciplinaId() {
		return disciplinaId;
	}

	public void setDisciplinaId(int disciplinaId) {
		this.disciplinaId = disciplinaId;
	}

	public int getTipoProvaId() {
		return tipoProvaId;
	}

	public void setTipoProvaId(int tipoProvaId) {
		this.tipoProvaId = tipoProvaId;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}