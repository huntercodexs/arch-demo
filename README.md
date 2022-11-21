
# ARCH DEMO
Exemplo para construir um sistema simples ou complexo baseado em arquitetura de micro serviços


# Projeto

- Language: Português Brazil (pt-br)  |  <a href="README-EN.md">Inglês (en)</a>
- Version: 1.0
- Date: 03/01/22


# Sumario

- Recursos
- Dependências
- Extras
- Overview
- Como funciona
- Mais detalhes
- Diagrama de Sequencia
- Processo de Autorização
- Ambiente Seguro
- Como usar
- OpenAPI (Swagger)


# Requisitos de Sistema

- Java Spring Boot
- Maven 2.3.6
- JDK-1.8 (jdk1.8.0_212)
- Mysql-5.7 / Mysql-8.0


# Dependências

- Eureka
- Zuul
- OAuth2
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


# Overview

> Sobre

O objetivo desse projeto é mostrar como funciona um ambiente com uma arquitetura orientada a microsserviços, onde é 
possível notar que essa filosofia de trabalho torna o ambiente mais organizado, fácil de entender e por fim, fácil de 
manter. 

A arquitetura orientada a microsserviços é uma exigência de mercado que precisa desenvolver rapidamente soluções com 
garantias, segurança, organização, escalabilidade, flexibilidade entre outros.

Por isso é importante antes de tudo entender bem o conceito para depois aplica-los, na prática, garantindo uma satisfatória 
integração entre desenvolvimento e manutenção.

Durante as explicações abaixo serão usadas os seguintes termos:

- check-service-status
  - esse procedimento é interno ao SERVICE-ROUTER e tem a função de verificar se um serviço está OK
- request-token
  - procedimento inicial para que um operador possa gerar um token OAuth2 e ter uma autorização para consumir serviços
- request-service
  - procedimento utilizado quando o operador quer consumir um serviço qualquer (exemplo: SERVICE-DEMO)
- check-token
  - procedimento interno e automático que o serviço SERVICE-ROUTER executa para validar uma operação via OAuth2
- access-token
  - token aleatório gerado pelo SERVICE-AUTHORIZATOR que deve ser utilizado para validação nas transações

> Detalhes

Foram desenvolvidos alguns modelos gráficos, diagramas e fluxogramas, para ilustrar de muitas maneiras como o ambiente 
arch-demo funciona. O diagrama de sequência é o mais específico para verificar o funcionamento técnico e direto dos 
fluxos presentes nessa arquitetura, um exemplo seria o fluxo get-token para autorização e autenticação.

Os modelos gráficos são os seguintes: 

- Complete Flow
- Details
- Compact
- Request Token
- Request Service
- Check Token
- Data Relation
- Sequence
- Sequence (Nginx)
- Sequence Service Status (Router Details)

Todos esses diagramas serão detalhados e explicados logo mais abaixo.


# Como funciona

O diagrama esquemático "Complete Flow" mostra como são feitas as requisições de request-token, check-token e
request-service, sendo abordado todo o fluxo e tempo de vida da requisição, repare que existe um fluxo interno ao 
SERVICE-ROUTER que verifica se um serviço está OK (check-service-status).

A requisição é feita por um REQUESTER qualquer, exemplo usando o POSTMAN, chegando até o SERVICE-ROUTER que é o gateway
do ambiente e o qual possui o conhecimento de todos os micro serviços contidos no ambiente. Todos os serviços são
acessados por meio desse SERVICE-ROUTER (API-GATEWAY) que ao capturar um request por uma rota qualquer, acessa o recurso
alvo através de informações obtidas do serviço de SERVICE-DISCOVERY (EUREKA) onde todos os serviços do ambiente 
estão devidamente autenticados e registrados.

O micro serviço SERVICE-ROUTER verifica a requisição, caso seja uma requisição request-token, apenas encaminha
a mesma para o serviço SERVICE-AUTHORIZATOR. Caso seja uma requisição de request-service, realiza automaticamente uma
chamada para si mesmo do tipo check-token para garantir a segurança da transação, ou seja, antes de executar um
request-service é preciso executar um request-token. 

