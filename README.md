# 🎓 Sistema de Notas — Java Swing + MariaDB

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing-blueviolet?style=for-the-badge&logo=java&logoColor=white)
![MariaDB](https://img.shields.io/badge/Banco-MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![JDBC](https://img.shields.io/badge/Acesso%20a%20Dados-JDBC-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/status-conclu%C3%ADdo-brightgreen?style=for-the-badge)
![License](https://img.shields.io/badge/license-MIT-lightgrey?style=for-the-badge)
![OO](https://img.shields.io/badge/Paradigma-Orienta%C3%A7%C3%A3o%20a%20Objetos-9cf?style=for-the-badge)

Aplicativo desktop em Java para gerenciamento de alunos e notas, desenvolvido em dupla para a disciplina de **Orientação a Objetos — PPOO**, utilizando **Swing/AWT** para a interface gráfica e **JDBC** para a comunicação com um banco de dados **MariaDB**.

> Aulas 05 e 06 PPOO 

---

## 📚 Sobre a atividade

Este projeto parte de um **aplicativo-base de laboratório**, que a dupla precisou compreender, corrigir e evoluir com novas funcionalidades. Ele demonstra a integração entre **Java, Swing, JDBC e MariaDB** em uma arquitetura em camadas (Model / DAO / View), aplicando:

- Separação de responsabilidades entre **modelo de dados** (`model`), **acesso a dados** (`dao`) e **interface gráfica** (`view`);
- Uso de `PreparedStatement` em todas as operações que recebem dados do usuário, evitando concatenação insegura de SQL;
- Consultas com `JOIN` entre as tabelas `alunos`, `disciplinas`, `tipos_provas` e `notas`;
- Apresentação de resultados de consulta em janelas separadas (`JDialog`) e de listagens em `JTable` dentro de `JScrollPane`;
- Tratamento centralizado de exceções (`SQLException`), com mensagens compreensíveis ao usuário, em vez de expor detalhes técnicos.

## 🎯 Situação-problema

Uma instituição de ensino deseja um sistema para gerenciamento das notas de seus alunos, cobrindo:

- cadastro de alunos;
- lançamento de notas;
- consulta da nota de um aluno em uma disciplina/prova específica, exibida em janela separada;
- boletim completo do aluno, exibido em uma `JTable`.

## 🗂 Estrutura do projeto

```
SistemaNotas/
├── src/
│   ├── Main.java                      # Ponto de entrada da aplicação
│   ├── model/
│   │   ├── Aluno.java                 # RA, nome, data de nascimento, RG
│   │   ├── Disciplina.java            # id, nome
│   │   ├── TipoProva.java             # id, descrição (ex.: P1, P2)
│   │   ├── Nota.java                  # RA do aluno, disciplina, tipo de prova, valor
│   │   └── ResultadoConsulta.java     # DTO usado na consulta individual de nota
│   ├── dao/
│   │   ├── AlunoDAO.java              # cadastro, verificação de RA/RG duplicados, listagem
│   │   ├── DisciplinaDAO.java         # listagem de disciplinas
│   │   ├── TipoProvaDAO.java          # listagem de tipos de prova
│   │   └── NotaDAO.java               # cadastro, verificação de duplicidade, consulta (JOIN) e boletim
│   ├── view/
│   │   ├── JanelaPrincipal.java       # JFrame com abas (JTabbedPane)
│   │   ├── PainelCadastroAluno.java   # aba de cadastro de alunos
│   │   ├── PainelLancamentoNota.java  # aba de lançamento de notas
│   │   ├── PainelConsultaNota.java    # aba de consulta de nota + acesso ao boletim
│   │   └── JanelaBoletim.java         # JDialog com JTable do boletim do aluno
│   └── util/
│       ├── Conexao.java               # abertura de conexão JDBC com o MariaDB
│       └── Mensagens.java             # utilitário central de mensagens de erro de banco
├── .classpath / .project              # configuração do projeto Eclipse
└── README.md                          # este arquivo
```

## 🖥️ Funcionalidades

### 1. Cadastro de alunos (`PainelCadastroAluno`)
- Campos: RA, Nome, Data de Nascimento (`dd/MM/aaaa`), RG;
- Validações: campos obrigatórios preenchidos, tamanho do RA (8 caracteres), data em formato válido, **RA duplicado**, **RG duplicado** (com mensagens específicas para cada caso);
- Inserção via `PreparedStatement`;
- Após o cadastro, os campos são limpos e o aluno passa a aparecer automaticamente nas demais abas (via `atualizarCombos()`).

### 2. Lançamento de notas (`PainelLancamentoNota`)
- Seleção de Aluno, Disciplina e Tipo de Prova via `JComboBox`;
- Campo de nota (aceita vírgula ou ponto decimal);
- **Validação do intervalo da nota entre 0,00 e 10,00**, usando `BigDecimal.compareTo(...)`;
- Verificação de nota duplicada para a combinação **Aluno + Disciplina + Prova**;
- Inserção via `PreparedStatement`.

### 3. Consulta de nota (`PainelConsultaNota`)
- Seleção de Aluno, Disciplina e Tipo de Prova;
- Botão **Consultar nota**: executa uma consulta com `JOIN` entre `notas`, `alunos`, `disciplinas` e `tipos_provas`, exibindo o resultado em um `JDialog` (Aluno, RA, Disciplina, Prova, Nota);
- Caso não haja nota lançada para a combinação, o sistema informa que não há resultado;
- Botão **Ver Boletim Completo**: abre `JanelaBoletim`, um `JDialog` com uma `JTable` listando todas as notas do aluno selecionado (Disciplina, Prova, Nota), também via `JOIN`.

### 4. Tratamento de erros
- Mensagens de erro amigáveis para: campos obrigatórios vazios, RA com tamanho incorreto, data inválida, RA/RG duplicados, nota inválida, nota fora do intervalo, nota duplicada;
- **Toda falha de banco de dados (`SQLException`)** — em cadastro de aluno, lançamento de nota, atualização de combos, consulta e boletim — é tratada de forma centralizada pela classe `util.Mensagens.erroBanco(...)`, que identifica falhas de conexão e exibe uma mensagem amigável, sem expor `SQLException` cru ao usuário.

## 🗄️ Banco de dados

A aplicação se conecta a um banco **MariaDB** via `util/Conexao.java`:

```java
private static final String URL = "jdbc:mariadb://localhost:3306/";
private static final String USUARIO = "";
private static final String SENHA = "";
```

> ⚠️ Preencha `URL` (incluindo o nome do schema), `USUARIO` e `SENHA` conforme o ambiente de cada máquina antes de rodar o projeto.

Com base nas colunas usadas nas consultas SQL das classes DAO, o banco deve conter, no mínimo, as seguintes tabelas:

| Tabela | Colunas identificadas |
|---|---|
| `alunos` | `ra`, `nome`, `data_nascimento`, `rg` |
| `disciplinas` | `id_disciplina`, `nome_disciplina` |
| `tipos_provas` | `id_tipo_prova`, `nome_prova` |
| `notas` | `ra` (FK), `id_disciplina` (FK), `id_tipo_prova` (FK), `nota` |

> 📌 O script de criação das tabelas não está incluído neste repositório — recomenda-se criar um arquivo `sql/schema.sql` com o `CREATE TABLE` de cada entidade e os relacionamentos (`FOREIGN KEY`), para facilitar a configuração do ambiente por qualquer pessoa que for rodar o projeto.

## ▶️ Como executar

### Pré-requisitos
- **JDK 21** (ou compatível — configurado no `.classpath` do Eclipse);
- **MariaDB** em execução localmente (ou acessível pela rede);
- Driver JDBC do MariaDB (`mariadb-java-client`), referenciado no classpath do projeto.

### Passos
1. Crie o banco de dados e as tabelas (`alunos`, `disciplinas`, `tipos_provas`, `notas`) no MariaDB;
2. Preencha `URL`, `USUARIO` e `SENHA` em `src/util/Conexao.java` para o seu ambiente;
3. Importe o projeto no Eclipse (**File → Import → Existing Projects into Workspace**) ou compile manualmente:
   ```bash
   javac -cp ".;caminho/para/mariadb-java-client.jar" -d bin $(find src -name "*.java")
   ```
4. Execute a classe `Main`:
   ```bash
   java -cp "bin;caminho/para/mariadb-java-client.jar" Main
   ```
   *(em Linux/macOS, use `:` no lugar de `;` no classpath)*

## 📋 Requisitos do exercício de laboratório

O enunciado exige que a dupla **mantenha** as funcionalidades já existentes no aplicativo-base e **implemente melhorias adicionais**. Status final de cada requisito obrigatório:

### ✅ Implementado
- [x] Cadastro de aluno com RA, nome, data de nascimento e RG
- [x] Validação de campos obrigatórios no cadastro de aluno
- [x] Validação do tamanho do RA
- [x] Validação de data válida
- [x] Verificação de RA duplicado
- [x] Verificação de RG duplicado (com mensagem específica corrigida)
- [x] Atualização automática das listas de alunos nas demais telas
- [x] Lançamento de nota com seleção de aluno, disciplina e tipo de prova
- [x] **Validação do intervalo da nota (0,00 a 10,00)**
- [x] Verificação de nota duplicada (Aluno + Disciplina + Prova)
- [x] Uso de `PreparedStatement` em todas as operações com dados do usuário
- [x] Consulta de nota individual em janela separada (`JDialog`)
- [x] Mensagem adequada quando não há nota para a combinação consultada
- [x] Consultas SQL com `JOIN` entre as tabelas relacionadas
- [x] Boletim completo do aluno em janela separada com `JTable`
- [x] Tratamento de exceções com mensagens compreensíveis, **centralizado em `Mensagens.erroBanco(...)`** em todas as telas

## 🧩 Componentes e conceitos utilizados

| Componente / Conceito | Onde é usado |
|---|---|
| `JFrame` + `JTabbedPane` | Janela principal com abas (`JanelaPrincipal`) |
| `JPanel` + `GridBagLayout` | Layout dos formulários de cada aba |
| `JComboBox` | Seleção de Aluno, Disciplina e Tipo de Prova |
| `JDialog` | Janela de resultado da consulta e do boletim |
| `JTable` + `DefaultTableModel` | Exibição do boletim de notas |
| `JScrollPane` | Rolagem da tabela do boletim |
| `JOptionPane` | Mensagens de sucesso, aviso e erro |
| `PreparedStatement` | Todas as operações de INSERT/SELECT com dados do usuário |
| `JOIN` (SQL) | Consulta individual de nota e listagem do boletim |
| `BigDecimal` | Representação e validação do valor da nota |

## 🛠 Tecnologias utilizadas

- ☕ Java 21 (Swing/AWT)
- 🧩 Programação Orientada a Objetos (camadas Model / DAO / View)
- 🗄️ MariaDB + JDBC (`mariadb-java-client`)
- 📐 `GridBagLayout` para os formulários

## ✍️ Autores

Atividade desenvolvida como parte do laboratório da disciplina de Orientação a Objetos — Aulas 05 e 06 PPOO
