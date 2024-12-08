# sboot-face-comparator

Este é um projeto baseado em Spring Boot que utiliza a API AWS Rekognition para comparar faces entre imagens armazenadas em buckets do Amazon S3. A aplicação permite enviar uma imagem de referência e uma lista de imagens alvo, e retorna os resultados de comparação de faces com base em um limiar de similaridade.

### Funcionalidades

- Compara uma imagem de referência (`sourcePhoto`) com várias imagens alvo (`targetPhotos`) armazenadas em buckets do S3.
- Retorna a similaridade das faces detectadas nas imagens comparadas, indicando o nível de semelhança (em porcentagem).
- Utiliza a API **AWS Rekognition** para realizar a detecção e comparação de faces.

### Tecnologias Utilizadas

- **Spring Boot**: Framework para desenvolvimento de aplicações Java.
- **AWS Rekognition**: Serviço da AWS para análise de imagens e vídeos, utilizado para comparar faces.
- **AWS SDK for Java**: Biblioteca para interagir com serviços da AWS.
- **Amazon S3**: Serviço de armazenamento de objetos (imagens) utilizado para armazenar as fotos.

---

## Estrutura do Projeto

O projeto é composto pelas seguintes classes principais:

1. **RekognitionController**: Controlador REST que expõe um endpoint para realizar a comparação de faces.
2. **RekognitionService**: Serviço responsável pela lógica de comparação de faces utilizando a AWS Rekognition.
3. **AwsConfiguration**: Classe de configuração para inicializar o cliente AWS Rekognition com as credenciais de acesso à AWS.

---

## Como Usar

### Pré-requisitos

1. **JDK 21+**: O projeto foi desenvolvido usando o Java 21.
2. **AWS Account**: Uma conta AWS com permissões para usar o serviço **AWS Rekognition** e **Amazon S3**.
3. **Credenciais AWS**: Você precisa de uma chave de acesso (`aws.access-key`) e uma chave secreta (`aws.secret-key`) para configurar o acesso à API da AWS.

### Passos para Rodar a Aplicação

1. **Clone este repositório**:

   ```bash
   git clone https://github.com/seu-usuario/sboot-face-comparator.git
   cd sboot-face-comparator
   ```

2. **Configure as credenciais da AWS**:

   No arquivo `src/main/resources/application.properties` ou `application.yml`, insira as credenciais de acesso à sua conta AWS:

   ```properties
   aws.access-key=your-access-key-here
   aws.secret-key=your-secret-key-here
   aws.region=us-east-1  # Substitua pela sua região AWS
   ```

3. **Compile e execute a aplicação**:

   Se você estiver usando Maven:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   Ou se estiver usando Gradle:

   ```bash
   ./gradlew bootRun
   ```

4. **Acesse o Endpoint da API**:

   Após a aplicação iniciar, o servidor estará rodando na porta padrão do Spring Boot, geralmente `http://localhost:8080`. O endpoint para comparar faces é o seguinte:

   ```bash
   GET http://localhost:8080/api/compare-faces
   ```

### Parâmetros da Requisição

O endpoint `compare-faces` exige os seguintes parâmetros:

- **sourceBucket**: O nome do bucket S3 onde está armazenada a foto de referência.
- **sourcePhoto**: O nome do arquivo de imagem (foto de referência) no bucket S3.
- **targetBucket**: O nome do bucket S3 onde estão armazenadas as fotos alvo.
- **targetPhotos**: Uma lista de nomes de arquivos de imagens (fotos alvo) no bucket S3.

#### Exemplo de Requisição com cURL:

```bash
curl --location 'http://localhost:8080/api/compare-faces?sourceBucket=my-source-bucket&sourcePhoto=source-image.jpg&targetBucket=my-target-bucket&targetPhotos=target-image1.jpg%2Ctarget-image2.jpg'
```

### Resposta da API

A resposta será uma lista com os resultados da comparação das faces, mostrando o nome da imagem alvo e o nível de similaridade em porcentagem:

```json
[
  "Comparing with target-image1.jpg: Similarity 95.4%",
  "Comparing with target-image2.jpg: No match for target-image2.jpg"
]
```

---

## Arquitetura

A aplicação segue a arquitetura padrão de um projeto Spring Boot com a separação de responsabilidades:

- **Controller**: Gerencia as requisições HTTP e encaminha para o serviço adequado.
- **Service**: Contém a lógica de negócios, como a interação com a API AWS Rekognition.
- **Configuration**: Configura o acesso à AWS e fornece o cliente necessário para interagir com o serviço Rekognition.

---

## Segurança e Considerações Finais

- **Segurança das Credenciais**: Certifique-se de que suas credenciais da AWS não estejam expostas publicamente. Você pode utilizar variáveis de ambiente ou um serviço de gerenciamento de credenciais para proteger suas chaves.
- **Limitação de Chaves**: Certifique-se de que o serviço AWS Rekognition não esteja sendo chamado em excesso para evitar custos altos ou limites de uso da API.

---

## Licença

Este projeto está licenciado sob a **MIT License** - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

Se precisar de mais informações ou tiver dúvidas, não hesite em abrir uma issue neste repositório.

---

Esse README cobre os detalhes básicos de configuração e uso da sua aplicação. Se você tiver mais funcionalidades ou detalhes sobre o funcionamento, pode adicionar mais seções conforme necessário.