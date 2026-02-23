package com.example.queue.config;

import com.example.queue.model.Counter;
import com.example.queue.model.Queue;
import com.example.queue.model.User;
import com.example.queue.repository.CounterRepository;
import com.example.queue.repository.QueueRepository;
import com.example.queue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final QueueRepository queueRepository;
    private final CounterRepository counterRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User(null, "Admin", "admin@smartqueue.com",
                    passwordEncoder.encode("admin123"), User.Role.ADMIN);
            userRepository.save(admin);

            User user = new User(null, "Test User", "user@smartqueue.com",
                    passwordEncoder.encode("user123"), User.Role.USER);
            userRepository.save(user);
        }

        if (queueRepository.count() == 0) {
            Queue generalQueue = new Queue(null, "General", "General Services", null, null);
            generalQueue = queueRepository.save(generalQueue);

            Counter counter1 = new Counter(null, 1, generalQueue, Counter.CounterStatus.AVAILABLE, null);
            counterRepository.save(counter1);

            Counter counter2 = new Counter(null, 2, generalQueue, Counter.CounterStatus.AVAILABLE, null);
            counterRepository.save(counter2);
        }
    }
}
