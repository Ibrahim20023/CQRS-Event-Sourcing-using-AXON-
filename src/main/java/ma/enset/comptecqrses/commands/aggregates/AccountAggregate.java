package ma.enset.comptecqrses.commands.aggregates;

import ma.enset.comptecqrses.commonapi.commands.DebitAccountCommand;
import ma.enset.comptecqrses.commonapi.events.AccountCreditedEvent;
import ma.enset.comptecqrses.commonapi.events.AccountDebitedEvent;
import ma.enset.comptecqrses.commonapi.exceptions.AmountNegativeException;
import ma.enset.comptecqrses.commonapi.commands.CreateAccountCommand;
import ma.enset.comptecqrses.commonapi.commands.CreditAccountCommand;
import ma.enset.comptecqrses.commonapi.enums.AccountStatus;
import ma.enset.comptecqrses.commonapi.events.AccountActivatedEvent;
import ma.enset.comptecqrses.commonapi.events.AccountCreatedEvent;
import ma.enset.comptecqrses.commonapi.exceptions.BallanceInsufficientException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
        // REQUIRED by AXON
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        if (createAccountCommand.getInitialBalance() < 0) throw new RuntimeException("Balance negativee!!!");

        AggregateLifecycle.apply(new AccountCreatedEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency(),
                AccountStatus.CREATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getId();
        this.balance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = AccountStatus.CREATED;

        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(), AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        this.status = event.getStatus();
    }
    @CommandHandler
    public void handle(CreditAccountCommand creditAccountCommand){
        if(creditAccountCommand.getAmount() < 0) throw new AmountNegativeException("Amount negative!!!");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                creditAccountCommand.getId(),
                creditAccountCommand.getAmount(),
                creditAccountCommand.getCurrency()
        ));
    }
    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        this.balance += event.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand debitAccountCommand){
        if(debitAccountCommand.getAmount() < 0) throw new AmountNegativeException("Amount negative!!!");
        if (this.balance < debitAccountCommand.getAmount()) throw new BallanceInsufficientException("Balance not sufficient!!!");
        AggregateLifecycle.apply(new AccountDebitedEvent(
                debitAccountCommand.getId(),
                debitAccountCommand.getAmount(),
                debitAccountCommand.getCurrency()
        ));
    }
    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        this.balance -= event.getAmount();
    }
}
