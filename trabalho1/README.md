# Sistema de Gerenciamento de Produtos e Usuários

Este é um projeto para gerenciamento de produtos, usuários e pedidos desenvolvido para a materia INF008 (POO). O sistema segue princípios de Programação Orientada a Objetos (POO) e utiliza persistência de dados para armazenar informações.

## Funcionalidades Principais

- **Cadastro e gerenciamento de produtos**: Criação, listagem e busca de produtos.
- **Cadastro de usuários**: Suporte para Administradores e Clientes.
- **Processamento de pedidos**: Carrinho de compras, finalização de pedidos.
- **Relatórios**: Produtos com menor estoque, pedidos mais caros.
- **Autenticação**: Login para usuários do sistema.

---

## Pré-requisitos

- **Java 17 ou superior**
- **Apache Maven** (para gerenciamento de dependências e build)

---


## Como Executar o Projeto

### 1. Instalar/atualizar o Maven

```bash
sudo apt update
sudo apt install maven
```

### 2. Compilar o projeto
No terminal, na raiz do projeto (A mesma que tem o pom.xml), executar o seguinte comando:

```bash
mvn clean compile
```

### 3. Executar o projeto
Após a compilação, execute o seguinte comando para iniciar o sistema:

```bash
java -cp target/classes App
```

---

## Autor(es)

- **Raphael de Jesus Gramosa**.
