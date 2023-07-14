package com.maximilian.es.service;

import com.maximilian.es.aggregate.AggregateEntity;
import com.maximilian.es.aggregate.AggregateRoot;
import com.maximilian.es.aggregate.Customer;
import com.maximilian.es.command.BalanceChangeCommand;
import com.maximilian.es.command.BalanceChangeType;
import com.maximilian.es.command.CreateCustomerCommand;
import com.maximilian.es.event.BalanceUpdatedEvent;
import com.maximilian.es.event.BaseEvent;
import com.maximilian.es.event.CustomerCreatedEvent;
import com.maximilian.es.event.data.BalanceChangeData;
import com.maximilian.es.event.data.CustomerCreateData;
import com.maximilian.es.exception.GeneralException;
import com.maximilian.es.repository.AggregateEntityRepository;
import com.maximilian.es.repository.AggregateRootRepository;
import com.maximilian.es.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class CustomerService {

    private final EventRepository eventRepository;
    private final AggregateRootRepository aggregateRootRepository;
    private final AggregateEntityRepository aggregateEntityRepository;
    private final SerializationHelper serializationHelper;

    public CustomerService(EventRepository eventRepository,
                           AggregateRootRepository aggregateRootRepository,
                           AggregateEntityRepository aggregateEntityRepository, SerializationHelper serializationHelper) {
        this.eventRepository = eventRepository;
        this.aggregateRootRepository = aggregateRootRepository;
        this.aggregateEntityRepository = aggregateEntityRepository;
        this.serializationHelper = serializationHelper;
    }

    @PostConstruct
    private void init() {
        Optional<AggregateRoot> root = aggregateRootRepository
                .findAggregateRootByName(Customer.CUSTOMER_AGGREGATE_NAME);
        if(root.isEmpty()) {
            log.info("Creating customer root");
            createCustomerRoot();
        }
    }

    public Customer createCustomer(CreateCustomerCommand command) {

        List<BaseEvent> baseEvents = processCreateCommand(command);
        AggregateRoot customerRoot = getCustomerRoot();
        AggregateEntity entity = createNewAggregateEntity(customerRoot);
        entity = aggregateEntityRepository.getAggregateEntityByEntityId(entity.getEntityId());

        for (BaseEvent baseEvent : baseEvents) {
            baseEvent.setEntityType(customerRoot.getId());
            baseEvent.setEntityId(entity.getEntityId());
        }
        baseEvents = eventRepository.saveAll(baseEvents);

        Customer customer = new Customer();
        applyEvents(baseEvents, customer);
        return customer;
    }

    public Customer getCustomer(Long id) {
        List<BaseEvent> events = eventRepository.getBaseEventsByEntityIdOrderByCreationDate(id);
        if (events.isEmpty()) {
            throw new GeneralException("Cannot find customer " + id);
        }
        Customer customer = new Customer();
        applyEvents(events, customer);
        return customer;
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class,
            backoff = @Backoff(delay = 300))
    public Customer updateBalance(Long id, BalanceChangeCommand command) {
        AggregateEntity entity = aggregateEntityRepository
                .getAggregateEntityByEntityId(id);
        Customer customer = getCustomer(id);
        AggregateRoot customerRoot = getCustomerRoot();

        if (command.getType() == BalanceChangeType.WITHDRAW &&
                customer.getBalance() - command.getAmount() < 0) {
            throw new GeneralException("Customer " + customer + " has not enough money for withdrawal");
        }
        log.info("Withdrawal for " + command.getAmount() + " is ok");

        List<BaseEvent> events = processBalanceCommand(command);

        for (BaseEvent baseEvent : events) {
            baseEvent.setEntityType(customerRoot.getId());
            baseEvent.setEntityId(entity.getEntityId());
        }
        events = eventRepository.saveAll(events);

        applyEvents(events, customer);
        return customer;
    }

    public AggregateRoot getCustomerRoot() {
        return aggregateRootRepository.findAggregateRootByName(Customer.CUSTOMER_AGGREGATE_NAME)
                .orElseThrow(() ->
                        new GeneralException("Aggregate root " +
                                Customer.CUSTOMER_AGGREGATE_NAME + " does not exist.")
                );
    }

    private AggregateRoot createCustomerRoot() {
        AggregateRoot root = new AggregateRoot();
        root.setName(Customer.CUSTOMER_AGGREGATE_NAME);
        return aggregateRootRepository.save(root);
    }

    private List<BaseEvent> processCreateCommand(CreateCustomerCommand command) {
        CustomerCreatedEvent event = new CustomerCreatedEvent();
        event.setEventType(CustomerCreatedEvent.EVENT_TYPE);
        event.setData(serializationHelper.toJson(
                new CustomerCreateData(command.getName(), 500L)
        ));
        return List.of(event);
    }

    private List<BaseEvent> processBalanceCommand(BalanceChangeCommand command) {
        BalanceUpdatedEvent event = new BalanceUpdatedEvent();
        event.setEventType(BalanceUpdatedEvent.EVENT_TYPE);
        event.setData(serializationHelper.toJson(
                new BalanceChangeData(command.getAmount(), command.getType())
        ));
        return List.of(event);
    }

    private AggregateEntity createNewAggregateEntity(AggregateRoot customerRoot) {
        AggregateEntity entity = new AggregateEntity();
        entity.setEntityType(customerRoot.getId());
        return aggregateEntityRepository.saveAndFlush(entity);
    }

    private void applyEvents(List<BaseEvent> events, Customer aggregate) {
        log.info("Got " + events.size() + " events.");
        for (BaseEvent baseEvent : events) {
            log.info("Applying event " + baseEvent);
            baseEvent.apply(aggregate, serializationHelper);
        }
        log.info("Result = " + aggregate);
    }

}
