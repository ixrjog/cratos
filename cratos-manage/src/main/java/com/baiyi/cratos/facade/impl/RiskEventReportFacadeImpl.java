package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.facade.RiskEventReportFacade;
import com.baiyi.cratos.service.RiskEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/16 下午2:24
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RiskEventReportFacadeImpl implements RiskEventReportFacade {

    private final RiskEventService eventService;

    public OptionsVO.Options getYearOptions() {
        List<OptionsVO.Option> options = eventService.queryYears()
                .stream()
                .map(e -> OptionsVO.Option.builder()
                        .value(e)
                        .label(e)
                        .build())
                .collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(options)
                .build();
    }

}
