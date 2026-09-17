package view;

import dao.AlunoDAO;
import model.Aluno;
import util.Mensagens;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PainelCadastroAluno extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTextField campoRa = new JTextField(10);
    private final JTextField campoNome = new JTextField(20);
    private final JTextField campoDataNascimento = new JTextField(10);
    private final JTextField campoRg = new JTextField(12);
    private final JButton botaoCadastrar = new JButton("Cadastrar Aluno");

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private static final int TAMANHO_RA = 8;

    public PainelCadastroAluno() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; add(new JLabel("RA:"), c);
        c.gridx = 1; add(campoRa, c);

        c.gridx = 0; c.gridy = 1; add(new JLabel("Nome:"), c);
        c.gridx = 1; add(campoNome, c);

        c.gridx = 0; c.gridy = 2; add(new JLabel("Data nasc. (dd/MM/aaaa):"), c);
        c.gridx = 1; add(campoDataNascimento, c);

        c.gridx = 0; c.gridy = 3; add(new JLabel("RG:"), c);
        c.gridx = 1; add(campoRg, c);

        c.gridx = 1; c.gridy = 4; add(botaoCadastrar, c);

        botaoCadastrar.addActionListener(e -> cadastrar());
    }

    private void cadastrar() {
        String ra = campoRa.getText().trim();
        String nome = campoNome.getText().trim();
        String dataTexto = campoDataNascimento.getText().trim();
        String rg = campoRg.getText().trim();

        if (ra.isEmpty() || nome.isEmpty() || dataTexto.isEmpty() || rg.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Preencha todos os campos antes de cadastrar.",
                "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ra.length() != TAMANHO_RA) {
            JOptionPane.showMessageDialog(this,
                "O RA deve ter exatamente " + TAMANHO_RA + " caracteres.",
                "RA inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate dataNascimento;
        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dataNascimento = LocalDate.parse(dataTexto, formato);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                "Data de nascimento inválida. Use o formato dd/MM/aaaa.",
                "Data inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (alunoDAO.existeRa(ra)) {
                JOptionPane.showMessageDialog(this,
                    "Já existe um aluno cadastrado com este RA.",
                    "RA duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (alunoDAO.existeRg(rg)) {
                JOptionPane.showMessageDialog(this,
                    "Já existe um aluno cadastrado com este RG.",
                    "RG duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Aluno aluno = new Aluno(ra, nome, dataNascimento, rg);
            alunoDAO.cadastrar(aluno);

            JOptionPane.showMessageDialog(this,
                "Aluno cadastrado com sucesso!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();

        } catch (SQLException ex) {
            Mensagens.erroBanco(this, ex);
        }
    }

    private void limparCampos() {
        campoRa.setText("");
        campoNome.setText("");
        campoDataNascimento.setText("");
        campoRg.setText("");
    }
}