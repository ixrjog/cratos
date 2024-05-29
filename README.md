# CRATOS
> 安全的运维通用开发框架

+ OpenJDK 21
+ SpringBoot 3.3.0 (GA)
+ MySql 8+

---
# annotation

#### Domain加解密注解使用
```
@DomainDecrypt DomainClass
@FieldEncrypt  Domain字段中使用
@DomainDecrypt Service的方法中解密Domain(在BaseService接口中默认实现)
@DomainEncrypt Service的方法中加密Domain(在BaseService接口中默认实现)
```

```
    @Override
    @DomainDecrypt
    public List<BusinessDocument> selectByBusiness(BaseBusiness.IBusiness business) {
       ...
    }
```

#### View脱敏注解使用
```
@FieldSensitive VO字段中使用
@Sensitive Wrapper的方法中使用
```


#### 反射说明

+ 获取范型接口实现类的对象类型
```Java
public interface IToTarget<T> {

    default T toTarget() {
        return BeanCopierUtil.copyProperties(this, getTargetClazz());
    }

    default Class<T> getTargetClazz() {
        //  return (Class<T>) AopUtils.getTargetClass(this).getAnnotation(TargetClazz.class).clazz();
        // 反射获取范型T的具体类型
        return Generics.find(this.getClass(), IToTarget.class, 0);
    }

}
```