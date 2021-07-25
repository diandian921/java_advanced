package week05.spring.autowire.pojo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Created by diandian
 * @date 2021/7/25.
 */
@Configuration
@ComponentScan("week05.spring.autowire.pojo")
public class Config {

    @Bean
    public Person2 person2() {
        return new Person2();
    }

    @Bean
    public Dog dog() {
        return new Dog();
    }

    @Bean
    public Cat cat() {
        return new Cat();
    }
}
