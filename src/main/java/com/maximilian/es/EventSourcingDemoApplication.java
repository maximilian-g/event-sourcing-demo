package com.maximilian.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventSourcingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventSourcingDemoApplication.class, args);
    }

//    @Bean
//    CommandLineRunner runner(CustomerService service) {
//        return args -> {
//            Customer bob = service.createCustomer(new CreateCustomerCommand("Bob"));
//            Customer customer = service.getCustomer(bob.getId());
//            Thread t1 = new Thread(() -> service.updateBalance(customer.getId(),
//                    new BalanceCommand(400L, BalanceCommandType.WITHDRAW)));
//            Thread t2 = new Thread(() -> service.updateBalance(customer.getId(),
//                    new BalanceCommand(200L, BalanceCommandType.WITHDRAW)));
//            t1.start();
//            t2.start();
//            t1.join();
//            t2.join();
//            Customer result = service.getCustomer(bob.getId());
//            System.out.println("Got " + result);
//        };
//    }

}
