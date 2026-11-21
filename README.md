# Árvore B+ (Pesquisa e Ordenação)

Implementação em Java puro da estrutura de dados **Árvore B+** com operações de inserção, exclusão (com suporte a ordens dinâmicas, como $n = 4$ e $n = 5$), percurso sequencial nas folhas e percurso in-ordem.

---

## Pré-requisitos e Configuração Inicial

1. **Aceitar o convite no GitHub:** Verifique seu e-mail ou notificações do GitHub e aceite o convite de colaborador do repositório.
2. **Clonar o repositório:**
   * Abra o terminal e navegue até a pasta pai onde deseja salvar seus projetos (ex: `cd Documentos/Projetos`).
   * **Não** selecione como pasta pai uma pasta sincronizada em Nuvem (como OneDrive, Google Drive ou Dropbox), pois problemas de versionamento, bloqueio de arquivos e corrupção do `.git` podem ocorrer.
   * **Não** crie a pasta `arvore-b-mais` manualmente, pois o comando `git clone` já a criará automaticamente.
   * Execute o clone e entre na pasta gerada:
   ```bash
   git clone https://github.com/AdemirAlcantara/arvore-b-mais.git
   cd arvore-b-mais
   ```
3. **Abrir na IDE (IntelliJ IDEA ou VS Code):**
   * **IntelliJ:** Vá em *File > Open* e selecione a pasta raiz `arvore-b-mais` (o arquivo `pom.xml` será reconhecido automaticamente).
   * **VS Code:** Abra a pasta raiz `arvore-b-mais` com o *Extension Pack for Java* instalado.

---

## Fluxo de Trabalho com Git e GitHub (Triangular Workflow)

Cada integrante trabalha em **sua própria branch fixa** (ex: `dev/ademir` e `dev/nome-do-amigo`). Configuramos o Git para que o `git pull` sempre puxe atualizações diretamente da `main` e o `git push` sempre envie para a sua branch individual, sem precisar ficar alternando de branch no terminal.

### 1. Configurar sua branch individual (apenas na primeira vez)
Execute os comandos abaixo em sequência para criar sua branch e configurar o fluxo automático:
```bash
# Cria e entra na sua branch individual
git checkout -b dev/seu-nome

# Faz o "git pull" sempre puxar as atualizações direto da main
git branch --set-upstream-to=origin/main

# Faz o "git push" sempre enviar para a sua branch no GitHub (origin/dev/seu-nome)
git config push.default current
```

### 2. Rotina diária: Atualizar, Editar, Salvar e Enviar

> **⚠️ ATENÇÃO:** Sempre execute `git pull` **ANTES** de começar a editar qualquer arquivo.
>
> Se você alterar um arquivo localmente sem commitar e o mesmo arquivo tiver sido modificado na `main`, o Git bloqueará o `git pull` até que você salve suas alterações (`git commit` ou `git stash`).

```bash
# 1. Traz tudo o que há de novo na main direto para a sua branch
git pull

# 2. (Após editar os arquivos na IDE) Verifica o que foi alterado
git status

# 3. Adiciona as alterações para o commit
git add .
# Ou para incluir arquivos/pastas individualmente:
git add caminho/do/arquivo-ou-pasta

# 4. Verifica se os arquivos em verde são realmente os que devem ir para o commit
git status
# Se precisar retirar algum arquivo/pasta da área de preparação (staged):
git restore --staged caminho/do/arquivo-ou-pasta

# 5. Registra um commit descritivo
git commit -m "feat: implementa percurso sequencial nas folhas"

# 6. Envia as alterações para a sua branch remota (dev/seu-nome)
git push
```

**Padrão de prefixos para commits:**
* `feat:` Nova Implementação
	* exemplo: Nova funcionalidade, classe, método ou teste implementado.
* `fix:` Correção de Implementação
	* exemplo: Correção de bugs, falhas de lógica ou ajustes de conflito.
* `docs:` Alteração em Documentos
	* exemplo: `README.md`, comentários no código ou configurações do projeto.

### 3. Integrar na `main` (Pull Request)
Quando concluir uma parte funcional e testada:
1. Acesse o repositório no GitHub.
2. Clique no botão **Compare & pull request** referente à sua branch (`dev/seu-nome`).
3. Confira se o destino está apontando para `main` <- `dev/seu-nome` e clique em **Create pull request**.
4. Avise o parceiro da dupla para revisar e clicar em **Merge pull request** *(não delete sua branch após o merge, pois ela continuará sendo usada)*.

---

## Requisitos da Entrega

* **a) Exibição das folhas:** Descer sempre pela ligação `0` até alcançar a primeira folha e percorrer todas as folhas sequencialmente até o final, exibindo os valores.
* **b) Percurso in-ordem:** Exibir os elementos percorrendo a árvore em ordem.
* **c) Classe `Main` de validação:** Realizar centenas de inserções e exclusões testando pelo menos com ordem $n = 4$ e $n = 5$.
