
# ARCH DEMO
Exemplo para construir um sistema simples ou complexo baseado em arquitetura de micro serviços


# Projeto

- Language: Portugues Brazil (pt-br)  |  <a href="README-EN.md">Inglês (en)</a>
- Version: 1.0
- Date: 03/01/22


# Sumario

- Recursos
- Dependencias
- Extras
- Atualizações
- Overview
- Como funciona
- Mais detalhes
- Diagrama de Sequencia
- Processo de Autorização
- Ambiente Seguro
- Como usar
- TODO


# Recursos

- Java Spring Boot
- Maven 2.3.6
- JDK-1.8 (jdk1.8.0_212)
- Mysql-5.7 / Mysql-8.0


# Dependencias

- OAuth2
- Eureka
- Zuul
- Zipkin
- Prometheus
- Elasticsearch
- Kibana
- Logback
- Swagger
- Lombok
- JPA Spring Data
- RabbitMQ


# Extras

- Docker
- Docker Compose
- Nginx


# Atualizações


# Overview

> Sobre

Este projeto foi criado com a intenção de mostrar como funciona um ambiente orientado a microsserviços, além de mais 
organizado e fácil de entender e, por fim, mais fácil de manter. A arquitetura orientada a microsserviços é uma 
exigência atual do mercado que precisa desenvolver rapidamente soluções com agilidade, garantias, flexibilidade, 
segurança, organização, escalabilidade, entre outros.

Por isso é importante antes de tudo entender bem o conceito e depois aplicar seus benefícios na prática, garantindo 
a perfeita integração entre desenvolvimento e manutenção.

Durante as explicações abaixo serão usadas os seguintes termos:

- request-token
  - procedimento inicial para que um operador possa gerar um token OAuth2 e ter uma autorização para consumir serviços
- request-service
  - procedimento utilizado quando o operador quer consumir um serviço qualquer (DEMO)
- check-token
  - procedimento interno e automatico que o sreviço ROUTER executa para validar uma operação via OAuth2
- access-token
  - token aleatório gerado pelo AUTHORIZATOR e que deve ser utilizado para validação nas transações

> Detalhes

Foram desenvolvidos varios recursos graficos (diagramas e fluxogramas) para ilustrar de muitas maneiras como o ambiente 
arch-demo funciona. O diagrama de sequência é o mais especifico para verificar o funcionamento técnico e direto dos 
fluxos presentes na arquitetura, por exemplo o fluxo get-token para autorização e autenticação.

Ao todo serão 9 diagramas, sendo eles: 

- Complete Flow
- Details
- Compact
- Request Token
- Request Service
- Check Token
- Data Relation
- Sequence
- Sequence (Nginx)

Todos esses diagramas serão detalhados e explicados a seguir.


# Como funciona

Nesse diagrama esquematico é mostrado como são feitas as requisições de request-token, check-token e
request-service, sendo abordado todo o fluxo e tempo de vida da requisição.

A requisição é feita por um REQUESTER qualquer, pode ser o POSTMAN como exemplo, chegando até o ROUTER que é o gateway
do ambiente e o qual possui o conhecimento de todoss os micro serviços contidos no ambiente. Todos os serviços são
acessados por meio do ROUTER (api-gateway) que ao capturar um request por meio de uma rota qualquer acessa o recurso
alvo por meio de informações obtidas do serviço de DISCOVERY que nada mais é que um SERVICE-DISCOVERY (Eureka) onde
todos os serviços do ambiente estão registrados.

O micro serviço ROUTER verifica qual é o tipo de requisição e caso seja uma requisição request-token, apenas encaminha
a mesma para o serviço AUTHORIZATOR. Caso seja um requisição do tipo request-service, realiza automaticamente uma
chamada para si mesmo do tipo check-token para garantir a segurança da transação, ou seja, antes de executar um
request-service é preciso executar um request-token.

> Complete Flow

![img.png](midias/Complete-Flow.png)

Nos casos da requisições to tipo request-service e check-token, é obrigatório que o REQUESTER informe o código de 
acesso ao ROUTER referente ao CLIENT que deseja consumir, nesse caso um micro serviço chamado de DEMO, para que o 
ROUTER possa obter a autorização do CLIENT, sendo que a requisição do tipo check-token é automatica e interna ao ROUTER. 
A requisição request-token tem que estar alinhada com a requisição check-token, ou seja, os dados para autorização de 
uso do CLIENT tem que estar de acordo, conforme mostrado na imagem abaixo:

