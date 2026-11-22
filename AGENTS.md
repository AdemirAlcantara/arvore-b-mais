# AGENTS.md

Diretrizes operacionais para desenvolvimento da **Árvore B+** (Pesquisa e Ordenação). Mantenha este arquivo enxuto (< 150 linhas) e atualize a seção final sempre que um erro recorrente for resolvido.

---

## 1. Visão Geral e Stack Tecnológica

- **Objetivo:** Implementar em **Java puro** (100% iterativo, sem recursividade) uma Árvore B+ para ordens **`n = 4`** e **`n = 5`**, atendendo aos 3 requisitos da atividade:
  1. **Exibição das folhas:** descer sempre por `vLig[0]` até a primeira folha e percorrer sequencialmente até o fim pelo encadeamento de folhas.
  2. **Percurso in-ordem:** percorrer e exibir os elementos da árvore em ordem (iterativamente com `Pilha`).
  3. **`Main` de validação:** executar centenas de inserções e exclusões via `for` para `n = 4` e `n = 5`.
- **Stack:** Java 21 (JDK 21), Maven (wrapper `mvnw` / `mvnw.cmd`), JUnit Jupiter `5.12.1`, Surefire `3.5.4`.
- **JavaFX (`21.0.6`):** Presente no `pom.xml` por herança de template. **Não** adicione dependência de UI à lógica da Árvore B+; a entrega roda via console (`Main`).

---

## 2. Arquitetura e Estrutura de Diretórios

```text
arvore-b-mais/
├── pom.xml                                      # Build Maven (Java 21 + JUnit 5)
├── src/main/java/admeguimas/arvorebmais/        # Main, No, NoPilha, Pilha, ArvoreBMais
├── src/test/java/admeguimas/arvorebmais/        # ArvoreBMaisTest (JUnit 5)
└── context/                                     # SOMENTE LEITURA (referências da disciplina)
    ├── ProjBTree/src/                           # Referência de estilo/lógica (Árvore B)
    └── docs/                                    # Regras B+ (Arvore B+.pdf) e Pilha (Estrutura de Pilha.md)
```

- **Nunca** modifique arquivos dentro de `context/`.
- Mantenha todas as classes de produção no pacote `admeguimas.arvorebmais`.

---

## 3. Comandos de Build, Execução e Testes

Execute a partir da raiz do repositório (Windows PowerShell):

```powershell
# Compilar e executar testes automatizados (JUnit 5)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"; .\mvnw.cmd clean test

# Executar a classe Main após compilar
& "C:\Program Files\Java\jdk-21\bin\java.exe" -cp target/classes admeguimas.arvorebmais.Main
```

- Sempre valide com `.\mvnw.cmd clean test` (ou `-o` offline quando já em cache) antes de concluir qualquer alteração.

---

## 4. Convenções e Restrições Estritas de Código (Code Style)

Siga a sintaxe, estrutura de dados e lógica de `context/ProjBTree/src/` e `context/docs/Estrutura de Pilha.md` adaptadas às regras da Árvore B+ (`context/docs/arvore-b-mais/Arvore B+.pdf`):

### Estruturas e Restrições
- **Proibido recursividade:** Nenhuma função recursiva em `src/main/java/`. Substitua propagação e percursos usando `Pilha` e `NoPilha`.
- **Proibido `java.util`:** Não use coleções do `java.util` nas estruturas da árvore/pilha. Use arrays fixos (`int[]`, `No[]`) controlados por `tl` e nós encadeados (`NoPilha`).
- **Escopo estrito de ordem:** Suporte e teste exclusivamente `n = 4` e `n = 5`. Em `ArvoreBMais`, `n` = ordem (máx. `n` ligações e `n - 1` infos por nó) e `m = (n - 1) / 2` = mínimo de infos em nó não-raiz (`1` para `n=4`, `2` para `n=5`; equivale ao `No.m` do `ProjBTree`). `No(int n)` usa `n` só para dimensionar os vetores.
- **Fluxo de controle:** **Nunca** use `return;`, `break;`, `continue;` nem `throw`. Encerre laços com flags `boolean` e use `if/else`; retorno de valor no padrão `if (raiz != null) { ... } return null;`.
- **Simplicidade:** `Main` e testes usam laços `for` simples (crescente, decrescente, pares/ímpares), sem geradores ou embaralhamentos.
- **Sem `vPos[]`:** Não há manipulação de arquivo; `No` mantém apenas `vInfo[]` (tamanho `n`), `vLig[]` (tamanho `n + 1`), `tl`, `prox` e `ant`.
- **Identificação de folha:** Considere nó folha quando `no.getvLig(0) == null`.

