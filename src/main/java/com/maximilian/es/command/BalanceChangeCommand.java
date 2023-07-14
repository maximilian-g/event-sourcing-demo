package com.maximilian.es.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceChangeCommand {

    @NotNull(message = "Amount should not be null")
    @PositiveOrZero(message = "Amount should be greater or equal to 0")
    private Long amount;
    @NotNull(message = "Type should not be null")
    private BalanceChangeType type;

}