> Data Relation

![img.png](midias/Data-Relation.png)

Veja que o REQUESTER (Operator) esta usando os dados para autorização de um CLIENT chamado por arch_demo_client_1 
seguido por sua secret-key 11111111-2222-3333-4444-555555555555, os quais formam a STRING final para autorização 
no AUTHORIZATOR, sendo essa STRING usada como BASIC-AUTH conforme mostrado abaixo:

<pre>
>> Data-Client: arch_demo_client_1:11111111-2222-3333-4444-555555555555
<< Basic-Auth: YXJjaF9kZW1vX2NsaWVudF8xOjExMTExMTExLTIyMjItMzMzMy00NDQ0LTU1NTU1NTU1NTU1NQ==
</pre>

<pre>
Request-Header[Authorization]: Basic YXJjaF9kZW1vX2NsaWVudF8xOjExMTExMTExLTIyMjItMzMzMy00NDQ0LTU1NTU1NTU1NTU1NQ==
Request-Body[grant_type]: password
Request-Body[username]: OAUTH2DEMO_USER
Request-Body[password]: 1234567890 
</pre>

Isso faz com que seja gerado um token no AUTHORIZATOR para o CLIENT identificado pelo BASIC-AUTH acima, e nas próximas 
requisições será necessário enviar o token gerado para esse  CLIENT junto com a chave de acesso ao ROUTER, que 
nesse caso é XYZ-123 e faz referencia a autorização do CLIENT no ambiente, conforme a seguir:

<pre>
>> Access-Code: XYZ-123
<< Basic-Auth: YXJjaF9kZW1vX2NsaWVudF8xOjExMTExMTExLTIyMjItMzMzMy00NDQ0LTU1NTU1NTU1NTU1NQ==
</pre>

<pre>
Request-Header[Authorization]: Basic YXJjaF9kZW1vX2NsaWVudF8xOjExMTExMTExLTIyMjItMzMzMy00NDQ0LTU1NTU1NTU1NTU1NQ==
Request-Body[access-token]: bcb88919-562f-4564-9976-afc8672b613b
</pre>

Todo o processo de autorização e autenticação é feito através do ROUTER integrado ao AUTHORIZATOR, veja que no final 
desse processo temos a requisição to tipo request-service que chama um serviço DEMO passando os seguintes parametros:

<pre>
Request-Header[Access-Code]: XYZ-123
Request-Header[Authorization]: Bearer bcb88919-562f-4564-9976-afc8672b613b
Request-Body[JSON]: {"rulesCode": "XYZ12345","postalCode": "12090002","webhook": ""}
</pre>

Conforme mencionado acima, o Access-Code é usado para obter a autorização BASIC-AUTH do client para junto com o token 
do tipo Bearer validar a transação referente a requisição atual. 

Ainda nesse fluxo podemos notar que após a autorização e autenticaçao existe um serviço chamado RULES o qual faz a 
gerencia do ambiente levando em consideração as configurações e permissões de cada serviço no ambiente, ou seja, 
ele é quem diz se o serviço DEMO pode proseguir e se esta corretamente configurado. Toda a configuração desse serviço 
é feita no banco de dados e acessada através do JPA para então ser processada dentro do RULES de acordo com as regras 
devidamente aplicadas.


# Mais detalhes

A seguir temos um detalhamento mais profundo do ambiente, tendo uma visão mais ampla de como estão organizados os 
serviços e operações, quais são as entidades (models) de cada serviço e uma representação simbólica do banco de dados. 
Podemos ver que cada serviço esta separado por sua função espeficica e possui uma base de dados propria, assim como 
recursos externos ou adjacentes referentes a uma unica unidade de trabalho. 

> Details

![img.png](midias/Details.png)

Sem muitos detalhes o fluxograma abaixo mostra de maneira bem compacta e direta o fluxo completo de operações dentro 
do ambiente orientado a micro-serviços ARCH-DEMO.

> Compact

![img.png](midias/Compact.png)


# Diagrama de Sequencia

O diagrama de sequência abaixo mostra um fluxo completo de requisição feita pelo operador com a inteção em consumir o
serviço chamado DEMO. Podemos ver que todo o fluxo começa após a requisição request-token ser iniciada, a qual é 
encaminhada pelo ROUTER ao serviço AUTHORIZATOR para geração do token.

