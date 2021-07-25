import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pojo.Config;
import pojo.Person2;

/**
 * @author Created by diandian
 * @date 2021/7/25.
 */
public class TestAutoWireAnnotation {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Person2 person = context.getBean("person2", Person2.class);
        person.getCat().speak();
        person.getDog().speak();
        System.out.println(person.getName());
    }
}