>> Um ponto importante nesse fluxo é o procedimento automático que o SERVICE-ROUTER faz definido como 
> check-service-status para validar se o serviço está disponível, esse procedimento pode ser ignorado com uma simples 
> configuração no arquivo application.properties do SERVICE-ROUTER.

> Complete Flow

![img.png](midias/Complete-Flow.png)

Nos casos das requisições to tipo request-service e check-token, é obrigatório que o REQUESTER informe o código de 
acesso ao SERVICE-ROUTER referente ao CLIENT/MICROSERVICE que deseja consumir, ex SERVICE-DEMO (Address) identificado pelo 
código de acesso "XYZ-123", para que o SERVICE-ROUTER possa obter a autorização básica do CLIENT/MICROSERVICE, sendo que 
a requisição do tipo check-token é automaticamente feita pelo SERVICE-ROUTER. 

A requisição request-token tem que estar alinhada com a requisição check-token, ou seja, os dados para autorização de 
uso do CLIENT/MICROSERVICE precisam estar corretos, conforme mostrado na imagem abaixo:

> Data Relation

![img.png](midias/Data-Relation.png)

Veja que o REQUESTER (Operator) esta usando os dados para autorização de um CLIENT/MICROSERVICE nomeado arch_demo_client_1 
seguido por sua secret-key 11111111-2222-3333-4444-555555555555, os quais formam a STRING final para autorização 
no SERVICE-AUTHORIZATOR, sendo essa STRING usada como BASIC-AUTH conforme mostrado abaixo:

<pre>
Data-Client[username]: arch_demo_client_1
Data-Client[password]: 11111111-2222-3333-4444-555555555555
Basic-Auth[base64]: YXJjaF9kZW1vX2NsaWVudF8xOjExMTExMTExLTIyMjItMzMzMy00NDQ0LTU1NTU1NTU1NTU1NQ==
</pre>

<pre>
Request-Header[Authorization]: Basic YXJjaF9kZW1vX2NsaWVudF8xOjExMTExMTExLTIyMjItMzMzMy00NDQ0LTU1NTU1NTU1NTU1NQ==
Request-Body[grant_type]: password
Request-Body[username]: OAUTH2DEMO_USER
Request-Body[password]: 1234567890 
</pre>

Isso faz com que seja gerado um token no SERVICE-AUTHORIZATOR para o REQUESTER consumir o CLIENT/MICROSERVICE identificado 
na Request-Header[Authorization] acima.

Nas próximas requisições será necessário enviar o token gerado para esse CLIENT/MICROSERVICE com o código de acesso do 
SERVICE-ROUTER, que nesse caso é XYZ-123 referente do CLIENT/MICROSERVICE no ambiente, conforme a seguir:

<pre>
Access-Code: XYZ-123
</pre>

<pre>
Basic-Auth[base64]: YXJjaF9kZW1vX2NsaWVudF8xOjExMTExMTExLTIyMjItMzMzMy00NDQ0LTU1NTU1NTU1NTU1NQ==
</pre>

<pre>
Request-Header[Authorization]: Basic YXJjaF9kZW1vX2NsaWVudF8xOjExMTExMTExLTIyMjItMzMzMy00NDQ0LTU1NTU1NTU1NTU1NQ==
Request-Body[access-token]: bcb88919-562f-4564-9976-afc8672b613b
</pre>

Todo o processo de autorização e autenticação é feito através do SERVICE-ROUTER integrado ao SERVICE-AUTHORIZATOR, veja 
que no final desse processo temos a requisição to tipo request-service que chama um serviço SERVICE-DEMO passando os 
seguintes parametros:

<pre>
Request-Header[Access-Code]: XYZ-123
Request-Header[Authorization]: Bearer bcb88919-562f-4564-9976-afc8672b613b
Request-Body[JSON]: {"rulesCode": "XYZ12345","postalCode": "12090002","webhook": ""}
</pre>

