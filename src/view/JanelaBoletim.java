package view;

import dao.NotaDAO;
import util.Mensagens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class JanelaBoletim extends JDialog {

    private static final long serialVersionUID = 1L;

    public JanelaBoletim(JFrame pai, String ra, String nomeAluno) {
        super(pai, "Boletim de " + nomeAluno, true);

        String[] colunas = { "Disciplina", "Prova", "Nota" };
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        try {
            NotaDAO notaDAO = new NotaDAO();
            List<Object[]> linhas = notaDAO.listarBoletim(ra);
            for (Object[] linha : linhas) {
                modelo.addRow(linha);
            }
        } catch (SQLException ex) {
            Mensagens.erroBanco(this, ex);
        }

        JTable tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);

        setLayout(new java.awt.BorderLayout());
        add(scroll, java.awt.BorderLayout.CENTER);
        setSize(420, 300);
        setLocationRelativeTo(pai);
        setVisible(true);
    }
}