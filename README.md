# CRATOS

+ 账户过期
+ 用户私密信息（Password,OTP,SSHKey）保存到凭据
  + 用户密码过期；强制用户修改密码，且不能与之前密码相同
+ 凭据支持过期 
+ 用户授权资源支持过期

---
# annotation

#### Domain加解密注解使用
```
@DomainDecrypt DomainClass
@FieldEncrypt  Domain字段中使用
@DomainDecrypt Service的方法中解密Domain(在BaseService接口中默认实现)
@DomainEncrypt Service的方法中加密Domain(在BaseService接口中默认实现)
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