Conforme mencionado acima, o Access-Code é usado para obter a autorização BASIC-AUTH do CLIENT/MICROSERVICE que junto ao 
access-token contido em "Request-Header[Authorization]: Bearer bcb88919-562f-4564-9976-afc8672b613b" torna possível 
validar a transação referente a requisição atual. 

Ainda nesse fluxo, logo após o processo de autorização e autenticação, existe um serviço chamado SERVICE-RULES que 
está encarregado de validar regras de uso do ambiente e dos serviços disponíveis no mesmo, diminuindo o risco de uso 
inapropriado. Esse serviço faz a gerência do ambiente considerando as configurações e permissões de cada 
serviço no ambiente, ou seja, ele é quem diz se uma aplicação, exemplo SERVICE-DEMO, pode prosseguir e ser executada na 
plataforma, e se está corretamente configurada. Toda a configuração desse serviço é feita no banco de dados e acessada 
através do JPA para então ser processada dentro do SERVICE-RULES conforme as regras devidamente aplicadas.


# Mais detalhes

A seguir um detalhamento mais profundo do ambiente, tendo uma visão mais ampla de como estão organizados os 
serviços e operações, quais são as entidades (models) de cada serviço e uma representação simbólica do banco de dados. 
É possível ver que cada serviço está separado por sua função específica e possui uma base de dados propria, assim como 
recursos externos ou adjacentes referentes a uma única unidade de trabalho. 

> Details

![img.png](midias/Details.png)

Sem muitos detalhes o fluxograma abaixo mostra de maneira bem compacta e direta o fluxo completo de operações feitas no 
ambiente orientado a micro-serviços ARCH-DEMO, ARCH-PRO ou ainda CODEXS-ARCH.

> Compact

![img.png](midias/Compact.png)

> Router Details

O diagrama de sequência abaixo ilustra como o serviço roteador de requisições REST atua de modo a verificar se os serviços 
estão realmente disponíveis antes mesmo de encaminhar a requisição para seu destino. A etapa Check Service Status é 
automaticamente executada no SERVICE-ROUTER quando uma requisição chega até ele, sendo de facil entendimento ou 
compreensão seu funcionamento mediante, análise do código-fonte.

![img.png](midias/Sequence-Diragram-Router-Details.png)

Essa verificação "service status" precisa ser adicionada em todos os micro serviços que fazem parte do ambiente de 
trabalho da plataforma, nesse caso ARCH-DEMO ou ARCH-PRO ou ainda CODEXS-ARCH. Esse procedimento é feito incluindo 
em cada micro serviço um controller para receber e responder a requisições REST devidamente autorizadas por meio da 
autenticação básica "BASIC AUTH".

Para configurar um novo serviço no ambiente siga as instruções abaixo:

- Crie um novo "package", exemplo "archdemo", dentro de um "package" já existente, exemplo "config", conforme ilustrado abaixo:

![img.png](midias/Service-Status.png)

- Crie os arquivos AliveController.java e AliveService.java dentro do "package" criado anteriormente, veja os exemplos 
abaixo para criar esses arquivos:

> NOTA: Não altere a lógica dos arquivos, apenas configure as rotas e os dados a serem utilizados

AliveController.java
<pre>
 1 package com.huntercodexs.archdemo.demo.config.archdemo;
 2 
 3 import lombok.extern.slf4j.Slf4j;
 4 import org.springframework.beans.factory.annotation.Autowired;
 5 import org.springframework.web.bind.annotation.*;
 6 
 7 import javax.servlet.http.HttpServletRequest;
 8 
 9 @Slf4j
10 @RestController
11 @CrossOrigin(origins = "*")
12 @RequestMapping("${api.prefix}")
13 public class AliveController {
14
15     @Autowired
16     AliveService aliveService;
17 
18     @GetMapping(path = "/address/arch-demo-status")
19     @ResponseBody
20     public String alive(HttpServletRequest request) {
21         return aliveService.alive(request);
22     }
23 
24 }
</pre>

