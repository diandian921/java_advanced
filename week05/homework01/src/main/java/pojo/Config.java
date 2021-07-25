package pojo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pojo.Cat;
import pojo.Dog;
import pojo.Person2;

/**
 * @author Created by diandian
 * @date 2021/7/25.
 */
@Configuration
@ComponentScan("pojo")
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
