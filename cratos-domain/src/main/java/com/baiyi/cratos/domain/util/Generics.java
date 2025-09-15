package com.baiyi.cratos.domain.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author baiyi
 * @Date 2024/1/9 11:43
 * @Version 1.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Generics {

    /**
     * 查找类对象clazz绑定的genericClass声明的泛型参数
     *
     * @param clazz        绑定泛型参数的类
     * @param genericClass 声明泛型的类
     * @param index        泛型在声明类中的索引位置
     * @return 如果绑定了泛型参数, 则返回泛型类型, 否则返回null
     */
    public static Class find(Class clazz, Class genericClass, int index) {
        if (!genericClass.isAssignableFrom(clazz)) {
            return null;
        }
        List<Type> types = getGenericTypes(clazz);
        return types.stream()
                .map(type -> find(type, genericClass, index))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * 查找类type上绑定的genericClass声明的泛型类型
     *
     * @param type         泛型对象
     * @param genericClass 声明泛型的类
     * @param index        泛型在声明类中的索引位置
     * @return
     */
    private static Class find(Type type, Class genericClass, int index) {
        if (type instanceof Class) {
            return find((Class) type, genericClass, index);
        }
        if (type instanceof ParameterizedType pType) {
            Type rawType = pType.getRawType();
            if (rawType instanceof Class rawClass) {
                if (rawClass == genericClass) {
                    Type realType = pType.getActualTypeArguments()[index];
                    switch (realType) {
                        case Class aClass -> {
                            return aClass;
                        }
                        case ParameterizedType parameterizedType -> {
                            //这里是泛型的泛型
                            return (Class) parameterizedType.getRawType();
                            //这里是泛型的泛型
                        }
                        default -> {
                        }
                    }
                } else if (genericClass.isAssignableFrom(rawClass)) {
                    Map<String, Type> map = combine(pType.getActualTypeArguments(), rawClass);
                    return find(rawClass, genericClass, index, map);
                }
            }
        }
        return null;
    }

    /**
     * 查找类currentClass上绑定的genericClass声明的泛型类型
     *
     * @param currentClass 绑定泛型参数的类
     * @param genericClass 声明泛型的类
     * @param index        泛型在声明类中的索引位置
     * @param typeMap      已绑定的泛型类型映射表
     * @return
     */
    private static Class find(Class currentClass, Class genericClass, int index, Map<String, Type> typeMap) {
        List<Type> types = getGenericTypes(currentClass);
        for (Type type : types) {
            if (type instanceof ParameterizedType pType) {
                Type rawType = pType.getRawType();
                if (rawType instanceof Class rawClass) {
                    Map<String, Type> map = transfer(pType, typeMap);
                    Type[] typeArray = map.values()
                            .toArray(new Type[0]);
                    if (rawClass == genericClass) {
                        Type realType = typeArray[index];
                        switch (realType) {
                            case Class aClass -> {
                                return aClass;
                            }
                            case ParameterizedType parameterizedType -> {
                                //这里是泛型的泛型
                                return (Class) parameterizedType.getRawType();
                                //这里是泛型的泛型
                            }
                            case null, default -> {
                            }
                        }
                    } else if (genericClass.isAssignableFrom(rawClass)) {
                        return find(rawClass, genericClass, index, combine(typeArray, rawClass));
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取当前类继承的父类和实现接口的泛型列表
     *
     * @param clazz
     * @return
     */
    private static List<Type> getGenericTypes(Class clazz) {
        if (clazz == Object.class) {
            return Collections.EMPTY_LIST;
        }
        List<Type> list = new ArrayList<>();
        Type[] types = clazz.getGenericInterfaces();
        Collections.addAll(list, types);
        list.add(clazz.getGenericSuperclass());
        return list;
    }

    /**
     * 构建泛型映射表
     *
     * @param typeArguments 泛型参数
     * @param rawClass      声明泛型的类
     * @return
     */
    private static Map<String, Type> combine(Type[] typeArguments, Class rawClass) {
        Map<String, Type> map;
        TypeVariable[] typeParameters = rawClass.getTypeParameters();
        return IntStream.range(0, typeParameters.length)
                .boxed()
                .collect(Collectors.toMap(i -> typeParameters[i].getName(), i -> typeArguments[i], (a, b) -> b,
                        () -> new LinkedHashMap<>(typeArguments.length)));
    }

    /**
     * 转换泛型映射表
     *
     * @param parameterizedType 绑定参数类型的泛型对象
     * @param typeMap           已绑定的泛型类型映射表
     * @return
     */
    private static Map<String, Type> transfer(ParameterizedType parameterizedType, Map<String, Type> typeMap) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        TypeVariable[] typeParameters = ((Class) parameterizedType.getRawType()).getTypeParameters();
        Map<String, Type> map = new LinkedHashMap<>();
        IntStream.range(0, actualTypeArguments.length)
                .forEach(i -> {
                    Type type = actualTypeArguments[i];
                    if (type instanceof TypeVariable) {
                        String variableName = ((TypeVariable) type).getName();
                        map.put(variableName, typeMap.get(variableName));
                    } else {
                        map.put(typeParameters[i].getName(), type);
                    }
                });
        return map;
    }

}