Na linha 18 do código acima use a seguinte configuração para um correto funcionamento: 
/${service-endpoint}/arch-demo-status, pois esse endpoint será chamado pelo SERVICE-ROUTER durante uma requisição REST, 
por exemplo, a requisição abaixo

<pre>
http://localhost:33001/huntercodexs/arch-demo/service-demo/api/address
</pre>

espera que exista um endpoint /address/arch-demo-status no serviço alvo SERVICE-DEMO (Address) conforme a seguir

<pre>
http://localhost:33001/huntercodexs/arch-demo/service-demo/api/address/arch-demo-status
</pre>

AliveService.java
<pre>
 1 package com.huntercodexs.archdemo.demo.config.archdemo;
 2 
 3 import lombok.extern.slf4j.Slf4j;
 4 import org.springframework.beans.factory.annotation.Value;
 5 import org.springframework.stereotype.Service;
 6 
 7 import javax.servlet.http.HttpServletRequest;
 8 import java.util.Base64;
 9 
10 @Slf4j
11 @Service
12 public class AliveService {
13 
14     @Value("${eureka.security.login}")
15     String basicAuthService;
16 
17     public String alive(HttpServletRequest request) {
18         String basicAuthRequest = new String(Base64.getDecoder().decode(
19                 request.getHeader("Authorization").replaceFirst("Basic ", "")));
20         if (basicAuthRequest.equals(basicAuthService)) {
21             return "alive";
22         }
23         return null;
24     }
25 
26 }
</pre>

No código acima é preciso ficar atento apenas na linha 14, onde é configurado o valor para realizar autenticação do tipo 
"BASIC AUTH", por exemplo:

<pre>
eureka.security.login=arch-demo:1234567890-1111-2222-3411111-000001
</pre>

> NOTA: Esse valor é o mesmo utilizado para registrar a aplicação ou micro serviço no SERVICE-DISCOVERY (Eureka), por 
> isso não altere ele de modo algum, ele deve ser configurado no arquivo application.properties

Dessa forma sempre que o SERVICE-ROUTER receber uma requisição para rotear, ele primeiro ira verificar se o serviço 
esta disponível através de uma chamada REST, enviando na HEADER da requisição o valor de "eureka.security.login" para 
realizar a autenticação básica: Basic YXJjaC1kZW1vOjEyMzQ1Njc4OTAtMTExMS0yMjIyLTM0MTExMTEtMDAwMDAx.

# Diagrama de Sequencia

O diagrama de sequência abaixo mostra um fluxo completo de requisição feita pelo REQUESTER com a intenção em consumir o
serviço chamado DEMO (SERVICE-DEMO). Nota se que todo o fluxo começa após a requisição request-token ser iniciada, 
a qual é encaminhada pelo ROUTER (SERVICE-ROUTER) ao serviço AUTHORIZATOR (SERVICE-AUTHORIZATOR) para geração do token.

São duas requisições: request-token e request-service feitas pelo REQUESTER, porém o REQUESTER (operador) tem que estar 
previamente cadastrado no serviço AUTHORIZATOR (SERVICE-AUTHORIZATOR). O ROUTER (SERVICE-ROUTER) gerencia todas as 
chamadas, nesse caso ainda sem o uso de um WEBSERVER com REVERSE-PROXY, e assim que finalizadas todas as operações 
uma resposta é enviada ao REQUESTER para ser visualizada.

> Sequence

![img.png](midias/Sequence.png)


# Processo de Autorização

A seguir é ilustrado um fluxo completa para uma requisição do tipo request-token, onde é possível observar o que ja foi 
dito anteriormente sobre os dados que devem ser enviados para autorizar, autenticar e gerar um token para um REQUESTER 
qualquer. Veja que temos os dados do CLIENT/MICROSERVICE informados na HEADER da requisição com o formato BASIC-AUTH, 
assim como as credenciais do operador que nesse caso é um usuário chamado OAUTH2DEMO_USER, repare também que é informado 
o tipo GRANT-TYPE que o serviço AUTHORIZATOR (SERVICE-AUTHORIZATOR) espera, nesse caso "password".

