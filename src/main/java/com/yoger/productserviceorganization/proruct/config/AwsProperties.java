package com.yoger.productserviceorganization.proruct.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.aws.s3")
public record AwsProperties(
        String region,
        String bucket,
        String accessKey,
        String secretKey
) {
}
