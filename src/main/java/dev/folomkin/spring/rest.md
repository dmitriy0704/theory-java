# Spring REST аннотациии

## Отлов ошибок. Exception handler

```java
// Контроллер аспект

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = NotEnoughMoneyExceptions.class)
    public ResponseEntity<ErrorDetails> exception(
            NotEnoughMoneyExceptions notEnoughMoneyExceptions) {
        ErrorDetails errorDetails =
                new ErrorDetails(notEnoughMoneyExceptions.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(value =
              { IllegalArgumentException.class,
                    IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex,
                                                    WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}


// Описание ошибки:

//@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotEnoughMoneyExceptions extends RuntimeException {
    public NotEnoughMoneyExceptions(String message) {
        super(message);
    }
}


// Применение:

@Service
public class PaymentService {
    public PaymentDetails processPaymentDetails() {
        throw new NotEnoughMoneyExceptions("Not enough money");
    }
}
```

## REST clients

### OpenFeign

```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
```

```java
// Конфиг:

@Configuration
@EnableFeignClients(basePackages = "client.proxy")
public class ProjectConfig {}

// Прокси:

@FeignClient(name = "payment", url = "${name.service.url}")
public interface PaymentProxy {
    
    @PostMapping("/payment")
    Payment createPayment(@RequestHeader String requestId, @RequestBody Payment payment);
}

// Применение:

@RestController
public class PaymentController {
    
    private final PaymentProxy paymentProxy;
    
    public PaymentController(PaymentProxy paymentProxy) {
        this.paymentProxy = paymentProxy;
    }
    
    @PostMapping("/payment")
    public Payment createPayment(@RequestBody Payment payment) {
        String requestId = UUID.randomUUID().toString();
        return paymentProxy.createPayment(requestId, payment);
    }
}

```

### REST-Template

```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
```

```java
// Конфиг:

@Configuration
public class ProjectConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


// Прокси:

@Component
public class PaymentProxy {
    
    private final RestTemplate rest;
    
    @Value("${name.service.url}")
    private String paymentsServiceUrl;
    
    public PaymentProxy(RestTemplate rest) {
        this.rest = rest;
    }
    public Payment createPayment(Payment payment) {
        String uri = paymentsServiceUrl + "/payment";
        HttpHeaders headers = new HttpHeaders();
        headers.add("requestId", UUID.randomUUID().toString());
        HttpEntity<Payment> httpEntity = new HttpEntity<>(payment, headers);
        ResponseEntity<Payment> response =
                rest.exchange(uri,
                        HttpMethod.POST,
                        httpEntity,
                        Payment.class);
        return response.getBody();
    }
}

// Применение:

@RestController
public class PaymentController {

    private final PaymentProxy paymentProxy;

    public PaymentController(PaymentProxy paymentProxy) {
        this.paymentProxy = paymentProxy;
    }

    @PostMapping("/payment")
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentProxy.createPayment(payment);
    }
}

```

### Web Client

```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
```

```java
// Конфиг:

@Configuration
public class ProjectConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}

// Прокси:

@Component
public class PaymentsProxy {

    private final WebClient webClient;

    @Value("${name.service.url}")
    private String url;

    public PaymentsProxy(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Payment> createPayment(String requestId, Payment payment) {
        return webClient.post()
                .uri(url + "/payment")
                .accept(MediaType.ALL)
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("requestId", requestId)
                .body(Mono.just(payment), Payment.class)
                .retrieve()
                .bodyToMono(Payment.class);
    }
}


// Применение:

@RestController
public class PaymentsController {
    private final PaymentsProxy paymentsProxy;
    public PaymentsController(PaymentsProxy paymentsProxy) {
        this.paymentsProxy = paymentsProxy;
    }
    
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/payments")
    public Mono<Payment> createPayment(@RequestBody Payment payment) {
        String requestId = UUID.randomUUID().toString();
        return paymentsProxy.createPayment(requestId, payment);
    }
}

```
