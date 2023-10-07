package sys.profiling;

import com.badlogic.gdx.graphics.profiling.GLInterceptor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import sys.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PerformanceProfile implements InvocationHandler {

    private static Logger LOGGER = Log.addLogger(
            PerformanceProfile.class);

    private final Map<String, Method> methods = new HashMap<>();

    private Object target;

    public PerformanceProfile(Object target) {
        this.target = target;

        for(Method method: target.getClass().getDeclaredMethods()) {
            this.methods.put(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        long start = System.nanoTime();
        Object result = methods.get(method.getName()).invoke(target, args);
        long elapsed = System.nanoTime() - start;

        LOGGER.info("Executing {} finished in {} ns");

        return result;
    }
}