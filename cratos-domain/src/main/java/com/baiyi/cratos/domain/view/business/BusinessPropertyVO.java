package com.baiyi.cratos.domain.view.business;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/22 10:18
 * @Version 1.0
 */
public class BusinessPropertyVO {

    public interface IBusinessProperties extends BaseBusiness.IBusiness {

        void setBusinessProperties(List<BusinessProperty> businessProperties);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class BusinessProperty extends BaseVO implements BaseBusiness.IBusiness, Serializable {

        @Serial
        private static final long serialVersionUID = -4162849944226063024L;

        private Integer id;

        private String businessType;

        private Integer businessId;

        private String propertyName;

        private String propertyValue;

    }

}
