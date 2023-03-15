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
SOLID é uma sigla que representa cinco princípios de programação:

* Single Responsibility Principle (Princípio da Responsabilidade Única)
* Open-Closed Principle (Princípio Aberto-Fechado)
* Liskov Substitution Principle (Princípio da Substituição de Liskov)
* Interface Segregation Principle (Princípio da Segregação de Interface)
* Dependency Inversion Principle (Princípio da Inversão de Dependência)

Estamos aplicando os seguintes princípios do SOLID:

* Single Responsibility Principle (Princípio da responsabilidade única): porque cada classe de validação tem apenas uma responsabilidade.
* Open-Closed Principle (Princípio aberto-fechado): na classe service, AgendadeConsultas, porque ela está fechada para modificação, não precisamos mexer nela. Mas ela está aberta para extensão, conseguimos adicionar novos validadores apenas criando as classes implementando a interface.
* Dependency Inversion Principle (Princípio da inversão de dependência): porque nossa classe service depende de uma abstração, que é a interface, não depende dos validadores, das implementações especificamente. O módulo de alto nível, a service, não depende dos módulos de baixo nível, que são os validadores.


### IMPORTANTE
Mas é importante ficarmos atentos, pois muitas vezes não é necessário criar um Service e, consequentemente, adicionar mais uma camada e complexidade desnecessária à nossa aplicação. Uma regra que podemos utilizar é a seguinte: se não houverem regras de negócio, podemos simplesmente realizar a comunicação direta entre os controllers e os repositories da aplicação.

## Testes com in-memory database
Como citado no vídeo anterior, podemos realizar os testes de interfaces repository utilizando um banco de dados em memória, como o H2, ao invés de utilizar o mesmo banco de dados da aplicação.

Caso você queira utilizar essa estratégia de executar os testes com um banco de dados em memória, será necessário incluir o H2 no projeto, adicionando a seguinte dependência no arquivo pom.xml:

```
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```

E também deve remover as anotações @AutoConfigureTestDatabase e @ActiveProfiles na classe de teste, deixando-a apenas com a anotação @DataJpaTest:

```
@DataJpaTest
class MedicoRepositoryTest {
    //resto do código permanece igual
}
```

Você também pode apagar o arquivo application-test.properties, pois o Spring Boot realiza as configurações de url, username e password do banco de dados H2 de maneira automática.

