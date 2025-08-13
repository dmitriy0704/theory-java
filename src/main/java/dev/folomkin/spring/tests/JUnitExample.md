# Пример простого теста на JUnit 5

## Модульный тест

### Тестируемый сервис

```java
@Service
public class TransferService {

    private final AccountRepository accountRepository;
    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transferMoney(long idSender, long idReceiver, BigDecimal amount) {

        Account sender = accountRepository
                .findById(idSender)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        Account receiver = accountRepository
                .findById(idReceiver)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        BigDecimal newSenderNewAmount = sender.getAmount().subtract(amount);
        BigDecimal newReceiverNewAmount = receiver.getAmount().add(amount);
        accountRepository.changeAmount(idSender, newSenderNewAmount);
        accountRepository.changeAmount(idReceiver, newReceiverNewAmount);
    }

    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> findAccountsByName(String name) {
        return accountRepository.findAccountsByName(name);
    }
}

public interface AccountRepository extends CrudRepository<Account, Long> {
    @Query("select * from account where name = :name")
    List<Account> findAccountsByName(String name);
    @Modifying
    @Query("update account set amount = :amount where id = :id")
    void changeAmount(Long id, BigDecimal amount);
}

@RestController
public class AccountController {
    private final TransferService transferService;
    public AccountController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public void transferMoney(@RequestBody TransferRequest request) {
        transferService.transferMoney(
                request.senderAccountId(),
                request.receiverAccountId(),
                request.amount()
        );
    }

    @GetMapping("/accounts")
    public Iterable<Account> getAllAccounts(
            @RequestParam(required = false) String name) {
        if(name==null) {
            return transferService.getAllAccounts();
        } else {
            return transferService.findAccountsByName(name);
        }
    }
}

```

### Тест к нему

#### Успешный

```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import springdata.model.Account;
import springdata.repo.AccountRepository;
import java.math.BigDecimal;
import java.util.Optional;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransferServiceUnitTest {

// Любой тест состоит из трех основных частей:
// 1. Предпосылки. Определить входные данные и найти все зависимости для проверяемой логики, чтобы получить желаемый сценарий выполнения. Какие входные данные следует предоставить и как должны вести себя зависимости тестируемой логики, чтобы она работала так, как мы хотим 
// 2. Вызов/выполнение. Вызвать тестируемую логику и проверить ее поведение.
// 3. Проверки. Определить все операции проверки, которые нужно выполнить для данной части логики. Что должно произойти при вызове данной логики для заданных условий?

// Иногда эти три этапа (предпосылки, вызов, проверки) называют иначе: Arrange, Act, and Assert (настроить, выполнить, подтвердить) или Given, When, and Then  дано, если, то).

// === GIVEN === //
    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    TransferService transferService;

    @Test
    @DisplayName("Test the amount is transferred from one account to another " +
            "if no exception occurs.")
    public void moneyTransferHappyFlow() {

        // Если не используются аннотации @Mock и @InjectMock:
        // AccountRepository accountRepository = mock(AccountRepository.class);
        // TransferService transferService = new TransferService(accountRepository);

        // Создаем экземпляры Account для отправителя и получателя, где хранится информация об их счетах — предполагается, что в действительности приложение получает ее из базы данных

        Account sender = new Account();
        sender.setId(1);
        sender.setAmount(new BigDecimal(1000));
        Account destination = new Account();
        destination.setId(2);
        destination.setAmount(new BigDecimal(1000));

        /* Для управления действиями заглушки используется метод given().
        С помощью given() можно определить, как будет вести себя заглушка
        при вызове одного из ее методов. В данном случае нам нужно, чтобы
        метод findById() объекта AccountRepository возвращал определенный
        экземпляр Account в зависимости от заданного значения параметра.
        */

        /* Управляемый нами метод findById(), получая ID счета
        отправителя, возвращает экземпляр этого счета. Данную строку
        следует читать так: «Если вызвать findById() и передать
        ему ID счета отправителя в виде параметра, этот метод вернет
        экземпляр счета отправителя»
        */

        given(accountRepository.findById(sender.getId()))
                .willReturn(Optional.of(sender));
        given(accountRepository.findById(destination.getId()))
                .willReturn(Optional.of(destination));

// === WHEN === //

        // Вызываем метод, который хотим протестировать, передавая ему ID отправителя, ID получателя и сумму перевода

        transferService.transferMoney(1, 2, new BigDecimal(100));

// === THEN === //

        // Проверяем, что метод changeAmount() из AccountRepository вызван с ожидаемыми параметрами
        verify(accountRepository)
            .changeAmount(1L, new BigDecimal(900));
        verify(accountRepository)
            .changeAmount(2L, new BigDecimal(1100));
    }
}
```

#### С исключением

```java
@Test
@DisplayName("Testing ")
public void moneyTransferDestinationAccountNotFoundFlow() {
        Account sender = new Account();
        sender.setId(1);
        sender.setAmount(new BigDecimal(1000));

        given(accountRepository.findById(1L))
                .willReturn(Optional.of(sender));

// Управляя заглушкой AccountRepository, мы делаем так, чтобы метод findById(), вызванный для счета получателя, возвращал пустой объект Optional
        given(accountRepository.findById(2L))
                .willReturn(Optional.empty());

// Мы предполагаем, что для данного варианта выполнения метод должен выбрасывать исключение IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> transferService.transferMoney(1, 2, new BigDecimal(100))
        );

// Используем метод verify() с условием verify(accountRepository, never()) never() для уверенности, что метод changeAmount() не вызывается
        verify(accountRepository, never()).changeAmount(anyLong(), any());

}
```

## Интеграционный тест

```java

@SpringBootTest
class TransferServiceSpringIntegrationTest {
    // Создаем объект-заглушку, который входит в состав контекста Spring
    @MockBean
    private AccountRepository accountRepository;

    // Внедряем реальный объект из контекста Spring, поведение которого хотим протестировать
    @Autowired
    private TransferService transferService;

    @Test
    void transferServiceTransferAmountTest() {
        // Определяем все предпосылки для теста
        Account sender = new Account();
        sender.setId(1);
        sender.setAmount(new BigDecimal(1000));

        Account receiver = new Account();
        receiver.setId(2);
        receiver.setAmount(new BigDecimal(1000));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiver));

        // Вызываем тестируемый метод
        transferService.transferMoney(1, 2, new BigDecimal(100));

        verify(accountRepository).changeAmount(1L, new BigDecimal(900));
        verify(accountRepository).changeAmount(2L, new BigDecimal(1100));
    }

}
```