O serviço ROUTER (SERVICE-ROUTER) recebe a requisição e encaminha para o serviço AUTHORIZATOR (SERVICE-AUTHORIZATOR) que 
ao concluir as operações de autenticação, gera um access-token e response ao ROUTER (SERVICE-ROUTER) para ser retornado 
ao REQUESTER. A partir desse ponto o REQUESTER deve salvar o access-token e informa-lo em nas futuras requisições.

***Nesse caso não existe a header de controle de acesso identificada como: Access-Token***

> Request Token Flow

![img.png](midias/Request-Token.png)

O fluxo a seguir chamado request-service depende inevitavelmente do fluxo anterior request-token, pois é preciso gerar o 
access-token e usa-lo nessa etapa. Veja que no fluxo existe um bloco ilustrando uma requisição desse tipo simulando uma 
operação real, onde podemo notar a existência do token na HEADER da requisição. Ainda nesse ponto temos outra HEADER 
chamada Access-Code a qual é utilizada para obter a STRING BASIC-AUTH de autorização do CLIENT/MICROSERVICE, esse processo 
é feito internamente pelo ROUTER (SERVICE-ROUTER), pois é preciso saber quem foi autorizado a executar uma operação.

Continuando nesse processo de request-service temos a requisição interna e automática chamada check-token ativada 
pelo ROUTER (SERVICE-ROUTER) sempre que ele recebe uma requisição do tipo request-service. Essa etapa da operação ocorre 
no backend do ambiente, e quando tudo ocorre bem, o serviço ROUTER (SERVICE-ROUTER) encaminha a requisição request-service 
para seu destino original.

Logo após a "prova de fogo" ou o "desafio" de autorização e autenticação, a requisição chega até o serviço DEMO 
(SERVICE-DEMO) o qual faz uma chamada ao RULES (SERVICE-RULES), que valida se o código Rules-Code e o Service-Id estão 
corretos, e é claro demais regras para consumir esse serviço, as quais são especificas e inerentes ao ambiente onde os 
serviços estão rodando.

> NOTA: O SERVICE-RULES é um micro serviço com a responsabilidade garantir que os micro serviços estejam corretamente 
> configurados e permitidos dentro de um ambiente restrito de processamento.

> Request Service Flow

![img.png](midias/Request-Service.png)

A etapa de verificação de token previamente gerado anteriormente esta contida na requisição request-service e ja 
foi abordada anteriormente nesse conteudo. Sendo assim não necessita de muitas explanações sobre os escopos ja citados, 
entretanto na imagem abaixo podemos notar uma novidade no bloco do serviço AUTHORIZATOR (SERVICE-AUTHORIZATOR), vemos que 
o token gerado nas etapas anteriores está sendo mantido em memória na máquina que esta executando o SERVICE-AUTHORIZATOR, 
e é assim que o OAuth2 faz a verificação do token enviado na requisição check-token. É claro, esse é um procedimento 
interno ao OAuth2 e não cabe aqui explanar sobre as operações feitas nesse escopo do processo.

> Check Token Flow

![img.png](midias/Check-Token.png)


# Ambiente Seguro

No ambiente abaixo é exibido um ambiente mais restrito a acessos externos, anteriormente tínhamos uma estrutura aberta 
de micro serviços rodando independentemente, porém com uma exposição de seus integrantes a agentes externos, tornando 
inseguro a plataforma.

Com a proposta de infra-estrutura mostrada na figura a seguir, foi inserido um WEBSERVER para orquestrar as requisições 
externas de tal modo que toda a plataforma de serviços desde o ROUTER (SERVICE-ROUTER) até um serviço qualquer, por exemplo, 
o DEMO (SERVICE-DEMO) estão protegidos de acessos indevidos, pois o IP e a porta onde estão rodando são privados.

A requisição é feita pelo REQUESTER (operador) a qual chega até seu objetivo final passando pelas mesmas etapas abordadas 
nesse documento, porém com a implementação do NGINX que trabalha de forma reversa com as requisições encaminhando e 
traduzindo as requisições para dentro do ambiente seguro.

