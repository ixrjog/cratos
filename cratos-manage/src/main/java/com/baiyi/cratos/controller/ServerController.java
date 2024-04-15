package com.baiyi.cratos.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author baiyi
 * @Date 2024/4/15 上午11:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/server")
@Tag(name = "Server")
@RequiredArgsConstructor
public class ServerController {
}
