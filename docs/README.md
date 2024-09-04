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
        // 反射获取范型T的具体类型
        return Generics.find(this.getClass(), IToTarget.class, 0);
    }

}
```

#### BusinessTag & BusinessDoc
```Java
// extends SupportBusinessService
public interface DomainService extends BaseValidService<Domain, DomainMapper>, BaseUniqueKeyService<Domain>, SupportBusinessService, BaseQueryByExpiryService<Domain> {
    // ...
}

@Service
@RequiredArgsConstructor
// 增加注解
@BusinessType(type = BusinessTypeEnum.DOMAIN)
public class DomainServiceImpl implements DomainService {
    // ...
}
```