Note que existe uma rede interna limitada pela subnet 10.0.0.0/16 onde os seus integrantes podem se comunicar, mas não 
podem ser acessados de fora dessa rede senão for pelo NGINX. Note ainda que é possível personalizar a url de entrada 
no NGINX para com o ROUTER (SERVICE-ROUTER), por exemplo:

- NGINX
<pre>
Request Token
http://localhost:33001/nginx/huntercodexs/arch-demo/service-authorizator/api/rest/oauth/v1/oauth/token
</pre>

- ROUTER
<pre>
Reverse Proxy
http://10.0.0.2:33400/huntercodexs/arch-demo/service-authorizator/api/rest/oauth/v1/oauth/token
</pre>

Isso permite ocultar tanto o endpoint real dos micro serviços como também a porta onde ele esta sendo executando.

> NOTA: Para oferecer ainda mais segurança, pode-se utilizar um domínio próprio com um protocolo seguro de comunicação, 
> por exemplo: http://api.domain.com/nginx/huntercodexs/arch-demo/service-authorizator/api/rest/oauth/v1/oauth/token

> Using Nginx Webserver

![img.png](midias/Secure-Environment.png)


# Como usar

Para usar esse projeto como base para outros projetos e desenvolvimento, mesmo que apenas para testes ou estudos, seja 
como iniciador de um ambiente, seja para testes, seja para apresentações de trabalhos acadêmicos ou escolares ou para 
qualquer outro proposito no ramo de desenvolvimento de ‘software’, siga as instruções abaixo:

- Clonar o projeto em uma máquina local:

<pre>
git clone https://github.com/huntercodexs/arch-demo.git
</pre>

Veja que foi criada uma pasta chamada arch-demo, contendo os projetos desse ambiente, mídias e documentos, assim 
como o arquivo POSTMAN para testes.

Na pasta arch-demo/microservices estão os serviços de exemplo: SERVICE-DISCOVERY, SERVICE-ROUTER, SERVICE-RULES, 
SERVICE-AUTHORIZATOR e SERVICE-DEMO. Todos eles são apenas para exemplificar o ambiente podendo ser executados 
imediatamente após o carregamento do projeto em uma IDEA (exemplo: IntelliJ).

***Ainda será necessário configurar o projeto para rodar no ambiente seguro com o uso de containers docker***

Na imagem abaixo está ilustrado os micro-serviços registrados no SERVICE-DISCOVERY (EUREKA) os quais estão disponíveis 
para serem acessador através do SERVICE-ROUTER.

![img.png](midias/service-discovery-sample.png)

Outro ponto importante é a base de dados dedicadas para cada serviço, não possuindo ligações entre as mesmas, o que poderia 
causar confusão ja que estamos falando de micro serviços.

Na figura abaixo temos uma imagem desse cenário:

![img.png](midias/arch-demo-databases.png)

> SERVICE-DISCOVERY 

Esse serviço é o responsável pelas informações outros serviços com relação ao estado, endereço (IP), porta e demais 
recursos, dados esses que serão informados ao SERVICE-ROUTER quando necessário.

> SERVICE-ROUTER

Serviço responsável por rotear todas as requisições feitas para o ambiente ARCH-DEMO, esse serviço verifica com o serviço 
SERVICE-DISCOVERY onde esta o serviço alvo para encaminhar a requisição, seja para requisitar um token, seja para validar 
um token, seja para verificar se um serviço está disponível ou para consumir algum recurso na plataforma.

> SERVICE-AUTHORIZATOR

Serviço responsável pela geração e validação de tokens OAuth2.

> SERVICE-RULES

Nesse serviço estão as regras de negócio do ambiente, todo micro serviço deve estar alinhado com as regras impostas por 
esse integrante.

> SERVICE-DEMO

Serviço de exemplo para consultar endereços via Correios com um CEP qualquer, também pode ser chamado DEMO, Address ou Postal Code.


# OpenAPI (Swagger)

