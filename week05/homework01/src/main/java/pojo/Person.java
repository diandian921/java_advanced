package pojo;

/**
 * @author Created by diandian
 * @date 2021/7/25.
 */
public class Person {

    private String name;
    private Dog dog;
    private Cat cat;

    public Person() {
    }

    public Person(String name, Dog dog, Cat cat) {
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
