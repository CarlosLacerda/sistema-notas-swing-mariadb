package view;

import java.awt.GridBagLayout;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import dao.*;
import model.*;
import util.Mensagens;

public class PainelConsultaNota extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JComboBox<Aluno> comboAluno = new JComboBox<>();
    private final JComboBox<Disciplina> comboDisciplina = new JComboBox<>();
    private final JComboBox<TipoProva> comboTipoProva = new JComboBox<>();
    private final JButton botaoConsultar = new JButton("Consultar nota");
    private final JButton botaoBoletim = new JButton("Ver Boletim Completo");

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final TipoProvaDAO tipoProvaDAO = new TipoProvaDAO();
    private final NotaDAO notaDAO = new NotaDAO();

    public PainelConsultaNota() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; add(new JLabel("Aluno: "), c);
        c.gridx = 1; add(comboAluno, c);
        c.gridx = 0; c.gridy = 1; add(new JLabel("Disciplina: "), c);
        c.gridx = 1; add(comboDisciplina, c);
        c.gridx = 0; c.gridy = 2; add(new JLabel("Tipo de prova: "), c);
        c.gridx = 1; add(comboTipoProva, c);
        c.gridx = 1; c.gridy = 3; add(botaoConsultar, c);
        c.gridx = 1; c.gridy = 4; add(botaoBoletim, c);

        botaoConsultar.addActionListener(e -> consultar());
        botaoBoletim.addActionListener(e -> abrirBoletim());

        atualizarCombos();
    }

    public void atualizarCombos() {
        try {
            preencherCombo(comboAluno, alunoDAO.listarTodos());
            preencherCombo(comboDisciplina, disciplinaDAO.listarTodos());
            preencherCombo(comboTipoProva, tipoProvaDAO.listarTodos());
        } catch (SQLException ex) {
            Mensagens.erroBanco(this, ex);
        }
    }

    private <T> void preencherCombo(JComboBox<T> combo, List<T> itens) {
        combo.removeAllItems();
        for (T item : itens) {
            combo.addItem(item);
        }
    }

    private void consultar() {
        Aluno aluno = (Aluno) comboAluno.getSelectedItem();
        Disciplina disciplina = (Disciplina) comboDisciplina.getSelectedItem();
        TipoProva tipoProva = (TipoProva) comboTipoProva.getSelectedItem();

        if (aluno == null || disciplina == null || tipoProva == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione o aluno, a disciplina e o tipo de prova. ",
                    "Seleção obrigatória", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ResultadoConsulta resultado = notaDAO.consultar(
                    aluno.getRa(), disciplina.getId(), tipoProva.getId());

            if (resultado == null) {
                JOptionPane.showMessageDialog(this,
                        "Não há nota lançada para esta combinação de " +
                        "aluno, disciplina e prova.",
                        "Nenhum resultado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            abrirJanelaResultado(resultado);

        } catch (SQLException ex) {
            Mensagens.erroBanco(this, ex);
        }
    }

    private void abrirJanelaResultado(ResultadoConsulta r) {
        JDialog dialogo = new JDialog();
        dialogo.setTitle("Resultado da Consulta");
        dialogo.setModal(true);
        dialogo.setLayout(new GridLayout(5, 1, 4, 4));

        dialogo.add(new JLabel("Aluno: " + r.getNomeAluno()));
        dialogo.add(new JLabel("RA: " + r.getRa()));
        dialogo.add(new JLabel("Disciplina: " + r.getDisciplina()));
        dialogo.add(new JLabel("Prova: " + r.getProva()));
        dialogo.add(new JLabel("Nota: " + r.getNota()));

        dialogo.setSize(320, 220);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private void abrirBoletim() {
        Aluno aluno = (Aluno) comboAluno.getSelectedItem();

        if (aluno == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um aluno para ver o boletim.",
                    "Seleção obrigatória", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFrame janelaPai = (JFrame) SwingUtilities.getWindowAncestor(this);
        new JanelaBoletim(janelaPai, aluno.getRa(), aluno.getNome());
    }
}