A documentação de uma API é um ponto muito importante em um projeto, oferecendo qualidade, manutenção, entendimento e 
suporte aos desenvolvedores tanto da propria API quanto daqueles desenvolvedores que vão consumir a API. Por isso o uso 
desse conceito será aplicado aqui nesse projeto fazendo o uso do Swagger que é um framework para documentar APIs, 
também conhecido como OpenAPI.

Para documentar uma API que esta dentro de um micro serviço no ambiente ARCH-DEMO siga as instruções abaixo:

- Adicione a dependência no serviço que precisa ser documentado (exemplo: SERVICE-DEMO):

<pre>
      &lt;dependency&gt;
          &lt;groupId>org.springdoc&lt;/groupId&gt;
          &lt;artifactId>springdoc-openapi-ui&lt;/artifactId&gt;
          &lt;version>1.6.4&lt;/version&gt;
      &lt;/dependency&gt;
</pre>

- Faça a documentação da API, para exemplificar veja os exemplos abaixo:
  
> Controller ou RestController

<pre>
package com.huntercodexs.archdemo.demo.controller;

import com.huntercodexs.archdemo.demo.config.codexsresponser.dto.CodexsResponserDto;
import com.huntercodexs.archdemo.demo.dto.AddressRequestDto;
import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import com.huntercodexs.archdemo.demo.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${api.prefix}")
@Tag(name = "Address Service")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Operation(
            summary = "Find Address",
            description = "Microservice to get an address from anyone postal code"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found successfull", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CodexsResponserDto.class))
            }),
            @ApiResponse(responseCode = "401", description = "Access denied", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CodexsResponserDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Address not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CodexsResponserDto.class))
            }),
            @ApiResponse(responseCode = "406", description = "Not accceptable", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CodexsResponserDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CodexsResponserDto.class))
            })
    })
    @PostMapping(path = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity&lt;AddressResponseDto&gt; getAddress(
            @Valid @RequestBody(required = true) AddressRequestDto addressRequestDto
    ) {
        return addressService.getAddress(addressRequestDto);
    }

}
</pre>

> Error Response DTO

<pre>
package com.huntercodexs.archdemo.demo.config.codexsresponser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "This object refers to Codexs Responser Handler", name = "Codexs Responser")
public class CodexsResponserDto {

    @Schema(
            description = "Runtime code error or excetion defined in the application.",
            example = "9000",
            required = true)
    public int errorCode;

    @Schema(
            description = "Message refer to errorCode defined in the application.",
            example = "Address not found",
            required = true)
    public String message;
}
</pre>

> Request DTO

<pre>
package com.huntercodexs.archdemo.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "This object refers to Address Request", name = "Address Request")
public class AddressRequestDto {

    @Schema(
            description = "Rules code to access rules server according with the rules defined.",
            example = "XYZ-123",
            required = true)
    @NotNull @NotEmpty @NotBlank
    String rulesCode;

    @Schema(
            description = "Postal code number (only numbers).",
            example = "12090002",
            required = true)
    @NotNull @NotEmpty @NotBlank
    String postalCode;

    @Schema(
            description = "Webhook callback.",
            example = "http://api.sample.com/receptor",
            required = false)
    String webhook;
}
</pre>

> Response DTO

<pre>
package com.huntercodexs.archdemo.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "This object refers to Address Response", name = "Address Response")
public class AddressResponseDto {

    @Schema(
            description = "Postal number refer to query.",
            example = "12090002",
            required = true)
    public String cep;

    @Schema(
            description = "Name of the place found",
            example = "R Alameda Santos",
            required = true)
    public String logradouro;

    @Schema(
            description = "Addtional information about address found.",
            example = "Ao lado do shopping BigHouse",
            required = true)
    public String complemento;

    @Schema(
            description = "District name.",
            example = "Campo Belo",
            required = true)
    public String bairro;

    @Schema(
            description = "City or downtown.",
            example = "Sao Paulo",
            required = true)
    public String localidade;

    @Schema(
            description = "State Acronym.",
            example = "PR",
            required = true)
    public String uf;

    @Schema(
            description = "Others information about address (ibge).",
            example = "12345",
            required = false)
    public String ibge;

