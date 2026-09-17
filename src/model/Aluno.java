package model;

import java.time.LocalDate;

public class Aluno {
	
	private String ra;
	private String nome;
	private LocalDate dataNascimento;
	private String rg;
	
	public Aluno() {}

	public Aluno(String ra, String nome, LocalDate dataNascimento, String rg) {
		this.ra = ra;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.rg = rg;
	}

	public String getRa() {
		return ra;
	}

	public String getNome() {
		return nome;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public String getRg() {
		return rg;
	}

	@Override
	public String toString() {
		return nome +" (RA " + ra + ")";
	}	
}