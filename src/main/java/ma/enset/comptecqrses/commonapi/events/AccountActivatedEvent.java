package ma.enset.comptecqrses.commonapi.events;

import lombok.Getter;
import ma.enset.comptecqrses.commonapi.enums.AccountStatus;

public class AccountActivatedEvent extends BaseEvent<String> {
    @Getter private AccountStatus status;

    public AccountActivatedEvent(String id, AccountStatus status) {
        super(id);
        this.status = status;
    }
}