    @Schema(
            description = "Username of the user to be create (email or cpf).",
            example = "12345",
            required = false)
    public String gia;

    @Schema(
            description = "Phone Digit Code to address found (ddd).",
            example = "88888888888",
            required = false)
    public String ddd;

    @Schema(
            description = "Others information about address (siafi).",
            example = "12345",
            required = false)
    public String siafi;
}
</pre>

> NOTA: Caso queira ocultar algum controller na documentação use @Hidden conforme exemplo abaixo:

<pre>
package com.huntercodexs.archdemo.demo.config.archdemo;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${api.prefix}")
@Hidden
public class AliveController {

    @Autowired
    AliveService aliveService;

    @GetMapping(path = "/address/arch-demo-status")
    @ResponseBody
    public String alive(HttpServletRequest request) {
        return aliveService.alive(request);
    }

}
</pre>

> Configure o arquivo application.properties do SERVICE-DEMO conforme sugestão a seguir:

<pre>
# SWAGGER
#-------------------------------------------------------------------------------------------------------------------
# See more: https://springdoc.org/properties.html
#true, false
springdoc.swagger-ui.enabled=true
#Application path
springdoc.swagger-ui.path=/huntercodexs/arch-demo/service-demo/swagger-ui
#Ordered
springdoc.swagger-ui.operationsSorter=method
#StandaloneLayout, BaseLayout
springdoc.swagger-ui.layout=StandaloneLayout
#/api-docs, api-docs-guard, /api-docs-custom
springdoc.api-docs.path=/api-docs/service-demo
#true, false
springdoc.model-and-view-allowed=true
</pre>

***Um ponto importante é ficar atento a configuração do springdoc.api-docs.path=/api-docs/service-demo, pois ele deverá 
estar alinhado com a configuração de rotas no SERVICE-ROUTER conforme mostrado mais abaixo***

Após realizar todos os ajustes mencionados acima, e também a documentação da API, verifique se é possível acessar o 
swagger pela uri definida em springdoc.swagger-ui.path, conforme exemplo abaixo:

<pre>
http://localhost:41233/huntercodexs/arch-demo/service-demo/swagger-ui/index.html
</pre>

Para isso é preciso informar corretamente qual a porta que o micro serviço está sendo executado, nesse caso 41233. Ao 
acessar a página será possível visualizar uma tela conforme mostrada a seguir:

![img.png](midias/OpenAPI-Swagger.png)

- Ajuste o SERVICE-ROUTER para atender os requisitos de segurança do ambiente

Até agora só era possível acessar o Swagger através do link direto e tendo o conhecimento de onde o serviço esta sendo 
executado, porém, será necessário ajustar o SERVICE-ROUTER para entender essas requisições OpenAPI e direcionar para a 
rota correta.

> Adicione a seguinte configuração no arquivo application.properties do SERVICE-ROUTER

<pre>
zuul.routes.service-demo-swagger.path=/api-docs/service-demo/**
zuul.routes.service-demo-swagger.service-id=SERVICE-DEMO
zuul.routes.service-demo-swagger.strip-prefix=false
zuul.routes.service-demo-swagger.sensitive-headers=
</pre>

Repare que existe uma nova entrada referente ao SERVICE-DEMO, porém agora ela se refere ao recurso OpenAPI contida o 
micro serviço SERVICE-DEMO configurado na linha zuul.routes.service-demo-swagger.path=/api-docs/service-demo/**

***Veja que a linha zuul.routes.service-demo-swagger.path=/api-docs/service-demo refere-se ao path configurado no micro 
serviço SERVICE-DEMO mostrado anteriormente***

Após fazer as alterações no SERVICE-ROUTER, acesse o Swagger do micro serviço SERVICE-DEMO pela url 

<pre>
http://localhost:33001/huntercodexs/arch-demo/service-demo/swagger-ui/index.html
</pre>

> NOTA. No ambiente seguro as rotas internas não ficam disponíveis ao público, assim como as portas onde os micro 
> serviços estão sendo executados.