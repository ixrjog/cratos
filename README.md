# CRATOS



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



