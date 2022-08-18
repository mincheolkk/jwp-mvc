package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements RequestMappingInterface {
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(this::setHandlerExecutions);
    }

    private void setHandlerExecutions(Class<?> controllerClass) {
        Arrays.stream(controllerClass.getDeclaredMethods()).forEach(declaredMethod -> {
            RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);
            handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMapping.method()), new HandlerExecution(controllerClass, declaredMethod));
        });
    }

    @Override
    public Object findHandler(HttpServletRequest request) {
        return getHandler(request);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
