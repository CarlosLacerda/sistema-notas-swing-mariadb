package view;

import javax.swing.*;

public class JanelaPrincipal extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public JanelaPrincipal() {
		super("Sistema de Notas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(560, 420);
		setLocationRelativeTo(null);
		
		PainelCadastroAluno painelCadastro = new PainelCadastroAluno();
		PainelLancamentoNota painelLancamento = new PainelLancamentoNota();
		PainelConsultaNota painelConsulta = new PainelConsultaNota();
		
		JTabbedPane abas = new JTabbedPane();
		abas.addTab("Cadastro de Alunos", painelCadastro);
		abas.addTab("Lançamento de Notas", painelLancamento);
		abas.addTab("Consulta de Nota", painelConsulta);
		
		abas.addChangeListener(e -> {
			painelLancamento.atualizarCombos();
			painelConsulta.atualizarCombos();
		});
		
		add(abas);
		setVisible(true);
	}
}