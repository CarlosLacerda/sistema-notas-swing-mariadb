package util;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.sql.SQLException;

public class Mensagens {

    public static void erroBanco(Component pai, SQLException ex) {
        String texto;

        if (ex.getMessage() != null &&
            ex.getMessage().toLowerCase().contains("connection")) {
            texto = "Não foi possível conectar ao banco de dados. " +
                    "Verifique se o MariaDB está em execução.";
        } else {
            texto = "Ocorreu um erro ao acessar o banco de dados. " +
                    "Tente novamente em instantes.";
        }

        JOptionPane.showMessageDialog(pai, texto,
            "Erro", JOptionPane.ERROR_MESSAGE);
    }
}