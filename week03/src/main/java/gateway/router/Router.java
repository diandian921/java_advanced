package gateway.router;

import java.util.List;

/**
 * @author Created by diandian
 * @date 2021/7/11.
 */
public interface Router {

    String route(List<String> endPoints);

}
