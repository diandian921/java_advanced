import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pojo.Person;

/**
 * @author Created by diandian
 * @date 2021/7/25.
 */
public class TestAutoWireXml {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("by name");
        Person person = context.getBean("person", Person.class);
        person.getCat().speak();
        person.getDog().speak();
        System.out.println("--------------------------");
        System.out.println("by type");
        Person person1 = context.getBean("person1", Person.class);
        person1.getCat().speak();
        person1.getDog().speak();
    }
}
