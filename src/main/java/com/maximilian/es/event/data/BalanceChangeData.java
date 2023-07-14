package com.maximilian.es.event.data;

import com.maximilian.es.command.BalanceChangeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceChangeData {

    private Long amount;
    private BalanceChangeType type;

}
