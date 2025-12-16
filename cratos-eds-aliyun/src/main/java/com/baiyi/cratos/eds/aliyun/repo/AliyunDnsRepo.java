package com.baiyi.cratos.eds.aliyun.repo;


import com.aliyun.sdk.service.alidns20150109.models.*;
import com.baiyi.cratos.eds.aliyun.client.AliyunDnsClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/9 15:45
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class AliyunDnsRepo {

    private static final long PAGE_SIZE = 500L;

    public static List<DescribeDomainRecordsResponseBody.Record> describeDomainRecords(EdsConfigs.Aliyun aliyun,
                                                                                       String domainName) {
        long pageNumber = 1L;
        List<DescribeDomainRecordsResponseBody.Record> allRecords = Lists.newArrayList();
        try (com.aliyun.sdk.service.alidns20150109.AsyncClient client = AliyunDnsClient.createClient(aliyun)) {
            long totalCount;
            do {
                DescribeDomainRecordsRequest request = DescribeDomainRecordsRequest.builder()
                        .domainName(domainName)
                        .pageSize(PAGE_SIZE)
                        .pageNumber(pageNumber)
                        .build();
                DescribeDomainRecordsResponse response = client.describeDomainRecords(request)
                        .get();
                List<DescribeDomainRecordsResponseBody.Record> records = Optional.ofNullable(response)
                        .map(DescribeDomainRecordsResponse::getBody)
                        .map(DescribeDomainRecordsResponseBody::getDomainRecords)
                        .map(DescribeDomainRecordsResponseBody.DomainRecords::getRecord)
                        .orElse(List.of());
                if (CollectionUtils.isEmpty(records)) {
                    break;
                }
                allRecords.addAll(records);
                totalCount = Optional.of(response)
                        .map(DescribeDomainRecordsResponse::getBody)
                        .map(DescribeDomainRecordsResponseBody::getTotalCount)
                        .orElse(0L);
                pageNumber++;
            } while (allRecords.size() < totalCount);
            return allRecords;
        } catch (Exception e) {
            throw new RuntimeException("Failed to query DNS records", e);
        }
    }

    public static String addDomainRecord(EdsConfigs.Aliyun aliyun, String domainName, String rr, String type,
                                         String value, Long ttl) {
        AddDomainRecordRequest request = AddDomainRecordRequest.builder()
                .domainName(domainName)
                .rr(rr)
                .type(type)
                .value(value)
                .TTL(ttl)
                .build();
        try (com.aliyun.sdk.service.alidns20150109.AsyncClient client = AliyunDnsClient.createClient(aliyun)) {
            AddDomainRecordResponse response = client.addDomainRecord(request)
                    .get();
            return Optional.ofNullable(response)
                    .map(AddDomainRecordResponse::getBody)
                    .map(AddDomainRecordResponseBody::getRequestId)
                    .orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add DNS record", e);
        }
    }

    public static String updateDomainRecord(EdsConfigs.Aliyun aliyun, String recordId, String rr, String type,
                                            String value, Long ttl) {
        UpdateDomainRecordRequest request = UpdateDomainRecordRequest.builder()
                .recordId(recordId)
                .rr(rr)
                .type(type)
                .value(value)
                .TTL(ttl)
                .build();
        try (com.aliyun.sdk.service.alidns20150109.AsyncClient client = AliyunDnsClient.createClient(aliyun)) {
            UpdateDomainRecordResponse response = client.updateDomainRecord(request)
                    .get();
            return Optional.ofNullable(response)
                    .map(UpdateDomainRecordResponse::getBody)
                    .map(UpdateDomainRecordResponseBody::getRequestId)
                    .orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update DNS record", e);
        }
    }

    public static String deleteDomainRecord(EdsConfigs.Aliyun aliyun, String recordId) {
        DeleteDomainRecordRequest request = DeleteDomainRecordRequest.builder()
                .recordId(recordId)
                .build();
        try (com.aliyun.sdk.service.alidns20150109.AsyncClient client = AliyunDnsClient.createClient(aliyun)) {
            DeleteDomainRecordResponse response = client.deleteDomainRecord(request)
                    .get();
            return Optional.ofNullable(response)
                    .map(DeleteDomainRecordResponse::getBody)
                    .map(DeleteDomainRecordResponseBody::getRequestId)
                    .orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete DNS record", e);
        }
    }

}
