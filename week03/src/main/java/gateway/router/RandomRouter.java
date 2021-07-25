package gateway.router;

import java.util.List;
import java.util.Random;

/**
 * @author Created by diandian
 * @date 2021/7/11.
 */
public class RandomRouter implements Router {
    @Override
    public String route(List<String> endPoints) {
        int size = endPoints.size();
        Random random = new Random();
        return endPoints.get(random.nextInt(size));
    }
}
