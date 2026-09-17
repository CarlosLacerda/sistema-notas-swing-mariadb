package view;

import dao.*;
import model.*;
import util.Mensagens;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PainelLancamentoNota extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JComboBox<Aluno> comboAluno = new JComboBox<>();
    private final JComboBox<Disciplina> comboDisciplina = new JComboBox<>();
    private final JComboBox<TipoProva> comboTipoProva = new JComboBox<>();
    private final JTextField campoNota = new JTextField(6);
    private final JButton botaoLancar = new JButton("Lançar Nota");

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final TipoProvaDAO tipoProvaDAO = new TipoProvaDAO();
    private final NotaDAO notaDAO = new NotaDAO();

    public PainelLancamentoNota() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; add(new JLabel("Aluno:"), c);
        c.gridx = 1; add(comboAluno, c);

        c.gridx = 0; c.gridy = 1; add(new JLabel("Disciplina:"), c);
        c.gridx = 1; add(comboDisciplina, c);

        c.gridx = 0; c.gridy = 2; add(new JLabel("Tipo de prova:"), c);
        c.gridx = 1; add(comboTipoProva, c);

        c.gridx = 0; c.gridy = 3; add(new JLabel("Nota (0,00 a 10,00):"), c);
        c.gridx = 1; add(campoNota, c);

        c.gridx = 1; c.gridy = 4; add(botaoLancar, c);

        botaoLancar.addActionListener(e -> lancarNota());
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

    private void lancarNota() {
        Aluno aluno = (Aluno) comboAluno.getSelectedItem();
        Disciplina disciplina = (Disciplina) comboDisciplina.getSelectedItem();
        TipoProva tipoProva = (TipoProva) comboTipoProva.getSelectedItem();
        String notaTexto = campoNota.getText().trim().replace(",", ".");

        if (aluno == null || disciplina == null || tipoProva == null) {
            JOptionPane.showMessageDialog(this,
                "Selecione o aluno, a disciplina e o tipo de prova.",
                "Seleção obrigatória", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal valorNota;
        try {
            valorNota = new BigDecimal(notaTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Digite uma nota válida, por exemplo: 7.5",
                "Nota inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (valorNota.compareTo(BigDecimal.ZERO) < 0 ||
            valorNota.compareTo(BigDecimal.TEN) > 0) {
            JOptionPane.showMessageDialog(this,
                "A nota deve estar entre 0,00 e 10,00.",
                "Nota fora do intervalo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean jaExiste = notaDAO.existeNota(
                aluno.getRa(), disciplina.getId(), tipoProva.getId());
            if (jaExiste) {
                JOptionPane.showMessageDialog(this,
                    "Este aluno já possui uma nota lançada para esta " +
                    "disciplina e este tipo de prova.",
                    "Nota duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Nota nota = new Nota(aluno.getRa(), disciplina.getId(),
                                  tipoProva.getId(), valorNota);
            notaDAO.cadastrar(nota);

            JOptionPane.showMessageDialog(this,
                "Nota lançada com sucesso!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            campoNota.setText("");

        } catch (SQLException ex) {
            Mensagens.erroBanco(this, ex);
        }
    }
}