# Baozi Store

API REST para cadastrar clientes, cadastrar produtos e registrar pedidos de um produto por vez. O projeto usa Java 17, Spring Boot, Spring Data JPA e H2. Os pacotes principais são `model`, `repository` e `controller`.

## Executar

Com JDK 17 ou superior e Maven 3.6.3 ou superior, abra o terminal na pasta do projeto.

```bash
mvn spring-boot:run
```

A API fica em http://localhost:8080. O H2 grava os dados na pasta `dados/` e cria as tabelas ao iniciar.

| Operação | Clientes | Produtos | Pedidos |
| --- | --- | --- | --- |
| POST · criar | /clientes | /produtos | /pedidos |
| GET · listar | /clientes | /produtos | /pedidos |
| GET · consultar | /clientes/{id} | /produtos/{id} | /pedidos/{id} |
| DELETE · excluir | /clientes/{id} | /produtos/{id} | /pedidos/{id} |

O POST recebe JSON e devolve 201 com o registro e o cabeçalho Location. GET devolve 200 e DELETE devolve 204 com corpo vazio. Dados inválidos recebem 400. IDs ausentes recebem 404. Para excluir um cliente ou produto vinculado, exclua primeiro o pedido. A API devolve 409 se houver vínculo. O campo `estoque` é um indicador booleano de disponibilidade, sem contagem ou baixa automática de unidades.

## Postman

Importe `postman/Baozi Store.postman_collection.json` e execute as 12 requisições de cima para baixo. Nas criações, os testes da coleção salvam os IDs das respostas para usar nas requisições seguintes. O nome de exemplo `Samuel5208099` pode ser alterado no corpo do primeiro POST. Para usar o Postman sem conta, monte as requisições manualmente e copie os IDs retornados pelos três POSTs.
