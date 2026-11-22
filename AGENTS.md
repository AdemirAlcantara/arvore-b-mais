# AGENTS.md

Diretrizes operacionais para desenvolvimento da **Árvore B+** (Pesquisa e Ordenação). Mantenha este arquivo enxuto (< 150 linhas) e atualize a seção final sempre que um erro recorrente for resolvido.

---

## 1. Visão Geral e Stack Tecnológica

- **Objetivo:** Implementar em **Java puro** uma Árvore B+ genérica na ordem `n` (testada no mínimo com `n = 4` e `n = 5`) atendendo aos 3 requisitos da atividade:
  1. **Exibição das folhas:** descer sempre por `vLig[0]` até a primeira folha e percorrer sequencialmente até o fim pelo encadeamento de folhas.
  2. **Percurso in-ordem:** percorrer e exibir os elementos da árvore em ordem.
  3. **`Main` de validação:** executar centenas de inserções e exclusões para `n = 4` e `n = 5` (no mínimo).
- **Stack:** Java 21 (JDK 21), Maven (wrapper `mvnw` / `mvnw.cmd`), JUnit Jupiter `5.12.1`.
- **JavaFX (`21.0.6`):** Presente no `pom.xml` por herança de template. **Não** adicione dependência de UI à lógica da Árvore B+; a entrega roda via console (`Main`).

---

## 2. Arquitetura e Estrutura de Diretórios

```text
arvore-b-mais/
├── pom.xml                                      # Build Maven (Java 21)
├── src/main/java/
│   ├── module-info.java                         # Módulo admeguimas.arvorebmais
│   └── admeguimas/arvorebmais/                  # Pacote principal (Main, No, BPlusTree)
├── src/test/java/admeguimas/arvorebmais/        # Testes JUnit 5
└── context/                                     # SOMENTE LEITURA (referências da disciplina)
    ├── ProjBTree/src/                           # Referência de estilo/lógica (Árvore B)
    └── docs/arvore-b-mais/Arvore B+.pdf         # Regras oficiais de B+ da disciplina
```

- **Nunca** modifique arquivos dentro de `context/`.
- Mantenha todas as classes de produção no pacote `admeguimas.arvorebmais`.

---

## 3. Comandos de Build, Execução e Testes

Execute a partir da raiz do repositório (Windows PowerShell):

```powershell
# Compilar o projeto
.\mvnw.cmd clean compile

# Executar testes automatizados (JUnit 5)
.\mvnw.cmd test

# Executar a classe Main após compilar
java -cp target/classes admeguimas.arvorebmais.Main
```

- Sempre valide com `.\mvnw.cmd clean compile` (e `.\mvnw.cmd test` quando houver testes) antes de concluir qualquer alteração.

---

## 4. Convenções e Restrições Estritas de Código (Code Style)

Siga a sintaxe, estrutura de dados e lógica de referência de `context/ProjBTree/src/` adaptadas às regras da Árvore B+ (`context/docs/arvore-b-mais/Arvore B+.pdf`):

### Estruturas e Restrições
- **Proibido** usar `ArrayList`, `LinkedList`, `TreeMap` ou qualquer coleção do `java.util` para implementar nós ou ligações da árvore. Use **arrays primitivos/fixos** (`int[]`, `No[]`) controlados por tamanho lógico `tl`.
- **Ordem dinâmica `n`:** Diferente do `m = 2` estático de `ProjBTree`, receba `n` dinamicamente (construtor/parâmetro) para suportar `n = 4`, `n = 5` e outras ordens sem recompilar.
- **Dimensionamento do `No`:** Aloque `vInfo` com tamanho `n` (máximo `n - 1` chaves + 1 espaço de overflow) e `vLig` com tamanho `n + 1` (máximo `n` ponteiros + 1 espaço de overflow). Inclua referência `prox` (e `ant`, se necessário) para encadeamento das folhas.
- **Identificação de folha:** Considere nó folha quando `no.getvLig(0) == null` (mesmo padrão de `ProjBTree`).

### Nomenclatura e Padrão de Métodos
- Preserve os nomes e estilo de `ProjBTree`: `raiz`, `vInfo`, `vLig`, `tl`, `getvInfo`/`setvInfo`, `getvLig`/`setvLig`, `getTl`/`setTl`.
- No `No`: `procurarPosicao(int info)`, `remanejar(int pos)`, `remanejarExclusao(int pos)`.
- Na árvore: `navegarAteFolha`, `localizarPai`, `split`, `inserir`, `exclusao`, `redistribuir_concatenar`, `in_ordem`, `exibirFolhas`.

### Regras Algorítmicas Obrigatórias da B+
- **Navegação em nó interno:** Subárvore `vLig[i]` contém chaves `< vInfo[i]` e `>= vInfo[i-1]`. Se `info == vInfo[pos]` em nó interno, desça para `vLig[pos + 1]` (não pare no nó interno como na Árvore B).
- **Overflow (`tl > n - 1`):**
  - **Folha:** 1º nó fica com `⌈(n - 1) / 2⌉` chaves; 2º nó com o restante; **copie** `cx2.vInfo[0]` para o pai; atualize o encadeamento entre folhas (`prox`/`ant`).
  - **Interno:** 1º nó fica com `⌈n / 2⌉ - 1` chaves; **mova** (sem duplicar) a chave seguinte para o pai; 2º nó fica com o restante e respectivos `vLig`.
- **Underflow (`no != raiz && tl < ⌈n / 2⌉ - 1`):**
  - **Folha:** tente redistribuir com irmã (atualizando chave no pai com menor chave do nó direito); caso contrário, concatene para a esquerda, ajuste o encadeamento de folhas e remova a chave do pai.
  - **Interno:** redistribua passando pelo pai; na concatenação, desça a chave separadora do pai, junte valores/ponteiros e remova do pai. Se `raiz` ficar com `tl == 0`, promova o único filho a `raiz`.

---

## 5. Padrões de Git e Fluxo de Trabalho

- **Triangular Workflow:** Trabalhe sempre na sua branch fixa `dev/<nome>` (`git pull` puxa de `origin/main`, `git push` envia para `origin/dev/<nome>`).
- **Antes de editar:** Execute `git pull` para sincronizar com `main`.
- **Nunca** faça push direto na `main`. Integre via Pull Request (`main <- dev/<nome>`) e **não** delete a branch após o merge.
- **Prefixos de commit obrigatórios:**
  - `feat:` nova classe, método ou teste.
  - `fix:` correção de bug, lógica ou conflito.
  - `docs:` documentação, comentários ou configuração (`README.md`, `AGENTS.md`, `pom.xml`).
- **Não** versione `target/`, arquivos de IDE ou `context/ProjBTree/`.

---

## 6. Erros Conhecidos e Lições Aprendidas

- **Erro de sintaxe em `module-info.java` / `JavaSE-1.8 vs JRE 21`:** Mantenha `<source>21</source>` e `<target>21</target>` **dentro** de `<plugin>maven-compiler-plugin</plugin>` no `pom.xml` (nunca diretamente sob `<project>`).
- **Chave igual em nó interno da B+:** Ao navegar até a folha, trate `info >= vInfo[pos]` avançando o índice; usar apenas `>` (como na Árvore B) quebra buscas e exclusões de chaves promovidas.
