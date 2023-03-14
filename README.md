# alura-medvollapi

## CRUDs
Para implementar esta ou quaisquer outras funcionalidades, seguimos uma espécie de passo-a-passo. Precisamos criar sempre os seguintes tipos de códigos:

* Controller, para mapear a requisição da nova funcionalidade;
* DTOs, que representam os dados que chegam e saem da API; - Os nomes dos campos enviados no JSON para a API devem ser idênticos aos nomes dos atributos das classes DTO, pois assim o Spring consegue preencher corretamente as informações recebidas. Pode-se usar JsonAlias
* Entidade JPA;
* Repository, para isolar o acesso ao banco de dados;
* Migration, para fazer as alterações no banco de dados.

Estes são os cinco tipos de código que sempre desenvolveremos para uma nova funcionalidade. Isso também se aplica ao agendamento das consultas, incluindo um sexto item à lista, as regras de negócio. Nesta aula, entenderemos como implementar as regras de negócio com algoritmos mais complexos.

## O Padrão Service
Service pode ser interpretado de várias maneiras: 
* pode ser um Use Case (Application Service); 
* um Domain Service, que possui regras do seu domínio; 
* um Infrastructure Service, que usa algum pacote externo para realizar tarefas; 
* etc.

Apesar da interpretação ocorrer de várias formas, a ideia por trás do padrão é separar as regras de negócio, as regras da aplicação e as regras de apresentação para que elas possam ser facilmente testadas e reutilizadas em outras partes do sistema.

### Utilização
* Services mais genéricos, responsáveis por todas as atribuições de um Controller; 
* ou ser ainda mais específico, aplicando assim o S do SOLID

### SOLID
Single Responsibility Principle (Princípio da Responsabilidade Única). Esse princípio nos diz que uma classe/função/arquivo deve ter apenas uma única responsabilidade.

Pense em um sistema de vendas, no qual provavelmente teríamos algumas funções como: Cadastrar usuário, Efetuar login, Buscar produtos, Buscar produto por nome, etc. Logo, poderíamos criar os seguintes Services: CadastroDeUsuarioService, EfetuaLoginService, BuscaDeProdutosService, etc.

### IMPORTANTE
Mas é importante ficarmos atentos, pois muitas vezes não é necessário criar um Service e, consequentemente, adicionar mais uma camada e complexidade desnecessária à nossa aplicação. Uma regra que podemos utilizar é a seguinte: se não houverem regras de negócio, podemos simplesmente realizar a comunicação direta entre os controllers e os repositories da aplicação.