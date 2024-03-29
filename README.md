# alura-medvollapi
Spring Boot 3

- Criação do zero uma API Rest em Java com Spring Boot e CRUDs utilizando o banco de dados MySQL
- Flyway como ferramenta de Migrations da API
- Validações utilizando o Bean Validation
- Paginação dos dados da API
- Padronização dos retornos dos controllers da API
- Uso dos códigos HTTP corretamente
- Spring Security
- Mecanismo de autenticação na API
- Controle de acesso na API
- Json Web Token para controlar o acesso na API
- Isolar códigos de regras de negócio em uma aplicação
- Princípios SOLID
- API documentada seguindo o padrão OpenAPI
- Testes automatizados em uma aplicação com Spring Boot
- Build de uma aplicação com Spring Boot
- Variáveis de ambiente e prepare uma aplicação para o deploy

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

## Build
gerar o jar da aplicação via terminal/prompt com o seguinte comando:

mvn clean package

Uma alternativa à opção anterior é gerar o jar pelo IntelliJ, acessando a aba Maven, que fica localizada no canto superior direito da tela, expandindo o item Lifecycle, clicando com o botão direito do mouse no item package e escolhendo a opção Run Maven Build
### Local
Após realizar qualquer uma das opções indicadas anteriormente, o jar será gerado dentro da pasta target do projeto.



### Execução
java -Dspring.profiles.active=prod -DDATASOURCE_URL=jdbc:mysql://localhost/vollmed_api -DDATASOURCE_USERNAME=root -DDATASOURCE_PASSWORD=root -jar api-0.0.1-SNAPSHOT.jar

## Profiles

### Dev - [application.properties](src%2Fmain%2Fresources%2Fapplication.properties)
spring.datasource.url=jdbc:mysql://localhost/vollmed_api
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

#spring.jpa.hibernate.ddl-auto=update

#spring.data.web.pageable.page-parameter=pagina
#spring.data.web.pageable.size-parameter=tamanho
#spring.data.web.sort.sort-parameter=ordem

server.error.include-stacktrace=never

api.security.token.secret={JWT_SECRET:12345678}

### Test - [application-test.properties](src%2Fmain%2Fresources%2Fapplication-test.properties)
spring.datasource.url=jdbc:mysql://localhost/vollmed_api_test

### Prod - [application-prod.properties](src%2Fmain%2Fresources%2Fapplication-prod.properties)
spring.datasource.url=${DATASOURCE_URL}
spring.datasource.username=${DATASOURCE_USERNAME}
spring.datasource.password=${DATASOURCE_PASSWORD}

spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

## Native Image
Imagem nativa é uma tecnologia utilizada para compilar uma aplicação Java, incluindo todas as suas dependências, gerando um arquivo binário executável que pode ser executado diretamente no sistema operacional, sem a necessidade de se utilizar a JVM. Mesmo sem executar numa JVM, a aplicação também contará com os recursos dela, como gerenciamento de memória, garbage collector e controle de execução de threads.

Para saber mais detalhes sobre a tecnologia de imagens nativas acesse a documentação no site: https://www.graalvm.org/native-image

### Native Imagem com Spring Boot 3
Uma maneira bem simples de gerar uma imagem nativa da aplicação é utilizando um plugin do Maven, que deve ser incluído no arquivo pom.xml:

<plugin>
  <groupId>org.graalvm.buildtools</groupId>
  <artifactId>native-maven-plugin</artifactId>
</plugin>COPIAR CÓDIGO
Pronto! Essa é a única alteração necessária no projeto. Após isso, a geração da imagem deve ser feita via terminal, com o seguinte comando Maven sendo executado no diretório raiz do projeto:

./mvnw -Pnative native:compileCOPIAR CÓDIGO
O comando anterior pode levar vários minutos para finalizar sua execução, sendo totalmente normal essa demora.

Atenção! Para executar o comando anterior e gerar a imagem nativa do projeto, é necessário que você tenha instalado em seu computador o GraalVM (máquina virtual Java com suporte ao recurso de Native Image) em uma versão igual ou superior a 22.3.

Após o comando anterior finalizar, será gerado no terminal um log como o seguinte:

Top 10 packages in code area:           Top 10 object types in image heap:
3,32MB jdk.proxy4                      19,44MB byte[] for embedded resources
1,70MB sun.security.ssl                16,01MB byte[] for code metadata
1,18MB java.util                        8,91MB java.lang.Class
936,28KB java.lang.invoke                 6,74MB java.lang.String
794,65KB com.mysql.cj.jdbc                6,51MB byte[] for java.lang.String
724,02KB com.sun.crypto.provider          4,89MB byte[] for general heap data
650,46KB org.hibernate.dialect            3,07MB c.o.s.c.h.DynamicHubCompanion
566,00KB org.hibernate.dialect.function   2,40MB byte[] for reflection metadata
563,59KB com.oracle.svm.core.code         1,30MB java.lang.String[]
544,48KB org.apache.catalina.core         1,25MB c.o.s.c.h.DynamicHu~onMetadata
61,46MB for 1482 more packages           9,74MB for 6281 more object types
--------------------------------------------------------------------------------
    9,7s (5,7% of total time) in 77 GCs | Peak RSS: 8,03GB | CPU load: 7,27
--------------------------------------------------------------------------------
Produced artifacts:
/home/rodrigo/Desktop/api/target/api (executable)
/home/rodrigo/Desktop/api/target/api.build_artifacts.txt (txt)
================================================================================
Finished generating 'api' in 2m 50s.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  03:03 min
[INFO] Finished at: 2023-01-17T12:13:04-03:00
[INFO] ------------------------------------------------------------------------COPIAR CÓDIGO
A imagem nativa é gerada no diretório target, juntamente com o arquivo .jar da aplicação, como um arquivo executável de nome api, conforme demonstrado na imagem a seguir:

alt text: Lista de arquivos e diretórios localizados dentro do diretório target do projeto, estando entre eles o arquivo da imagem nativa, cujo nome é **api**

Diferente do arquivo .jar, que é executado pela JVM via comando java -jar, a imagem nativa é um arquivo binário e deve ser executada diretamente pelo terminal:

target/apiCOPIAR CÓDIGO
Ao rodar o comando anterior será gerado o log de inicialização da aplicação, que ao final exibe o tempo que levou para a aplicação inicializar:

INFO 127815 --- [restartedMain] med.voll.api.ApiApplication : Started ApiApplication in 0.3 seconds (process running for 0.304)COPIAR CÓDIGO
Repare que a aplicação levou menos de meio segundo para inicializar, algo realmente impressionante, pois quando a executamos pela JVM, via arquivo .jar, esse tempo sobe para algo em torno de 5 segundos.

Para saber mais detalhes sobre a geração de uma imagem nativa com Spring Boot 3 acesse a documentação no site:

GraalVM Native Image Support


