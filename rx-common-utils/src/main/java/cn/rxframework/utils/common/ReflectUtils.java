package cn.rxframework.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Java对象反射工具
 */
public class ReflectUtils {

    private static final Logger log = LoggerFactory.getLogger(ReflectUtils.class);

    /**
     * 获取类的所有字段(包含上层父类继承)
     */
    public static List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
        Field[] currentFields = clazz.getDeclaredFields();
        for (Field field : currentFields) {
            fields.add(field);
        }
        return getFields(clazz, fields);
    }

    /**
     * 获取类的所有字段(包含上层父类继承)
     */
    public static List<Field> getFields(Class<?> clazz, List<Field> fields) {
        Class<?> superClazz = clazz.getSuperclass();
        if (!clazz.equals(Object.class)) {
            Field[] newFields = superClazz.getDeclaredFields();
            for (Field field : newFields) {
                fields.add(field);
            }
            getFields(superClazz, fields);
        }
        return fields;
    }

    /**
     * 获取类的所有字段set和get方法(包含上层父类继承)
     */
    public static List<Method> getMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<Method>();
        Method[] currentMethods = clazz.getDeclaredMethods();
        for (Method method : currentMethods) {
            String methodName = method.getName();
            if (!methodName.equals("getClass") && (methodName.startsWith("set") || methodName.startsWith("get"))) {
                methods.add(method);
            }
        }
        return getMethods(clazz, methods);
    }

    /**
     * 获取类的所有字段set和get方法(包含上层父类继承)
     */
    public static List<Method> getMethods(Class<?> clazz, List<Method> methods) {
        Class<?> superClazz = clazz.getSuperclass();
        if (!clazz.equals(Object.class)) {
            Method[] newMethods = superClazz.getDeclaredMethods();
            for (Method method : newMethods) {
                String methodName = method.getName();
                if (!methodName.equals("getClass") && (methodName.startsWith("set") || methodName.startsWith("get"))) {
                    methods.add(method);
                }
            }
            getMethods(superClazz, methods);
        }
        return methods;
    }

    /**
     * 获取类的指定字段(包含父级搜索)
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 获取类的指定方法(包含父级搜索)
     */
    public static Method getMethod(Class<?> clazz, String methodName) {
        List<Method> methods = getMethods(clazz);
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    /**
     * 生成字段的set方法名
     */
    public static String getGetterMethodName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * 生成字段的get方法名
     */
    public static String getSetterMethodName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * 通过字段获取参数值
     *
     * @param obj   对象实例
     * @param field 实例字段对象
     * @return 参数值
     */
    public static Object readValueByField(Object obj, Field field) {
        try {
            return field.get(obj);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 通过方法获取参数值
     *
     * @param obj    对象实例
     * @param method 实例方法对象
     * @return 参数值
     */
    public static Object readValueByMethod(Object obj, Method method) {
        try {
            return method.invoke(obj);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 重写参数值通过字段对象
     *
     * @param obj      对象实例
     * @param field    实例字段对象
     * @param newValue 设置值
     */
    public static void writeValueByField(Object obj, Field field, Object newValue) {
        field.setAccessible(true);
        try {
            field.set(obj, newValue);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 重写参数值通过方法对象
     *
     * @param obj      对象实例
     * @param method   实例方法对象
     * @param newValue 设置值
     */
    public static void writeValueByMethod(Object obj, Method method, Object newValue) {
        try {
            method.invoke(obj, newValue);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 根据字段名获取对象的值
     *
     * @param obj
     * @param fieldName
     * @return Object
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Field field = getField(obj.getClass(), fieldName);
        field.setAccessible(true);
        return readValueByField(obj, field);
    }

}
