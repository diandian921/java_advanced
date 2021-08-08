package aspect;

import annotation.Routing;
import config.DataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
@Component
public class DynamicDataSourceAspect {

    @Around("@annotation(annotation.Routing)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        try {
            String dataSource = DataSourceContextHolder.getDB();
            if (StringUtils.isEmpty(dataSource)) {
                MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
                Method method = methodSignature.getMethod();
                dataSource = DataSourceContextHolder.DEFAULT_DATASOURCE;
                if (method.isAnnotationPresent(Routing.class)) {
                    Routing routingDataSource = method.getDeclaredAnnotation(Routing.class);
                    dataSource = routingDataSource.value();
                }
                DataSourceContextHolder.setDB(dataSource);
            }
            return pjp.proceed();

        } finally {
            DataSourceContextHolder.clearDB();
        }
    }
}