São duas requisições: request-token e request-service feitas pelo REQUESTER através de um operador qualquer, porém o 
operador tem que estar previamente cadastrado no serviço AUTHORIZATOR. O ROUTER faz todos redirecionamentos e chamadas 
internas, nesse caso ainda sem o uso de um webserver para REVERSE-PROXY, e assim que finalizadas todas as operações 
uma resposta é enviada ao REQUESTER para ser visualizada pelo operador.

> Sequence

![img.png](midias/Sequence.png)


# Processo de Autorização

A seguir é ilustrado um fluxo completa para uma requisição do tipo request-token, onde podemos observaro que ja foi 
dito anteriormente sobre os dados que devem ser enviados para autorizar, autenticar e gerar um token para um operador 
qualquer. Veja que temos os dados do client informados na HEADER da requisição com o formato BASIC-AUTH, assim como as 
credenciais do operador que nesse caso é um usuário chamado OAUTH2DEMO_USER, repare também que é informado o tipo
grant_type que o serviço AUTHORIZATOR aceita, nesse caso password.

O serviço ROUTER recebe a requisição e encaminha para o serviço AUTHORIZATOR que ao concluir as operações de validação 
gerar o token e response ao ROUTER para que seja retornado ao REQUESTER. A partir desse ponto o REQUESTER deve guardar 
o token (access-token) e informar ele nas futuras requisições.

** Nesse caso não existe a header de controle de acesso identificada como: Access-Token

> Request Token Flow

![img.png](midias/Request-Token.png)

O fluxo a seguir chamado request-service depende inevitavelmente do fluxo anterior, é preciso gerar o access-token 
para informar ele nessa etapa. Veja que no fluxo existe um bloco ilustrando uma requisição desse tipo simulando uma 
operação real, onde podemo notar a existência do token na HEADER da requisição. Ainda nesse ponto temos outra HEADER 
chamada Access-Code a qual é utilizada para obter a STRING BASIC-AUTH de autorização do CLIENT, esse processo é feito 
internamente pelo ROUTER, pois é preciso saber quem foi autorizado a executar uma operação.

Continuando nesse processo de request-service temos a requisição interna e automatica chamada check-token ativada 
pelo ROUTER sempre que ele recebe uma requisição do tipo request-service. Essa etapa da operação ocorre no back-end do 
ambiente, e quando tudo ocorre bem, o serviço ROUTER encaminha a requisição request-service para seu destino original.

Logo após a "prova de fogo" de autorização a requisição chega até o serviço DEMO o qual faz uma chamada ao RULES, que 
por sua vez valida se o codigo Rules-Code e o Service-Id estão corretos, e é claro demais regras para consumir esse 
serviço, as quais são especificas e inerentes ao ambiente onde os serviços estão rodando.

> Request Service Flow

![img.png](midias/Request-Service.png)

A etapa de verificação de token previamente gerado anteriormente esta contida dentro da requisição request-service e ja 
foi abordada anteriormente nesse conteudo. Sendo assim não necessita de muitas explanações sobre os escopos ja citados, 
entretanto na imagem abaixo podemos notar uma novidade no bloco do serviço AUTHORIZATOR, vemos que o token gerado nas 
etapas anteriores esta sendo mantido na memoria da maquina, e é assim que o OAuth2 faz a verificação do token enviado 
na requisição check-token. É claro, esse é um procedimento interno ao OAuth2 e não nos cabe explanar sobre as operações 
feitas nesse escopo do processo como um todo.

> Check Token Flow

![img.png](midias/Check-Token.png)


# Ambiente Seguro

No ambiente abaixo temos um ambiente mais restrito a acessos externos, anteriormente apenas tinhamos um estrutura aberta 
de microserviços rodando independentemente, porém com uma exposição de seus elementos a agentes externos. Com a proposta 
de infra estrutura mostrada na figura a seguir, temos um servidor web para orquestrar as requisições externas de tal 
modo que todo a plataforma de serviços desde o ROUTER até um serviço qualquer como o DEMO estão protegidos de acessos 
indevidos.

A requisição é feita pelo operador a qual chega até seu objetivo final passando pelas mesmas etapas abordadas nesse 
documento, porém agora temos o Nginx que trabalha de forma reversa com as requisições encaminhando e traduzindo as 
requisições para dentro do ambiente seguro.

