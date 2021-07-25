package week05.spring.autowire.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Created by diandian
 * @date 2021/7/25.
 */
public class Person2 {
    @Value("zhangsan")
    private String name;
    @Autowired
    private Dog dog;
    @Autowired
    private Cat cat;

    public Person2() {
    }

    public Person2(String name, Dog dog, Cat cat) {
        this.name = name;
        this.dog = dog;
        this.cat = cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }
}