### Nomenclatura e Regras Algorítmicas
- **Nomes obrigatórios (apenas `camelCase`):** `ArvoreBMais`, `No`, `NoPilha`, `Pilha`, `raiz`, `vInfo`, `vLig`, `tl`, `m`, `buscaNo`, `buscaPos`, `buscaPosInterno`, `buscarPai`, `navegarAteFolha`, `remanejar`, `remanejarExclusao`, `split`, `inserir`, `exclusao`, `redistribuirConcatenar`, `inOrdem`, `exibirFolhas`. Use estritamente `camelCase` para variáveis e funções (sem `snake_case`).
- **Sem o termo "chave":** Valores inteiros se chamam `info` (como em `vInfo`); auxiliares de nó usam `noAux`, `noPai`, `noFilho`, `noAtual`, `noAnt`, `noProx`.
- **Navegação em nó interno:** Subárvore `vLig[i]` contém chaves `< vInfo[i]` e `>= vInfo[i-1]`. Use `buscaPosInterno(info)` (`info >= vInfo[pos]`) para descer sempre até a folha.
- **Overflow (`tl > n - 1`):**
  - **Folha:** `cx1` fica com `n / 2` chaves; `cx2` com o restante; **copie** `cx2.vInfo[0]` para o pai; atualize `ant`/`prox`.
  - **Interno:** `cx1` fica com `m` chaves; **mova** (sem duplicar) `vInfo[m]` para o pai; `cx2` fica com o restante e respectivos `vLig`.
- **Underflow (`no != raiz && tl < m`):**
  - **Folha:** redistribua com irmã (atualizando separador no pai com menor chave da direita); senão, concatene na esquerda, atualize `ant`/`prox` e remova separador do pai.
  - **Interno:** redistribua passando pelo pai; na concatenação, desça o separador do pai e junte valores/ponteiros na esquerda. Se `raiz` ficar com `tl == 0`, promova `esq` a `raiz`. Atualize eventual cópia da chave removida em ancestral interno.

---

## 5. Padrões de Git e Fluxo de Trabalho

- **Triangular Workflow:** Trabalhe sempre na branch `dev/<nome>` (`git pull` de `origin/main`, `git push` para `origin/dev/<nome>`).
- **Nunca** faça push direto na `main`. Integre via PR (`main <- dev/<nome>`) e **não** delete a branch após merge.
- **Prefixos:** `feat:` | `fix:` | `docs:`. **Não** versione `target/`, arquivos de IDE ou `context/ProjBTree/`.

---

## 6. Erros Conhecidos e Lições Aprendidas

- **`pom.xml` / Java 21 / Surefire:** Mantenha `<source>21</source>` e `<target>21</target>` dentro de `maven-compiler-plugin`, inclua `maven-surefire-plugin` (`3.5.4`) para JUnit 5 no Maven 3.8.5, e use `JAVA_HOME` apontando para `jdk-21` no terminal se `javapath` travar.
- **Chave igual em nó interno (`>=`):** Ao descer até a folha, use `buscaPosInterno` (`info >= vInfo[pos]`); usar apenas `>` quebra buscas e exclusões.
- **Underflow com `tl == 0` em `n = 4`:** Nunca busque o pai usando `no.getvInfo(0)` durante `redistribuirConcatenar`, pois em `n = 4` (`m = 1`) o nó entra em underflow com `tl == 0`; recupere o pai via `Pilha` e localize `posPai` por identidade (`pai.getvLig(i) == no`).