Veja que existe uma rede interna limitada pela subnet 10.0.0.0/16 onde os seus integrantes podem comunicar-se entre si 
mas não podem ser acessados de fora dessa rede senão for pelo Nginx. Note ainda que é possível personalizar a url de entrada 
no Nginx para com o ROUTER, por exemplo:

- Nginx
<pre>
Request Token
http://localhost:33001/nginx/huntercodexs/arch-demo/service-authorizator/api/rest/oauth/v1/oauth/token
</pre>

- ROUTER
<pre>
Reverse Proxy
http://10.0.0.2:33400/huntercodexs/arch-demo/service-authorizator/api/rest/oauth/v1/oauth/token
</pre>

Isso permite ocultar tanto o endpoint real dos microserviços como também a porta onde ele esta sendo excutado.

> Using Nginx Webserver

![img.png](midias/Secure-Environment.png)


# Como usar

Para usar esse projeto, seja como iniciador de um ambiente, seja para testes, seja para apresentações de trabalhos 
academicos ou escolares ou para qualquer outro proposito no ramo de softwares, siga as instruções abaixo:

- Clonar o projeto em uma maquina local:

<pre>
git clone https://github.com/huntercodexs/arch-demo.git
</pre>

Veja que foi criada uma pasta chamada arch-demo, na qual esta contido os projetos de exemplo, midias e documentos, assim 
como o arquivo POSTMAN para testes.

Dentro da pasta arch-demo/microservices temos os serviços de exemplo: SERVICE-DISCOVERY, SERVICE-ROUTER, SERVICE-RULES, 
SERVICE-AUTHORIZATOR e SERVICE-DEMO. Todos eles são apenas para exemplificar o ambiente podendo ser executados 
imediatamente após o carregamento do projeto em uma IDEA (exemplo: IntelliJ).

** Ainda teremos que configurar o projeto para rodar no ambinte seguro com o uso de containers docker

Abaixo podemos ver os micro-serviços registrados no SERVICE-DISCOVERY (Eureka) os quais estão disponiveis para serem 
acessador via ROUTER.

![img.png](midias/service-discovery-sample.png)

Outro ponto importante é a base de dados dedicadas para cada serviço, não tendo vinculos entre as mesmas, o que poderia 
causar confusão ja que estamos falando de microserviços. Na figura abaixo temos uma imagem desse cenário:

![img.png](midias/arch-demo-databases.png)

> SERVICE-DISCOVERY 

Esse serviço é o responsavel pelo gerenciamento dos outros serviços com relação ao estado, endereço (IP), porta e demais 
recursos, dados esses que serão informados ao ROUTER quando necessário.

> SERVICE-ROUTER

Serviço responsavel por rotear todas as requsições feitas para o ambiente ARCH-DEMO, esse serviço verifica com o serviço 
DISCOVERY onde esta o serviço alvo para encaminhar a requisição, isso antes de pasar pelo processo de autorização.

> SERVICE-AUTHORIZATOR

Serviço responsavel pelo gerenciamente de tokens OAuth2.

> SERVICE-RULES

Nesse serviço estão as regras de negócio do ambiente, todo micro serviço deve estar intergrado com esse serviço para 
que as regras de negocio sejam validadas corretamente.

> SERVICE-DEMO

Service de exemplo para consultar endereços via Correios com um CEP qualquer


# TODO

<table>
<tr>
<th>#</th>
<th>Descrição</th><th>Situação</th>
</tr>
<tr>
<td>1</td>
<td>
Criar ambiente completo e-commerce com segurança anti-fraude e bancos de dados diversos, usar PHP para criar o site, 
Python para processar arquivos, Java (Spring Boot) para APIs, Javascript (Jshunter) para fazer o front-end. Incluir
também modulos em NodeJS e ReactJS, banco de dados diversos.
</td>
<td>TODO</td>
</tr>
<tr>
<td>2</td>
<td>
Inserir segurança no processo de registro do SERVICE-DISCOVERY (Eureka).
</td>
<td>DONE</td>
</tr>
<tr>
<td>3</td>
<td>
Inserir um ExceptionHanlder para tratamentos de erros nos micro-serviços do ambiente.
</td>
<td>WORK</td>
</tr>
<tr>
<td>4</td>
<td>
Verificar se é possivel obter os dados de login no SERVICE-DISCOVERY do banco de dados.
</td>
<td>CANCELADO</td>
</tr>
</table>

